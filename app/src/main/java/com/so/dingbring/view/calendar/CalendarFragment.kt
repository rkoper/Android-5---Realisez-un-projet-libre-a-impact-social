package com.so.dingbring.view.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.so.dingbring.R
import com.so.dingbring.databinding.FragmentCalendarBinding
import com.so.dingbring.databinding.FragmentDetailBinding
import kotlinx.android.synthetic.main.fragment_calendar.*

class CalendarFragment : Fragment() {

    private lateinit var mBinding: FragmentCalendarBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false)
        initBottom(mBinding, view)
        return mBinding.root}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        test()
        super.onViewCreated(view, savedInstanceState) }

    private fun initBottom(mBinding: FragmentCalendarBinding, view: View?) {
        mBinding.floatingTopBarNavigation.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.adventpro))
        mBinding.floatingTopBarNavigation.setNavigationChangeListener { view , position ->
            when (position) {
                0 -> goHome(view)
                1 -> goCreate(view)
                2 -> print("here")
                3 -> goProfil(view)
                4 -> goSettings(view)
                else -> { print("Error")}
            }
        }
    }

    private fun goHome(view: View) {view.findNavController().navigate(R.id.action_calendar_fragment_to_homeFragment)}

    private fun goCreate(view: View) {view.findNavController().navigate(R.id.action_calendar_fragment_to_create_fragment)}

    private fun goProfil(view: View) {view.findNavController().navigate(R.id.action_calendar_fragment_to_profil_fragment)}

    private fun goSettings(view: View) {view.findNavController().navigate(R.id.action_calendar_fragment_to_settings_fragment)}

    private fun test() {
        calendar_textView.text = "Calendar"
    }
}