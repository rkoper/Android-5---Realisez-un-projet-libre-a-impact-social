package com.so.dingbring.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore

class MyUserRepository {

    private val dbFire = FirebaseFirestore.getInstance()


    fun createUser(mDataUser: MutableMap<String, Any>){
        dbFire.collection("user").document(mDataUser["EmailUser"].toString()).set(mDataUser)
    }
/*
    fun getUser() : LiveData<MutableList<MyUser>>  {
        val mDetailMutableList = mutableListOf<MyItem>()
        dbFire.collection("user").whereEqualTo("ItemEventId", mEventId)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    val mIStatus: String? = doc.getString("itemStatus")
                    val mIQty: String? = doc.getString("itemQty")
                    val mIName: String? = doc.getString("itemName")
                    val mEUser: String? = doc.getString("itemUser")
                    val mIEventId: String? = doc.getString("ItemEventId")
                    val mIDocId: String? = doc.id
                    val myDetail =
                        MyItem(mIStatus!!, mIQty!!, mIName!!, mEUser!!, mIDocId!!, mIEventId!!)
                    mDetailMutableList.add(myDetail)
                }
                mItemSet.value = mDetailMutableList
            }
            .addOnFailureListener { exception -> println("Error getting documents: " + exception) }
        return mItemSet
    }

 */

}
