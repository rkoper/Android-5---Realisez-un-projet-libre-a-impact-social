package com.so.dingbring.view.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.so.dingbring.R
import com.so.dingbring.data.MyEventViewModel
import com.so.dingbring.data.MyItemViewModel
import com.so.dingbring.data.MyItem
import com.so.dingbring.databinding.FragmentDetailBinding
import org.koin.android.viewmodel.ext.android.viewModel

class DetailFragment : Fragment() {
    var dataList = mutableListOf<MyItem>()
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
    var mDataOne = arrayListOf<MutableList<MyItem>>()
    val mDataTwo = arrayListOf<MyItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)

        initView(mBinding)
        initHeader(mBinding)
        initRV(mBinding)

        return mBinding.root
    }

    private fun initView(mBinding: FragmentDetailBinding) {
        mBinding.detailReturn.setOnClickListener {
            it.findNavController().navigate(R.id.action_detailFragment_to_homeFragment)
        }
    }


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
                    mEventId = myEvent.mEventId
                }

            }
        })
    }

    private fun initRV(mBinding: FragmentDetailBinding) {
        mDetailAdapterOne= DetailAdapterOne(requireActivity(), object:DetailAdapterOne.ItemSelectedListener {
            override fun onItemSelected(mItemID: String, mItemStatus: String) {
                mItemVM.updateStatus(mItemID, mEventId, mItemStatus)} })
      mItemVM.getAllItem(mEventId).observeForever {
          mBinding.recyclerViewDetailOne.layoutManager= LinearLayoutManager(context)
          mBinding.recyclerViewDetailOne.adapter= mDetailAdapterOne
          mDetailAdapterOne.setListDetail(it)

      }




            mDetailAdapterOne.notifyDataSetChanged()

    }
}



