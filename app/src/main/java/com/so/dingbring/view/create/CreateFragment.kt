package com.so.dingbring.view.create

import android.app.DatePickerDialog
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gauravk.bubblenavigation.BubbleNavigationConstraintView
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.Place.Field
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.so.dingbring.R
import com.so.dingbring.Utils.FindDay
import com.so.dingbring.Utils.formatDate
import com.so.dingbring.data.*
import com.so.dingbring.view.detail.create.CreateAdapter
import com.so.dingbring.view.main.ItemActivity
import kotlinx.android.synthetic.main.fragment_create.*
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

    var varbutton : BubbleNavigationConstraintView? = null

    var mListMyItem = arrayListOf<MyItem>()

    private lateinit var mCreateAdapter: CreateAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_create, container, false)

        mIdUser = ItemActivity.mIdUser
        mNameUser =ItemActivity.mNameUser
        mEmailUser = ItemActivity.mEmailUser
        mPhotoUser = ItemActivity.mPhotoUser


        println("mNameUser----1--->" + mNameUser)
   //     varbutton = activity?.findViewById(R.id.main_button)



        return  view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        varbutton = activity?.findViewById(R.id.floating_top_bar_navigation)

        iteminvisible()
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    //Handle back event from any fragment

                    view?.findNavController()?.navigate(R.id.action_create_to_home)
                    varbutton?.setCurrentActiveItem(1)
                }
            })


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
    if (!Places.isInitialized()) { Places.initialize(requireActivity().applicationContext, "AIzaSyA29ttP7zVNeG68hHXh4g6VpOMZxJRDE58") }

        /*
     var autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?
    var etTextInput: EditText? = autocompleteFragment?.view?.findViewById(R.id.places_autocomplete_search_input)
     val searchIcon = (autocompleteFragment?.view as LinearLayout).getChildAt(0) as ImageView
     searchIcon.visibility = View.GONE
     autocompleteFragment?.setPlaceFields(listOf(Field.ID, Field.NAME, Field.ADDRESS_COMPONENTS))

   */

        val autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?
        val fView: View? = autocompleteFragment?.view
        val etTextInput: EditText? = fView?.findViewById(R.id.places_autocomplete_search_input)
        etTextInput?.setBackgroundColor(resources.getColor(R.color.white))
        etTextInput?.setTextColor(resources.getColor(R.color.white))
        etTextInput?.setHintTextColor(resources.getColor(R.color.white))
        etTextInput?.gravity = Gravity.CENTER
      //  etTextInput?.hint = hint
        val font: Typeface? = ResourcesCompat.getFont(requireContext(), R.font.roboto)
        etTextInput?.setTypeface(font, Typeface.BOLD)
        val searchIcon = (autocompleteFragment?.view as LinearLayout).getChildAt(0) as ImageView
        searchIcon.visibility = View.GONE
        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS)
        autocompleteFragment?.setPlaceFields(listOf(Field.ID, Field.NAME, Field.ADDRESS_COMPONENTS))
        autocompleteFragment?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) { place.addressComponents?.asList()?.forEach { mAdressComp ->
                    Log.i("TAG", "AutoComplet: " + mAdressComp.types + " ")
                    when {mAdressComp.types.contains("street_number") -> { mStreetNumber = mAdressComp.name }
                        mAdressComp.types.contains("route") -> { mStreetName = mAdressComp.name }
                        mAdressComp.types.contains("locality") -> { mCity = mAdressComp.name } } }

              //  autocompleteFragment?.isVisible =  false
                etTextInput?.visibility = View.INVISIBLE
                create_address_txt_display.visibility = View.VISIBLE
                create_address_txt_display.text = mStreetNumber +" " + mStreetName +", " + mCity
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
                create_date_edit.setText("$newDate")
                mEventDate = create_date_edit.text.toString() }
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
        println("mNameUser----1--->" + mNameUser)
        create_orga_txt_display.text = mNameUser
            }

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

            else {

                println("mNameUser----initCreateItem--->" + mNameUser)
                println("mPhotoUser----initCreateItem--->" + mPhotoUser)


                var mMyItem = MyItem(
                mItemStatus,
                mItemQuantity,
                mItemName,
                mEmailUser,
                mItemUniqueID,
                mEventUniqueID,
                mPhotoUser
            )


                   mListMyItem.add(mMyItem)
                   mCreateAdapter.notifyDataSetChanged()

                iteminvisible()

                val zoom1 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomout_main)

                create_status_bring.startAnimation(zoom1)
                create_status_need.startAnimation(zoom1)
                test1.startAnimation(zoom1)
                create_qty_cl.startAnimation(zoom1)
                test.startAnimation(zoom1)
                create_add_b.startAnimation(zoom1)


            } } }

    private fun initItem() {
        test1.doOnTextChanged { text, start, before, count ->
            if (count ==1)
            {    goAnimImage(create_add_b)
                goAnimLayout(create_qty_cl)
            }
            mItemName = test1.text.toString() }
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

            goAnimTxt(test1)
            goAnimEdit(test1)
            test1.visibility = View.VISIBLE
            test.visibility = View.VISIBLE

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

            goAnimTxt(test1)
            goAnimEdit(test1)
          //  goAnimView(create_view)
            test1.visibility = View.VISIBLE
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
            if (mNameUser == "") {Toast.makeText(
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
                println("mEventDate------->" + mEventDate)
                println("mEventName------->" + mEventName)
                println("mEventAddress------->" + mEventAddress)
                println("mEventUniqueID------->" + mEventUniqueID)
                println("mIdUser------->" + mIdUser)
                println("mNameUser------->" + mNameUser)
                println("mPhotoUser------->" + mPhotoUser)
                val mDataEvent = MyEvent(
                    mEventAddress,
                    mEventDate,
                    mEventUniqueID,
                    mEventName,
                    mIdUser,
                    mNameUser,
                    mPhotoUser
                )

                mEventVM.createEvent(mDataEvent)
                createItem()

                view?.findNavController()?.navigate(R.id.action_create_to_home)
                varbutton?.setCurrentActiveItem(1)


                addEventIdToUser()
            } } }



    private fun addEventIdToUser() { mUserVM.upadateEventUser(mIdUser, mEventUniqueID) }


    private fun iteminvisible() {
        create_status_bring.visibility = View.INVISIBLE
        create_status_need.visibility = View.INVISIBLE
        test1.visibility = View.INVISIBLE
        create_qty_cl.visibility = View.INVISIBLE
        test.visibility = View.INVISIBLE
        create_add_b.visibility = View.INVISIBLE
    }


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

    private fun goAnimZoomOut(detailAdd: ImageView?) {


    }

}