package com.so.dingbring.login

import androidx.lifecycle.ViewModel

class LoginViewModel(private val mLoginRepository: LoginRepository): ViewModel() {



    fun createUser(mDataUser: MutableMap<String, Any>) {
        mLoginRepository.createUser(mDataUser)
    }
}