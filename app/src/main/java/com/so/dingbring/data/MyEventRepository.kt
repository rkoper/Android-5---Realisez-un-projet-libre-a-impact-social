package com.so.dingbring.data

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.HashMap

class MyEventRepository {

    private var mEventSet: MutableLiveData<MutableList<MyEvent>> = MutableLiveData()
    private var mEventDate: String? = ""
    private var mEventName: String? = ""
    private var mEventAdress: String? = ""
    private var mEventId: String? = ""
    private var mEventUserId: String? = ""
    private var mEventHour: String? = ""
    private var mEventDesc: String? = ""
    private var mEventOrga: String? = ""
    var mNameUser: String? = ""

    private var myData = MyEvent(mEventAdress!!, mEventDate!!, mEventId!!, mEventName!!, mEventHour!!, mEventDesc!!, mEventUserId!!,mEventOrga!!)
    private val dbFire = FirebaseFirestore.getInstance()

    fun createEvent(myData: MyEvent): String {
        val items = HashMap<String, Any>()
        items["eventAddress"] = myData.mEventAdress
        items["eventDate"] = myData.mEventDate
        items["eventId"] = myData.mEventId
        items["eventName"] = myData.mEventName
        items["eventHour"] = myData.mEventHour
        items["eventDesc"] = myData.mEventDesc
        items["eventUserId"] = myData.mEventUserId
        items["eventOrga"] = myData.mEventOrga

        dbFire.collection("event").document(myData.mEventId).set(items)
        return dbFire.collection("event").document().id }


    fun getUserEvent(
        mEventUser: ArrayList<String>,
        requireActivity: FragmentActivity
    ): MutableLiveData<ArrayList<ArrayList<String>>> {

        val mEventU: MutableLiveData<MutableList<MyEvent>> = MutableLiveData()
        val mVtest: MutableLiveData<ArrayList<ArrayList<String>>> = MutableLiveData()
        dbFire.collection("event").get().addOnSuccessListener {
            val mutableList = mutableListOf<MyEvent>()
            for (document in it.documents) {
                if (mEventUser.contains(document.getString("eventId"))) {
                    val  eventDate = document.getString("eventDate")
                    val   eventName = document.getString("eventName")
                    val  eventAddress = document.getString("eventAddress")
                    val  eventId = document.getString("eventId")
                    val  eventUserId = document.getString("eventUserId")
                    val eventHour = document.getString("eventHour")
                    val eventDesc = document.getString("eventDesc")
                    val eventOrga = document.getString("eventOrga")


                    myData = MyEvent(eventAddress!!, eventDate!!, eventId!!, eventName!!, eventHour!!, eventDesc!!, eventUserId!!, eventOrga!!)
                    mutableList.add(myData) } }
            mEventU.value = mutableList }

        val lfDEventMega = arrayListOf<ArrayList<String>>()
        mEventU.observe(requireActivity, Observer {listmyevent ->
            listmyevent.forEach {myevent -> val  a = myevent.mEventUserId
                dbFire.collection("user").get().addOnSuccessListener { querySnapshot ->
                    if (querySnapshot != null) {
                        val mutableList = mutableListOf<MyEvent>()
                    for (doc in querySnapshot.documents) {
                            val lfDEvent = arrayListOf<String>()
                            if (a == doc.getString("DocIdUser")) {
                                val mEventAddress = myevent.mEventAdress
                                val mEventDate    = myevent.mEventDate
                                val mEventId      = myevent.mEventId
                                val mEventName    = myevent.mEventName
                                val mEventUserId  = myevent.mEventUserId
                                val mEventHour    = myevent.mEventHour
                                val mEventDesc    = myevent.mEventDesc
                                val mEventOrga    = myevent.mEventOrga
                                val mNameUser: String? = doc.getString("NameUser")
                                val mPicUser: String? = doc.getString("PhotoUser")

                                lfDEvent.add(mEventAddress)
                                lfDEvent.add(mEventDate)
                                lfDEvent.add(mEventId)
                                lfDEvent.add(mEventName)
                                lfDEvent.add(mEventUserId)
                                lfDEvent.add(mNameUser.toString())
                                lfDEvent.add(mPicUser.toString())
                                lfDEvent.add(mEventHour)
                                lfDEvent.add(mEventDesc)
                                lfDEvent.add(mEventOrga)

                                lfDEventMega.add(lfDEvent)

                            }}
                        mVtest.value = lfDEventMega }} } })
        return mVtest }




    fun getEventById(mEventIdS:String) : LiveData<MyEvent>  {
        val mEventIDSet: MutableLiveData<MyEvent> = MutableLiveData()
        dbFire.collection("event").whereEqualTo("eventId", mEventIdS)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    mEventAdress = document.getString("eventAddress")
                    mEventDate = document.getString("eventDate")
                    mEventId = document.getString("eventId")!!
                    mEventName = document.getString("eventName")
                    mEventHour = document.getString("eventHour")
                    mEventDesc = document.getString("eventDesc")
                    mEventUserId = document.getString("eventUserId")
                    mEventOrga = document.getString("eventOrga")
                    myData = MyEvent(mEventAdress!!, mEventDate!!, mEventId!!, mEventName!!, mEventHour!!, mEventDesc!!, mEventUserId!!,mEventOrga!!)
                    mEventIDSet.value = myData } }
        return mEventIDSet }


}
