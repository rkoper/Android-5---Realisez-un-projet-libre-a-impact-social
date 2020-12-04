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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.facebook.AccessToken
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
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private lateinit var mDrawable: Drawable
    var mNameUser = " .... "
    private var mEmailUser = "...."
    private var mPhotoUser = "...."
    private var mIdUser  = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private var mNbUser = 0
    private val mUserVM by viewModel<MyUserViewModel>()
    private var mUserEvent = arrayListOf("", "")

    @SuppressLint("WrongConstant", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_main)
        initDynamicLink()
        initUser()
        initItemClick()

    }

    private fun initItemClick() {
        val intent = Intent(this, ItemActivity::class.java)
        intent.putExtra("GlobalIdUser", mIdUser)
        card_t_event.setOnClickListener { intent.putExtra("msg", 1)
            startActivity(intent) }

        card_t_create.setOnClickListener { intent.putExtra("msg", 2)
            startActivity(intent) }

        card_t_calendar.setOnClickListener { intent.putExtra("msg", 3)
            startActivity(intent) }

        card_t_setting.setOnClickListener { intent.putExtra("msg", 4)
            startActivity(intent) }
    }

    private fun initUser() {
        if (mIdUser != "null") {
            mUserVM.getifNewUser(mIdUser)?.observe(this, androidx.lifecycle.Observer { condition ->
                if (condition) {

                    mPhotoUser = FirebaseAuth.getInstance().currentUser?.photoUrl.toString()

                    if (mPhotoUser == "null") { mPhotoUser = "https://i.ibb.co/7YHdHKt/C7.png"}

                    else { val mPhotoUserSplit1 = mPhotoUser.split("//")
                        val mPhotoUserSplit2 = mPhotoUserSplit1[1].split(".com/")
                        val mPhotoUserSplit3 = mPhotoUserSplit2[0]
                        if (mPhotoUserSplit3 == "graph.facebook") {
                            val token = AccessToken.getCurrentAccessToken().token
                            val mImageUrl = "$mPhotoUser?access_token=$token"
                            mPhotoUser = mImageUrl } }

                    mNameUser = FirebaseAuth.getInstance().currentUser?.displayName.toString()
                    mEmailUser = FirebaseAuth.getInstance().currentUser?.email.toString()
                    mIdUser = FirebaseAuth.getInstance().currentUser?.uid.toString()

                    initHeader()
                    createFireStoreUser()

                } else {
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

        card_t_profil_name.text = mNameUser

        goAnimText(card_t_profil_name)
        Glide.with(this).load(mPhotoUser).apply(RequestOptions.circleCropTransform()).into(card_t_profil_photo)

        goAnimImage(card_t_profil_photo)
        goAnimText(card_t_profil_status)
        initMedal()

    }

    private fun initMedal() {

        if(mNbUser in 0..4)   { mDrawable = resources.getDrawable(medalone)
            card_t_profil_status.text = "Newbie"
            Glide.with(this).load(mDrawable).apply(RequestOptions.circleCropTransform()).into(main_medal) }
        if(mNbUser in 5..9)   { mDrawable = resources.getDrawable(medaltwo)
            card_t_profil_status.text = "Beginner"
            Glide.with(this).load(mDrawable).apply(RequestOptions.circleCropTransform()).into(main_medal) }
        if(mNbUser in 10..14) { mDrawable = resources.getDrawable(medalthree)
            card_t_profil_status.text = "Intermediate"
            Glide.with(this).load(mDrawable).apply(RequestOptions.circleCropTransform()).into(main_medal) }
        if(mNbUser in 15..19)   { mDrawable = resources.getDrawable(medalfour)
            card_t_profil_status.text = "Experienced"
            Glide.with(this).load(mDrawable).apply(RequestOptions.circleCropTransform()).into(main_medal) }
        if(mNbUser in 20..24)   { mDrawable = resources.getDrawable(medalfive)
            card_t_profil_status.text = "Advanced"
            Glide.with(this).load(mDrawable).apply(RequestOptions.circleCropTransform()).into(main_medal) }
        if(mNbUser > 24)   { mDrawable = resources.getDrawable(medalsix)
            card_t_profil_status.text = "Expert"
            Glide.with(this).load(mDrawable).apply(RequestOptions.circleCropTransform()).into(main_medal)}


        goAnimImage(main_medal)

    }


    private fun createFireStoreUser() {
        val emptylist = arrayListOf<String>()
        val mDataUser: MutableMap<String, Any> = HashMap()

        mDataUser["NameUser"] = FirebaseAuth.getInstance().currentUser?.displayName.toString()
            mDataUser["EmailUser"] = FirebaseAuth.getInstance().currentUser?.email.toString()
            mDataUser["PhotoUser"] = mPhotoUser
            mDataUser["DocIdUser"] = FirebaseAuth.getInstance().currentUser?.uid.toString()
            mDataUser["eventUser"] = emptylist
            mDataUser["NbCreateEventUser"] = 0
            mUserVM.createUser(mDataUser)

    }


    private fun initDynamicLink() {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                if (pendingDynamicLinkData != null) {
                   val deepLink = pendingDynamicLinkData.link.toString()
                    val mDeepId =  deepLink.split("link/")[1]
                    mUserVM.upadateEventUser(mIdUser, mDeepId)
                    Toast.makeText(this, "New Event !", Toast.LENGTH_SHORT).show()

                }
            }

            .addOnFailureListener(this) { e ->
                Log.w(ContentValues.TAG, "getDynamicLink:onFailure", e)

            }
    }

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
