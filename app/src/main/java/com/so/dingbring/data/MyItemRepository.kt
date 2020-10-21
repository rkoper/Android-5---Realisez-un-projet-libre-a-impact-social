package com.so.dingbring.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.so.dingbring.view.detail.DetailAdapter
import java.util.*
import kotlin.collections.HashMap

class MyItemRepository {

    private var mItemSet: MutableLiveData<MutableList<MyItem>> = MutableLiveData()

    private val dbFire = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val userId = UUID.randomUUID().toString()

    fun getAllItem(): LiveData<MutableList<MyItem>> {
        val mDetailMutableList = mutableListOf<MyItem>()

        dbFire.collection("item").addSnapshotListener { querySnapshot, _ ->
            if (querySnapshot != null) {
                for (doc in querySnapshot.documents) {
                    val mIStatus: String? = doc.getString("itemStatus")
                    val mIQty: String? = doc.getString("itemQty")
                    val mIName: String? = doc.getString("itemName")
                    val mEUser: String? = doc.getString("itemUser")
                    val mIEventId: String? = doc.getString("ItemEventId")
                    val mIDocId: String? = doc.id
                    val myDetail =
                        MyItem(mIStatus!!, mIQty!!, mIName!!, mEUser!!, mIDocId!!, mIEventId!!)
                    mDetailMutableList.add(myDetail)
                }
                mItemSet.value = mDetailMutableList
            }
        }
        println("mItemSet--------" + mItemSet.toString())
        return mItemSet
    }


    fun getTestItem(mEventId:String) : LiveData<MutableList<MyItem>>  {
        val mDetailMutableList = mutableListOf<MyItem>()
        dbFire.collection("item")
            .whereEqualTo("ItemEventId", mEventId)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    val mIStatus: String? = doc.getString("itemStatus")
                    val mIQty: String? = doc.getString("itemQty")
                    val mIName: String? = doc.getString("itemName")
                    val mEUser: String? = doc.getString("ItemUserID")
                    val mIEventId: String? = doc.getString("ItemEventId")
                    val mIDocId: String? = doc.id
                    val myDetail =
                        MyItem(mIStatus!!, mIQty!!, mIName!!, mEUser!!, mIDocId!!, mIEventId!!)
                    mDetailMutableList.add(myDetail)
                }
                mItemSet.value = mDetailMutableList
            }
            .addOnFailureListener { exception -> println("Error getting documents: " + exception) }
        return mItemSet
    }


        fun createItem(mListItem: ArrayList<MyItem>) {
            mListItem.forEach { mMyItem ->
                var mItemEach = HashMap<String, String>()
                var mUniqueID = UUID.randomUUID().toString()
                mItemEach["itemStatus"] = mMyItem.mItemStatus
                mItemEach["itemName"] = mMyItem.mItemName
                mItemEach["itemUser"] = mMyItem.mItemUser
                mItemEach["itemQty"] = mMyItem.mItemQty
                mItemEach["itemId"] = mUniqueID
                mItemEach["ItemEventId"] = mMyItem.mItemEventId
                dbFire.collection("item").document(mUniqueID).set(mItemEach) } }

    fun createUniqueItem(mListItem: MyItem) {
            var mItemEach = HashMap<String, String>()
            var mUniqueID = UUID.randomUUID().toString()
            mItemEach["itemStatus"] = mListItem.mItemStatus
            mItemEach["itemName"] = mListItem.mItemName
            mItemEach["itemUser"] = mListItem.mItemUser
            mItemEach["itemQty"] = mListItem.mItemQty
            mItemEach["itemId"] = mUniqueID
            mItemEach["ItemEventId"] = mListItem.mItemEventId
            dbFire.collection("item").document(mUniqueID).set(mItemEach) }



        fun updateStatusItem(mData: MyItem, i: Int) : String {
            if (i == 1)
            {dbFire.collection("item").document(mData.mItemId).update("itemStatus", "I need")}
            if (i == 2)
            {dbFire.collection("item").document(mData.mItemId).update("itemStatus", "I bring")}
            if (i == 3)
            {dbFire.collection("item").document(mData.mItemId)
                .update("itemQty", mData.mItemQty.toInt().plus(1).toString())}


            return dbFire.collection("item").document(mData.mItemId).id
        }

    }

