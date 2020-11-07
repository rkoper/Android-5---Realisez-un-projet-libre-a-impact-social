package com.so.dingbring.view.calendar

import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import com.so.dingbring.R
import com.so.dingbring.data.MyEventViewModel
import com.so.dingbring.data.MyUserViewModel
import com.so.dingbring.view.main.MainActivity
import com.tejpratapsingh.recyclercalendar.model.RecyclerCalendarConfiguration
import kotlinx.android.synthetic.main.fragment_calendar.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class CalendarFragment : Fragment() {

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
    var varbutton : ImageView? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_calendar, container, false)


   //     mIdUser = arguments?.get(MainActivity.USERID).toString()


        varbutton = activity?.findViewById(R.id.main_button)
        return  view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendar_ll_botoom.visibility = View.INVISIBLE

      //  varbutton = activity?.findViewById(R.id.main_button)
        varbutton?.visibility  = View.VISIBLE
        initCal()
        initRV()

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



        mUserVM.getUserById(mIdUser)?.observe(requireActivity()) { myUser ->
            mEventVM.getUserEvent(myUser.mEventUser).observe(requireActivity(),androidx.lifecycle.Observer { mlme ->
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
                            "," + myevent.mEventUserId

                    mDataPass.add(newDate)
                }     })


            var calendarRecyclerView = calendarRecyclerView
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

        }
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
        calendar_ll_botoom.visibility = View.VISIBLE
    //    mBinding.calendarLlBotoomCach.visibility = View.INVISIBLE
        calendar_ll_botoom.startAnimation(animationL)

        calendar_event_date.text = mDateDisplay
        calendar_event_name.text = mEventName
        calendar_event_orga.text = mEventOrga
        calendar_event_adress.text = mEventAddress +", "+mEventCity

        textViewSelectedIcon.setOnClickListener {

            val animationZ = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomoutbig)
            textViewSelectedIcon.startAnimation(animationZ)

//            SpringAnimation(img, DynamicAnimation.TRANSLATION_Y, 0f)

            var bundle = bundleOf("GlobalIdEvent" to mEventID)
            bundle.putString("GlobalIdUSer", mIdUser)
            view?.findNavController()?.navigate(R.id.action_calendar_to_detail, bundle)
        } }


}
