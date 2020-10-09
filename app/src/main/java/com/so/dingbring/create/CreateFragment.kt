package com.so.dingbring.create

import android.app.DatePickerDialog
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.so.dingbring.R
import com.so.dingbring.Utils
import com.so.dingbring.Utils.formatDate
import com.so.dingbring.databinding.FragmentCreateBinding
import com.so.dingbring.home.HomeViewModel
import kotlinx.android.synthetic.main.fragment_create.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.ParseException
import java.text.SimpleDateFormat


class CreateFragment : Fragment() {

    private var mDate = "xxx"
    private var mAddress = "xxx"
    private var mOrga = "xxx"
    private var mEvent = "xxx"


    private val mHomeViewModel by viewModel<HomeViewModel>()
    var mStatus: String = "I bring"
    var mItem: String = "<3"
    var mQuantity: String = "1"

    var mStatusList = arrayListOf<String>()
    var mItemList = arrayListOf<String>()
    var mQuantityList = arrayListOf<String>()

    private lateinit var mCreateAdapter: CreateAdapter
    private lateinit var mBinding: FragmentCreateBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_create, container, false)
        initView(mBinding)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRV()
        initQuantity()
        initStatus()
        initAdresse()
        initDate()

        create_add?.setOnClickListener {
            initItem()

            if (mItem == "")
            {
                Toast.makeText(requireContext(), "Please add item <3", Toast.LENGTH_LONG).show()}
            else {
            mStatusList.add(mStatus)
            mItemList.add(mItem)
            mQuantityList.add(mQuantity)
            mCreateAdapter.notifyDataSetChanged()}
        }

        create_save?.setOnClickListener {
            initEvent()
        }
    }

    private fun initRV() {
        mCreateAdapter = CreateAdapter(requireActivity(), mStatusList, mItemList, mQuantityList)
        create_recyclerView.layoutManager = LinearLayoutManager(context)
        create_recyclerView.adapter = mCreateAdapter
    }

    private fun initEvent() {
        mEvent = create_name_Event.text.toString()
    }

    private fun initItem() {
        mItem = create_item.text.toString()
    }

    private fun initQuantity() {
        val mSelectQuantity = resources.getStringArray(((R.array.Quantity)))
        if (create_quantity != null) {
            create_quantity.background.setColorFilter(
                resources.getColor(R.color.black), PorterDuff.Mode.SRC_ATOP
            )
            val adapter = ArrayAdapter(requireContext(), R.layout.spinner_custom, mSelectQuantity)
            create_quantity.adapter = adapter
        }

        create_quantity?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, i: Int, id: Long) {
                mQuantity = mSelectQuantity[i]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                mQuantity = "1"
            }
        }
    }

    private fun initStatus() {
        create_status_bring?.isChecked = true
        mStatus = "I bring"
        create_status_need?.setOnCheckedChangeListener { _, b ->
            if (b) {
                mStatus = "I need"
                create_status_bring.isChecked = false; }
        }

        create_status_bring?.setOnCheckedChangeListener { _, b ->
            if (b) {
                mStatus = "I bring"
                create_status_need.isChecked = false; }
        }
    }


    private fun initDate() {
        create_date?.setOnClickListener {

            val dpd = DatePickerDialog.OnDateSetListener { a, y, m, d ->
                val newDate = formatDate(y, m, d)

                val dayDate = Utils.FindDay(y,m,d)



                create_date.text =   dayDate
                mDate = create_date.text.toString()
            }
            val now = android.text.format.Time()
            now.setToNow()
            val d = DatePickerDialog(
                requireContext(),
                R.style.MyAppThemeCalendar,
                dpd, now.year, now.month, now.monthDay
            )
            d.show()
            d.getButton(DatePickerDialog.BUTTON_POSITIVE)
                .setTextColor(resources.getColor(R.color.black))
            d.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                .setTextColor(resources.getColor(R.color.black))
        }
    }

    private fun initAdresse() {
        var mStreetNumber = ""
        var mStreetName = ""
        var mCity = ""
        if (!Places.isInitialized()) {
            Places.initialize(
                activity?.applicationContext!!,
                "AIzaSyByK0jz-yxjpZFX88W8zjzTwtzMtkPYC4w"
            )
        }
        val autocompleteFragment = Utils.configureAutoCompleteFrag(
            childFragmentManager,
            resources,
            requireActivity(),
            "Enter Adress"
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

                mAddress = "$mStreetNumber $mStreetName, $mCity "
                create_address.text = mAddress
                create_address.visibility = View.VISIBLE
            }

            override fun onError(status: Status) {
                Log.i("TAG", "An error occurred: $mStatus")
            }
        })

    }

    private fun initView(mBinding: FragmentCreateBinding) {
        mBinding.createCancel.setOnClickListener {
            it.findNavController().navigate(R.id.action_createFragment_to_homeFragment) } }
}


