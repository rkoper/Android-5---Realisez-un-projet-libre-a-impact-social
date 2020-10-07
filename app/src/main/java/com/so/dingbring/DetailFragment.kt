package com.so.dingbring

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.so.dingbring.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    var mEventName = ""
    var mEventDate = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding:  FragmentDetailBinding= DataBindingUtil.inflate(inflater, R.layout.fragment_detail,container,false)
        initHeader(binding)
        return binding.root
    }

    private fun initHeader(binding: FragmentDetailBinding) {

        mEventName = arguments?.get("eventName").toString()
        mEventDate = arguments?.get("eventDate").toString()

        binding.fragDetailName.text = mEventName
        binding.fragDetailDate.text = mEventDate

    }
}
