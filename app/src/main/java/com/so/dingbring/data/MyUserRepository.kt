package com.so.dingbring.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import bolts.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MyUserRepository {

    private val dbFire = FirebaseFirestore.getInstance()

    private var mUserSet: MutableLiveData<MyUser> = MutableLiveData()

    private var mTestX : MutableLiveData<String> = MutableLiveData()
    private var mUserEvent: MutableLiveData<ArrayList<String>> = MutableLiveData()
    private var mUserIDSet: MutableLiveData<Boolean>? = MutableLiveData()
    private var mUserListSet: MutableLiveData<MutableList<MyUser>> = MutableLiveData()
    private var mUserSring: MutableLiveData<String> = MutableLiveData()


    private var mUserTest: MutableLiveData<String> = MutableLiveData()

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
                    val mNbUserEvent  = doc.get("eventNbEvent") as Long?
                    val myUser = MyUser(mNameUser!!, mMailUser!!, mPicUser!!, mDocIdUser!!,
                        mEventUser as ArrayList<String>, mNbUserEvent!!
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

/*
    fun getUserEventById(mUserId:String) : LiveData<ArrayList<String>>  {
        var mEventUser: ArrayList<String>
        var mEventUserId: String
        var mEventDate: String
        var mEventAddress: String
        var mEventId: String
        var mUserName: String
        var mUserPhoto: String

        var mList = arrayListOf<String>()
        dbFire.collection("user").whereEqualTo("DocIdUser", mUserId)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    mEventUser = doc.get("eventUser") as ArrayList<String>

                    mEventUser.forEach {mIdEvent ->
                        dbFire.collection("event").document(mIdEvent)
                            .get()
                            .addOnSuccessListener { doc ->
                                mUserName = doc.get("eventName").toString()
                                mEventDate = doc.get("eventDate").toString()
                                mEventAddress = doc.get("eventAddress").toString()

                                mEventUserId = doc.get("eventUserId").toString()
                                mEventId = doc.get("eventId").toString()
                                mList.add(mUserName)
                                mList.add(mEventDate)
                                mList.add(mEventAddress)
                                mList.add(mEventUserId)
                                mList.add(mEventId)
                                dbFire.collection("user").document(mEventUserId)
                                    .get()
                                    .addOnSuccessListener { docu ->
                                        mUserName = docu.get("NameUser").toString()
                                        mUserPhoto = docu.get("PhotoUser").toString()
                                        mList.add(mUserName)
                                        mList.add(mUserPhoto)

                                        mUserEvent.value = mList
                                    } } } } }
        return mUserEvent
    }

 */



    fun getUserNamePhotoById(mUserId:String) : LiveData<String> {
        dbFire.collection("user").whereEqualTo("DocIdUser", mUserId)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    val mNameUser: String? = doc.getString("NameUser")
                    val mPicUser: String? = doc.getString("PhotoUser")
                    mUserSring?.value = mNameUser
                } }
        return mUserSring
    }


}
