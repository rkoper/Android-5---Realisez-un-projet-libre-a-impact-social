package com.so.dingbring

import android.content.Context
import android.content.res.Resources
import androidx.fragment.app.FragmentManager
import com.so.dingbring.R.string
import java.util.*

object Utils {

    fun formatDate(y: Int, m: Int, d: Int) : String {
        val s = m + 1
        if  (s < 10 && d >10 ) {return "$d/0$s/$y"}
        if  (d <10 && s > 10 ) {return  "0$d/$s/$y"}
        return if (s < 10 && d <10 ) {
            "0$d/0$s/$y"
        } else {
            "$d/$s/$y"
        } }


}