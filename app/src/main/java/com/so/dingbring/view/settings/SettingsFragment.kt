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
        initBottom(mBinding, view)
        return mBinding.root}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        test()
        super.onViewCreated(view, savedInstanceState) }

    private fun initBottom(mBinding: FragmentSettingsBinding, view: View?) {
        mBinding.floatingTopBarNavigation.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.adventpro))
        mBinding.floatingTopBarNavigation.setNavigationChangeListener { view , position ->
            when (position) {
                0 -> goHome(view)
                1 -> goCreate(view)
                2 -> goCalendar(view)
                3 -> goProfil(view)
                4 -> print("here")
                else -> { print("Error")}
            }
        }
    }

    private fun goHome(view: View) {view.findNavController().navigate(R.id.action_settings_fragment_to_homeFragment)}

    private fun goCreate(view: View) {view.findNavController().navigate(R.id.action_settings_fragment_to_create_fragment)}

    private fun goProfil(view: View) {view.findNavController().navigate(R.id.action_settings_fragment_to_profil_fragment)}

    private fun goCalendar(view: View) {view.findNavController().navigate(R.id.action_settings_fragment_to_calendar_fragment)}


    private fun test() {
        settings_textView.text = "Settings"
    }
}