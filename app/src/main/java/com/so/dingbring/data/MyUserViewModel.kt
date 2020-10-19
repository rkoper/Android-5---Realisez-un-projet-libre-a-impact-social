package com.so.dingbring.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class MyUserViewModel(private val mUserRepository: MyUserRepository): ViewModel() {



    fun createUser(mDataUser: MutableMap<String, Any>) {
        mUserRepository.createUser(mDataUser)
    }


    fun getUser(mMailUser : String) : LiveData<MyUser>? {
        return mUserRepository.getUser(mMailUser)
    }
}