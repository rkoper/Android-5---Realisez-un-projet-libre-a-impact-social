package com.so.dingbring.view.create

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isInvisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.Place.*
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.so.dingbring.R
import com.so.dingbring.Utils.FindDay
import com.so.dingbring.Utils.formatDate
import com.so.dingbring.data.*
import com.so.dingbring.databinding.FragmentCreateBinding
import com.so.dingbring.view.detail.create.CreateAdapter
import com.so.dingbring.view.login.LoginActivity
import com.so.dingbring.view.main.MainActivity
import kotlinx.android.synthetic.main.fragment_create.*
import nl.dionsegijn.steppertouch.OnStepCallback
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.reflect.Array
import java.util.*


class CreateFragment : Fragment() {


    private val mUserVM by viewModel<MyUserViewModel>()
    private val mEventVM by viewModel<MyEventViewModel>()
    private val mItemVM by viewModel<MyItemViewModel>()

    private var mEventDate = ""
    private var mEventOrga = ""
    private var mEventName = ""
    private var mEventAdress = ""

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


    var mListMyItem = arrayListOf<MyItem>()

    private lateinit var mCreateAdapter: CreateAdapter
    private lateinit var mBinding: FragmentCreateBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_create, container, false)

        mIdUser  = MainActivity.mIdUser
        mNameUser  = MainActivity.mNameUser
        mEmailUser  = MainActivity.mEmailUser
        mPhotoUser  = MainActivity.mPhotoUser
        println("( Create )" + mIdUser + "( - )" + mNameUser + "( - )" + mEmailUser + "( - )" + mPhotoUser)

      //  testprintln()


        return mBinding.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        if (!Places.isInitialized()) {
            Places.initialize(
                requireActivity().applicationContext,
                "AIzaSyDmX6nCTHfCYGTS4-LhPSA0y2lYwFRitPI"
            )
        }
        var autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?
        var etTextInput: EditText? = autocompleteFragment?.view?.findViewById(R.id.places_autocomplete_search_input)
        autocompleteFragment?.setPlaceFields(
            listOf(
                Field.ID,
                Field.NAME,
                Field.ADDRESS_COMPONENTS
            )
        )
        autocompleteFragment?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                place.addressComponents?.asList()?.forEach { mAdressComp ->
                    Log.i("TAG", "AutoComplet: " + mAdressComp.types + " ")
                    when {
                        mAdressComp.types.contains("street_number") -> {
                            mStreetNumber = mAdressComp.name
                        }
                        mAdressComp.types.contains("route") -> {
                            mStreetName = mAdressComp.name
                        }
                        mAdressComp.types.contains("locality") -> {
                            mCity = mAdressComp.name
                        }
                    }
                }
                autocompleteFragment.view?.isInvisible
                etTextInput?.visibility = View.INVISIBLE
                create_name_txt.text = mStreetNumber +" " + mStreetName +", " + mCity
                mEventAdress = "$mStreetNumber $mStreetName, $mCity "

            }

            override fun onError(status: Status) {
                Log.i("TAG", "An error occurred: $mItemStatus")
            }
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


    private fun initEvent() {
        mEventName = create_name_edit.text.toString()
    }

    private fun initOrga() {
        mUserVM.getUserById(mIdUser)?.observe(requireActivity(), { mlmu ->
            if (mlmu != null) {
                mNameUser = mlmu.mNameUser
                create_orga_txt.text = mNameUser
                mEventOrga = create_orga_txt.text.toString()
            }
        })}

    private fun initCreateItem() {
        initItem()
        initRV()
       initQuantity()
        initStatus()
        create_add?.setOnClickListener {
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
        mItemName = create_item.text.toString() }

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
                mItemQuantity = qty.toString()
                println(" mItemQty ------>$mItemQuantity")
            }
        })
    }

    private fun initStatus() {
        create_status_bring?.isChecked = true
        mItemStatus = "I bring"
        create_status_need?.setOnCheckedChangeListener { _, b ->
            if (b) { mItemStatus = "I need"; create_status_bring.isChecked = false; } }
        create_status_bring?.setOnCheckedChangeListener { _, b ->
            if (b) { mItemStatus = "I bring" ; create_status_need.isChecked = false; } }
            }


    private fun createItem() {
        mItemVM.createItem(mListMyItem)  }

    fun createEvent(){
        create_save?.setOnClickListener {
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
            if (mEventAdress == "") {Toast.makeText(
                requireContext(),
                "Please add address...",
                Toast.LENGTH_SHORT
            ).show()}

            else {
                val mDataEvent = MyEvent(
                    mEventDate,
                    mEventName,
                    mEventOrga,
                    mEventAdress,
                    mEmailUser,
                    mEventUniqueID
                )

                mEventVM.createEvent(mDataEvent)
                createItem()
                println("------|ADD US in Ev.|--  5  ----" + mIdUser + "/// " + mEventUniqueID)


                val mIntent = Intent(requireContext(), MainActivity::class.java)
                mIntent.putExtra(LoginActivity.USERID, mIdUser)
                startActivity(mIntent)


                addEventIdToUser()
            } } }

    private fun addEventIdToUser() {

        println("------|ADD US in Ev.|--  3  ----" + mIdUser + "/// " + mEventUniqueID)


        mUserVM.upadateEventUser(mIdUser, mEventUniqueID)
        }


}