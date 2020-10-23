package com.so.dingbring.view.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.robertlevonyan.views.customfloatingactionbutton.FloatingLayout
import com.so.dingbring.R
import com.so.dingbring.data.MyEvent
import com.so.dingbring.data.MyEventViewModel
import com.so.dingbring.data.MyUserViewModel
import com.so.dingbring.databinding.FragmentHomeBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.HashMap

class HomeFragment : Fragment() {


    private val mEventVM by viewModel<MyEventViewModel>()
    private val mUserVM by viewModel<MyUserViewModel>()
    private lateinit var mHomeAdapter: HomeAdapter
    private lateinit var mBinding: FragmentHomeBinding
    var mDataEvent: MutableList<MyEvent> = mutableListOf()
    var mUserName = "///"
    var mEmailUser = " /// "
    var mUserPP = " /// "
    var mUserId = "////"
    var mUserEvent = arrayListOf<String>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FirebaseApp.initializeApp(requireContext())
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        if (FirebaseAuth.getInstance().currentUser?.email != null)
        { mEmailUser = FirebaseAuth.getInstance().currentUser?.email.toString()}

        mUserVM.getUserByMail(mEmailUser)?.observe(requireActivity(), {mlmu ->
            if (mlmu != null){
                mUserName = mlmu.mNameUser
                mUserPP = mlmu.mPhotoUser
                mUserId = mlmu.mUserId
                mUserEvent = mlmu.mEventUser

            test(mUserName, mUserId, mUserPP)}})
            return mBinding.root }

    private fun test(mUserName: String, mUserId: String, mUserPP: String) {
        println("-----Home------|mEmailUser|----2-----" + mEmailUser)
        println("-----Home------|mUserName|-----2----" + mUserName)
        println("-----Home------|mUserPP|-------2--" + mUserPP)
        println("-----Home------|mUserId|-------2--" + mUserId)

        initHeader()
       initRV()

    }


    private fun initHeader() {
        val animation100 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoominbacktofront)
        mBinding.homeName.startAnimation(animation100)
        mBinding.homeName.text = mUserName
        mBinding.homeImage.startAnimation(animation100)
        Glide.with(requireActivity())
            .load(mUserPP).apply(RequestOptions.circleCropTransform()).into(mBinding.homeImage) }


    @SuppressLint("CheckResult")
    private fun initRV() {
        mHomeAdapter = HomeAdapter(requireActivity(), mDataEvent)
        mBinding.recyclerView.setHasFixedSize(true)
        mBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        mBinding.recyclerView.adapter = mHomeAdapter

        mHomeAdapter.itemClick.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
                val bundle = Bundle()
                bundle.putString("GlobalIdEvent", data.mEventId)
                bundle.putString("GlobalName", mUserName)
                bundle.putString("GlobalEmail", mEmailUser)
                bundle.putString("GlobalPhoto", mUserPP)
                mBinding.root.findNavController().navigate(R.id.action_homeFragment_to_detail_fragment, bundle) }


        loadRV()

    }




    private fun loadRV() {
            mEventVM.getSelectedEvent(mUserEvent).observe(requireActivity(), { a ->
                mDataEvent.clear()
                mDataEvent.addAll(a)
                mHomeAdapter.notifyDataSetChanged() })}

}



