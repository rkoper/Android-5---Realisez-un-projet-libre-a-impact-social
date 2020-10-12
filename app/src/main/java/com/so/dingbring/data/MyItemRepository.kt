package com.so.dingbring.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Completable
import java.util.*
import kotlin.collections.HashMap

class MyItemRepository {

    private var mItemSet: MutableLiveData<MutableList<MyItem>> = MutableLiveData()

    private val dbFire = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val userId = UUID.randomUUID().toString()

    fun getAllItem(mEventId: String): LiveData<MutableList<MyItem>> {
        val mDetailMutableList = mutableListOf<MyItem>()

        dbFire.collection("item").document(mEventId).collection(mEventId)
            .addSnapshotListener { querySnapshot, exception ->
                if (querySnapshot != null) {
                    for (document in querySnapshot.documents) {
                        val mItemStatus: String? = document.getString("itemStatus")
                        val mItemQty: String? = document.getString("itemQty")
                        val mItemType: String? = document.getString("itemType")
                        val mItemUser: String? = document.getString("itemUser")
                        val mItemIdDoc: String? = document.id
                        val myDetail =
                            MyItem(
                                mItemStatus!!,
                                mItemQty!!,
                                mItemType!!,
                                mItemUser!!,
                                mItemIdDoc!!
                            )
                        mDetailMutableList.add(myDetail)
                    }

                    mItemSet.value = mDetailMutableList
                }
            }
        return mItemSet
    }


    fun createItem(
        mStatusList: ArrayList<String>,
        mItemList: ArrayList<String>,
        mQuantityList: ArrayList<String>,
        mUserName: String,
        mDocId: String
    ) = Completable.create { emitter ->

        for (i in mStatusList.indices) {
            var mItemEach = HashMap<String, String>()
            var mUniqueID = UUID.randomUUID().toString()
            mItemEach["itemStatus"] = mStatusList[i]
            mItemEach["itemType"] = mItemList[i]
            mItemEach["itemUser"] = mUserName
            mItemEach["itemQty"] = mQuantityList[i]
            mItemEach["itemId"] = mUniqueID
            dbFire.collection("item").document(mDocId).collection(mDocId).document(mUniqueID)
                .set(mItemEach)
        }
    }

    fun updateStatusItem(
        mItemID: String,
        mEventId: String,
        mItemStatus: String,
        mCase: Int
    ) = Completable.create { emitter ->

        if (mCase == 1){
        dbFire.collection("item")
            .document(mEventId)
            .collection(mEventId)
            .document(mItemID)
            .update("itemStatus", "I bring")}

        if (mCase == 2 ) {
            dbFire.collection("item")
                .document(mEventId)
                .collection(mEventId)
                .document(mItemID)
                .update("itemStatus", "I need")}

        if (mCase == 3 ){
            dbFire.collection("item")
                .document(mEventId)
                .collection(mEventId)
                .document(mItemID)
                .update("itemQty", mItemStatus.toInt().plus(1).toString())}
        }




}





/*

        items["itemStatus"] = mStatusList[i]
        items["itemType"] = mItemList[i]
        items["itemQty"] = mQuantityList[i]
        items["itemUser"] = mUserName


        dbFire.collection("data").document(mDocId).set(items)
            .addOnSuccessListener { emitter.onComplete() }.addOnFailureListener {}


    */

