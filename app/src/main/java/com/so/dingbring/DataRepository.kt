package com.so.dingbring

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Completable
import java.util.*
import kotlin.collections.HashMap

class DataRepository {

    private var dataSet: MutableLiveData<MutableList<MyData>> = MutableLiveData()


    private val fStore= FirebaseFirestore.getInstance()
    private val storage= FirebaseStorage.getInstance()
    private val userId= UUID.randomUUID().toString()
    private val  ref=storage.getReference("/image/$userId")
    fun getAllInfo(): LiveData<MutableList<MyData>> {
        val mutableList= mutableListOf<MyData>()
        fStore.collection("event").addSnapshotListener { querySnapshot, exception ->

            if (exception != null) {  }
            else {

                if (querySnapshot != null) {
                    for (document in querySnapshot.documents) {

                        val eventDate: String? = document.getString("eventDate")
                        val eventName: String? = document.getString("eventName")

                        println("-----------eventName------------------" + eventName)
                        println("----------eventDate-------------------" + eventDate)

                        val myData =
                            MyData(
                                    eventDate!!,
                                    eventName!!
                            )
                        mutableList.add(myData)
                    }

                    dataSet.value = mutableList
                } else {
                    //
                }

            }
        }
        return dataSet

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


