package com.so.dingbring.view.fragment

import android.content.Context
import androidx.fragment.app.Fragment
import com.so.dingbring.view.activity.ItemActivity

abstract class BaseFragment : Fragment() {

    private lateinit var ITEMACTIVITY: ItemActivity

    override fun onAttach(context: Context)  {
        super.onAttach(context)
        ITEMACTIVITY = context as ItemActivity }


}