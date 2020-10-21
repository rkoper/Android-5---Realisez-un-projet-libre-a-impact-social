package com.so.dingbring.view.profil

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
import kotlinx.android.synthetic.main.dialog_layout.*
import org.koin.android.viewmodel.ext.android.viewModel


class ProfilFragment : Fragment() {

    private lateinit var mBng: FragmentProfilBinding
    private val mUserVM by viewModel<MyUserViewModel>()
    private val mEventVM by viewModel<MyEventViewModel>()
    private var mUserName = "XXXXXX"
    private var mUserMail = "XXXXX"
    private var mUserPP = "XXXXX"
    var mImageList = arrayListOf<String>()
    var mNameUser = "..."
    var mEmailUser = "..."
    var mPhotoUser = "..."
    var mUserId = "..."

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        FirebaseApp.initializeApp(requireContext())
        mBng = DataBindingUtil.inflate(inflater, R.layout.fragment_profil, container, false)

      //  mNameUser = arguments?.get("GlobalName").toString()
        mEmailUser = arguments?.get("GlobalEmail").toString()
        //mPhotoUser = arguments?.get("GlobalPhoto").toString()

        println("--profil--–|mNameUser|----"+mPhotoUser + "----–|mEmailUser|----"+ mEmailUser+ "----–|mPhotoUser|----"+mPhotoUser )

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

    private fun visibleButton() {
        mBng.profilRefresh.visibility = View.VISIBLE
        mBng.profilSave.visibility = View.VISIBLE
    }



    private fun refreshInfo() {
        mBng.profilRefresh.setOnClickListener {
            checkFireStoreUser(mBng)
        }
    }

    private fun editNameUser(mBng: FragmentProfilBinding?) {
        mBng?.profilEditName?.setOnClickListener {
            val d = Dialog(requireContext())
            d.requestWindowFeature(Window.FEATURE_NO_TITLE)
            d.window?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
            d.setContentView(R.layout.dialog_layout)
            d.custom_dialog_txt.hint = mUserName
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
                mUserName = d.custom_dialog_txt.text.toString()
                mBng!!.profilName.text = mUserName}

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        test()
        super.onViewCreated(view, savedInstanceState) }

    private fun checkFireStoreUser(mBng: FragmentProfilBinding?) {
        mUserVM.getUserByMail(mEmailUser)?.observe(requireActivity(), {mlmu ->
                if (mlmu != null){
                    mUserName = mlmu.mNameUser
                    mUserPP = mlmu.mPhotoUser
                    mUserId = mlmu.mUserId
                    createDisplay(mBng)} })}

    private fun createDisplay(mBng: FragmentProfilBinding?) {
     mBng!!.profilName.text = mUserName


        Glide.with(requireContext())
            .load(mUserPP)
            .apply(RequestOptions.circleCropTransform())
            .into(mBng.profilImage)

    }


    private fun test() {
        val c = HashMap<Int,ImageView>()
        var mLstDrawable = arrayListOf(R.drawable.img1, R.drawable.img2,R.drawable.img3,R.drawable.img4,R.drawable.img5,R.drawable.img6,R.drawable.img7)
        var mLstImageV = arrayListOf(mBng.profilImg1,mBng.profilImg2,mBng.profilImg3,mBng.profilImg4,mBng.profilImg5,mBng.profilImg6,mBng.profilImg7)

           for (i in 0..6) { c[mLstDrawable[i]] = mLstImageV[i] }

        c.forEach { testtwo(it) }
    }

    private fun testtwo(it: Map.Entry<Int, ImageView>) {
        var key = it.key
        Glide.with(requireContext())
            .load(it.key)
            .apply(RequestOptions.circleCropTransform())
            .into(it.value)

        it.value.setOnClickListener {view ->
            visibleButton()
            println("-------|photo|-----" + it.key + "//" + it.value + "//" + view  )
            Glide.with(requireContext())
                .load(key)
                    // -------- 1
                .apply(RequestOptions.circleCropTransform())
                .into( mBng.profilImage)
        }
    }

        // IL FAUT CHANGER LES VALEURS NAME ET PHOTOS PAR DEFAUT
        private fun saveNewUserInfo() {
            mBng.profilSave.setOnClickListener {
               mUserVM.updateUserName(mUserId, "1")
                mUserVM.updateUserPhoto(mUserId, "https://media3.artoyz.net/shop/38711-thickbox_default/udf-gizmo-les-gremlins.jpg")

                checkFireStoreUser(mBng)
            }
        }


}