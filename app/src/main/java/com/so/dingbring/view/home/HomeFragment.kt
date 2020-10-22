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
    var mNameUser = "Fifi"
    var mEmailUser = "fifi@gmail.com"
    var mPhotoUser = "https://i.ibb.co/r6W0hxp/Capture-d-e-cran-2020-10-16-a-21-09-59.png"
    var mDefaultPhoto = "https://i.ibb.co/r6W0hxp/Capture-d-e-cran-2020-10-16-a-21-09-59.png"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FirebaseApp.initializeApp(requireContext())
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        if (FirebaseAuth.getInstance().currentUser?.displayName != null)
        { mNameUser = FirebaseAuth.getInstance().currentUser?.displayName.toString()}
        if (FirebaseAuth.getInstance().currentUser?.email != null)
        { mEmailUser = FirebaseAuth.getInstance().currentUser?.displayName.toString()}
        if (FirebaseAuth.getInstance().currentUser?.photoUrl != null)
        { mPhotoUser = FirebaseAuth.getInstance().currentUser?.displayName.toString()}

        initHeader(mBinding)
        initRV(mBinding)




        return mBinding.root }

    private fun initHeader(mBinding: FragmentHomeBinding?) {
        mBinding!!.homeName.text = mNameUser

        Glide.with(requireActivity())
            .load(mPhotoUser)
            .apply(RequestOptions.circleCropTransform())
            .into(mBinding.homeImage) }


    @SuppressLint("CheckResult")
    private fun initRV(mBinding: FragmentHomeBinding) {
        mHomeAdapter = HomeAdapter(requireActivity(), mDataEvent)
        mBinding.recyclerView.setHasFixedSize(true)
        mBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        mBinding.recyclerView.adapter = mHomeAdapter

        mHomeAdapter.itemClick.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                val bundle = Bundle()
                bundle.putString("GlobalIdEvent", data.mEventId)
                bundle.putString("GlobalName", mNameUser)
                bundle.putString("GlobalEmail", mEmailUser)
                bundle.putString("GlobalPhoto", mPhotoUser)
                bundle.putString("GlobalPhoto", mPhotoUser)
                mBinding.root.findNavController()
                    .navigate(R.id.action_homeFragment_to_detail_fragment, bundle)
            }

        loadRV()

    }

    private fun loadRV() {
        mUserVM.getUserByMail(mEmailUser)?.observe(requireActivity(),{ mlmu ->
            mEventVM.getSelectedEvent(mlmu.mEventUser).observe(requireActivity(), { a ->
                mDataEvent.addAll(a)
                mHomeAdapter.notifyDataSetChanged() })})


    }

}



