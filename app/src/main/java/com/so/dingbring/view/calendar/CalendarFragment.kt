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
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gauravk.bubblenavigation.BubbleNavigationLinearView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.so.dingbring.R
import com.so.dingbring.data.MyEventViewModel
import com.so.dingbring.data.MyUserViewModel
import com.so.dingbring.view.base.BaseFragment
import com.tejpratapsingh.recyclercalendar.model.RecyclerCalendarConfiguration
import kotlinx.android.synthetic.main.dialog_layout_calendar.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class CalendarFragment : BaseFragment() {
    private var mDataEvent: MutableList<MutableList<String>> = mutableListOf()
    private var startCal = Calendar.getInstance()
    private var endCal = Calendar.getInstance()
    private var date = Date()
    private lateinit var configuration: RecyclerCalendarConfiguration
    private val mEventVM by viewModel<MyEventViewModel>()
    private val mUserVM by viewModel<MyUserViewModel>()
    var mNameUser = "..."
    private var mIdUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private var mUserEvent = arrayListOf("", "")
    private var mPosBottomBar: BubbleNavigationLinearView? = null
    private var mFloat_back : FloatingActionButton? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mUserVM.getUserById(mIdUser)
            .observe(requireActivity(), androidx.lifecycle.Observer { mlmu ->
                if (mlmu != null) {
                    mUserEvent = mlmu.mEventUser
                    initCal()
                    initRV()
                }
            })

        onBackPressed()
        onBackBarPressed()
    }

    private fun onBackBarPressed() {
        mFloat_back = activity?.findViewById(R.id.item_tb_fb_back)
        mFloat_back?.setOnClickListener { navToHome() }
    }

    private fun onBackPressed() {
        mPosBottomBar = activity?.findViewById(R.id.float_bottom_bar)
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navToHome() } })
    }

    private fun navToHome() {
        Navigation.findNavController(requireActivity(), R.id.hostFragment).navigate(R.id.event_fragment)
        mPosBottomBar?.setCurrentActiveItem(1)
    }



    private fun initCal() {

        date = Date()
        date.time = System.currentTimeMillis()
        startCal = Calendar.getInstance()
        endCal = Calendar.getInstance()
        endCal.time = date
        endCal.add(Calendar.MONTH, 9)
    }


    private fun initRV() {

        configuration = RecyclerCalendarConfiguration(
            RecyclerCalendarConfiguration.CalenderViewType.VERTICAL,
            Locale.getDefault(),
            true
        )

        val calendarRecyclerView = calendarRecyclerView
        val calendarAdapterVertical = CalendarAdapter(
            requireContext(), startCal.time, endCal.time, configuration, mDataEvent,
            object : CalendarAdapter.OnDateSelected {
                override fun onDateSelected(datalist: MutableList<String>) {
                    createAlert(datalist)
                }
            })

        calendarRecyclerView.adapter = calendarAdapterVertical
        loadRV(calendarAdapterVertical)

    }

    private fun loadRV(calendarAdapterVertical: CalendarAdapter) {
        mEventVM.getUserEvent(mUserEvent, requireActivity())
            .observe(requireActivity(), androidx.lifecycle.Observer { listMyEvent ->
                mDataEvent.clear()
                mDataEvent.addAll(listMyEvent)
                calendarAdapterVertical.notifyDataSetChanged()
            })
    }


    private fun createAlert(datalist: MutableList<String>) {

        println("-----0-------->>" + datalist[0])
        println("-----1-------->>" + datalist[1])
        println("-----2-------->>" + datalist[2])
        println("-----3-------->>" + datalist[3])
        println("-----4-------->>" + datalist[4])
        println("-----5-------->>" + datalist[5])
        println("-----6-------->>" + datalist[6])
        println("-----7-------->>" + datalist[7])
        println("----8--------->>" + datalist[8])
        println("----9 --------->>" + datalist[9])

        val d = Dialog(requireContext())
        d.requestWindowFeature(Window.FEATURE_NO_TITLE)
        d.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        d.setContentView(R.layout.dialog_layout_calendar)
        d.dialog_cal_addressevent.text = datalist[0]
        d.dialog_cal_dateevent.text = datalist[1]
        // Event Id
        d.dialog_cal_nameevent.text = datalist[3]
        // User Id
        d.dialog_cal_nameuser.text = datalist[5]
        Glide.with(requireContext()).load(datalist[6]).apply(RequestOptions.circleCropTransform()).into(d.dialog_cal_photouser)
        d.dialog_cal_hourevent.text = datalist[7]
        d.dialog_cal_descevent.text = datalist[8]



        d.dialog_cal_see.setOnClickListener {

            val bundle = bundleOf("GlobalIdEvent" to datalist[2])

            Navigation.findNavController(requireActivity(), R.id.hostFragment).navigate(R.id.detail_Fragment, bundle)

            d.dismiss() }

        d.dialog_cal_cancel.setOnClickListener { d.dismiss() }

        d.show()
    }
}



