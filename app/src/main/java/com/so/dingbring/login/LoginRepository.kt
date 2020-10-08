package com.so.dingbring.login

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore


class LoginRepository {

    private var eventSet: MutableLiveData<MutableList<MyLogin>> = MutableLiveData()


    private val fStore = FirebaseFirestore.getInstance()


    fun createUser(mDataUser: MutableMap<String, Any>){
        fStore.collection("user").document(mDataUser["EmailUser"].toString()).set(mDataUser)
    }




}