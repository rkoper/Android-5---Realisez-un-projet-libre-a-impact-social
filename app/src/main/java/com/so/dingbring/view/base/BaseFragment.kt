package com.so.dingbring.view.base

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.so.dingbring.data.MyEventViewModel
import com.so.dingbring.view.main.ItemActivity
import com.so.dingbring.view.main.MainActivity
import org.koin.android.viewmodel.ext.android.viewModel

abstract class BaseFragment : Fragment() {

    lateinit var ITEM_ACTIVITY: ItemActivity


    override fun onAttach(context: Context)  {
        super.onAttach(context)
        ITEM_ACTIVITY = context as ItemActivity

    }

}