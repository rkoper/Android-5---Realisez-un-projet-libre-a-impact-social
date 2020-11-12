package com.so.dingbring.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.HashMap

class MyEventRepository {

    private var mEventSet: MutableLiveData<MutableList<MyEvent>> = MutableLiveData()
    private var mEventIDSet: MutableLiveData<MyEvent> = MutableLiveData()
    var eventDate: String? = ""
    var eventName: String? = ""
    var eventAddress: String? = ""
    var eventId: String? = ""
    var eventUserId: String? = ""
    var eventUserName: String? = ""
    var eventUserPhoto: String? = ""
    var myData = MyEvent(eventDate!!, eventName!!, eventAddress!!, eventId!!,eventUserId!!, eventUserName!!, eventUserPhoto!!)

    private val dbFire = FirebaseFirestore.getInstance()


    fun getAllEvent(): LiveData<MutableList<MyEvent>> {
        val mutableList = mutableListOf<MyEvent>()
        dbFire.collection("event").addSnapshotListener { querySnapshot, exception ->
            if (querySnapshot != null) {
                for (document in querySnapshot.documents) {
                    eventDate = document.getString("eventDate")
                    eventName = document.getString("eventName")
                    eventAddress = document.getString("eventAddress")
                    eventId = document.getString("eventId")
                    eventUserId = document.getString("eventUserId")
                    eventUserName = document.getString("eventUserName")
                    eventUserPhoto = document.getString("eventUserPhoto")
                    myData = MyEvent(
                        eventAddress!!,
                        eventDate!!,
                        eventId!!,
                        eventName!!,
                        eventUserId!!,
                        eventUserName!!,
                        eventUserPhoto!!)
                    mutableList.add(myData)
                }
                mEventSet.value = mutableList
            }
        }
        return mEventSet
    }


    fun createEvent(myData: MyEvent): String {
        val items = HashMap<String, Any>()
        items["eventName"] = myData.mEventName
        items["eventDate"] = myData.mEventDate
        items["eventAddress"] = myData.mEventAdress
        items["eventId"] = myData.mEventId
        items["eventUserId"] = myData.mEventUserId
        items["eventUserPhoto"] = myData.mEventUserPhoto
        items["eventUserName"] = myData.mEventUserName

        dbFire.collection("event").document(myData.mEventId).set(items)

        return dbFire.collection("event").document().id
    }



    fun getUserEvent(mEventUser: ArrayList<String>): LiveData<MutableList<MyEvent>> {

        dbFire.collection("event").addSnapshotListener { querySnapshot, exception ->
            if (querySnapshot != null) {
                val mutableList = mutableListOf<MyEvent>()
                for (document in querySnapshot.documents) {
                    if (mEventUser.contains(document.getString("eventId")) ){
                        eventDate = document.getString("eventDate")
                        eventName = document.getString("eventName")
                        eventAddress = document.getString("eventAddress")
                        eventId = document.getString("eventId")
                        eventUserId = document.getString("eventUserId")
                        eventUserName = document.getString("eventUserName")
                        eventUserPhoto = document.getString("eventUserPhoto")

                        if (eventUserName.isNullOrBlank()){
                            println("-------> NOT ok " + eventName)}
                        else
                        {    println("-------> ok " + eventName + eventUserName)
                        myData = MyEvent(
                            eventAddress!!,
                         eventDate!!,
                         eventId!!,
                         eventName!!,
                         eventUserId!!,
                         eventUserName!!,
                         eventUserPhoto!!)

                        mutableList.add(myData)}
                }}
                mEventSet.value = mutableList
            }
        }
        return mEventSet
    }


    fun getEventById(mEventId:String) : LiveData<MyEvent>  {
        dbFire.collection("event").whereEqualTo("eventId", mEventId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {

                    eventDate = document.getString("eventDate")
                    eventName = document.getString("eventName")
                    eventAddress = document.getString("eventAddress")
                    eventId = document.getString("eventId")
                    eventUserId = document.getString("eventUserId")
                    eventUserName = document.getString("eventUserName")
                    eventUserPhoto = document.getString("eventUserPhoto")
                    myData = MyEvent(
                        eventAddress!!,
                        eventDate!!,
                        eventId!!,
                        eventName!!,
                        eventUserId!!,
                        eventUserName!!,
                        eventUserPhoto!!)

                    mEventIDSet.value = myData
                } }

        return mEventIDSet
    }









}
