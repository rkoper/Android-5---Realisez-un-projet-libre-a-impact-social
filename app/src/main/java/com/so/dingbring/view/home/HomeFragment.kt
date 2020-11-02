package com.so.dingbring.view.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.FirebaseApp
import com.so.dingbring.R
import com.so.dingbring.data.MyEvent
import com.so.dingbring.data.MyEventViewModel
import com.so.dingbring.data.MyUserViewModel
import com.so.dingbring.databinding.FragmentHomeBinding
import com.so.dingbring.view.main.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.viewmodel.ext.android.viewModel

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

        mUserVM.getifNewUser(mIdUser)?.observe(requireActivity(), androidx.lifecycle.Observer { condition ->
            if (condition){
                mIdUser  = MainActivity.mIdUser
                mNameUser  = MainActivity.mNameUser
                mEmailUser  = MainActivity.mEmailUser
                mPhotoUser  = MainActivity.mPhotoUser
                initHeader()
                initRV()}

            else{ mUserVM.getUserById(mIdUser)?.observe(requireActivity(),androidx.lifecycle.Observer {mlmu ->
                    mIdUser  = mlmu.mUserId
                    mNameUser  = mlmu.mNameUser
                    mEmailUser  = mlmu.mEmailUser
                    mPhotoUser  = mlmu.mPhotoUser
                    mUserEvent = mlmu.mEventUser

                    initHeader()
                    initRV() })} })

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
                val intent = Intent(activity, MainActivity::class.java)
                intent.putExtra("GlobalIdEvent", data.mEventId)
                intent.putExtra("GlobalIdUser", mIdUser)
                startActivity(intent) }
        loadRV() }


    private fun loadRV() {
            mEventVM.getUserEvent(mUserEvent).observe(requireActivity(), androidx.lifecycle.Observer{ a ->
                mDataEvent.clear()
                mDataEvent.addAll(a)

             mHomeAdapter.notifyDataSetChanged() })}

}



