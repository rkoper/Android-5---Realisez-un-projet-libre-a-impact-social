package com.so.dingbring.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gauravk.bubblenavigation.BubbleNavigationLinearView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.so.dingbring.R
import com.so.dingbring.data.MyEventViewModel
import com.so.dingbring.data.MyUserViewModel
import com.so.dingbring.view.adapter.EventAdapter
import com.so.dingbring.view.activity.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.viewmodel.ext.android.viewModel

class EventFragment : BaseFragment() {

    private val mEventVM by viewModel<MyEventViewModel>()
    private val mUserVM by viewModel<MyUserViewModel>()
    private lateinit var mEventAdapter: EventAdapter
    private var mDataEvent: MutableList<MutableList<String>> = mutableListOf()
    var mNameUser = "///"
    private var mIdUser = "////"
    private var mUserEvent = arrayListOf("", "")
    private var mFloatBack : FloatingActionButton? = null
    private var mTxtNoEventYet : TextView? = null
    private var mPosBottomBar: BubbleNavigationLinearView? = null

 override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_event, container, false)
     mIdUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
        return view }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mUserVM.getUserById(mIdUser).observe(requireActivity(), Observer { mlmu ->
            if (mlmu != null) {
                mUserEvent = mlmu.mEventUser

                if (mUserEvent.isEmpty())
                {

                    mTxtNoEventYet = activity?.findViewById(R.id.item_no_event_yet)
                    mTxtNoEventYet?.visibility = View.VISIBLE

                }
                initRV() } })

        onBackPressed()
        onBackBarPressed()
    }

    private fun onBackBarPressed() {
        mFloatBack = activity?.findViewById(R.id.item_tb_fb_back)
        mFloatBack?.setOnClickListener {
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
        mEventAdapter = EventAdapter(
            requireActivity(),
            mDataEvent
        )
        mRecyclerView?.setHasFixedSize(true)
        mRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
        mRecyclerView?.adapter = mEventAdapter

        mEventAdapter.itemClick.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
                val bundle = bundleOf("GlobalIdEvent" to data)
                view?.findNavController()?.navigate(R.id.action_event_fragment_to_detailFragment, bundle) }
        loadRV() }


    private fun loadRV() {

        mTxtNoEventYet = activity?.findViewById(R.id.item_no_event_yet)
        mEventVM.getUserEvent(mUserEvent, requireActivity()).observe(requireActivity(), androidx.lifecycle.Observer {listMyEvent ->
                mDataEvent.clear()
                mDataEvent.addAll(listMyEvent)
                mEventAdapter.notifyDataSetChanged() }) }


}







