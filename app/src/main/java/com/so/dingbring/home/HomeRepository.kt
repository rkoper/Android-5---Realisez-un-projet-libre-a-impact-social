package com.so.dingbring.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Completable
import java.util.*
import kotlin.collections.HashMap

class HomeRepository {

    private var eventSet: MutableLiveData<MutableList<MyEvent>> = MutableLiveData()


    private val fStore= FirebaseFirestore.getInstance()
    private val storage= FirebaseStorage.getInstance()
    private val userId= UUID.randomUUID().toString()

    fun getAllInfo(): LiveData<MutableList<MyEvent>> {
        val mutableList= mutableListOf<MyEvent>()
        fStore.collection("event").addSnapshotListener { querySnapshot, exception ->
            if (querySnapshot != null) {
                    for (document in querySnapshot.documents) {

                        val eventDate: String? = document.getString("eventDate")
                        val eventName: String? = document.getString("eventName")
                        val eventId:String? = document.id
                        val myData = MyEvent(
                            eventDate!!,
                            eventName!!,
                            eventId!!
                        )
                        mutableList.add(myData)
                    }
                    eventSet.value = mutableList
                }
        }
        return eventSet
    }

    fun uploadData(one:String, two:String)= Completable.create { emitter->
        val items = HashMap<String, Any>()
        items["description"] = one
        items["imageUrl"] = two

        fStore.collection("data").document(userId).set(items)
            .addOnSuccessListener {
                emitter.onComplete()

            }.addOnFailureListener {

            }
    }
}


