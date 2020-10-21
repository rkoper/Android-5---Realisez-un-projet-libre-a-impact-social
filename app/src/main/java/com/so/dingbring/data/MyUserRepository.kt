package com.so.dingbring.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.ArrayList

class MyUserRepository {

    private val dbFire = FirebaseFirestore.getInstance()
    private var mUserS: MutableLiveData<MutableList<MyDetailItem>> = MutableLiveData()
    private var mUserSet: MutableLiveData<MyUser> = MutableLiveData()
    private var mEventIdSet: MutableLiveData<MutableList<String>> = MutableLiveData()
    var mUserMutableList = mutableListOf<MyDetailItem>()

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

    fun getUserByID(myItem: MyItem) : LiveData<MutableList<MyDetailItem>> {

        var a = "XXXX"
        var b = "YYY"
        var c = myItem.mItemStatus
        var d = myItem.mItemQty
        var e = myItem.mItemName
        var f = myItem.mItemUser
        var g = myItem.mItemId
        var h = myItem.mItemEventId

        dbFire.collection("user").whereEqualTo("DocIdUser", myItem.mItemUser)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    a = doc.getString("PhotoUser").toString()
                    b = doc.getString("NameUser").toString()
                    val myDetailItem = MyDetailItem(c, d, e, f, g, h, a, b)
                    mUserMutableList.clear()
                    mUserMutableList.add(myDetailItem)


                //    println("(_)(_)(_) "+ mUserMutableList.size + " (_)(_)(_)" +  b + myItem)

                }

        mUserS.value = mUserMutableList
            }
            .addOnFailureListener { exception -> println("Error getting documents: " + exception) }
        return mUserS
    }
}
