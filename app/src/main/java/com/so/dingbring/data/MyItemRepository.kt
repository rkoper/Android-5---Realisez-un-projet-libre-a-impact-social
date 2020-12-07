package com.so.dingbring.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.HashMap

class MyItemRepository {

    private var mItemSet: MutableLiveData<MutableList<MyItem>> = MutableLiveData()
    private var mMegaListItem: ArrayList<ArrayList<String>> = arrayListOf()
    private val dbFire = FirebaseFirestore.getInstance()
    private var mMegaListStringSend: MutableLiveData<ArrayList<ArrayList<String>>> =  MutableLiveData()


    fun getAllItem(): LiveData<MutableList<MyItem>> {
        val mDetailMutableList = mutableListOf<MyItem>()

        dbFire.collection("item").addSnapshotListener { querySnapshot, _ ->
            if (querySnapshot != null) {
                for (doc in querySnapshot.documents) {
                    val mIStatus: String? = doc.getString("itemStatus")
                    val mIQty: String? = doc.getString("itemQty")
                    val mIName: String? = doc.getString("itemName")
                    val mUserId: String? = doc.getString("itemUserId")
                    val mIEventId: String? = doc.getString("itemEventId")
                    val mIDocId: String? = doc.id
                    val myDetail =
                        MyItem(mIStatus!!, mIQty!!, mIName!!, mUserId!!, mIDocId!!, mIEventId!!)
                    mDetailMutableList.add(myDetail)
                }
                mItemSet.value = mDetailMutableList
            }
        }
        return mItemSet
    }

    fun getItem(mEventId:String) : LiveData<ArrayList<ArrayList<String>>> {

        val itemUserId = ""
        var mItemStatus = ""
        var mItemName = ""
        var mItemQty = ""
        var mItemId = ""
        var mItemUserId = ""
        var mItemEventId = ""
        var mUserId = ""
        var mUserName = ""

        var mUserPic = ""
    //    mMegaListItem.clear()



        dbFire.collection("item")
            .whereEqualTo("itemEventId", mEventId)
            .get()
            .addOnSuccessListener { documents ->
                for (docItem in documents) {
                    mMegaListStringSend.value?.clear()
                    val mListItem: ArrayList<String> = arrayListOf()
                    mListItem.clear()
                    mUserId = docItem.getString("itemUserId").toString()
                    mListItem.add(itemUserId)
                    mItemStatus = docItem.getString("itemStatus").toString()
                    mListItem.add(mItemStatus)
                    mItemName = docItem.getString("itemName").toString()
                    mListItem.add(mItemName)
                    mItemQty = docItem.getString("itemQty").toString()
                    mListItem.add(mItemQty)
                    mItemId = docItem.getString("itemId").toString()
                    mListItem.add(mItemId)
                    mItemUserId = docItem.getString("itemUser").toString()
                    mListItem.add(mItemUserId)
                    mItemEventId = docItem.getString("itemEventId").toString()
                    mListItem.add(mItemEventId)

                    dbFire.collection("user").whereEqualTo("DocIdUser", mUserId)
                        .get()
                        .addOnSuccessListener { documents ->
                            for (docUser in documents) {
                                mUserPic = docUser.getString("PhotoUser").toString()
                                mListItem.add(mUserPic)
                                mUserName = docUser.getString("NameUser").toString()
                                mListItem.add(mUserName)
                                mMegaListItem.add(mListItem)
                               }

                            mMegaListStringSend.postValue(mMegaListItem)
                        } } }

            .addOnFailureListener { exception -> println("Error getting documents: $exception") }
        return mMegaListStringSend
    }

    fun createUniqueItem(mListItem: MyItem) {
        val mItemEach = HashMap<String, String>()
        val mUniqueID = UUID.randomUUID().toString()
        mItemEach["itemStatus"] = mListItem.mItemStatus.toString()
        mItemEach["itemName"] = mListItem.mItemName.toString()
        mItemEach["itemUserId"] = mListItem.mItemUserId.toString()
        mItemEach["itemQty"] = mListItem.mItemQty.toString()
        mItemEach["itemId"] = mUniqueID
        mItemEach["itemEventId"] = mListItem.mItemEventId.toString()
        dbFire.collection("item").document(mUniqueID).set(mItemEach) }



    fun updateStatusItem(mData: HashMap<Int, ArrayList<String>>) {

        if (mData.containsKey(0)) { val i=0
            val mItemStatus = mData[i]!![1]
            val mItemId = mData[i]!![4]
        val mShortLink = dbFire.collection("item").document(mItemId)
        if (mItemStatus == "I need" ){mShortLink.update("itemStatus", "I bring")}
        else {mShortLink.update("itemStatus", "I need")}}



        if (mData.containsKey(1)) {    val i=1
            val mItemQty = mData[i]!![3]
            val mItemId = mData[i]!![4]
            val mShortLink = dbFire.collection("item").document(mItemId)
            mShortLink.update("itemQty", mItemQty.toInt().plus(1).toString()) }

        if (mData.containsKey(2)) {    val i=2
            val mItemQty = mData[i]!![3]
            val mItemId = mData[i]!![4]
            val mShortLink = dbFire.collection("item").document(mItemId)
            if (mItemQty.toInt() > 1)
            {  mShortLink.update("itemQty", mItemQty.toInt().minus(1).toString())}}


        if (mData.containsKey(3)) {    val i=3
            val mItemId = mData[i]!![4]
            val mShortLink = dbFire.collection("item").document(mItemId)
            mShortLink.delete()}


    }

}

