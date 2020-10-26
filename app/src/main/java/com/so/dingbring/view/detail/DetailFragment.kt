package com.so.dingbring.view.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.so.dingbring.R
import com.so.dingbring.data.*
import com.so.dingbring.databinding.ActivityMainBinding
import com.so.dingbring.databinding.FragmentDetailBinding
import com.so.dingbring.view.login.LoginActivity
import com.so.dingbring.view.main.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_detail.*
import nl.dionsegijn.steppertouch.OnStepCallback
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class DetailFragment : Fragment() {
    var mEventId = ""

    private lateinit var mBg: FragmentDetailBinding
    private lateinit var mDetailAdapter: DetailAdapter

    private val mItemVM by viewModel<MyItemViewModel>()
    private val mEventVM by viewModel<MyEventViewModel>()
    private val mUserVM by viewModel<MyUserViewModel>()

    var i: Int = 1
    var mItemStatus: String = "I bring"
    var mItemName: String = "<3"
    var mItemQuantity: String = "1"
    var mItemUniqueID = UUID.randomUUID().toString()
    var mListMyItem = arrayListOf<MyItem>()
    private lateinit var mBgMain: ActivityMainBinding
    var mNameUser = "..."
    var mPhotoUser = "..."
    var mIdUser = "////"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBg = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)

        mEventId = arguments?.get("GlobalIdEvent").toString()
        mIdUser=  arguments?.get("GlobalIdUSer").toString()
        initHeader()
        return mBg.root}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRetrieveItem()
        initCreateItem()
        hideBottom()
        initBack()
        prepareToShare()}

    private fun prepareToShare() {
       mBg.detailShare.setOnClickListener {
           println("--- GO----> SHARE----> LINK -----")
       }
    }

    private fun initBack() {
        mBg.detailBack.setOnClickListener {
            println("--- initBack----> DetailFrag----> -----" + mIdUser + "/ / " )
            val mIntent = Intent(requireContext(), MainActivity::class.java)
            mIntent.putExtra(LoginActivity.USERID, mIdUser)
            startActivity(mIntent)
        }
    }

    private fun hideBottom() {
        val activity = activity as MainActivity?
        activity?.hideBottomBar(true)
    }


    @SuppressLint("CheckResult")
    private fun initRetrieveItem() {
        mDetailAdapter = DetailAdapter(requireContext(), mListMyItem)
        mBg.recyclerViewDetailOne.layoutManager = LinearLayoutManager(context)
        mBg.recyclerViewDetailOne.adapter = mDetailAdapter
        initRVObserver()

        mDetailAdapter.itemClickN.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe { data ->
                    mItemVM.updateStatus(data)
                    initRVObserver()
                }

        }


    private fun initRVObserver() {
        mItemVM.getTestItem(mEventId).observeForever { mlmi ->
            mlmi.sortBy { it.mItemStatus }
            with(mListMyItem){
                clear()
                addAll(mlmi)}

            mListMyItem.let {
        mDetailAdapter.notifyDataSetChanged() }

        } }

  private fun initHeader() {
        mEventVM.getEventrById(mEventId).observe(requireActivity(), { myevent ->

            mBg.detailNameEvent.text = myevent.mEventName
            mBg.detailDate.text = myevent.mEventDate
            mBg.detailAddress.text = myevent.mEventAdress
            mBg.detailOrga.text = myevent.mEventOrga

        }) }


    @SuppressLint("ResourceType")
    private fun initCreateItem() {
        initItem(); initQuantity(); initStatus();
        detail_add?.setOnClickListener {
            initItem()
            if (mItemName == "") { Toast.makeText(
                requireContext(),
                "Please add item <3",
                Toast.LENGTH_LONG
            ).show() }
            else { val mMyItem = MyItem(
                mItemStatus,
                mItemQuantity,
                mItemName,
                mNameUser,
                mItemUniqueID,
                mEventId,
                mPhotoUser
            )
                mItemVM.createUniqueItem(mMyItem)
                initRVObserver() }
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
        detail_quantity_item.count = 1
        detail_quantity_item.minValue = 1
        detail_quantity_item.maxValue = 50
        detail_quantity_item.sideTapEnabled = true
        detail_quantity_item.addStepCallback(object : OnStepCallback {
            override fun onStep(qty: Int, positive: Boolean) {
                mItemQuantity = qty.toString()
                println(" mItemQty ------>$mItemQuantity")
            }
        }) }

    private fun initItem() {
        mItemName = detail_item.text.toString() }

/*
    private fun initView(mBg: FragmentDetailBinding) {
        mBg.detailReturn.setOnClickListener {
            it.findNavController().navigate(R.id.action_detailFragment_to_homeFragment) } }
 */


}
