package com.so.dingbring.view.detail

import android.annotation.SuppressLint
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
import com.so.dingbring.data.MyItem
import com.so.dingbring.data.MyItemViewModel
import com.so.dingbring.databinding.FragmentDetailBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_detail.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class DetailFragment : Fragment() {
    var mEventName = ""
    var mEventDate = ""
    var mEventOrga = ""
    var mEventAddress = ""
    var mEventId = ""
    var mNb = 0

    private lateinit var mBinding: FragmentDetailBinding
    private lateinit var mDetailAdapter: DetailAdapter

    private val mItemVM by viewModel<MyItemViewModel>()
    private val mEventVM by viewModel<MyEventViewModel>()

    var i: Int = 1
    var mItemStatus: String = "I bring"
    var mItemName: String = "<3"
    var mItemQuantity: String = "1"
    var mItemUniqueID = UUID.randomUUID().toString()
    var mListMyItem = arrayListOf<MyItem>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        initView(mBinding)
        initHeader(mBinding)
        return mBinding.root}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRetrieveItem()
        initCreateItem() }


    @SuppressLint("CheckResult")
    private fun initRetrieveItem() {
        mDetailAdapter = DetailAdapter(requireContext(), mListMyItem)
        mBinding.recyclerViewDetailOne.layoutManager = LinearLayoutManager(context)
        mBinding.recyclerViewDetailOne.adapter = mDetailAdapter
        initRVObserver()

        with(mDetailAdapter){

        itemClickFull.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
            .subscribe { data -> mItemVM.updateStatus(data, 1)
                initRVObserver()}

        itemClickEmpty.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
            .subscribe { data -> mItemVM.updateStatus(data, 2)
                initRVObserver()}

        itemClickN.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
            .subscribe { data -> mItemVM.updateStatus(data, 3)
                initRVObserver()}}}


   private fun initRVObserver() {
       mItemVM.getTestItem(mEventId).observeForever { mlmi ->
           mlmi.sortBy { it.mItemStatus }
           with(mListMyItem){
               clear()
                addAll(mlmi)}
          mListMyItem.let { mDetailAdapter.notifyDataSetChanged() }

   } }


    private fun initView(mBinding: FragmentDetailBinding) {
        mBinding.detailReturn.setOnClickListener {
            it.findNavController().navigate(R.id.action_detailFragment_to_homeFragment) } }

    private fun initHeader(mBinding: FragmentDetailBinding) {
        mEventId = arguments?.get("eventId").toString()
        mEventVM.getAllEvent().observe(requireActivity(), {
            it.forEach { myEvent ->
                if (myEvent.mEventId == mEventId) {
                    with(mBinding){
                    detailNameEvent.text = myEvent.mEventName
                    detailDate.text = myEvent.mEventDate
                    detailAddress.text = myEvent.mEventAdress
                    detailOrga.text = myEvent.mEventOrga}

                    mEventName = myEvent.mEventName
                    mEventDate = myEvent.mEventDate
                    mEventAddress = myEvent.mEventAdress
                    mEventOrga = myEvent.mEventOrga
                    mEventId = myEvent.mEventId
                } } }) }


    private fun initCreateItem() {
        initItem(); initQuantity(); initStatus();
        detail_add?.setOnClickListener {
            initItem()
            if (mItemName == "") { Toast.makeText(requireContext(), "Please add item <3", Toast.LENGTH_LONG).show() }
            else { val mMyItem = MyItem(mItemStatus, mItemQuantity, mItemName, mEventOrga, mItemUniqueID, mEventId)
                mItemVM.createUniqueItem(mMyItem)
                initRVObserver()
            }
        }
    }


    private fun initStatus() {
        detail_status_bring?.isChecked = true
        mItemStatus = "I bring"
        detail_status_need?.setOnCheckedChangeListener { _, b -> if (b) { mItemStatus = "I need"
                detail_status_bring.isChecked = false; } }

        detail_status_bring?.setOnCheckedChangeListener { _, b -> if (b) { mItemStatus = "I bring"
                detail_status_need.isChecked = false; } } }

    private fun initQuantity() {
        detail_quantity_item.text = i.toString()
        detail_plus.setOnClickListener {
            i = i.plus(1)
            mItemQuantity = i.toString()
            detail_quantity_item.text = i.toString()
            mItemQuantity = i.toString() }

        detail_minus.setOnClickListener {
            if (i != 1) { i = i.minus(1)
                detail_quantity_item.text = i.toString()
                mItemQuantity = i.toString() }
            else { i = 1
                mItemQuantity = i.toString() } } }

    private fun initItem() {
        mItemName = detail_item.text.toString() }

}


