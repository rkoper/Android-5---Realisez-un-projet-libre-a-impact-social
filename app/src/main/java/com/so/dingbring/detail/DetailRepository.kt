package com.so.dingbring.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Completable
import java.util.*
import kotlin.collections.HashMap

class DetailRepository {

    private var mDetailSet: MutableLiveData<MutableList<MyDetail>> = MutableLiveData()


    private val fStore= FirebaseFirestore.getInstance()
    private val storage= FirebaseStorage.getInstance()
    private val userId= UUID.randomUUID().toString()

    fun getAllDetail(mEventId: String): LiveData<MutableList<MyDetail>> {
        val mDetailMutableList= mutableListOf<MyDetail>()

        fStore.collection("item").document(mEventId).collection(mEventId).addSnapshotListener { querySnapshot, exception ->
            if (querySnapshot != null) {
                for (document in querySnapshot.documents) {
                    val mItemStatus: String? = document.getString("itemStatus")
                    val mItemQty: String? = document.getString("itemQty")
                    val mItemType: String? = document.getString("itemType")
                    val mItemUser: String? = document.getString("itemUser")
                    val myDetail =
                        MyDetail(
                            mItemStatus!!,
                            mItemQty!!,
                            mItemType!!,
                            mItemUser!!
                        )
                    mDetailMutableList.add(myDetail)
                }

                mDetailSet.value = mDetailMutableList }}
        return mDetailSet }


    fun uploadData(one:String, two:String)= Completable.create { emitter->
        val items = HashMap<String, Any>()
        items["description"] = one
        items["imageUrl"] = two

        fStore.collection("data").document(userId).set(items)
            .addOnSuccessListener { emitter.onComplete() }.addOnFailureListener {} }

}


