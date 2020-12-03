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

    fun  getItem(mEventId:String): LiveData<ArrayList<ArrayList<String>>>  { return mItemRepository.getItem(mEventId) }


    fun createUniqueItem(mMyItem :MyItem) { mItemRepository.createUniqueItem(mMyItem) }

    fun updateStatus(myItem: HashMap<Int, java.util.ArrayList<String>>) { mItemRepository. updateStatusItem( myItem) }

}