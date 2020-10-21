package com.so.dingbring.view.calendar

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AnimationUtils
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
    var mNameUser = "..."
    var mEmailUser = "..."
    var mPhotoUser = "..."

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false)



        mNameUser = arguments?.get("GlobalName").toString()
        mEmailUser = arguments?.get("GlobalEmail").toString()
        mPhotoUser = arguments?.get("GlobalPhoto").toString()

        println("--calendar--–|mNameUser|----"+mPhotoUser + "----–|mEmailUser|----"+ mEmailUser+ "----–|mPhotoUser|----"+mPhotoUser )


        initBottomInvisible()
        initCal()
        initRV()
        return mBinding.root
    }

    private fun initBottomInvisible() {
        mBinding.textViewSelectedDate.visibility = View.INVISIBLE
        mBinding.textViewSelectedName.visibility = View.INVISIBLE
        mBinding.textViewSelectedIcon.visibility = View.INVISIBLE
        mBinding.textViewSelectedIcon.visibility = View.INVISIBLE
        mBinding.textViewSelectedIcon.visibility = View.INVISIBLE
    }
    private fun initBottomVisible() {
        mBinding.textViewSelectedDate.visibility = View.VISIBLE
        mBinding.textViewSelectedName.visibility = View.VISIBLE
        mBinding.textViewSelectedIcon.visibility = View.VISIBLE
        mBinding.textViewSelectedIcon.visibility = View.VISIBLE
        mBinding.textViewSelectedIcon.visibility = View.VISIBLE
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
        val z1 = event.split(",")[0]
        val z2 = event.split(",")[1]
        val z3 = event.split(",")[2]

        val animationL = AnimationUtils.loadAnimation(requireContext(), R.anim.slideleft)
        val animationR = AnimationUtils.loadAnimation(requireContext(), R.anim.slideright)
        val animationD = AnimationUtils.loadAnimation(requireContext(), R.anim.slidedown)
        mBinding.textViewSelectedDate.startAnimation(animationR)
        mBinding.textViewSelectedName.startAnimation(animationL)
        mBinding.textViewSelectedIcon.startAnimation(animationD)





      initBottomVisible()

        val a = z1.split("")[0]
        val a1 = z1.split("")[1]
        val a2 = z1.split("")[2]
        val a3 = z1.split("")[3]
        val a4 = z1.split("")[4]
        val a5 = z1.split("")[5]
        val a6 = z1.split("")[6]
        val a7 = z1.split("")[7]

        val dateDispolay = (a7+a6+"/"+a5+a4+"/"+a+a1+a2+a3).toString()


        mBinding.textViewSelectedDate.text = dateDispolay
        mBinding.textViewSelectedName.text = z2

        mBinding.textViewSelectedIcon.setOnClickListener {
            var bundle = bundleOf("eventId" to z3)
            mBinding.root.findNavController().navigate(R.id.action_calendar_to_detail, bundle)
        }


    }

}
