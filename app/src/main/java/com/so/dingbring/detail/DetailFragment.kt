package com.so.dingbring.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.so.dingbring.R
import com.so.dingbring.databinding.FragmentDetailBinding
import com.so.dingbring.databinding.FragmentHomeBinding
import org.koin.android.viewmodel.ext.android.viewModel

class DetailFragment : Fragment() {

    var mEventName = ""
    var mEventDate = ""
    var mEventId = ""
    private lateinit var mBinding: FragmentDetailBinding
    private lateinit var mDetailAdapterOne: DetailAdapterOne
    private lateinit var mDetailAdapterTwo: DetailAdapterTwo
    private val mDetailViewModel by viewModel<DetailViewModel>()
    var mDataOne = arrayListOf<MyDetail>()
    val mDataTwo = arrayListOf<MyDetail>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail,container,false)

        initView(mBinding)
        initHeader(mBinding)
        initRV(mBinding)

        return mBinding.root
    }
    private fun initView(mBinding: FragmentDetailBinding) {
        mBinding.detailReturn.setOnClickListener {
            it.findNavController().navigate(R.id.action_detailFragment_to_homeFragment) } }


    private fun initHeader(mBinding: FragmentDetailBinding) {
        mEventName = arguments?.get("eventName").toString()
        mEventDate = arguments?.get("eventDate").toString()
        mEventId = arguments?.get("eventId").toString()

        mBinding.fragDetailName.text = mEventName
        mBinding.fragDetailDate.text = mEventDate
        mBinding.fragDetailId.text = mEventId
    }

    private fun initRV(mBinding: FragmentDetailBinding) {
        mDetailAdapterOne= DetailAdapterOne(requireActivity())
        mDetailAdapterTwo= DetailAdapterTwo(requireActivity())
        mBinding.recyclerViewDetailOne.layoutManager= LinearLayoutManager(context)
        mBinding.recyclerViewDetailTwo.layoutManager= LinearLayoutManager(context)
        mBinding.recyclerViewDetailOne.adapter= mDetailAdapterOne
        mBinding.recyclerViewDetailTwo.adapter= mDetailAdapterTwo


        mDetailViewModel.getAllDetail(mEventId).observe(requireActivity(), Observer {
            it.forEach {myD ->
                if (myD.mItemStatus == "I need" )
                { mDataOne.add(myD)}
                else
                {mDataTwo.add(myD)}

            }
            mDetailAdapterOne.setListDetail(mDataOne)
            mDetailAdapterTwo.setListDetail(mDataTwo)
            mDetailAdapterOne.notifyDataSetChanged()
            mDetailAdapterTwo.notifyDataSetChanged()

        })
    }


}
