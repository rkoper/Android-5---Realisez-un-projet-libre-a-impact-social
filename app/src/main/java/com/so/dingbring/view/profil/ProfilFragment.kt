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

    private lateinit var mBinding: FragmentProfilBinding
    private val mUserVM by viewModel<MyUserViewModel>()
    private val mEventVM by viewModel<MyEventViewModel>()
    private var mUserName = "XXXXXX"
    private var mUserMail = "XXXXX"
    private var mUserPP = "XXXXX"
    var mImageList = arrayListOf<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        FirebaseApp.initializeApp(requireContext())
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profil, container, false)
        checkFireStoreUser(mBinding)
       saveNewUserInfo()
        refreshInfo()
        editNameUser(mBinding)




        return mBinding.root}

    private fun saveNewUserInfo() {
        // SAVE NEW INFOS -----> MUSER.UPLOAD PHOTO NAME
    }

    private fun refreshInfo() {
        mBinding.profilRefresh.setOnClickListener {
            checkFireStoreUser(mBinding)
        }
    }

    private fun editNameUser(mBinding: FragmentProfilBinding?) {
        mBinding?.profilEditName?.setOnClickListener {
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
                mUserName = d.custom_dialog_txt.text.toString()
                mBinding!!.profilName.text = mUserName}

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        test()
        super.onViewCreated(view, savedInstanceState) }

    private fun checkFireStoreUser(mBinding: FragmentProfilBinding?) {
        mUserVM.getUser(FirebaseAuth.getInstance().currentUser?.email.toString())?.observe(requireActivity(), {mlmu ->
                if (mlmu != null){
                    mUserName = mlmu.mNameUser
                    mUserPP = mlmu.mPhotoUser
                    createDisplay(mBinding)} })}

    private fun createDisplay(mBinding: FragmentProfilBinding?) {
     mBinding!!.profilName.text = mUserName


        Glide.with(requireContext())
            .load(FirebaseAuth.getInstance().currentUser?.photoUrl)
            .apply(RequestOptions.circleCropTransform())
            .into(mBinding.profilImage)

    }


    private fun test() {
        val c = HashMap<Int,ImageView>()
            c[R.drawable.img1] = mBinding.profilImg1
            c[R.drawable.img2]= mBinding.profilImg2
            c[R.drawable.img3]= mBinding.profilImg3
            c[R.drawable.img4]= mBinding.profilImg4
            c[R.drawable.img5]= mBinding.profilImg5
            c[R.drawable.img6]= mBinding.profilImg6
            c[R.drawable.img7]= mBinding.profilImg7

        c.forEach { testtwo(it) }
    }

    private fun testtwo(it: Map.Entry<Int, ImageView>) {
             Glide.with(requireContext())
            .load(it.key)
            .apply(RequestOptions.circleCropTransform())
            .into(it.value)

        mBinding.profilImg1.setOnClickListener {
            Glide.with(requireContext())
                .load(R.drawable.img1)
                .apply(RequestOptions.circleCropTransform())
                .into( mBinding.profilImage) }

        mBinding.profilImg2.setOnClickListener {
            Glide.with(requireContext())
                .load(R.drawable.img2)
                .apply(RequestOptions.circleCropTransform())
                .into( mBinding.profilImage) }

        mBinding.profilImg3.setOnClickListener {
            Glide.with(requireContext())
                .load(R.drawable.img3)
                .apply(RequestOptions.circleCropTransform())
                .into( mBinding.profilImage) }

        mBinding.profilImg4.setOnClickListener {
            Glide.with(requireContext())
                .load(R.drawable.img4)
                .apply(RequestOptions.circleCropTransform())
                .into( mBinding.profilImage) }

        mBinding.profilImg5.setOnClickListener {
            Glide.with(requireContext())
                .load(R.drawable.img5)
                .apply(RequestOptions.circleCropTransform())
                .into( mBinding.profilImage) }

        mBinding.profilImg6.setOnClickListener {
            Glide.with(requireContext())
                .load(R.drawable.img6)
                .apply(RequestOptions.circleCropTransform())
                .into( mBinding.profilImage) }

        mBinding.profilImg7.setOnClickListener {
            Glide.with(requireContext())
                .load(R.drawable.img7)
                .apply(RequestOptions.circleCropTransform())
                .into( mBinding.profilImage) }

    }


}