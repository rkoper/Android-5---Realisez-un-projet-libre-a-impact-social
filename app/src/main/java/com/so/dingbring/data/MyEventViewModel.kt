package com.so.dingbring.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyEventViewModel(private val mEventRepository: MyEventRepository): ViewModel() {

    fun getAllEvent() : LiveData<MutableList<MyEvent>> {
        val mutableData = MutableLiveData<MutableList<MyEvent>>()
        mEventRepository.getAllEvent().observeForever{
            mutableData.value=it }
        return mutableData
    }

    fun createEvent(myData : MyEvent) {
        mEventRepository.createEvent(myData)
    }


}