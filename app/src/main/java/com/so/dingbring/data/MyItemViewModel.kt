package com.so.dingbring.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.so.dingbring.UploadListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MyItemViewModel(private val mItemRepository: MyItemRepository): ViewModel() {


    var uploadListener: UploadListener?=null
    private val disposables= CompositeDisposable()


    fun getAllItem(mEventId: String): LiveData<MutableList<MyItem>> {
        val mMutableData= MutableLiveData<MutableList<MyItem>>()
        mItemRepository.getAllItem(mEventId).observeForever{
            mMutableData.value= it
        }
        return mMutableData
    }

    fun createItemList(
        mStatusList: ArrayList<String>,
        mItemList: ArrayList<String>,
        mQuantityList: ArrayList<String>,
        mUserName : String,
        mDocId: String
    ) {
        val disposable=mItemRepository.createItemList(mStatusList, mItemList,mQuantityList, mUserName, mDocId)
            .subscribeOn(Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()).subscribe({
                uploadListener?.onSuccess()
            },{
                uploadListener?.onFailure(it.message!!)
            })

        disposables.add(disposable)

    }

    fun createItem(
        mStatus: String,
        mItem: String,
        mQuantity: String,
        mUser : String,
        mDocId: String
    ) {

        mItemRepository.createItem(mStatus, mItem,mQuantity, mUser, mDocId)


    }


    fun updateStatus(mItemID: String, mEventId: String, mItemStatus: String, mCase: Int) {
        val disposable=mItemRepository.updateStatusItem( mItemID ,mEventId, mItemStatus, mCase )
            .subscribeOn(Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()).subscribe({
                uploadListener?.onSuccess()
            },{
                uploadListener?.onFailure(it.message!!)
            })

        disposables.add(disposable)

    }

}