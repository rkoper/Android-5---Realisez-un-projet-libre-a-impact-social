package com.so.dingbring.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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
                    val mIEventId: String? = doc.getString("itemEventId")
                    val mIDocId: String? = doc.id
                    val mUserPhoto:String? = doc.getString("itemUserPhoto")
                    val myDetail =
                        MyItem(mIStatus!!, mIQty!!, mIName!!, mEUser!!, mIDocId!!, mIEventId!!,mUserPhoto)
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
            .whereEqualTo("itemEventId", mEventId)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    val mIStatus: String? = doc.getString("itemStatus")
                    val mIQty: String? = doc.getString("itemQty")
                    val mIName: String? = doc.getString("itemName")
                    val mEUser: String? = doc.getString("itemUser")
                    val mIEventId: String? = doc.getString("itemEventId")
                    val mIDocId: String? = doc.id
                    val mUserPhoto:String? = doc.getString("itemUserPhoto")
                    val myDetail =
                        MyItem(mIStatus, mIQty, mIName, mEUser, mIDocId, mIEventId, mUserPhoto)
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
            mItemEach["itemStatus"] = mMyItem.mItemStatus.toString()
            mItemEach["itemName"] = mMyItem.mItemName.toString()
            mItemEach["itemUser"] = mMyItem.mItemUser.toString()
            mItemEach["itemQty"] = mMyItem.mItemQty.toString()
            mItemEach["itemId"] = mUniqueID
            mItemEach["itemEventId"] = mMyItem.mItemEventId.toString()
            mItemEach["itemUserPhoto"] = mMyItem.mItemUserPhoto.toString()
            dbFire.collection("item").document(mUniqueID).set(mItemEach) } }

    fun createUniqueItem(mListItem: MyItem) {
        var mItemEach = HashMap<String, String>()
        var mUniqueID = UUID.randomUUID().toString()
        mItemEach["itemStatus"] = mListItem.mItemStatus.toString()
        mItemEach["itemName"] = mListItem.mItemName.toString()
        mItemEach["itemUser"] = mListItem.mItemUser.toString()
        mItemEach["itemQty"] = mListItem.mItemQty.toString()
        mItemEach["itemId"] = mUniqueID
        mItemEach["itemEventId"] = mListItem.mItemEventId.toString()
        mItemEach["itemUserPhoto"] = mListItem.mItemUserPhoto.toString()
        dbFire.collection("item").document(mUniqueID).set(mItemEach) }



    fun updateStatusItem(mData: HashMap<Int, MyItem>) {

        println(" --------->>>>   " + mData.toString())



        if (mData.containsKey(0)) {
            var i=0
            var mShortLink = dbFire.collection("item").document(mData[i]!!.mItemId!!)
            if (mData[i]!!.mItemStatus == "I need" ){mShortLink.update("itemStatus", "I bring")}
            else {mShortLink.update("itemStatus", "I need")}

        }
        if (mData.containsKey(1))
        {    var i=1
            var mShortLink = dbFire.collection("item").document(mData[i]!!.mItemId!!)
            mShortLink.update("itemQty", mData[i]!!.mItemQty.toString().toInt().plus(1).toString())

          //
        }
        if (mData.containsKey(2))
        {    var i=2
            var mShortLink = dbFire.collection("item").document(mData[i]!!.mItemId!!)
            mShortLink.delete()


         //   dbFire.collection("item").document(mData.mItemId.toString()).update("itemQty", mData.mItemQty.toString().toInt().plus(1).toString())
        }

    }

}

