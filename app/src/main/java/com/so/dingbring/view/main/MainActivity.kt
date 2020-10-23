package com.so.dingbring.view.main

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavOptions.Builder
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.so.dingbring.R
import com.so.dingbring.data.MyEventViewModel
import com.so.dingbring.data.MyUserViewModel
import com.so.dingbring.databinding.ActivityMainBinding
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity(){

    lateinit var mBinding: ActivityMainBinding
    var mNameUser = "Fifi"
    var mEmailUser = "fifi@gmail.com"
    var mPhotoUser = "https://i.ibb.co/r6W0hxp/Capture-d-e-cran-2020-10-16-a-21-09-59.png"
    private val mEventVM by viewModel<MyEventViewModel>()
    private val mUserVM by viewModel<MyUserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)



        if (FirebaseAuth.getInstance().currentUser?.displayName != null)
        { mNameUser = FirebaseAuth.getInstance().currentUser?.displayName.toString()}
        if (FirebaseAuth.getInstance().currentUser?.email != null)
        { mEmailUser = FirebaseAuth.getInstance().currentUser?.email.toString()}
        if (FirebaseAuth.getInstance().currentUser?.photoUrl != null)
        { mPhotoUser = FirebaseAuth.getInstance().currentUser?.photoUrl.toString()}


        println("--Main--–|mNameUser|----"+mPhotoUser + "----–|mEmailUser|----"+ mEmailUser+ "----–|mPhotoUser|----"+mPhotoUser )

        checkFireStoreUser()

        initBottom(mBinding)
        hideBottomBar(false)

    }

    private fun checkFireStoreUser() {

      mUserVM.getUserByMail(FirebaseAuth.getInstance().currentUser?.email.toString())?.observe(this,{mlmu ->
//        mUserVM.getUserByMail(mEmailUser)?.observe(this,{ mlmu ->
            if (mlmu == null){createFireStoreUser()}

    })}

    private fun createFireStoreUser() {
        val userId = UUID.randomUUID().toString()
        val emptylist = arrayListOf<String>()
        val mDataUser: MutableMap<String, Any> = HashMap()
        mDataUser["NameUser"] = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        mDataUser["EmailUser"] = FirebaseAuth.getInstance().currentUser?.email.toString()
        mDataUser["PhotoUser"] = FirebaseAuth.getInstance().currentUser?.photoUrl.toString()
        mDataUser["DocIdUser"] = userId
        mDataUser["eventUser"] = emptylist

        mUserVM.createUser(mDataUser)

    }


    fun hideBottomBar(isHidden: Boolean) {
            if (isHidden) {
               val animationD = AnimationUtils.loadAnimation(this, R.anim.slideup)
                mBinding.floatingTopBarNavigation.startAnimation(animationD)
                mBinding.floatingTopBarNavigation.visibility = View.INVISIBLE
            }
            else {
                val animationD = AnimationUtils.loadAnimation(this, R.anim.slidedown)
                mBinding.floatingTopBarNavigation.startAnimation(animationD)
                mBinding.floatingTopBarNavigation.visibility = View.VISIBLE }
    }


    private fun initBottom(mBinding: ActivityMainBinding) {
            mBinding.floatingTopBarNavigation.setTypeface(
                ResourcesCompat.getFont(
                    this,
                    R.font.roboto
                )
            )
            mBinding.floatingTopBarNavigation.setNavigationChangeListener { view, position ->


                 val navBuilder  =   Builder();
                navBuilder.setEnterAnim(R.anim.slideright)
                val bundle = Bundle()
                bundle.putString("GlobalName", mNameUser)
                bundle.putString("GlobalEmail", mEmailUser)
                bundle.putString("GlobalPhoto", mPhotoUser)



                when (position) {
                    0 -> Navigation.findNavController(this, R.id.hostFragment)
                        .navigate(R.id.homeFragment, bundle, navBuilder.build())
                    1 -> Navigation.findNavController(this, R.id.hostFragment)
                        .navigate(R.id.createFragment, bundle, navBuilder.build())
                    2 -> Navigation.findNavController(this, R.id.hostFragment)
                        .navigate(R.id.calendar_fragment, bundle, navBuilder.build())
                    3 -> Navigation.findNavController(this, R.id.hostFragment)
                        .navigate(R.id.profil_fragment, bundle, navBuilder.build())
                    4 -> Navigation.findNavController(this, R.id.hostFragment)
                        .navigate(R.id.settings_fragment, bundle, navBuilder.build())
                    else -> { mBinding.floatingTopBarNavigation.visibility = View.INVISIBLE}
                }
            }
        }


    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }



}