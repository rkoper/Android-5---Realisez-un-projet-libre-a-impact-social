package com.so.dingbring.view.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.so.dingbring.R
import com.so.dingbring.data.MyEventViewModel
import com.so.dingbring.data.MyItemViewModel
import com.so.dingbring.data.MyItem
import com.so.dingbring.databinding.FragmentDetailBinding
import kotlinx.android.synthetic.main.fragment_create.*
import kotlinx.android.synthetic.main.fragment_detail.*
import org.koin.android.viewmodel.ext.android.viewModel

class DetailFragment : Fragment() {
    var mEventName = ""
    var mEventDate = ""
    var mEventOrga = ""
    var mEventAddress = ""
    var mEventId = ""

    private lateinit var mBinding: FragmentDetailBinding
    private lateinit var mDetailAdapterOne: DetailAdapterOne
    private lateinit var mDetailAdapterTwo: DetailAdapterTwo
    private val mItemVM by viewModel<MyItemViewModel>()
    private val mEventVM by viewModel<MyEventViewModel>()

    var i : Int = 1
    var mStatus: String = "I bring"
    var mNameItem: String = "<3"
    var mQuantity: String = "1"
    var mUser: String = "1"
    var mIdItem: String = "1"

    var mStatusList = arrayListOf<String>()
    var mItemList = arrayListOf<String>()
    var mQuantityList = arrayListOf<String>()
    var mUserList = arrayListOf<String>()
    var mItemIdList = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)

        initView(mBinding)
        initHeader(mBinding)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCreateItem() }

    private fun initView(mBinding: FragmentDetailBinding) {
        mBinding.detailReturn.setOnClickListener {
            it.findNavController().navigate(R.id.action_detailFragment_to_homeFragment) } }


    private fun initHeader(mBinding: FragmentDetailBinding) {
        mEventId = arguments?.get("eventId").toString()

        mEventVM.getAllEvent().observe(requireActivity(), {
            it.forEach { myEvent ->
                if (myEvent.mEventId == mEventId) {
                    mBinding.detailNameEvent.text = myEvent.mEventName
                    mEventName = myEvent.mEventName
                    mBinding.detailDate.text = myEvent.mEventDate
                    mEventDate = myEvent.mEventDate
                    mBinding.detailAddress.text = myEvent.mEventAdress
                    mEventAddress = myEvent.mEventAdress
                    mBinding.detailOrga.text = myEvent.mEventOrga
                    mEventOrga = myEvent.mEventOrga
                    mEventId = myEvent.mEventId } } }) }

    private fun initRV() {
        mDetailAdapterOne= DetailAdapterOne(requireActivity() , object:DetailAdapterOne.ItemSelectedListener {
            override fun onItemSelected(mItemID: String, mItemState: String, mCase : Int) {
                mItemVM.updateStatus(mItemID, mEventId, mItemState, mCase )} })

        mItemVM.getAllItem(mEventId).observeForever {mlmi ->
            mlmi.sortBy { it.mItemStatus }
            mBinding.recyclerViewDetailOne.layoutManager= LinearLayoutManager(context)
            mBinding.recyclerViewDetailOne.adapter= mDetailAdapterOne

            mlmi.forEach { mStatusList.add(it.mItemStatus)
                 mItemList.add(it.mItemName)
                 mQuantityList.add(it.mItemQty)
                 mUserList.add(it.mItemUser)
                 mItemIdList.add(it.mItemId)
                 mUser = it.mItemUser
                 mIdItem = it.mItemId }


            mDetailAdapterOne.setListDetail(mStatusList,mItemList,mQuantityList,mUserList,mItemIdList) }

        mDetailAdapterOne.notifyDataSetChanged() }

    private fun initCreateItem() {
        initItem()
        initRV()
        initQuantity()
        initStatus()
        detail_add?.setOnClickListener {
            initItem()
            if (mNameItem == "") { Toast.makeText(requireContext(), "Please add item <3", Toast.LENGTH_LONG).show()}
            else{  mStatusList.add(mStatus)
                mItemList.add(mNameItem)
                mQuantityList.add(mQuantity)
                mUserList.add(mUser)
                mItemIdList.add(mIdItem)

                createItem()
                mDetailAdapterOne.notifyDataSetChanged() }

            println("-|Stat |-" + mStatus  +  "-| item|-" + mNameItem + "-| qté|-" + mQuantity  )
            } }


    private fun initStatus() {
        detail_status_bring?.isChecked = true
        mStatus = "I bring"
        detail_status_need?.setOnCheckedChangeListener { _, b ->
            if (b) { mStatus = "I need"; detail_status_bring.isChecked = false; } }
        detail_status_bring?.setOnCheckedChangeListener { _, b ->
            if (b) { mStatus = "I bring" ; detail_status_need.isChecked = false; } } }

    private fun initQuantity() {
        detail_quantity_item.text = i.toString()
        detail_plus.setOnClickListener {
            i = i.plus(1) ; mQuantity = i.toString()
            detail_quantity_item.text = i.toString() ; mQuantity = i.toString()}

        detail_minus.setOnClickListener {
            if (i != 1) { i =  i.minus(1) ;detail_quantity_item.text = i.toString() ;   mQuantity = i.toString()}
            else {i = 1 ;  mQuantity = i.toString()} }  }

    private fun initItem() {
        mNameItem = detail_item.text.toString() }

    private fun createItem() {
        mItemVM.createItem(mStatus, mNameItem, mQuantity, mUser, mIdItem )  }
}
