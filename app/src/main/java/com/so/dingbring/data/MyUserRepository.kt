package com.so.dingbring.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class MyUserRepository {

    private val dbFire = FirebaseFirestore.getInstance()
    private var mUserSet: MutableLiveData<MyUser> = MutableLiveData()
    private var mEventIdSet: MutableLiveData<MutableList<String>> = MutableLiveData()

    fun createUser(mDataUser: MutableMap<String, Any>){
        dbFire.collection("user").document(mDataUser["DocIdUser"].toString()).set(mDataUser)
    }

    fun getUser(mUserEmail:String) : LiveData<MyUser>?  {
        dbFire.collection("user").whereEqualTo("EmailUser", mUserEmail)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    val mNameUser: String? = doc.getString("NameUser")
                    val mMailUser: String? = doc.getString("EmailUser")
                    val mPicUser: String? = doc.getString("PhotoUser")
                    val mDocIdUser: String? = doc.getString("DocIdUser")
                    val myUser = MyUser(mNameUser!!, mMailUser!!, mPicUser!!, mDocIdUser!!)

                    mUserSet.value = myUser
                } }
            .addOnFailureListener { exception -> println("Error getting documents: " + exception) }
        return mUserSet
    }
}
