package com.so.dingbring.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyItemViewModel(private val mItemRepository: MyItemRepository): ViewModel() {

 fun getAllItem(): LiveData<MutableList<MyItem>> {
        val mMutableData= MutableLiveData<MutableList<MyItem>>()
        mItemRepository.getAllItem().observeForever{
            mMutableData.value= it }
        return mMutableData }

    fun getTestItem(mEventId:String): LiveData<MutableList<MyItem>> {
        val mMutableData= MutableLiveData<MutableList<MyItem>>()
        mItemRepository.getTestItem(mEventId).observeForever{
            mMutableData.value= it }
        return mMutableData }

    fun createItem(mMyItem :ArrayList<MyItem>) { mItemRepository.createItem(mMyItem) }

    fun updateStatus(myItem : MyItem, i:Int) { mItemRepository.updateStatusItem( myItem, i ) }

}