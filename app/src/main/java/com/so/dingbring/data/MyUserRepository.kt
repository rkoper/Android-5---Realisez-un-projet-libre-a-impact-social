package com.so.dingbring.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.ArrayList

class MyUserRepository {

    private val dbFire = FirebaseFirestore.getInstance()
    private var mUserSet: MutableLiveData<MyUser> = MutableLiveData()


    fun createUser(mDataUser: MutableMap<String, Any>){
        dbFire.collection("user").document(mDataUser["DocIdUser"].toString()).set(mDataUser)
    }

    fun getUserByMail(mUserMail:String) : LiveData<MyUser>?  {
        dbFire.collection("user").whereEqualTo("EmailUser", mUserMail)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    val mNameUser: String? = doc.getString("NameUser")
                    val mMailUser: String? = doc.getString("EmailUser")
                    val mPicUser: String? = doc.getString("PhotoUser")
                    val mDocIdUser: String? = doc.getString("DocIdUser")
                    val mEventUser = doc.get("eventUser")
                    val myUser = MyUser(mNameUser!!, mMailUser!!, mPicUser!!, mDocIdUser!!,
                        mEventUser as ArrayList<String>
                    )


                    mUserSet.value = myUser
                } }
            .addOnFailureListener { exception -> println("Error getting documents: " + exception) }
        return mUserSet
    }


    fun getUserById(mUserMail:String) : LiveData<MyUser>?  {
        dbFire.collection("user").whereEqualTo("DocIdUser", mUserMail)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    val mNameUser: String? = doc.getString("NameUser")
                    val mMailUser: String? = doc.getString("EmailUser")
                    val mPicUser: String? = doc.getString("PhotoUser")
                    val mDocIdUser: String? = doc.getString("DocIdUser")
                    val mEventUser = doc.get("eventUser")
                    val myUser = MyUser(mNameUser!!, mMailUser!!, mPicUser!!, mDocIdUser!!,
                        mEventUser as ArrayList<String>
                    )


                    mUserSet.value = myUser
                } }
            .addOnFailureListener { exception -> println("Error getting documents: " + exception) }
        return mUserSet
    }


    fun updateUserName(mUserId: String, mUserName: String) {
      dbFire.collection("user").document(mUserId)
            .update("NameUser", mUserName)

    }

    fun updateUserPhoto(mUserId: String, mUserPhoto: String) {
        dbFire.collection("user").document(mUserId)
            .update("PhotoUser", mUserPhoto)

    }




}
