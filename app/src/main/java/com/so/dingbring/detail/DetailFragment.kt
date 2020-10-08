package com.so.dingbring.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.so.dingbring.R
import com.so.dingbring.databinding.FragmentDetailBinding
import org.koin.android.viewmodel.ext.android.viewModel

class DetailFragment : Fragment() {

    var mEventName = ""
    var mEventDate = ""
    var mEventId = ""
    private lateinit var mBinding: FragmentDetailBinding
    private lateinit var mDetailAdapter: DetailAdapter
    private val mDetailViewModel by viewModel<DetailViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail,container,false)
        initHeader(mBinding)
        initRV(mBinding)
        return mBinding.root
    }

    private fun initHeader(mBinding: FragmentDetailBinding) {
        mEventName = arguments?.get("eventName").toString()
        mEventDate = arguments?.get("eventDate").toString()
        mEventId = arguments?.get("eventId").toString()

        mBinding.fragDetailName.text = mEventName
        mBinding.fragDetailDate.text = mEventDate
        mBinding.fragDetailId.text = mEventId
    }

    private fun initRV(mBinding: FragmentDetailBinding) {
        mDetailAdapter= DetailAdapter(requireActivity())
        mBinding.recyclerViewDetail.layoutManager= LinearLayoutManager(context)
        mBinding.recyclerViewDetail.adapter= mDetailAdapter


        mDetailViewModel.getAllDetail(mEventId).observe(requireActivity(), Observer {
            mDetailAdapter.setListDetail(it)
            mDetailAdapter.notifyDataSetChanged()

        })
    }


}
