package com.so.dingbring.view.settings

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
import com.so.dingbring.databinding.FragmentSettingsBinding
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    private lateinit var mBinding: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        return mBinding.root}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        test()
        super.onViewCreated(view, savedInstanceState) }



    private fun test() {
        settings_textView.text = "Settings"
    }
}