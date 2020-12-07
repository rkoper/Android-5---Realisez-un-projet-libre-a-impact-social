package com.so.dingbring

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.dialog_layout_profil.*


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


    fun autoCompleteFrag(
        childFragmentManager: FragmentManager
    )  : AutocompleteSupportFragment {
        val autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?
        val searchIcon = (autocompleteFragment?.view as LinearLayout).getChildAt(0) as ImageView
        searchIcon.visibility = View.GONE
        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS)
        autocompleteFragment.setPlaceFields(
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS_COMPONENTS))

        return autocompleteFragment
    }



    fun autoCompleteTxtInput(childFragmentManager: FragmentManager,
                             resources: Resources,
                             font:Typeface) : EditText? {
        val autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?
        val fView: View? = autocompleteFragment?.view
        val etTextInput: EditText? = fView?.findViewById(R.id.places_autocomplete_search_input)
        etTextInput?.setBackgroundColor(resources.getColor(R.color.fui_transparent))
        etTextInput?.setTextColor(resources.getColor(R.color.white))
        etTextInput?.setHintTextColor(resources.getColor(R.color.white))
        etTextInput?.gravity = Gravity.CENTER
        etTextInput?.hint = " "
        etTextInput?.setTypeface(font, Typeface.BOLD)

        return etTextInput
    }

    fun listDrawable() : ArrayList<String> {
        return arrayListOf(
             "https://i.ibb.co/Vtwz7tL/C6.png", "https://i.ibb.co/PN1dfmb/C5.png",
             "https://i.ibb.co/SywTtCy/C4.png", "https://i.ibb.co/XjFxB00/C3.png",
             "https://i.ibb.co/nsbNLwq/c2.png", "https://i.ibb.co/WDjtfWR/c1.png",
             "https://i.ibb.co/7YHdHKt/C7.png")
    }

    fun listImageV(d: Dialog): ArrayList<ImageView> {
        return arrayListOf(d.setting_img_profil_1, d.setting_img_profil_2, d.setting_img_profil_3,
            d.setting_img_profil_4, d.setting_img_profil_5, d.setting_img_profil_6, d.setting_img_profil_7)
    }


    fun testAnim(
        mContext: Context,
        mDetailEditMinus: FloatingActionButton,
        mDetailEditPlus: FloatingActionButton,
        mDetailEditDelete: FloatingActionButton,
        mDetailEditSub: ImageView,
        mDetailImageUser: ImageView
    ) {
        val animation100right = AnimationUtils.loadAnimation(mContext, R.anim.slideright100)
        val animation100 = AnimationUtils.loadAnimation(mContext, R.anim.slideleft100)
        val animation200 = AnimationUtils.loadAnimation(mContext, R.anim.slideleft200)
        val animation300 = AnimationUtils.loadAnimation(mContext, R.anim.slideleft300)
        val animation400 = AnimationUtils.loadAnimation(mContext, R.anim.slideleft400)
        mDetailEditMinus.visibility = View.VISIBLE
        mDetailEditPlus.visibility = View.VISIBLE
        mDetailEditDelete.visibility = View.VISIBLE
        mDetailEditSub.visibility = View.VISIBLE
        mDetailEditPlus.startAnimation(animation300)
        mDetailEditMinus.startAnimation(animation200)
        mDetailEditDelete.startAnimation(animation100)
        mDetailEditSub.startAnimation(animation100)
        mDetailImageUser.startAnimation(animation100right)
        mDetailImageUser.visibility = View.INVISIBLE
    }


    fun testAnim2(
        mContext: Context,
        mDetailEditMinus: FloatingActionButton,
        mDetailEditPlus: FloatingActionButton,
        mDetailEditDelete: FloatingActionButton,
        mDetailEditSub: ImageView,
        mDetailImageUser: ImageView
    ) {
        val animation100left = AnimationUtils.loadAnimation(mContext, R.anim.slideleft100)
        val animation100 = AnimationUtils.loadAnimation(mContext, R.anim.slideright100)
        val animation200 = AnimationUtils.loadAnimation(mContext, R.anim.slideright200)
        val animation300 = AnimationUtils.loadAnimation(mContext, R.anim.slideright300)
        val animation400 = AnimationUtils.loadAnimation(mContext, R.anim.slideright400)
        mDetailImageUser.startAnimation(animation100left)
        mDetailEditPlus.startAnimation(animation200)
        mDetailEditMinus.startAnimation(animation300)
        mDetailEditDelete.startAnimation(animation400)
        mDetailEditSub.startAnimation(animation400)
        mDetailEditMinus.visibility = View.GONE
        mDetailEditPlus.visibility = View.GONE
        mDetailEditDelete.visibility = View.GONE
        mDetailEditSub.visibility = View.GONE
        mDetailImageUser.visibility = View.VISIBLE
    }
}