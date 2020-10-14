package com.so.dingbring.data

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore

class MyUserRepository {

    private val dbFire = FirebaseFirestore.getInstance()


    fun createUser(mDataUser: MutableMap<String, Any>){
        dbFire.collection("user").document(mDataUser["EmailUser"].toString()).set(mDataUser)
    }

}
