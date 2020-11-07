package com.so.dingbring.view.profil

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gauravk.bubblenavigation.BubbleToggleView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.so.dingbring.R
import com.so.dingbring.data.MyEventViewModel
import com.so.dingbring.data.MyUserViewModel
import com.so.dingbring.view.main.MainActivity
import kotlinx.android.synthetic.main.dialog_layout.*
import kotlinx.android.synthetic.main.fragment_profil.*
import org.koin.android.viewmodel.ext.android.viewModel


class ProfilFragment : Fragment() {

    private val mUserVM by viewModel<MyUserViewModel>()
    var mNameUser = "..."
    var mEmailUser = "..."
    var mPhotoUser = "..."
    var mIdUser = "..."
    var varbutton : ImageView ? = null
    var mItemSelect : BubbleToggleView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        FirebaseApp.initializeApp(requireContext())
        val view: View = inflater.inflate(R.layout.fragment_profil, container, false)


        mIdUser  = MainActivity.mIdUser
        mNameUser  = MainActivity.mNameUser
        mEmailUser  = MainActivity.mEmailUser
        mPhotoUser  = MainActivity.mPhotoUser




    //    mItemSelect = activity?.findViewById(R.id.main_item_personn)
       varbutton = activity?.findViewById(R.id.main_button)

        return  view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        invisibleButton()
        varbutton = activity?.findViewById(R.id.main_button)
        varbutton?.visibility  = View.VISIBLE
        changeUserProfil()}


    private fun invisibleButton() {
        profil_refresh.visibility = View.INVISIBLE
        profil_save.visibility = View.INVISIBLE
    }
    /*
private fun invisibleAnimButton() {
    val animationD = AnimationUtils.loadAnimation(requireContext(), R.anim.zoominbig)
    mBng.profil_save.startAnimation(animationD)
    mBng.profilRefresh.startAnimation(animationD)
}



private fun visibleAnimButton() {
    val animationD = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomoutbig)
    mBng.profil_save.startAnimation(animationD)
    mBng.profilRefresh.startAnimation(animationD)
}

     */
private fun visibleButton() {
        varbutton?.visibility  = View.VISIBLE
        profil_refresh.visibility = View.VISIBLE
    profil_save.visibility = View.VISIBLE }



    private fun refreshInfo() {
        profil_refresh.setOnClickListener {
            checkFireStoreUser() } }

    private fun editNameUser() {
        profil_edit_name?.setOnClickListener {
            val d = Dialog(requireContext())
            d.requestWindowFeature(Window.FEATURE_NO_TITLE)
            d.window?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
            d.setContentView(R.layout.dialog_layout)
            d.custom_dialog_txt.hint = mNameUser
            d.show()

            d.custom_dialog_txt.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (start == 0 )
                    { d.custom_dialog_ok.isVisible = false
                        d.custom_dialog_not_ok.isVisible = true}
                    else {
                        d.custom_dialog_ok.isVisible = true
                        d.custom_dialog_not_ok.isVisible = false}
                } })

            d.custom_dialog_not_ok.setOnClickListener {
                Toast.makeText(requireContext(), " Please add description...", Toast.LENGTH_SHORT).show() }

            d.custom_dialog_ok.setOnClickListener {
                d.dismiss()
                visibleButton()
                // --------- 2
                mNameUser = d.custom_dialog_txt.text.toString()
                profil_name.text = mNameUser
                mUserVM.updateUserName(mIdUser, mNameUser)}

        }

    }



    private fun checkFireStoreUser() {
        mUserVM.getUserById(mIdUser)?.observeForever { mlmu ->
                if (mlmu != null){
                    mNameUser = mlmu.mNameUser
                    mPhotoUser = mlmu.mPhotoUser
                    mIdUser = mlmu.mUserId
                    createDisplay()} }}

    private fun createDisplay() {
     profil_name.text = mNameUser
        Glide.with(requireContext())
            .load(mPhotoUser)
            .apply(RequestOptions.circleCropTransform())
            .into(profil_image)

    }

    private fun changeUserProfil() {
        val c = HashMap<String,ImageView>()
        var mLstDrawable
                = arrayListOf("https://i.ibb.co/Vtwz7tL/C6.png",
            "https://i.ibb.co/PN1dfmb/C5.png",
            "https://i.ibb.co/SywTtCy/C4.png",
            "https://i.ibb.co/XjFxB00/C3.png",
            "https://i.ibb.co/nsbNLwq/c2.png",
            "https://i.ibb.co/WDjtfWR/c1.png",
            "https://i.ibb.co/7YHdHKt/C7.png")
        var mLstImageV = arrayListOf(
            profil_img1,
            profil_img2,
            profil_img3,
            profil_img4,
            profil_img5,
            profil_img6,
            profil_img7)

           for (i in 0..6) { c[mLstDrawable[i]] = mLstImageV[i] }

        c.forEach { loadUserChange(it) }
    }

    private fun loadUserChange(it: Map.Entry<String, ImageView>) {
        var key = it.key
        Glide.with(requireContext())
            .load(it.key).apply(RequestOptions.circleCropTransform()).into(it.value)

        it.value.setOnClickListener {view ->
            visibleButton()
            Glide.with(requireContext())
                .load(key).apply(RequestOptions.circleCropTransform()).into( profil_image)
                mPhotoUser = key } }

        private fun saveNewUserInfo() {
            profil_save.setOnClickListener {
                mUserVM.updateUserPhoto(mIdUser, mPhotoUser)

                invisibleButton()

            }

        }


}