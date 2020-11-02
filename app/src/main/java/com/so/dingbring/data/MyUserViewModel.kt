package com.so.dingbring.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyUserViewModel(private val mUserRepository: MyUserRepository): ViewModel() {

    fun createUser(mDataUser: MutableMap<String, Any>) { mUserRepository.createUser(mDataUser) }

    fun getUserById(mUserId : String) : LiveData<MyUser> { return mUserRepository.getUserById(mUserId) }

    fun getifNewUser(mUserId : String) : LiveData<Boolean>? {
        return mUserRepository.getifNewUser(mUserId) }

    fun updateUserName(mUserId:String, mUserName:String) { mUserRepository.updateUserName(mUserId,mUserName )  }

    fun updateUserPhoto(mUserId:String, mUserPhoto:String) { mUserRepository.updateUserPhoto(mUserId, mUserPhoto )  }

    fun upadateEventUser(mIDUser: String, mEventUniqueID: String) {
        mUserRepository.upadateEventUser(mIDUser, mEventUniqueID )  }


}