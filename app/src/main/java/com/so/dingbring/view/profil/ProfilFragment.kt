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
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.so.dingbring.R
import com.so.dingbring.data.MyEventViewModel
import com.so.dingbring.data.MyUserViewModel
import com.so.dingbring.databinding.FragmentProfilBinding
import com.so.dingbring.view.main.MainActivity
import kotlinx.android.synthetic.main.dialog_layout.*
import org.koin.android.viewmodel.ext.android.viewModel


class ProfilFragment : Fragment() {

    private lateinit var mBng: FragmentProfilBinding
    private val mUserVM by viewModel<MyUserViewModel>()
    var mNameUser = "..."
    var mEmailUser = "..."
    var mPhotoUser = "..."
    var mIdUser = "..."

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        FirebaseApp.initializeApp(requireContext())
        mBng = DataBindingUtil.inflate(inflater, R.layout.fragment_profil, container, false)

        mIdUser  = MainActivity.mIdUser
        mNameUser  = MainActivity.mNameUser
        mEmailUser  = MainActivity.mEmailUser
        mPhotoUser  = MainActivity.mPhotoUser

        checkFireStoreUser(mBng)
        saveNewUserInfo()
        refreshInfo()
        invisibleButton()

        editNameUser(mBng)

        return mBng.root}

    private fun invisibleButton() {
        mBng.profilRefresh.visibility = View.INVISIBLE
        mBng.profilSave.visibility = View.INVISIBLE
    }
    /*
private fun invisibleAnimButton() {
    val animationD = AnimationUtils.loadAnimation(requireContext(), R.anim.zoominbig)
    mBng.profilSave.startAnimation(animationD)
    mBng.profilRefresh.startAnimation(animationD)
}



private fun visibleAnimButton() {
    val animationD = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomoutbig)
    mBng.profilSave.startAnimation(animationD)
    mBng.profilRefresh.startAnimation(animationD)
}

     */
private fun visibleButton() {
    mBng.profilRefresh.visibility = View.VISIBLE
    mBng.profilSave.visibility = View.VISIBLE }



    private fun refreshInfo() {
        mBng.profilRefresh.setOnClickListener {
            checkFireStoreUser(mBng) } }

    private fun editNameUser(mBng: FragmentProfilBinding?) {
        mBng?.profilEditName?.setOnClickListener {
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
                mBng!!.profilName.text = mNameUser
                mUserVM.updateUserName(mIdUser, mNameUser)}

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        changeUserProfil()
        super.onViewCreated(view, savedInstanceState) }

    private fun checkFireStoreUser(mBng: FragmentProfilBinding?) {
        mUserVM.getUserById(mIdUser)?.observeForever { mlmu ->
                if (mlmu != null){
                    mNameUser = mlmu.mNameUser
                    mPhotoUser = mlmu.mPhotoUser
                    mIdUser = mlmu.mUserId
                    createDisplay(mBng)} }}

    private fun createDisplay(mBng: FragmentProfilBinding?) {
     mBng!!.profilName.text = mNameUser
        Glide.with(requireContext())
            .load(mPhotoUser)
            .apply(RequestOptions.circleCropTransform())
            .into(mBng.profilImage)

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
        var mLstImageV = arrayListOf(mBng.profilImg1,mBng.profilImg2,mBng.profilImg3,mBng.profilImg4,mBng.profilImg5,mBng.profilImg6,mBng.profilImg7)

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
                .load(key).apply(RequestOptions.circleCropTransform()).into( mBng.profilImage)
                mPhotoUser = key } }

        private fun saveNewUserInfo() {
            mBng.profilSave.setOnClickListener {
                mUserVM.updateUserPhoto(mIdUser, mPhotoUser)

                invisibleButton()

            }

        }


}