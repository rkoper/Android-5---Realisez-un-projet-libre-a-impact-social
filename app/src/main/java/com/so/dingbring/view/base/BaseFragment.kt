package com.so.dingbring.view.base

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.so.dingbring.data.MyEventViewModel
import com.so.dingbring.view.main.ItemActivity
import org.koin.android.viewmodel.ext.android.viewModel

abstract class BaseFragment : Fragment() {

    lateinit var ITEMACTIVITY: ItemActivity


    override fun onAttach(context: Context)  {
        super.onAttach(context)
        ITEMACTIVITY = context as ItemActivity

    }

}