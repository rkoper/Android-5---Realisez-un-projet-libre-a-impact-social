package com.so.dingbring.create

import android.app.DatePickerDialog
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.so.dingbring.R
import com.so.dingbring.Utils.formatDate
import com.so.dingbring.databinding.FragmentCreateBinding
import com.so.dingbring.home.HomeViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class CreateFragment : Fragment() {

    private var mDate : Long = 3
    private lateinit var mBinding: FragmentCreateBinding
    private val mHomeViewModel by viewModel<HomeViewModel>()
    var mStatus: String? = "%"
    var mItem: String? = "%"
    var mQuantity: String? = "%"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_create, container, false)

        initDate(mBinding)
        initAdd(mBinding)

        return mBinding.root
    }

    private fun initAdd(mBinding: FragmentCreateBinding?) {

        initStatus(mBinding)
        initItem(mBinding)
        initQuantity(mBinding)

        }

    private fun initQuantity(mBinding: FragmentCreateBinding?) {
        val mSelectQuantity = resources.getStringArray(((R.array.Quantity)))
        if (mBinding?.createQuantity != null) { mBinding?.createQuantity.background.setColorFilter(resources.getColor(R.color.black), PorterDuff.Mode.SRC_ATOP)
            val adapter = ArrayAdapter(requireContext(), R.layout.spinner_custom, mSelectQuantity)
            mBinding?.createQuantity.adapter = adapter}

        mBinding?.createQuantity?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, i: Int, id: Long) {
                mQuantity = mSelectQuantity[i]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun initStatus(mBinding: FragmentCreateBinding?) {
    }

    private fun initItem(mBinding: FragmentCreateBinding?) {

    }


    private fun initDate(mBinding: FragmentCreateBinding?) {
        mBinding?.createDate?.text = SimpleDateFormat("dd/MM/yyyy").format(Date())
        mBinding?.createDate?.setOnClickListener {
            val dpd = DatePickerDialog.OnDateSetListener { _, y, m, d ->
                mBinding.createDate.text =  formatDate(y,m,d) }
            val now = android.text.format.Time()
            now.setToNow()
            val d = DatePickerDialog(requireContext(), R.style.MyAppThemeCalendar, dpd, now.year, now.month, now.monthDay)
            d.show()
            d.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.black))
            d.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.black)) } }

    }


