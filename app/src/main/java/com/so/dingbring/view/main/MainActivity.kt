package com.so.dingbring.view.main


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.facebook.AccessToken
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.so.dingbring.R
import com.so.dingbring.R.drawable.*
import com.so.dingbring.data.MyUserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    lateinit var mDrawable: Drawable
    var mNameUser = " .... "
    var mEmailUser = "...."
    var mPhotoUser = "...."
    var mIdUser  = FirebaseAuth.getInstance().currentUser?.uid.toString()
    var mNbUser = 0
    var mTest = "...."
    private val mUserVM by viewModel<MyUserViewModel>()
    var mUserEvent = arrayListOf("", "")
    var isOpen = false
    var toDetail = true

    @SuppressLint("WrongConstant", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_main)




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
                        var mImageUrl = mPhotoUser + "?access_token=" + token
                        mPhotoUser = mImageUrl } }

                    mNameUser = FirebaseAuth.getInstance().currentUser?.displayName.toString()
                    mEmailUser = FirebaseAuth.getInstance().currentUser?.email.toString()
                    mIdUser = FirebaseAuth.getInstance().currentUser?.uid.toString()

                    initHeader()
                    createFireStoreUser()

                } else { mUserVM.getUserById(mIdUser)?.observe(this, androidx.lifecycle.Observer { mlmu ->
                            mIdUser = mlmu.mUserId
                            mNameUser = mlmu.mNameUser
                            mEmailUser = mlmu.mEmailUser
                            mPhotoUser = mlmu.mPhotoUser
                            mUserEvent = mlmu.mEventUser
                            mNbUser = mlmu.mNbEvent!!.toInt()
                            initHeader() }) } }) }


        card_t_event.setOnClickListener {
            val intent = Intent(this, ItemActivity::class.java)
            intent.putExtra("GlobalIdUser", mIdUser)
            intent.putExtra("msg", 1)
            startActivity(intent) }

        card_t_create.setOnClickListener {
            val intent = Intent(this, ItemActivity::class.java)
             intent.putExtra("GlobalIdUser", mIdUser)
            intent.putExtra("msg", 2)
            startActivity(intent) }

        card_t_calendar.setOnClickListener {
            val intent = Intent(this, ItemActivity::class.java)
             intent.putExtra("GlobalIdUser", mIdUser)
            intent.putExtra("msg", 3)
            startActivity(intent) }

        card_t_setting.setOnClickListener {
            val intent = Intent(this, ItemActivity::class.java)
             intent.putExtra("GlobalIdUser", mIdUser)
            intent.putExtra("msg", 4)
            startActivity(intent) }
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
            mDataUser["eventNbEvent"] = 0
            mUserVM.createUser(mDataUser)

    }

    private fun goAnimText(mLink: TextView?) {
        val zoom1 = AnimationUtils.loadAnimation(this, R.anim.zoomin_v2)
        val zoom2 = AnimationUtils.loadAnimation(this, R.anim.zoomout_2)
        mLink?.startAnimation(zoom1)
        mLink?.startAnimation(zoom2) }

    private fun goAnimImage(mLink: ImageView?) {
        val zoom1 = AnimationUtils.loadAnimation(this, R.anim.zoomin_v2)
        val zoom2 = AnimationUtils.loadAnimation(this, R.anim.zoomout_2)
        mLink?.startAnimation(zoom1)
        mLink?.startAnimation(zoom2) }

    }
