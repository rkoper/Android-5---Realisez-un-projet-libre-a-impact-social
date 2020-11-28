package com.so.dingbring.view.create

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.navigation.findNavController
import com.gauravk.bubblenavigation.BubbleNavigationLinearView
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.Place.Field
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.so.dingbring.R
import com.so.dingbring.Utils.formatDate
import com.so.dingbring.data.MyEvent
import com.so.dingbring.data.MyEventViewModel
import com.so.dingbring.data.MyUserViewModel
import com.so.dingbring.view.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_create.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class CreateFragment : BaseFragment() {
    private val mUserVM by viewModel<MyUserViewModel>()
    private val mEventVM by viewModel<MyEventViewModel>()
    private var mEventDate = ""
    private var mEventOrga = ""
    private var mEventName = ""
    private var mEventAddress = ""
    private var mEventHour = ""
    private var mEventDescription = ""
    var mNameUser = ""
    var mIdUser = ""
    var mItemStatus: String = "I bring"
    var mEventUniqueID = UUID.randomUUID().toString()
    var varbutton: BubbleNavigationLinearView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_create, container, false)

        mIdUser = ITEMACTIVITY.mIdUser
        return view }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        varbutton = activity?.findViewById(R.id.floating_top_bar_navigation)
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    view?.findNavController()?.navigate(R.id.action_create_to_home)
                    varbutton?.setCurrentActiveItem(1) } })
        initCreateEvent() }

        private fun initCreateEvent() {
            initAdresse()
            initDate()
            initEvent()
            initOrga()
            initHour()
            initDescription()
            createEvent()
        }

    private fun initDescription() { create_info_edit.doOnTextChanged { text, start, before, count ->
            if (count > 0) {mEventDescription = create_info_edit.text.toString() } } }

    private fun initEvent() { create_name_edit.doOnTextChanged { text, start, before, count ->
            if (count > 0) {mEventName = create_name_edit.text.toString() } } }

    private fun initOrga() { create_orga_edit.doOnTextChanged { text, start, before, count ->
            if (count > 0) {mEventOrga = create_orga_edit.text.toString() } } }

    private fun initAdresse() {
            var mStreetNumber = "";
            var mStreetName = "";
            var mCity = ""
            if (!Places.isInitialized()) { Places.initialize(requireActivity().applicationContext, "AIzaSyA29ttP7zVNeG68hHXh4g6VpOMZxJRDE58") }

            val autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?
            val fView: View? = autocompleteFragment?.view
            val etTextInput: EditText? = fView?.findViewById(R.id.places_autocomplete_search_input)
            etTextInput?.setBackgroundColor(resources.getColor(R.color.fui_transparent))
            etTextInput?.setTextColor(resources.getColor(R.color.white))
            etTextInput?.setHintTextColor(resources.getColor(R.color.white))
            etTextInput?.gravity = Gravity.CENTER
             etTextInput?.hint = " "
            val font: Typeface? = ResourcesCompat.getFont(requireContext(), R.font.roboto)
            etTextInput?.setTypeface(font, Typeface.BOLD)
            val searchIcon = (autocompleteFragment?.view as LinearLayout).getChildAt(0) as ImageView
            searchIcon.visibility = View.GONE
            autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS)
            autocompleteFragment?.setPlaceFields(
                listOf(Field.ID, Field.NAME, Field.ADDRESS_COMPONENTS))
            autocompleteFragment?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
                override fun onPlaceSelected(place: Place) {
                    place.addressComponents?.asList()?.forEach { mAdressComp ->
                        Log.i("TAG", "AutoComplet: " + mAdressComp.types + " ")
                        when {
                            mAdressComp.types.contains("street_number") -> {
                                mStreetNumber = mAdressComp.name }
                            mAdressComp.types.contains("route") -> {
                                mStreetName = mAdressComp.name }
                            mAdressComp.types.contains("locality") -> {
                                mCity = mAdressComp.name } } }

                    etTextInput?.visibility = View.INVISIBLE
                    create_address_txt_display.visibility = View.VISIBLE
                    create_address_txt_display.text =
                        mStreetNumber + " " + mStreetName + ", " + mCity
                    mEventAddress = "$mStreetNumber $mStreetName, $mCity "}

                override fun onError(status: Status) {
                    Log.i("TAG", "An error occurred: $mItemStatus") } }) }


        private fun initDate() {
            create_date_txt?.setOnClickListener {

                val dpd = DatePickerDialog.OnDateSetListener { a, y, m, d ->
                    val newDate = formatDate(y, m, d)
                    create_date_txt.text = "$newDate"
                    mEventDate = newDate   }
                val now = android.text.format.Time()
                now.setToNow()
                val d = DatePickerDialog(requireContext(), R.style.MyAppThemeCalendar, dpd, now.year, now.month, now.monthDay)
                d.show()



                d.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.grey_800))
                d.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.grey_800)) } }

    private fun initHour() {
        create_hour_txt.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                create_hour_txt.text = SimpleDateFormat("HH:mm").format(cal.time)
                mEventHour =  create_hour_txt.text.toString() }
            TimePickerDialog(requireContext(), timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show() } }

        fun createEvent() {
            create_save?.setOnClickListener {
                if (mEventAddress == "" ) { Toast.makeText(requireContext(), "Please complete address...", Toast.LENGTH_SHORT).show() }
                if (mEventDate == "" ) { Toast.makeText(requireContext(), "Please complete date...", Toast.LENGTH_SHORT).show() }
                if (mEventUniqueID == "" ) { Toast.makeText(requireContext(), "Error...", Toast.LENGTH_SHORT).show() }
                if (mEventName == "" ) { Toast.makeText(requireContext(), "Please complete event name...", Toast.LENGTH_SHORT).show() }
                if (mEventHour == "" ) { Toast.makeText(requireContext(), "Please complete hour...", Toast.LENGTH_SHORT).show() }
                if (mIdUser == "" ) { Toast.makeText(requireContext(), "Error...", Toast.LENGTH_SHORT).show() }
                if (mEventOrga == "" ) { Toast.makeText(requireContext(), "Please complete Organizer...", Toast.LENGTH_SHORT).show() }

                else { val mDataEvent = MyEvent(mEventAddress,mEventDate,mEventUniqueID,mEventName,mEventHour,mEventDescription,mIdUser,mEventOrga)

                    mEventVM.createEvent(mDataEvent)
                    mUserVM.upadateEventUser(mIdUser,mEventUniqueID )
                    view?.findNavController()?.navigate(R.id.action_create_to_home)
                    varbutton?.setCurrentActiveItem(1) } } }


    }


