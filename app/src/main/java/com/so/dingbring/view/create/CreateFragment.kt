package com.so.dingbring.view.create

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.Place.Field
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.so.dingbring.R
import com.so.dingbring.Utils.FindDay
import com.so.dingbring.Utils.formatDate
import com.so.dingbring.data.*
import com.so.dingbring.view.detail.create.CreateAdapter
import com.so.dingbring.view.main.MainActivity
import kotlinx.android.synthetic.main.fragment_create.*
import kotlinx.android.synthetic.main.fragment_detail.*
import nl.dionsegijn.steppertouch.OnStepCallback
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class CreateFragment : Fragment() {


    private val mUserVM by viewModel<MyUserViewModel>()
    private val mEventVM by viewModel<MyEventViewModel>()
    private val mItemVM by viewModel<MyItemViewModel>()

    private var mEventDate = ""
    private var mEventOrga = ""
    private var mEventName = ""
    private var mEventAddress = ""
    private var mEventUserId = ""
    private var mEventUserName = ""
    private var mEventUserPhoto = ""

    var mNameUser = ""
    var mEmailUser = ""
    var mPhotoUser = ""
    var mIdUser = ""

    var i : Int = 1
    var mItemStatus: String = "I bring"
    var mItemName: String = "<3"
    var mItemQuantity: String = "1"
    var mEventUniqueID = UUID.randomUUID().toString()
    var mItemUniqueID = UUID.randomUUID().toString()

    var varbutton : ImageView ? = null

    var mListMyItem = arrayListOf<MyItem>()

    private lateinit var mCreateAdapter: CreateAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view: View = inflater.inflate(R.layout.fragment_create, container, false)

        mIdUser  = MainActivity.mIdUser
        mNameUser  = MainActivity.mNameUser
        mEmailUser  = MainActivity.mEmailUser
        mPhotoUser  = MainActivity.mPhotoUser




        varbutton = activity?.findViewById(R.id.main_button)



        return  view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        varbutton = activity?.findViewById(R.id.main_button)
        varbutton?.visibility  = View.VISIBLE
        create_status_bring.visibility = View.INVISIBLE
        create_status_need.visibility = View.INVISIBLE
        create_txtV_item.visibility = View.INVISIBLE
        create_edit_item.visibility = View.INVISIBLE
        create_qty_cl.visibility = View.INVISIBLE
        create_view.visibility = View.INVISIBLE
        create_add_b.visibility = View.INVISIBLE


        initCreateEvent()
        initCreateItem() }

    private fun initCreateEvent() {

        initAdresse()
        initDate()
        initEvent()
        initOrga()
        createEvent() }

    private fun initAdresse() {
        var mStreetNumber = "";
        var mStreetName = "";
        var mCity = ""
        if (!Places.isInitialized()) { Places.initialize(requireActivity().applicationContext, "AIzaSyDmX6nCTHfCYGTS4-LhPSA0y2lYwFRitPI") }
        var autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?
       var etTextInput: EditText? = autocompleteFragment?.view?.findViewById(R.id.places_autocomplete_search_input)
        val searchIcon = (autocompleteFragment?.view as LinearLayout).getChildAt(0) as ImageView
        searchIcon.visibility = View.GONE
        etTextInput?.gravity = Gravity.CENTER
        autocompleteFragment?.setPlaceFields(listOf(Field.ID, Field.NAME, Field.ADDRESS_COMPONENTS))
        autocompleteFragment?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) { place.addressComponents?.asList()?.forEach { mAdressComp ->
                    Log.i("TAG", "AutoComplet: " + mAdressComp.types + " ")
                    when {mAdressComp.types.contains("street_number") -> { mStreetNumber = mAdressComp.name }
                        mAdressComp.types.contains("route") -> { mStreetName = mAdressComp.name }
                        mAdressComp.types.contains("locality") -> { mCity = mAdressComp.name } } }

              //  autocompleteFragment?.isVisible =  false
                etTextInput?.visibility = View.INVISIBLE
                create_address_txt.text = mStreetNumber +" " + mStreetName +", " + mCity
                mEventAddress = "$mStreetNumber $mStreetName, $mCity "

            }

            override fun onError(status: Status) { Log.i("TAG", "An error occurred: $mItemStatus") }
        })
    }


            private fun initDate() {
        create_date_txt?.setOnClickListener {


            val dpd = DatePickerDialog.OnDateSetListener { a, y, m, d ->
                val newDate = formatDate(y, m, d)
                val dayDate = FindDay(requireContext(), y, m, d)
                create_date_txt.setText("$newDate")
                mEventDate = create_date_txt.text.toString() }
            val now = android.text.format.Time()
            now.setToNow()
            val d = DatePickerDialog(
                requireContext(),
                R.style.MyAppThemeCalendar,
                dpd,
                now.year,
                now.month,
                now.monthDay
            )
            d.show()
            d.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.black))
            d.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.black)) } }


    private fun initButton() {
        create_add_button.setOnClickListener {
            val zoom1 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomin_v2)
            val zoom2 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomout_2)
            create_status_bring.startAnimation(zoom1)
            create_status_bring.startAnimation(zoom2)
            create_status_need.startAnimation(zoom1)
            create_status_need.startAnimation(zoom2)
        }
    }


    private fun initEvent() {
        mEventName = create_name_edit.text.toString()
    }

    private fun initOrga() {
        mUserVM.getUserById(mIdUser)?.observe(requireActivity(), androidx.lifecycle.Observer{ mlmu ->
            if (mlmu != null) {
                mEventOrga = mlmu.mNameUser
                mEventOrga = create_orga_txt.text.toString()
            }
        })}

    private fun initCreateItem() {
        initButton()
        initItem()
        initRV()
        initQuantity()
        initStatus()
        create_add_b?.setOnClickListener {
            initItem()
            if (mItemName == "") { Toast.makeText(
                requireContext(),
                "Please add item <3",
                Toast.LENGTH_LONG
            ).show()}

            else {  var mMyItem = MyItem(
                mItemStatus,
                mItemQuantity,
                mItemName,
                mEventOrga,
                mItemUniqueID,
                mEventUniqueID,
                mPhotoUser
            )

                   mListMyItem.add(mMyItem)
                   mCreateAdapter.notifyDataSetChanged()} } }

    private fun initItem() {
        create_edit_item.doOnTextChanged { text, start, before, count ->
            if (count ==1)
            {    goAnimImage(create_add_b)
                goAnimLayout(create_qty_cl)
            }
            mItemName = create_edit_item.text.toString() }
    }



    private fun initRV() {
        mCreateAdapter = CreateAdapter(requireActivity(), mListMyItem)
        create_recyclerView.layoutManager = LinearLayoutManager(context)
        create_recyclerView.adapter = mCreateAdapter }


    private fun initQuantity() {
        create_quantity_item.count = 1
        create_quantity_item.minValue = 1
        create_quantity_item.maxValue = 50
        create_quantity_item.sideTapEnabled = true
        create_quantity_item.addStepCallback(object : OnStepCallback {
            override fun onStep(qty: Int, positive: Boolean) {
                mItemQuantity = qty.toString() } })
    }



    private fun initStatus() {
        create_status_bring?.setOnClickListener {
            create_status_bring.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.blue_800))
            mItemStatus = "I bring"
            create_status_need.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.blue_200))

            goAnimTxt(create_txtV_item)
            goAnimEdit(create_edit_item)
            goAnimView(create_view)
            create_edit_item.visibility = View.VISIBLE

        }

        create_status_need?.setOnClickListener {
            create_status_need.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.blue_800
                )
            )
            mItemStatus = "I need"
            create_status_bring.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.blue_200))

            goAnimTxt(create_txtV_item)
            goAnimEdit(create_edit_item)
            goAnimView(create_view)
            create_edit_item.visibility = View.VISIBLE
        }
    }


    private fun createItem() {
        mItemVM.createItem(mListMyItem)  }

    fun createEvent(){
        create_save?.setOnClickListener {


            println("event-------------Click ")
            initEvent()
            initOrga()

            if (mEventDate== "") {Toast.makeText(
                requireContext(),
                "Please add date...",
                Toast.LENGTH_SHORT
            ).show()}
            if (mEventName == "") {Toast.makeText(
                requireContext(),
                "Please add event name...",
                Toast.LENGTH_SHORT
            ).show()}
            if (mEventOrga == "") {Toast.makeText(
                requireContext(),
                "Please add organizer...",
                Toast.LENGTH_SHORT
            ).show()}
            if (mEventAddress == "") {Toast.makeText(
                requireContext(),
                "Please add address...",
                Toast.LENGTH_SHORT
            ).show()}

            else {
                val mDataEvent = MyEvent(
                    mEventDate,
                    mEventName,
                    mEventAddress,
                    mEventUniqueID,
                    mIdUser,
                    mNameUser,
                    mPhotoUser
                )

                mEventVM.createEvent(mDataEvent)
                createItem()

/*
                val mIntent = Intent(requireContext(), MainActivity::class.java)
                mIntent.putExtra(LoginActivity.USERID, mIdUser)
                startActivity(mIntent)

 */

            //    Navigation.findNavController(requireActivity(), R.id.hostFragment).navigate(R.id.homeFragment)


                addEventIdToUser()
            } } }



    private fun addEventIdToUser() { mUserVM.upadateEventUser(mIdUser, mEventUniqueID) }

    private fun goAnimView(createView: View?) {
        val zoom1 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomin_v2)
        val zoom2 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomout_2)
        createView?.startAnimation(zoom1)
        createView?.startAnimation(zoom2) }

    private fun goAnimEdit(createEditItem: EditText?) {
        val zoom1 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomin_v2)
        val zoom2 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomout_2)
        createEditItem?.startAnimation(zoom1)
        createEditItem?.startAnimation(zoom2) }

    private fun goAnimTxt(createTxtvItem: TextView?) {
        val zoom1 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomin_v2)
        val zoom2 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomout_2)
        createTxtvItem?.startAnimation(zoom1)
        createTxtvItem?.startAnimation(zoom2)
    }

    private fun goAnimLayout(detailQtyCl: ConstraintLayout?) {
        val zoom1 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomin_v2)
        val zoom2 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomout_2)
        detailQtyCl?.startAnimation(zoom1)
        detailQtyCl?.startAnimation(zoom2)

    }
    private fun goAnimImage(detailAdd: ImageView?) {
        val zoom1 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomin_v2)
        val zoom2 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomout_2)
        detailAdd?.startAnimation(zoom1)
        detailAdd?.startAnimation(zoom2)

    }

}