package com.so.dingbring

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import com.so.dingbring.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), MyCommunication {


    private val repository= DataRepository()
    private val factory= InfoViewModelFactory(repository)
    private val viewModel by lazy { ViewModelProviders.of(requireActivity(),factory).get(InfoViewModel::class.java) }
    private lateinit var adapter: InfoAdapter


    companion object{

        lateinit var storage: FirebaseStorage
        var mHomeFragment = this
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FirebaseApp.initializeApp(requireContext())

        val binding: FragmentHomeBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_home,container,false)


        binding.upload.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_imageFragment)
        }

        adapter= InfoAdapter(requireActivity())
        binding.recyclerView.layoutManager= LinearLayoutManager(context)
        binding.recyclerView.adapter= adapter

        viewModel.getAllInformation().observe(requireActivity(), Observer {
            adapter.setList(it)
            adapter.notifyDataSetChanged()

        })


        return binding.root
    }

    
}