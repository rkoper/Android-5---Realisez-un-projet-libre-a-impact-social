package com.so.dingbring.data

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.HashMap

class MyEventRepository {

    private var mEventSet: MutableLiveData<MutableList<MyEvent>> = MutableLiveData()
    private var mEventIDSet: MutableLiveData<MyEvent> = MutableLiveData()
    var mEventDate: String? = ""
    var mEventName: String? = ""
    var mEventAdress: String? = ""
    var mEventId: String? = ""
    var mEventUserId: String? = ""
    var mEventHour: String? = ""
    var mEventDesc: String? = ""
    var mEventOrga: String? = ""
    var mNameUser: String? = ""
    var mPicUser: String? = ""

    var myData = MyEvent(mEventAdress!!, mEventDate!!, mEventId!!, mEventName!!, mEventHour!!, mEventDesc!!, mEventUserId!!,mEventOrga!!)
    private var mUserSet: MutableLiveData<MyUser> = MutableLiveData()
    private val dbFire = FirebaseFirestore.getInstance()


    fun getAllEvent(): LiveData<MutableList<MyEvent>> {
        val mutableList = mutableListOf<MyEvent>()
        dbFire.collection("event").addSnapshotListener { querySnapshot, exception ->
            if (querySnapshot != null) {
                for (document in querySnapshot.documents) {
                    mEventAdress = document.getString("eventAddress")
                    mEventDate = document.getString("eventDate")
                    mEventId = document.getString("eventId")
                    mEventName = document.getString("eventName")
                    mEventHour = document.getString("eventHour")
                    mEventDesc = document.getString("eventDesc")
                    mEventUserId = document.getString("eventUserId")
                    mEventOrga = document.getString("eventOrga")
                    myData  = MyEvent(mEventAdress!!, mEventDate!!, mEventId!!, mEventName!!, mEventHour!!, mEventDesc!!, mEventUserId!!,mEventOrga!!)
                    mutableList.add(myData) }
                mEventSet.value = mutableList } }
        return mEventSet }


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

        return dbFire.collection("event").document().id
    }






    fun getUserEventv2(mEventUser: ArrayList<String>, requireActivity: FragmentActivity): MutableLiveData<ArrayList<ArrayList<String>>> {
        var mEventU: MutableLiveData<MutableList<MyEvent>> = MutableLiveData()
        var mVtest: MutableLiveData<ArrayList<ArrayList<String>>> = MutableLiveData()

        dbFire.collection("event").addSnapshotListener { querySnapshot, exception ->
            if (querySnapshot != null) {
                val mutableList = mutableListOf<MyEvent>()
                for (document in querySnapshot.documents) {
                    if (mEventUser.contains(document.getString("eventId"))) {

                        mEventAdress = document.getString("eventAddress")
                        mEventDate = document.getString("eventDate")
                        mEventId = document.getString("eventId")
                        mEventName = document.getString("eventName")
                        mEventHour = document.getString("eventHour")
                        mEventDesc = document.getString("eventDesc")
                        mEventUserId = document.getString("eventUserId")
                        mEventOrga = document.getString("eventOrga")
                        myData = MyEvent(mEventAdress!!, mEventDate!!, mEventId!!, mEventName!!, mEventHour!!, mEventDesc!!, mEventUserId!!,mEventOrga!!)
                        mutableList.add(myData)
                    }
                }
                mEventU.value = mutableList

            }
        }


        val mAllList = arrayListOf<ArrayList<String>>()
        mEventU.observe(requireActivity, Observer {listmyevent ->
            println("     ---------------0-------------")
            listmyevent.forEach { myevent ->
                println("     ---------------1-------------")
                mEventUserId = myevent.mEventUserId
                dbFire.collection("user").addSnapshotListener { querySnapshot, exception ->
                    println("     ---------------2-------------")
                    if (querySnapshot != null) {
                        println("     ---------------3-------------")
                        for (doc in querySnapshot.documents) {
                            println("     ---------------4-------------")
                            if (mEventUserId == doc.getString("DocIdUser")) {
                                println("     ---------------5-------------")
                                val mList = arrayListOf<String>()
                                mEventAdress = myData.mEventAdress
                                mEventDate = myData.mEventDate
                                mEventId = myData.mEventId
                                mEventName = myData.mEventName
                                mEventHour = myData.mEventHour
                                mEventDesc = myData.mEventDesc
                                mEventUserId = myData.mEventUserId
                                mEventOrga = myData.mEventOrga
                                mNameUser = doc.getString("NameUser")
                                mPicUser = doc.getString("PhotoUser")

                                mList.add(mEventAdress!!)
                                mList.add(mEventDate!!)
                                mList.add(mEventId!!)
                                mList.add(mEventName!!)
                                mList.add(mEventHour!!)
                                mList.add(mEventDesc!!)
                                mList.add(mEventUserId!!)
                                mList.add(mEventOrga!!)
                                mList.add(mNameUser.toString())
                                mList.add(mPicUser.toString())

                                mAllList.add(mList)


                                println("     YYYYYYYYYYYYYYYYYYYYYYYYYYY    " + mAllList)
                            }
                        }





                    }
                        }
                    }
                    mVtest.value = mAllList
        })
        return mVtest}


    fun getUserEvent(
        mEventUser: ArrayList<String>,
        requireActivity: FragmentActivity
    ): MutableLiveData<ArrayList<ArrayList<String>>> {

        var mEventU: MutableLiveData<MutableList<MyEvent>> = MutableLiveData()

        var mVtest: MutableLiveData<ArrayList<ArrayList<String>>> = MutableLiveData()

        dbFire.collection("event").addSnapshotListener { querySnapshot, exception ->
            if (querySnapshot != null) {
                val mutableList = mutableListOf<MyEvent>()
                for (document in querySnapshot.documents) {
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
                mEventU.value = mutableList } }

        val lfDEventMega = arrayListOf<ArrayList<String>>()
        mEventU.observe(requireActivity, Observer {listmyevent ->
            listmyevent.forEach {myevent -> val  a = myevent.mEventUserId
                dbFire.collection("user").addSnapshotListener { querySnapshot, exception ->
                    if (querySnapshot != null) { for (doc in querySnapshot.documents) {
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

                                lfDEventMega.add(lfDEvent) }}
                        mVtest.value = lfDEventMega }} } })
        return mVtest }




    fun getEventById(mEventIdS:String) : LiveData<MyEvent>  {
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

        return mEventIDSet
    }


}
