package com.so.dingbring.view.main


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.so.dingbring.R
import com.so.dingbring.R.drawable.*
import com.so.dingbring.data.MyUserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.card_t_calendar
import kotlinx.android.synthetic.main.activity_main.card_t_create
import kotlinx.android.synthetic.main.activity_main.card_t_event
import kotlinx.android.synthetic.main.activity_main.card_t_profil_name
import kotlinx.android.synthetic.main.activity_main.card_t_setting
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private lateinit var mDrawable: Drawable
    var mNameUser = " .... "
    private var mEmailUser = "...."
    private var mPhotoUser = "...."
    private var mIdUser  = "..////."
    private var mNbUser = 0
    private val mUserVM by viewModel<MyUserViewModel>()
    private var mUserEvent = arrayListOf("", "")



    @SuppressLint("WrongConstant", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_main)

        mIdUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
        println("mIdUser-------------Main------------------->> " +mIdUser )
        initDynamicLink()
        initUser()
        initItemClick() }

    private fun initItemClick() {
        val intent = Intent(this, ItemActivity::class.java)

        card_t_event.setOnClickListener {
            intent.putExtra("msg", 1)
            startActivity(intent) }

        card_t_create.setOnClickListener {
            intent.putExtra("msg", 2)
            startActivity(intent) }

        card_t_calendar.setOnClickListener {
            intent.putExtra("msg", 3)
            startActivity(intent) }

        card_t_setting.setOnClickListener {
            intent.putExtra("msg", 4)
            startActivity(intent) }
    }

    private fun initUser() {
        if (mIdUser != "null") {
            mUserVM.getifNewUser(mIdUser)?.observe(this, androidx.lifecycle.Observer { condition ->
                mIdUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
                println("Condiction-------------main------------------->> " +condition )
                if (condition) {

                    mPhotoUser = FirebaseAuth.getInstance().currentUser?.photoUrl.toString()

                    if (mPhotoUser == "null") { mPhotoUser = "https://i.ibb.co/7YHdHKt/C7.png"}

                    else { mPhotoUser =   mUserVM.SaveImage(mPhotoUser) }

                    mNameUser = FirebaseAuth.getInstance().currentUser?.displayName.toString()
                    mEmailUser = FirebaseAuth.getInstance().currentUser?.email.toString()
                    mIdUser = FirebaseAuth.getInstance().currentUser?.uid.toString()

                    initHeader()
                    createFireStoreUser()

                } else {
                    println("getUserById-------------main------------------->> " +mIdUser )
                    mUserVM.getUserById(mIdUser).observe(this, androidx.lifecycle.Observer { mlmu ->
                        mIdUser = mlmu.mUserId
                        mNameUser = mlmu.mNameUser
                        mEmailUser = mlmu.mEmailUser
                        mPhotoUser = mlmu.mPhotoUser
                        mUserEvent = mlmu.mEventUser
                        mNbUser = mlmu.mNbEvent!!.toInt()
                        initHeader() })
                } }) }
    }


    private fun initHeader() {
        println("mNameUser-------------main------------------->> " +mNameUser )
        card_t_profil_name.text = mNameUser
        goAnimText(card_t_profil_name)
        Glide.with(this).load(mPhotoUser).apply(RequestOptions.circleCropTransform()).into(card_t_profil_photo)
        goAnimImage(card_t_profil_photo)
        goAnimText(card_t_profil_status)
        initMedal()

    }

    private fun initMedal() {
        Glide.with(this).load( mUserVM.DisplayImg(mNbUser,this)).apply(RequestOptions.circleCropTransform()).into(main_medal)
        card_t_profil_status.text = mUserVM.DisplayName(mNbUser, this)
        goAnimImage(main_medal) }


    private fun createFireStoreUser() {
        val emptylist = arrayListOf<String>()
        mUserVM.createUser(mNameUser, mEmailUser, mPhotoUser, mIdUser, emptylist, 0)
    }



    private fun initDynamicLink() {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                if (pendingDynamicLinkData != null) {
                   val deepLink = pendingDynamicLinkData.link.toString()
                    val mDeepId =  deepLink.split("link/")[1]
                    mUserVM.upadateEventUser(mIdUser, mDeepId)
                    Toast.makeText(this, "New Event !", Toast.LENGTH_SHORT).show() } }
            .addOnFailureListener(this) { e -> Log.w(ContentValues.TAG, "getDynamicLink:onFailure", e) } }



    private fun goAnimText(mLink: TextView?) {
        val zoom1 = AnimationUtils.loadAnimation(this, R.anim.zoomin_1)
        val zoom2 = AnimationUtils.loadAnimation(this, R.anim.zoomout_1)
        mLink?.startAnimation(zoom1)
        mLink?.startAnimation(zoom2) }

    private fun goAnimImage(mLink: ImageView?) {
        val zoom1 = AnimationUtils.loadAnimation(this, R.anim.zoomin_1)
        val zoom2 = AnimationUtils.loadAnimation(this, R.anim.zoomout_1)
        mLink?.startAnimation(zoom1)
        mLink?.startAnimation(zoom2) }

    }
