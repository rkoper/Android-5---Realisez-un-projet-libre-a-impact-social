package com.so.dingbring.view.event

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gauravk.bubblenavigation.BubbleNavigationLinearView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.so.dingbring.R
import com.so.dingbring.data.MyEventViewModel
import com.so.dingbring.data.MyUserViewModel
import com.so.dingbring.view.base.BaseFragment
import com.so.dingbring.view.login.LoginActivity
import com.so.dingbring.view.main.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_event.*
import org.koin.android.viewmodel.ext.android.viewModel

class EventFragment : BaseFragment() {

    private val mEventVM by viewModel<MyEventViewModel>()
    private val mUserVM by viewModel<MyUserViewModel>()
    private lateinit var mEventAdapter: EventAdapter
    private var mDataEvent: MutableList<MutableList<String>> = mutableListOf()
    var mNameUser = "///"
    private var mIdUser = "////"
    private var mUserEvent = arrayListOf("", "")
    private var mFloat_back : FloatingActionButton? = null
    private var mPosBottomBar: BubbleNavigationLinearView? = null

 override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_event, container, false)
        mIdUser = LoginActivity.mIdUser

     println("----------------------->>" + mIdUser)

        return view }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mUserVM.getUserById(mIdUser).observe(requireActivity(), Observer { mlmu ->
            if (mlmu != null) {
                mUserEvent = mlmu.mEventUser

                println("-------mUserEvent--------------->>" + mUserEvent)
                initRV() } })

        onBackPressed()
        onBackBarPressed()
    }

    private fun onBackBarPressed() {
        mFloat_back = activity?.findViewById(R.id.item_tb_fb_back)
        mFloat_back?.setOnClickListener {
            navToMain()
        }
    }

    private fun onBackPressed() {
        mPosBottomBar = activity?.findViewById(R.id.float_bottom_bar)
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navToMain() } })
    }

    private fun navToMain() {
        val intent = Intent (activity, MainActivity::class.java)
        activity?.startActivity(intent)
    }


    @SuppressLint("CheckResult")
    private fun initRV() {
        val mRecyclerView = view?.findViewById<RecyclerView>(R.id.recyclerview_event)
        mEventAdapter = EventAdapter(requireActivity(), mDataEvent)
        mRecyclerView?.setHasFixedSize(true)
        mRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
        mRecyclerView?.adapter = mEventAdapter

        mEventAdapter.itemClick.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
                val bundle = bundleOf("GlobalIdEvent" to data)
                view?.findNavController()?.navigate(R.id.action_event_fragment_to_detailFragment, bundle) }
        loadRV() }


    private fun loadRV() {

        if (mUserEvent.size == 0) {event_no_event.visibility = View.VISIBLE}

        else { mEventVM.getUserEvent(mUserEvent, requireActivity()).observe(
                requireActivity(), androidx.lifecycle.Observer {listMyEvent ->

                println("-------listMyEvent--------------->>" + listMyEvent)

                mDataEvent.clear()
                mDataEvent.addAll(listMyEvent)
                mEventAdapter.notifyDataSetChanged() }) }}



}







