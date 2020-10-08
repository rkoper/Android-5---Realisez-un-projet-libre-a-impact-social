package com.so.dingbring.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.so.dingbring.UploadListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel(private val repository: HomeRepository): ViewModel() {


    var uploadListener: UploadListener?=null
    private val disposables= CompositeDisposable()


    fun getAllInformation() : LiveData<MutableList<MyEvent>> {
        val mutableData = MutableLiveData<MutableList<MyEvent>>()
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
