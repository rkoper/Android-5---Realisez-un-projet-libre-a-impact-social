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
import com.google.firebase.firestore.FirebaseFirestore
import com.robertlevonyan.views.customfloatingactionbutton.FloatingLayout
import com.so.dingbring.R
import com.so.dingbring.data.MyEvent
import com.so.dingbring.data.MyEventViewModel
import com.so.dingbring.data.MyUserViewModel
import com.so.dingbring.databinding.FragmentHomeBinding
import com.so.dingbring.view.main.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.HashMap

class HomeFragment : Fragment() {


    private val mEventVM by viewModel<MyEventViewModel>()
    private val mUserVM by viewModel<MyUserViewModel>()
    private lateinit var mHomeAdapter: HomeAdapter
    private lateinit var mBg: FragmentHomeBinding
    var mDataEvent: MutableList<MyEvent> = mutableListOf()
    var mNameUser = "///"
    var mEmailUser = " /// "
    var mPhotoUser = " /// "
    var mIdUser = "////"
    var mUserEvent = arrayListOf("", "")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FirebaseApp.initializeApp(requireContext())
        mBg = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)


        mIdUser  = MainActivity.mIdUser
        mNameUser  = MainActivity.mNameUser
        mEmailUser  = MainActivity.mEmailUser
        mPhotoUser  = MainActivity.mPhotoUser


        mUserVM.getifUserExist(mIdUser)?.observe(requireActivity(), { condition ->


            println("( condition------> )" + condition )


            if (condition){
                mIdUser  = MainActivity.mIdUser
                mNameUser  = MainActivity.mNameUser
                mEmailUser  = MainActivity.mEmailUser
                mPhotoUser  = MainActivity.mPhotoUser
                initHeader()
                initRV()}


            else{
                mUserVM.getUserById(mIdUser)?.observe(requireActivity(), {mlmu ->
                    mIdUser  = mlmu.mUserId
                    mNameUser  = mlmu.mNameUser
                    mEmailUser  = mlmu.mEmailUser
                    mPhotoUser  = mlmu.mPhotoUser


                    println("( mPhotoUser------> )" + mPhotoUser )
                    initHeader()
                    initRV()

                })}




        })


        return mBg.root}


    private fun initHeader() {
        val animation100 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoominbacktofront)
        mBg.homeName.startAnimation(animation100)
        mBg.homeName.text = mNameUser
        mBg.homeImage.startAnimation(animation100)
        Glide.with(requireActivity()).load(mPhotoUser).apply(RequestOptions.circleCropTransform()).into(mBg.homeImage) }


    @SuppressLint("CheckResult")
    private fun initRV() {
        mHomeAdapter = HomeAdapter(requireActivity(), mDataEvent)
        mBg.recyclerView.setHasFixedSize(true)
        mBg.recyclerView.layoutManager = LinearLayoutManager(context)
        mBg.recyclerView.adapter = mHomeAdapter

        mHomeAdapter.itemClick.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
                val bundle = Bundle()
                bundle.putString("GlobalIdEvent", data.mEventId)
                bundle.putString("GlobalName", mNameUser)
                mBg.root.findNavController().navigate(R.id.action_homeFragment_to_detail_fragment, bundle) }
        loadRV()

    }


    private fun loadRV() {
            mEventVM.getUserEvent(mUserEvent).observe(requireActivity(), { a ->
                mDataEvent.clear()
                mDataEvent.addAll(a)
                mHomeAdapter.notifyDataSetChanged() })}

}



