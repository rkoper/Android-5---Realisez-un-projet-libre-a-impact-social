package com.so.dingbring.data

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyEventViewModel(private val mEventRepository: MyEventRepository): ViewModel() {


    fun getEventrById(mEventId:String) : LiveData<MyEvent> { return mEventRepository.getEventById(mEventId) }

    fun createEvent(myData : MyEvent) : String{ return  mEventRepository.createEvent(myData) }

    fun getUserEvent(
        mEventUser: ArrayList<String>,
        requireActivity: FragmentActivity
    ): MutableLiveData<ArrayList<ArrayList<String>>>{
        return  mEventRepository.getUserEvent(mEventUser,requireActivity)
    }







}