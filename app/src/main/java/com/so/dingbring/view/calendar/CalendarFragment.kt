package com.so.dingbring.view.calendar

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.so.dingbring.ItemSelectionAnimator.Companion.END_CIRCLE_SIZE_RATIO
import com.so.dingbring.R
import com.so.dingbring.data.MyEventViewModel
import com.so.dingbring.data.MyUserViewModel
import com.so.dingbring.databinding.FragmentCalendarBinding
import com.so.dingbring.view.main.MainActivity
import com.tejpratapsingh.recyclercalendar.model.RecyclerCalendarConfiguration
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class CalendarFragment : Fragment() {

    private lateinit var mBinding: FragmentCalendarBinding
    val mDataPass = arrayListOf<String>()
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

    var mEventName = ""
    var mEventOrga = ""
    var mEventAddress = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false)
        mIdUser = arguments?.get(MainActivity.USERID).toString()
        mBinding.calendarLlBotoom.visibility = View.INVISIBLE
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



        mUserVM.getUserById(mIdUser)?.observe(requireActivity(),{myUser ->
            println("-------------myUser----------" + myUser.mEventUser)


     mEventVM.getUserEvent(myUser.mEventUser).observe(requireActivity(), { mlme ->
            mlme.forEach { myevent ->

                val initalDate = myevent.mEventDate
                val day = initalDate.split("/")[0]
                val month = initalDate.split("/")[1]
                val year = initalDate.split("/")[2]

                val afterDate = year + month + day
                val newDate = afterDate +
                        "," + myevent.mEventName +
                        "," + myevent.mEventId +
                        "," + myevent.mEventDate +
                        "," +myevent.mEventAdress +
                        "," + myevent.mEventOrga

                mDataPass.add(newDate)
            }     })


            var calendarRecyclerView = mBinding.calendarRecyclerView
            val calendarAdapterVertical = CalendarAdapter(
                requireContext(),
                startCal.time,
                endCal.time,
                configuration,
                mDataPass,
                object : CalendarAdapter.OnDateSelected {
                    override fun onDateSelected(date: String) {
                        createAlert(date)
                    }
                })

            calendarRecyclerView.adapter = calendarAdapterVertical

        })
    }

    private fun createAlert(event: String) {
        val mDate = event.split(",")[0]
        val mEventName = event.split(",")[1]
        val mEventID = event.split(",")[2]
        val mDateDisplay = event.split(",")[3]
        val mEventAddress = event.split(",")[4]
        val mEventCity = event.split(",")[5]
        val mEventOrga = event.split(",")[6]

        val animationL = AnimationUtils.loadAnimation(requireContext(), R.anim.slidelefttocal)
        mBinding.calendarLlBotoom.visibility = View.VISIBLE
        mBinding.calendarLlBotoomCach.visibility = View.INVISIBLE
        mBinding.calendarLlBotoom.startAnimation(animationL)

        mBinding.calendarEventDate.text = mDateDisplay
        mBinding.calendarEventName.text = mEventName
        mBinding.calendarEventOrga.text = mEventOrga
        mBinding.calendarEventAdress.text = mEventAddress +", "+mEventCity

        mBinding.textViewSelectedIcon.setOnClickListener {

            test(mBinding.textViewSelectedIcon.id)
            var bundle = bundleOf("GlobalIdEvent" to mEventID)
            bundle.putString("GlobalIdUSer", mIdUser)
            mBinding.root.findNavController().navigate(R.id.action_calendar_to_detail, bundle)
        } }

    private fun test(id: Int) {
        var currentCircleAngle: Float
        val circleAngleAnimation = ValueAnimator.ofFloat(20.toFloat(), 360.toFloat())
        circleAngleAnimation.duration = 1000.toLong()
        circleAngleAnimation.interpolator = DecelerateInterpolator()
        circleAngleAnimation.addUpdateListener { animation ->
            currentCircleAngle = animation.animatedValue as Float
            if (currentCircleAngle == 360.toFloat()) {
                startExitAnimation(id)
            }
        }
        circleAngleAnimation.start()
    }

    private fun startExitAnimation(buttonIndex: Int) {
        val circleSizeAnimation = ValueAnimator.ofFloat(2.0f, END_CIRCLE_SIZE_RATIO)
        circleSizeAnimation.duration = 1000.toLong()
        circleSizeAnimation.interpolator = DecelerateInterpolator()

        }
}
