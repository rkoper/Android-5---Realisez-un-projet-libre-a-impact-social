package com.so.dingbring.view.profil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.so.dingbring.R
import com.so.dingbring.databinding.FragmentDetailBinding
import com.so.dingbring.databinding.FragmentHomeBinding
import com.so.dingbring.databinding.FragmentProfilBinding
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.fragment_profil.*


class ProfilFragment : Fragment() {

    private lateinit var mBinding: FragmentProfilBinding
    private lateinit var mBindingHome: FragmentHomeBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profil, container, false)
        mBindingHome = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return mBinding.root}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        test()
        super.onViewCreated(view, savedInstanceState) }



    private fun test() {
        profil_textView.text = "Profil"
    }
}