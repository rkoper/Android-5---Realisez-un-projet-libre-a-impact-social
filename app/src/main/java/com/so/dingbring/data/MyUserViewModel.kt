package com.so.dingbring.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyUserViewModel(private val mUserRepository: MyUserRepository): ViewModel() {



    fun createUser(mDataUser: MutableMap<String, Any>) {
        mUserRepository.createUser(mDataUser)
    }


    fun getUserByMail(mMailUser : String) : LiveData<MyUser>? {
        return mUserRepository.getUserByMail(mMailUser)
    }


    fun  getUserByID(myItem: MyItem): LiveData<MutableList<MyDetailItem>> {
        return mUserRepository.getUserByID(myItem) }

}