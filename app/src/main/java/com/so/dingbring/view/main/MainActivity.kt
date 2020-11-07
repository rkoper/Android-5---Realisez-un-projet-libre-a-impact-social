package com.so.dingbring.view.main

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions.Builder
import androidx.navigation.Navigation
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.so.dingbring.data.MyUserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import com.so.dingbring.R
import com.so.dingbring.view.create.CreateFragment

class MainActivity : AppCompatActivity(){

    var mNameUser = " .... "
    var mEmailUser = "...."
    var mPhotoUser = "...."
    var mIdUser = "...."
    var mIdEvent = "...."
    var mTest = "...."
    private val mUserVM by viewModel<MyUserViewModel>()

    var isOpen = false
    var toDetail = true

    @SuppressLint("WrongConstant", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_main)

        mIdEvent = intent?.getStringExtra("GlobalIdEvent").toString()
        mIdUser = intent?.getStringExtra("GlobalIdUser").toString()

        if (mIdEvent != "null") { toDetail = false }

        mIdUser = intent?.getStringExtra(USERID).toString()

        initDynamicLink()

        if (mIdUser != "null"){
            mUserVM.getifNewUser(mIdUser)?.observe(this, androidx.lifecycle.Observer{ condition ->
                if (condition){createFireStoreUser() } })}

        hideMenuBottomBar(isOpen)

        initBottom(toDetail)

        main_button.setOnClickListener {
            if (!isOpen) { scalerIn()
                translaterIn()
                zoomIn()
                main_button.setBackgroundResource(R.drawable.round_black_small)
                main_button.setImageResource(R.drawable.ic_close)
                main_button.setPadding(10)
                isOpen = true }

            else { scalerOut()
                translaterOut()
                zoomOut()
                main_button.setBackgroundResource(R.drawable.round_black_small)
                main_button.setImageResource(R.drawable.ic_menu)
                main_button.setPadding(10)
                isOpen = false } }
    }


    @SuppressLint("ResourceType")
    fun initBottom(b: Boolean) {
        main_button.visibility =  View.INVISIBLE
        floating_top_bar_navigation.visibility = View.INVISIBLE
        floating_top_bar_navigation.setTypeface(ResourcesCompat.getFont(this, R.font.roboto))
        val navBuilder = Builder();
        navBuilder.setEnterAnim(R.anim.slideright)
        val bundle = Bundle()
        bundle.putString(USERID, Companion.mIdUser)
        if (b){
            floating_top_bar_navigation.setNavigationChangeListener { view, position ->
                if (position == 0 ) {
                    finish();
                    startActivity(intent);
                    main_bottom_cl.visibility =  View.INVISIBLE
                }
                if (position == 1 ) {
                    Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.event_fragment, bundle, navBuilder.build())
                    main_button.visibility =  View.VISIBLE
                }
                if (position == 2 ) {
                     main_button.visibility =  View.VISIBLE
                    Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.createFragment, bundle, navBuilder.build()) }
                if (position == 3 ) {
                     main_button.visibility =  View.VISIBLE
                    Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.calendar_fragment, bundle, navBuilder.build()) }
                if (position == 4 ) {
                     main_button.visibility =  View.VISIBLE
                    Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.profil_fragment, bundle, navBuilder.build()) }
                if (position == 5 ) {
                     main_button.visibility =  View.VISIBLE
                    Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.settings_fragment, bundle, navBuilder.build()) } }}

        else{
            bundle.putString("GlobalIdEvent", mIdEvent)
            Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.detailFragment, bundle, navBuilder.build())
            main_bottom_cl.visibility =  View.INVISIBLE

        }

    }


    private fun scalerIn() {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.3f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.3f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(main_button, scaleX, scaleY)
        animator.start()
    }

    private fun translaterIn() {
        val animator = ObjectAnimator.ofFloat(main_button, View.TRANSLATION_Y, 75f)
        animator.start()
    }

    private fun zoomIn() {
        val aniSlide = AnimationUtils.loadAnimation(applicationContext, R.anim.zoomin_main)
        floating_top_bar_navigation.startAnimation(aniSlide)
    }


    private fun scalerOut() {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(main_button, scaleX, scaleY)
        animator.start()
    }

    private fun translaterOut() {
        val animator = ObjectAnimator.ofFloat(main_button, View.TRANSLATION_Y, 0.5f)
        animator.start()
    }

    private fun zoomOut() {
        val aniSlide = AnimationUtils.loadAnimation(applicationContext, R.anim.zoomout_main)
        floating_top_bar_navigation.startAnimation(aniSlide)
    }


    private fun createFireStoreUser() {
        val emptylist = arrayListOf<String>()
        val mDataUser: MutableMap<String, Any> = HashMap()
        mDataUser["NameUser"] = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        mDataUser["EmailUser"] = FirebaseAuth.getInstance().currentUser?.email.toString()
        mDataUser["PhotoUser"] = FirebaseAuth.getInstance().currentUser?.photoUrl.toString()
        mDataUser["DocIdUser"] = FirebaseAuth.getInstance().currentUser?.uid.toString()
        mDataUser["eventUser"] = emptylist
        mUserVM.createUser(mDataUser)

    }


    fun hideMenuBottomBar(isOpen: Boolean) {
        if (isOpen) {
            val animationD = AnimationUtils.loadAnimation(this, R.anim.slideup)
            floating_top_bar_navigation.startAnimation(animationD)
            floating_top_bar_navigation.visibility = View.INVISIBLE
            this@MainActivity.isOpen = true
        }
        else {
            val animationD = AnimationUtils.loadAnimation(this, com.so.dingbring.R.anim.slidedown)
            floating_top_bar_navigation.startAnimation(animationD)
            floating_top_bar_navigation.visibility = View.VISIBLE }
    }



    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
    private fun initDynamicLink() {
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

                    var deep = deepLink.toString()
//                deep.split("/")[1]


                    //mUserVM.upadateEventUser(mIdUser, deep)

                    val navBuilder = Builder();
                    navBuilder.setEnterAnim(R.anim.slideright)
                    val bundle = Bundle()
                    bundle.putString(USERID, Companion.mIdUser)
                    Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.homeFragment, bundle, navBuilder.build()) }}

            .addOnFailureListener(this) { e -> Log.w(ContentValues.TAG, "getDynamicLink:onFailure", e) }
    }
    companion object {
        val USERID = "GlobalId"
        val mNameUser = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        val mEmailUser  = FirebaseAuth.getInstance().currentUser?.email.toString()
        val mPhotoUser   = FirebaseAuth.getInstance().currentUser?.photoUrl.toString()
        val mIdUser  = FirebaseAuth.getInstance().currentUser?.uid.toString()

    }
}
