package com.so.dingbring

import android.content.Context
import android.content.res.Resources
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentManager
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun formatDate(y: Int, m: Int, d: Int) : String {
        val s = m + 1
        if  (s < 10 && d >10 ) {return "$d/0$s/$y"}
        if  (d <10 && s > 10 ) {return  "0$d/$s/$y"}
        return if (s < 10 && d <10 ) {
            "0$d/ 0$s /$y"
        } else {
            "$d/ $s /$y"
        } }

    fun FindDay(date: Int, month:Int, year:Int) : String {
        val inFormat = SimpleDateFormat("dd-MM-yyyy")
            val myDate: Date = inFormat.parse("$date-$month-$year")
            val simpleDateFormat = SimpleDateFormat("EEEE")
            val dayName = simpleDateFormat.format(myDate)

        return dayName
    }

    fun configureAutoCompleteFrag(
        childFragmentManager: FragmentManager,
        resources: Resources,
        mContext: Context,
        hint: String
    ) : AutocompleteSupportFragment? {
        var autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?
        var fView: View? = autocompleteFragment?.view
        var etTextInput: EditText? = fView?.findViewById(R.id.places_autocomplete_search_input)
        etTextInput?.setBackgroundColor(resources.getColor(R.color.colorTransparent))
        etTextInput?.setTextColor(resources.getColor(R.color.white))
        etTextInput?.setHintTextColor(resources.getColor(R.color.white))
        etTextInput?.gravity = Gravity.CENTER
        etTextInput?.hint = hint
        val font: Typeface? = ResourcesCompat.getFont(mContext, R.font.montserrat)
        etTextInput?.typeface = font
        etTextInput?.textSize = 18f
        val searchIcon =
           (autocompleteFragment?.view as? LinearLayout)?.getChildAt(0) as? ImageView
        searchIcon?.visibility = View.GONE
        autocompleteFragment?.setTypeFilter(TypeFilter.ADDRESS)
        autocompleteFragment?.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS_COMPONENTS
            )
        )


        return autocompleteFragment
    }


}