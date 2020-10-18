package com.so.dingbring.view.calendar

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.so.dingbring.R
import com.so.dingbring.data.MyEventViewModel
import com.so.dingbring.databinding.FragmentCalendarBinding
import com.tejpratapsingh.recyclercalendar.model.RecyclerCalendarConfiguration
import kotlinx.android.synthetic.main.dialog_layout.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class CalendarFragment : Fragment() {

    private lateinit var mBinding: FragmentCalendarBinding
    val mTestMap = arrayListOf<String>()
    var startCal = Calendar.getInstance()
    var endCal = Calendar.getInstance()
    var date = Date()
    lateinit var configuration: RecyclerCalendarConfiguration
    private val mEventVM by viewModel<MyEventViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false)
        initCal()
        initRV()
        return mBinding.root
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
            Locale.getDefault(), true
        )

        mEventVM.getAllEvent().observe(requireActivity(), { mlme ->
            mlme.forEach { myevent ->
                println("------M EVENT-----" + myevent.mEventDate)

                val beforeDate = myevent.mEventDate
                val z = beforeDate.split(".")[1]
                val y = z.split(" ")[1]
                val a = y.split("/")[0]
                val b = y.split("/")[1]
                val c = y.split("/")[2]

                val afterDate = c + b + a
                val t = afterDate + "," + myevent.mEventName + "," + myevent.mEventId

                mTestMap.add(t)
            }


            var calendarRecyclerView = mBinding.calendarRecyclerView
            val calendarAdapterVertical = CalendarAdapter(
                requireContext(),
                startCal.time,
                endCal.time,
                configuration,
                mTestMap,
                object : CalendarAdapter.OnDateSelected {
                    override fun onDateSelected(date: String) {
                        createAlert(date)
                    }
                })

            calendarRecyclerView.adapter = calendarAdapterVertical

        })
    }

    private fun createAlert(event: String) {
        val d = Dialog(requireContext())
        d.requestWindowFeature(Window.FEATURE_NO_TITLE)
        d.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        d.setContentView(R.layout.dialog_layout)
        val z1 = event.split(",")[0]
        val z2 = event.split(",")[1]
        val z3 = event.split(",")[2]
        d.dialog_date.text = z1
        d.dialog_name.text = z2
        d.show()
        d.dialog_eye.setOnClickListener {
            goToDetail(z3)
            d.dismiss() } }


    private fun goToDetail(z3: String) {
        var bundle = bundleOf("eventId" to z3)
        mBinding.root.findNavController().navigate(R.id.action_calendar_to_detail, bundle)
    }
}
