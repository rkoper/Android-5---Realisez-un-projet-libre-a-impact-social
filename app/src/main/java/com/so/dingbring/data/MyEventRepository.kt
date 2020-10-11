package com.so.dingbring.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import kotlin.collections.HashMap

class MyEventRepository {

    private var mEventSet: MutableLiveData<MutableList<MyEvent>> = MutableLiveData()
    private var mEventSize: MutableLiveData<MutableList<Int>> = MutableLiveData()
    private var mEventOne: MutableLiveData<MyEvent> = MutableLiveData()



    private val dbFire= FirebaseFirestore.getInstance()

    fun getAllEvent(): LiveData<MutableList<MyEvent>> {
        val mutableList= mutableListOf<MyEvent>()
        dbFire.collection("event").addSnapshotListener { querySnapshot, exception ->
            if (querySnapshot != null) {
                    for (document in querySnapshot.documents) {
                    val eventDate: String? = document.getString("eventDate")
                        val eventUserMail: String? = document.getString("eventUserMail")
                        val eventName: String? = document.getString("eventName")
                        val eventOrga: String? = document.getString("eventOrga")
                        val eventAddress: String? = document.getString("eventAddress")
                        val eventId: String? = document.getString("eventId")
                        val myData = MyEvent(
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

        fun createEvent(myData : MyEvent) : String{
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
    fun getEventById(mEventID : String): LiveData<MyEvent> {
         var mEventOne: MutableLiveData<MyEvent> = MutableLiveData()
        dbFire.collection("event").document(mEventID).addSnapshotListener { querySnapshot, exception ->
            if (querySnapshot != null) {
                if (querySnapshot.contains(mEventID)){
                    val eventDate: String? = querySnapshot.getString("eventDate")
                    val eventUserMail: String? = querySnapshot.getString("eventUserMail")
                    val eventName: String? = querySnapshot.getString("eventName")
                    val eventOrga: String? = querySnapshot.getString("eventOrga")
                    val eventAddress: String? = querySnapshot.getString("eventAddress")
                    val eventId: String? = querySnapshot.getString("eventId")
                    var  myData = MyEvent(
                        eventDate!!,
                        eventName!!,
                        eventOrga!!,
                        eventAddress!!,
                        eventUserMail!!,
                        eventId!!
                    )
                    mEventOne.value = myData
                }}
        }
        return mEventOne
    }
}


