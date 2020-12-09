package com.so.dingbring.view.base

import android.content.Context
import androidx.fragment.app.Fragment
import com.so.dingbring.view.main.ItemActivity

abstract class BaseFragment : Fragment() {

    private lateinit var ITEM_ACTIVITY: ItemActivity

    override fun onAttach(context: Context)  {
        super.onAttach(context)
        ITEM_ACTIVITY = context as ItemActivity }

}