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
import com.so.dingbring.R
import com.so.dingbring.data.MyUserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class ItemActivity : AppCompatActivity() {

    var mNameUser = " .... "
    var mEmailUser = "...."
    var mPhotoUser = "...."
    var mIdUser = "...."
    var mIdEvent = "...."
    var mTest = "...."
    private val mUserVM by viewModel<MyUserViewModel>()

    var isOpen = false
    var mclick = 0
    var toDetail = true

    @SuppressLint("WrongConstant", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_main)

        mIdUser = ItemActivity.mIdUser
        mNameUser =ItemActivity.mNameUser
        mEmailUser = ItemActivity.mEmailUser
        mPhotoUser = ItemActivity.mPhotoUser
        mclick = intent?.getIntExtra("msg", 99)!!

        println("--------|mIdUser / mclick |--------" + mIdUser + "//"+ mclick)

        val navBuilder = Builder();
        navBuilder.setEnterAnim(R.anim.slideright)



        if (mclick == 0) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("GlobalIdUser", mIdUser)
        startActivity(intent)}

        if (mclick == 1) {
            Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.event_fragment)
            floating_top_bar_navigation.setCurrentActiveItem(1)
        }
        if (mclick == 2) {
            Navigation.findNavController(this, R.id.hostFragment)
                .navigate(R.id.createFragment)
            floating_top_bar_navigation.setCurrentActiveItem(2)
        }
        if (mclick == 3) {
            Navigation.findNavController(this, R.id.hostFragment)
                .navigate(R.id.calendar_fragment)
            floating_top_bar_navigation.setCurrentActiveItem(3)

        }
        if (mclick == 4) {
            Navigation.findNavController(this, R.id.hostFragment)
                .navigate(R.id.profil_fragment)
            floating_top_bar_navigation.setCurrentActiveItem(4)

        }
        if (mclick == 5) {
            Navigation.findNavController(this, R.id.hostFragment)
                .navigate(R.id.settings_fragment)
            floating_top_bar_navigation.setCurrentActiveItem(5)

        }

        floating_top_bar_navigation.visibility = View.VISIBLE

        floating_top_bar_navigation.setTypeface(ResourcesCompat.getFont(this, R.font.roboto))






        floating_top_bar_navigation.setNavigationChangeListener { view, position ->
            println("--------|MAIN ACT 2 |--------" + position)
            if (position == 0) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("GlobalIdUser", mIdUser)
                startActivity(intent)
            }
            if (position == 1) {
                Navigation.findNavController(this, R.id.hostFragment)
                    .navigate(R.id.event_fragment)
            }
            if (position == 2) {
                Navigation.findNavController(this, R.id.hostFragment)
                    .navigate(R.id.createFragment)
            }
            if (position == 3) {
                Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.calendar_fragment) }
            if (position == 4) {
                Navigation.findNavController(this, R.id.hostFragment)
                    .navigate(R.id.profil_fragment)
            }
            if (position == 5) {
                Navigation.findNavController(this, R.id.hostFragment)
                    .navigate(R.id.settings_fragment) }

        }
    }

    companion object {


     var mNameUser = FirebaseAuth.getInstance().currentUser?.displayName.toString()
       var mEmailUser = FirebaseAuth.getInstance().currentUser?.email.toString()
            var mPhotoUser = FirebaseAuth.getInstance().currentUser?.photoUrl.toString()
        var mIdUser= FirebaseAuth.getInstance().currentUser?.uid.toString()

    }

}
        /*
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

         */




/*
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
                    Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.homeFragment) }}

            .addOnFailureListener(this) { e -> Log.w(ContentValues.TAG, "getDynamicLink:onFailure", e) }
    }

 */

