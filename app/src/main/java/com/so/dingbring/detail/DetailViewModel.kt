package com.so.dingbring.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.so.dingbring.UploadListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailViewModel(private val mDetailRepository: DetailRepository): ViewModel() {


    var uploadListener: UploadListener?=null
    private val disposables= CompositeDisposable()


    fun getAllDetail(mEventId: String): LiveData<MutableList<MyDetail>> {
        val mMutableData= MutableLiveData<MutableList<MyDetail>>()
        mDetailRepository.getAllDetail(mEventId).observeForever{
            mMutableData.value= it
        }
        return mMutableData
    }

    fun uploadDetail(one:String, two:String) {
        val disposable=mDetailRepository.uploadData(one, two).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe({
            uploadListener?.onSuccess()
        },{
            uploadListener?.onFailure(it.message!!)
        })

        disposables.add(disposable)

    }


}

