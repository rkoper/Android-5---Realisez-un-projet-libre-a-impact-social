package com.so.dingbring

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class InfoViewModel(private val repository: DataRepository): ViewModel() {


    var uploadListener:UploadListener?=null
    private val disposables= CompositeDisposable()


    fun getAllInformation() : LiveData<MutableList<MyData>> {
        val mutableData= MutableLiveData<MutableList<MyData>>()
        repository.getAllInfo().observeForever{
            mutableData.value=it
        }
        return mutableData
    }

    fun upload(one:String, two:String) {
        val disposable=repository.uploadData(one, two).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe({
            uploadListener?.onSuccess()
        },{
            uploadListener?.onFailure(it.message!!)
        })

        disposables.add(disposable)

    }
}
