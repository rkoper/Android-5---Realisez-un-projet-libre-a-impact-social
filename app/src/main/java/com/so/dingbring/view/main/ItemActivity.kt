package com.so.dingbring.view.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavOptions.Builder
import androidx.navigation.Navigation
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.so.dingbring.R
import com.so.dingbring.data.MyEventViewModel
import com.so.dingbring.data.MyUserViewModel
import com.so.dingbring.view.login.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class ItemActivity : AppCompatActivity() {
    var mUserEvent = arrayListOf("", "")
    var mNameUser = " ..mNameUser.. "
    var mEmailUser = "..mEmailUser.."
    var mPhotoUser = "..mPhotoUser.."
    var mIdUser = "..mIdUser.."
    var mIdEvent = "..mIdEvent.."
    var mTest = "..mTest.."
    private val mUserVM by viewModel<MyUserViewModel>()
    var isOpen = false
    var mclick = 0
    var toDetail = true
    private val mEventVM by viewModel<MyEventViewModel>()
    var mDataEvent: MutableList<MutableList<String>> = mutableListOf()

    @SuppressLint("WrongConstant", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.item_main)
        mclick = intent?.getIntExtra("msg", 99)!!
        mIdUser = LoginActivity.mIdUser
        retrieveData()
        initClickOnItem()
        initBottomBar() }

    private fun initBottomBar() {
        floating_top_bar_navigation.visibility = View.VISIBLE
        floating_top_bar_navigation.setTypeface(ResourcesCompat.getFont(this, R.font.roboto))
        floating_top_bar_navigation.setNavigationChangeListener { view, position ->
            if (position == 0) { val intent = Intent(this, MainActivity::class.java) ; startActivity(intent) }
            if (position == 1) { Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.event_fragment) }
            if (position == 2) { Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.createFragment) }
            if (position == 3) { Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.calendar_fragment) }
            if (position == 4) { Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.settings_fragment) } }}

    private fun initClickOnItem() {
        val navBuilder = Builder();
        navBuilder.setEnterAnim(R.anim.slideright)
        if (mclick == 1) { floating_top_bar_navigation.setCurrentActiveItem(1) }
        if (mclick == 2) { Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.createFragment)
            floating_top_bar_navigation.setCurrentActiveItem(2) }
        if (mclick == 3) { Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.calendar_fragment)
            floating_top_bar_navigation.setCurrentActiveItem(3) }
        if (mclick == 4) { Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.settings_fragment)
            floating_top_bar_navigation.setCurrentActiveItem(4) }}


    private fun retrieveData() {
        mUserVM.getUserById(mIdUser)?.observe(this,androidx.lifecycle.Observer {mlmu ->
            mIdUser  = mlmu.mUserId
            mNameUser  = mlmu.mNameUser
            mEmailUser  = mlmu.mEmailUser
            mPhotoUser  = mlmu.mPhotoUser
            mUserEvent = mlmu.mEventUser }) }

    private fun initDynamicLink() {
        /*

        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                println(" Main  / LINKS / -----------3---------" + pendingDynamicLinkData)
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                    "https://dingbring.page.link/00349ac2-8839-4847-95c0-9db2cf368e78"
                    println(" Main  / LINKS / ----------1----------" + deepLink)
                    println(" Main  / LINKS / -----------2---------" + pendingDynamicLinkData)

                    var deep = deepLink.toString()               deep.split("/")[1]


                    //mUserVM.upadateEventUser(mIdUser, deep)

                    val navBuilder = Builder();
                    navBuilder.setEnterAnim(R.anim.slideright)
                    val bundle = Bundle()
                    bundle.putString(USERID, Companion.mIdUser)
                    Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.homeFragment) }}

            .addOnFailureListener(this) { e -> Log.w(ContentValues.TAG, "getDynamicLink:onFailure", e)

         */
            }

    }



