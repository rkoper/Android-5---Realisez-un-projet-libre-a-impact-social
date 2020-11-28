package com.so.dingbring.view.calendar

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gauravk.bubblenavigation.BubbleNavigationLinearView
import com.so.dingbring.R
import com.so.dingbring.data.MyEventViewModel
import com.so.dingbring.data.MyUserViewModel
import com.so.dingbring.view.base.BaseFragment
import com.so.dingbring.view.login.LoginActivity
import com.tejpratapsingh.recyclercalendar.model.RecyclerCalendarConfiguration
import kotlinx.android.synthetic.main.dialog_layout_calendar.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class CalendarFragment : BaseFragment() {
    var mDataEvent: MutableList<MutableList<String>> = mutableListOf()
    var mDataPass = arrayListOf<String>()
    var startCal = Calendar.getInstance()
    var endCal = Calendar.getInstance()
    var date = Date()
    lateinit var configuration: RecyclerCalendarConfiguration
    private val mEventVM by viewModel<MyEventViewModel>()
    private val mUserVM by viewModel<MyUserViewModel>()
    var mNameUser = "..."
    var mEmailUser = "..."
    var mPhotoUser = "..."
    var mIdUser = ""
    var mUserEvent = arrayListOf("", "")
    var varbutton: BubbleNavigationLinearView? = null
    var mDataEventTest : MutableList<MutableList<String>> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_calendar, container, false)
        mIdUser = LoginActivity.mIdUser
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        varbutton = activity?.findViewById(R.id.floating_top_bar_navigation)
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    view?.findNavController()?.navigate(R.id.action_calendar_to_home)
                    varbutton?.setCurrentActiveItem(1) } })

        mUserVM.getUserById(mIdUser).observe(requireActivity(), androidx.lifecycle.Observer {mlmu->
            if (mlmu != null) {
                mUserEvent = mlmu.mEventUser
                initCal()
                initRV()
            } }) }

    private fun initCal() {

        date = Date()
        date.time = System.currentTimeMillis()
        startCal = Calendar.getInstance()
        endCal = Calendar.getInstance()
        endCal.time = date
        endCal.add(Calendar.MONTH, 9) }


    private fun initRV() {

        configuration = RecyclerCalendarConfiguration(RecyclerCalendarConfiguration.CalenderViewType.VERTICAL, Locale.getDefault(), true)

            var calendarRecyclerView = calendarRecyclerView
            val calendarAdapterVertical = CalendarAdapter(
                requireContext(), startCal.time, endCal.time, configuration, mDataEvent,
                object : CalendarAdapter.OnDateSelected {
                    override fun onDateSelected(datalist: MutableList<String>) {
                        createAlert(datalist) } })

        calendarRecyclerView.adapter = calendarAdapterVertical
        loadRV(calendarAdapterVertical)

        }

    private fun loadRV(calendarAdapterVertical: CalendarAdapter) {
        mEventVM.getUserEvent(mUserEvent, requireActivity())
            .observe(requireActivity(), androidx.lifecycle.Observer {listMyEvent ->
                mDataEvent.clear()
                mDataEvent.addAll(listMyEvent)
                calendarAdapterVertical.notifyDataSetChanged() })


    }


    private fun createAlert(datalist:  MutableList<String>) {

            val d = Dialog(requireContext())
            d.requestWindowFeature(Window.FEATURE_NO_TITLE)
            d.window?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
            d.setContentView(R.layout.dialog_layout_calendar)
            d.dialog_cal_nameuser.text = datalist[5]
            d.dialog_cal_addressevent.text = datalist[0]
            d.dialog_cal_dateevent.text = datalist[1]
            d.dialog_cal_hourevent.text = datalist[7]
            d.dialog_cal_nameevent.text = datalist[3]
            Glide.with(requireContext())
                .load(datalist[6])
                .apply(RequestOptions.circleCropTransform())
                .into(d.dialog_cal_photouser)
            d.show()

            d.dialog_cal_see.setOnClickListener {
                var bundle = bundleOf("GlobalIdEvent" to datalist[2])
                bundle.putString("GlobalIdUSer", mIdUser)
                view?.findNavController()?.navigate(R.id.action_calendar_to_detail, bundle)
                d.dismiss() }

            d.dialog_cal_cancel.setOnClickListener {
                d.dismiss()
            }
    }
}


