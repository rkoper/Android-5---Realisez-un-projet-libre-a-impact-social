package com.so.dingbring.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.ArrayList

class MyUserRepository {

    private val dbFire = FirebaseFirestore.getInstance()
    private var mUserSet: MutableLiveData<MyUser> = MutableLiveData()
    private var mUserIDSet: MutableLiveData<Boolean>? = MutableLiveData()
    private var mUserListSet: MutableLiveData<MutableList<MyUser>> = MutableLiveData()

    fun createUser(mDataUser: MutableMap<String, Any>){
        dbFire.collection("user").document(mDataUser["DocIdUser"].toString()).set(mDataUser)
    }


    fun getUserById(mUserId:String) : LiveData<MyUser>  {
        dbFire.collection("user").whereEqualTo("DocIdUser", mUserId)
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

                    mUserSet?.value = myUser
                } }

        return mUserSet
    }

    fun getifNewUser(mUserId:String) : LiveData<Boolean>? {
        var a = false
        dbFire.collection("user").whereEqualTo("DocIdUser", mUserId)
            .get()
            .addOnCompleteListener { documents ->
                a = documents.result.isEmpty
                mUserIDSet?.postValue(a)
            }

        return mUserIDSet
    }


    fun updateUserName(mUserId: String, mUserName: String) {
        dbFire.collection("user").document(mUserId)
            .update("NameUser", mUserName)

    }

    fun updateUserPhoto(mUserId: String, mUserPhoto: String) {
        dbFire.collection("user").document(mUserId)
            .update("PhotoUser", mUserPhoto)

    }

    fun upadateEventUser(mIDUser: String, mEventUniqueID: String){
        dbFire.collection("user")
            .document(mIDUser)
            .update("eventUser", FieldValue.arrayUnion(mEventUniqueID))
    }





}
