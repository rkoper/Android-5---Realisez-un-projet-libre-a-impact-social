package com.so.dingbring.view.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
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
    var mDataEvent : MutableList<MyEvent> = mutableListOf()



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        FirebaseApp.initializeApp(requireContext())
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false)
        initRV(mBinding)
        checkFireStoreUser(mBinding)
        return mBinding.root
    }


    @SuppressLint("CheckResult")
    private fun initRV(mBinding: FragmentHomeBinding) {
        mHomeAdapter= HomeAdapter(requireActivity(), mDataEvent, )
        mBinding.recyclerView.setHasFixedSize(true)
        mBinding.recyclerView.layoutManager= LinearLayoutManager(context)
        mBinding.recyclerView.adapter= mHomeAdapter

        mHomeAdapter.itemClick.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                var bundle = bundleOf("eventId" to data.mEventId)
                mBinding.root.findNavController().navigate(R.id.action_homeFragment_to_detail_fragment, bundle) }

    }

    private fun checkFireStoreUser(mBinding: FragmentHomeBinding?) {

//       mUserVM.getUser(FirebaseAuth.getInstance().currentUser?.email.toString())?.observe(requireActivity(),{
        mUserVM.getUser("fifi@gmail.com")?.observe(requireActivity(),{ mlmu ->
            if (mlmu == null){createFireStoreUser(mBinding!!)}

            else {
                mBinding!!.homeName.text = mlmu.mNameUser
                Glide.with(requireActivity())
                    .load(mlmu.mPhotoUser)
                    .apply(RequestOptions.circleCropTransform())
                    .into(mBinding.homeImage)

                mEventVM.getSelectedEvent(mlmu.mEventUser).observe(requireActivity(), { a ->
                    mDataEvent.addAll(a)
                    mHomeAdapter.notifyDataSetChanged()
                })}})

    }



    private fun createFireStoreUser(mBinding: FragmentHomeBinding) {

        if (FirebaseAuth.getInstance().currentUser?.displayName != null)
        { mBinding.homeName.text =  FirebaseAuth.getInstance().currentUser?.displayName.toString()}

        if (FirebaseAuth.getInstance().currentUser?.photoUrl != null)
        { Glide.with(requireContext())
            .load(FirebaseAuth.getInstance().currentUser?.photoUrl)
            .apply(RequestOptions.circleCropTransform())
            .into(mBinding.homeImage)}

        val userId = UUID.randomUUID().toString()
        val mDataUser: MutableMap<String, Any> = HashMap()
        mDataUser["NameUser"] = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        mDataUser["EmailUser"] = FirebaseAuth.getInstance().currentUser?.email.toString()
        mDataUser["PhotoUser"] = FirebaseAuth.getInstance().currentUser?.photoUrl.toString()
        mDataUser["DocIdUser"] = userId

        mUserVM.createUser(mDataUser)

    }




}