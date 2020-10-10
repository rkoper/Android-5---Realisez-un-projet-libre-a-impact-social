package com.so.dingbring.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.so.dingbring.R
import com.so.dingbring.databinding.FragmentHomeBinding
import com.so.dingbring.login.LoginViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {


    private val mHomeViewModel by viewModel<HomeViewModel>()
    private val mLoginVM by viewModel<LoginViewModel>()
    private lateinit var mHomeAdapter: HomeAdapter
    private lateinit var mBinding: FragmentHomeBinding



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        FirebaseApp.initializeApp(requireContext())
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false)
        initView(mBinding)
        initRV(mBinding)
        createFireStoreUser(mBinding)
        return mBinding.root
    }

    private fun initView(mBinding: FragmentHomeBinding) {
        mBinding.homeCreate.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_createFragment) } }


    private fun initRV(mBinding: FragmentHomeBinding) {
        mHomeAdapter= HomeAdapter(requireActivity())
        mBinding.recyclerView.layoutManager= LinearLayoutManager(context)
        mBinding.recyclerView.adapter= mHomeAdapter

        mHomeViewModel.getAllInformation().observe(requireActivity(), Observer {

            mHomeAdapter.setList(it)
            mHomeAdapter.notifyDataSetChanged()
        })
    }


    private fun createFireStoreUser(mBinding: FragmentHomeBinding) {
        if (FirebaseAuth.getInstance().currentUser?.displayName != null)


        { mBinding.homeName.text =  FirebaseAuth.getInstance().currentUser?.displayName.toString()}
        else
        { mBinding.homeName.text = "Donald Duck"}

        if (FirebaseAuth.getInstance().currentUser?.photoUrl != null)
        {
        Glide
            .with(requireContext())
            .load(FirebaseAuth.getInstance().currentUser?.photoUrl)
            .apply(RequestOptions.circleCropTransform())
            .into(mBinding.homeImage)}
        else
        { Glide.with(requireContext())
            .load(R.drawable.donald)
            .apply(RequestOptions.circleCropTransform())
            .into(mBinding.homeImage)}


        println(" ------email----------" + FirebaseAuth.getInstance().currentUser?.email)
        println(" -----photoUrl-----------" + FirebaseAuth.getInstance().currentUser?.photoUrl)


        val mDataUser: MutableMap<String, Any> = HashMap()
        mDataUser["NameUser"] = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        mDataUser["EmailUser"] = FirebaseAuth.getInstance().currentUser?.email.toString()
        mDataUser["PhotoUser"] = FirebaseAuth.getInstance().currentUser?.photoUrl.toString()

        mLoginVM.createUser(mDataUser)


    }


    companion object{
        lateinit var storage: FirebaseStorage
        var mHomeFragment = this }




}