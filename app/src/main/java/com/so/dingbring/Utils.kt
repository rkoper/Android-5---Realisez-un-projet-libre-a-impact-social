package com.so.dingbring

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Typeface
import android.os.Build
import android.os.LocaleList
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
import com.google.android.material.internal.ContextUtils
import com.so.dingbring.R.string
import java.text.SimpleDateFormat
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

    fun FindDay(mContext: Context, y: Int, m: Int, d: Int) : String {
        val calendar = Calendar.getInstance();
        calendar.set(y, m, d);
        val dayName: Int = calendar.get(Calendar.DAY_OF_WEEK)
        val dayName1: Int = calendar.get(Calendar.DATE)
        val dayName2: Int = calendar.get(Calendar.DAY_OF_MONTH)
        val dayName3: Int = calendar.get(Calendar.DAY_OF_YEAR)


        if (dayName == 2)
        {return mContext.resources.getText(string.day1).toString()}
        if (dayName == 3)
        {return mContext.resources.getText(string.day2).toString()}
        if (dayName == 4)
        {return mContext.resources.getText(string.day3).toString()}
        if (dayName == 5)
        {return mContext.resources.getText(string.day4).toString()}
        if (dayName == 6)
        {return mContext.resources.getText(string.day5).toString()}
        if (dayName == 7)
        {return mContext.resources.getText(string.day6).toString()}
        if (dayName == 1)
        {return mContext.resources.getText(string.day7).toString()}

        else return ""

    }

    fun configureAutoCompleteFrag(

        childFragmentManager: FragmentManager,
        resources: Resources,
        mContext: Context,
        hint: String
    ) : String? {
        /*
       var autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?
       var fView: View? = autocompleteFragment?.view
       var etTextInput: EditText? = fView?.findViewById(R.id.places_autocomplete_search_input)
       etTextInput?.setBackgroundColor(resources.getColor(R.color.colorTransparent))
       etTextInput?.setTextColor(resources.getColor(R.color.white))
       etTextInput?.setHintTextColor(resources.getColor(R.color.white))
       etTextInput?.gravity = Gravity.CENTER
       etTextInput?.hint = hint
       val font: Typeface? = ResourcesCompat.getFont(mContext, R.font.ace)
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

        */
       return "a"


    }

    fun formatAdress(adress:String, case:Boolean) : String {
         val formataddress = adress.split(",")

        if (case)
        {return formataddress[0]}
        if (!case)
        {return formataddress[1]}
        else
        { return ""} }



    fun updateLocale(c: Context, localeToSwitchTo: Locale) {

    }
}