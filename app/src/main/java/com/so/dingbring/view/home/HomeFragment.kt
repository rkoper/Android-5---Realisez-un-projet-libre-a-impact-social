package com.so.dingbring.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.so.dingbring.R
import com.so.dingbring.data.MyEvent
import com.so.dingbring.data.MyEventViewModel
import com.so.dingbring.data.MyUserViewModel
import com.so.dingbring.databinding.FragmentHomeBinding
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.HashMap

class HomeFragment : Fragment() {


    private val mEventVM by viewModel<MyEventViewModel>()
    private val mUserVM by viewModel<MyUserViewModel>()
    private lateinit var mHomeAdapter: HomeAdapter
    private lateinit var mBinding: FragmentHomeBinding
    private  var mUserName = "No Name"
    var mDataEvent : MutableList<MyEvent> = mutableListOf()



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        FirebaseApp.initializeApp(requireContext())
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false)
        initRV(mBinding)
        checkFireStoreUser(mBinding)

        return mBinding.root
    }




    private fun initRV(mBinding: FragmentHomeBinding) {
        mHomeAdapter= HomeAdapter(requireActivity(), mDataEvent)
        mBinding.recyclerView.setHasFixedSize(true)
        mBinding.recyclerView.layoutManager= LinearLayoutManager(context)
        mBinding.recyclerView.adapter= mHomeAdapter

        mEventVM.getAllEvent().observe(requireActivity(), {
            mDataEvent.addAll(it)
            mHomeAdapter.notifyDataSetChanged()
        })


    }

    private fun checkFireStoreUser(mBinding: FragmentHomeBinding?) {
        mUserVM.getUser(FirebaseAuth.getInstance().currentUser?.email.toString())?.observe(requireActivity(),

            {mlmu -> if (mlmu == null){createFireStoreUser(mBinding!!)}

                else { mBinding!!.homeName.text = mlmu.mNameUser
                    Glide.with(requireActivity())
                        .load(mlmu.mPhotoUser)
                        .apply(RequestOptions.circleCropTransform())
                        .into(mBinding.homeImage)} })
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