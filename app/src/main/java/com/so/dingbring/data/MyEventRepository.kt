package com.so.dingbring.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.HashMap

class MyEventRepository {

    private var mEventSet: MutableLiveData<MutableList<MyEvent>> = MutableLiveData()
    private var mEventSetForId:MutableLiveData<MutableList<MyEvent>> = MutableLiveData()
    private var mEventSize: MutableLiveData<MutableList<Int>> = MutableLiveData()
    private var mEventOne: MutableLiveData<MyEvent> = MutableLiveData()
    var eventDate: String? = ""
    var eventUserMail: String? = ""
    var eventName: String? = ""
    var eventOrga: String? = ""
    var eventAddress: String? = ""
    var eventId: String? = ""
    var myData =
        MyEvent(eventDate!!, eventName!!, eventOrga!!, eventAddress!!, eventUserMail!!, eventId!!)
    var myDataForId =
        MyEvent(eventDate!!, eventName!!, eventOrga!!, eventAddress!!, eventUserMail!!, eventId!!)

    private val dbFire = FirebaseFirestore.getInstance()

    fun getAllEvent(): LiveData<MutableList<MyEvent>> {
        val mutableList = mutableListOf<MyEvent>()
        dbFire.collection("event").addSnapshotListener { querySnapshot, exception ->
            if (querySnapshot != null) {
                for (document in querySnapshot.documents) {
                    eventDate = document.getString("eventDate")
                    eventUserMail = document.getString("eventUserMail")
                    eventName = document.getString("eventName")
                    eventOrga = document.getString("eventOrga")
                    eventAddress = document.getString("eventAddress")
                    eventId = document.getString("eventId")
                    myData = MyEvent(
                        eventDate!!,
                        eventName!!,
                        eventOrga!!,
                        eventAddress!!,
                        eventUserMail!!,
                        eventId!!
                    )
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
        items["eventOrga"] = myData.mEventOrga
        items["eventUserMail"] = myData.mEventUserMail
        items["eventId"] = myData.mEventId

        dbFire.collection("event").document(myData.mEventId).set(items)

        return dbFire.collection("event").document().id
    }
    fun getSelectedEvent(mEventUser: ArrayList<String>): LiveData<MutableList<MyEvent>> {

        dbFire.collection("event").addSnapshotListener { querySnapshot, exception ->
            if (querySnapshot != null) {
                val mutableList = mutableListOf<MyEvent>()
                for (document in querySnapshot.documents) {
                    if (mEventUser.contains(document.getString("eventId")) ){
                        eventDate = document.getString("eventDate")
                        eventUserMail = document.getString("eventUserMail")
                        eventName = document.getString("eventName")
                        eventOrga = document.getString("eventOrga")
                        eventAddress = document.getString("eventAddress")
                        eventId = document.getString("eventId")
                        myData = MyEvent(
                            eventDate!!,
                            eventName!!,
                            eventOrga!!,
                            eventAddress!!,
                            eventUserMail!!,
                            eventId!!
                        )

                        mutableList.add(myData)}
                }
                mEventSet.value = mutableList
            }
        }
        return mEventSet
    }
}
