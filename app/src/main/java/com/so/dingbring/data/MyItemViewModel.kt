package com.so.dingbring.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.so.dingbring.view.detail.DetailAdapter

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


    // ON A GARFE TOUTE LES ITMES !!!!!!!!!!!!!!!!!!!!!!!!!!

    fun createItem(mMyItem :ArrayList<MyItem>) { mItemRepository.createItem(mMyItem) }

    fun createUniqueItem(mMyItem :MyItem) { mItemRepository.createUniqueItem(mMyItem) }

    fun updateStatus(myItem: MyItem, i: Int) : String {
        mItemRepository.updateStatusItem( myItem, i )
    return mItemRepository.updateStatusItem( myItem, i )  }

}