package com.so.dingbring.view.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.Navigation
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.so.dingbring.R
import com.so.dingbring.data.MyUserViewModel
import kotlinx.android.synthetic.main.activity_item.*
import org.koin.android.viewmodel.ext.android.viewModel


class ItemActivity : AppCompatActivity() {
    private var mUserEvent = arrayListOf("", "")
    var mNameUser = " ..mNameUser.. "
    private var mEmailUser = "..mEmailUser.."
    private var mPhotoUser = "..mPhotoUser.."
    private var mIdUser  = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private val mUserVM by viewModel<MyUserViewModel>()
    private var mclick = 0

    @SuppressLint("WrongConstant", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_item)
        mclick = intent?.getIntExtra("msg", 99)!!
        retrieveData()
        initClickOnItem(mclick)
        initBottomBar() }

    private fun initBottomBar() {
        float_bottom_bar.visibility = View.VISIBLE
        float_bottom_bar.setTypeface(ResourcesCompat.getFont(this, R.font.roboto))
        float_bottom_bar.setNavigationChangeListener { view, position ->

            if (position == 1) { initClickOnItem(11)}
            else { initClickOnItem(position)}  }}

    private fun initClickOnItem(mclick: Int) {

        if (mclick == 0 ) { val intent = Intent(this, MainActivity::class.java) ; startActivity(intent) }
        if (mclick == 1 ) { initBar(0,1, getString(R.string.event), R.color.red_300,View.INVISIBLE) }
        if (mclick == 11 ) { initBar(R.id.event_fragment,1, getString(R.string.event), R.color.red_300,View.INVISIBLE) }
        if (mclick == 2 ) {
            initBar(R.id.create_Fragment, mclick, "Create", R.color.blue_600,View.VISIBLE)
            item_tb_fb_action.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.blue_300))
            item_tb_fb_action.setImageResource(R.drawable.logo_save)
            item_tb_fb_action.setColorFilter(Color.argb(255, 255, 255, 255)) }
        if (mclick == 3 ) {initBar(R.id.calendar_fragment, mclick, getString(R.string.calendar), R.color.green_300,View.INVISIBLE)}
        if (mclick == 4) { initBar(R.id.settings_fragment, mclick,getString(R.string.settings) , R.color.yellow_900,View.INVISIBLE)}
    }

    private fun initBar(
        mFrag: Int,
        mClick: Int,
        mFragName: String,
        mColor: Int,
        mVisible: Int
    ) {
        if (mFrag != 0){ Navigation.findNavController(this, R.id.hostFragment).navigate(mFrag)}
        float_bottom_bar.setCurrentActiveItem(mClick)
        item_tool_bar.text = mFragName
        item_tool_bar.setTextColor(resources.getColor(mColor))
        item_tb_fb_back.backgroundTintList = ColorStateList.valueOf(resources.getColor(mColor))
        item_tb_fb_action.visibility = mVisible
    }


    private fun retrieveData() {
        mUserVM.getUserById(mIdUser).observe(this,androidx.lifecycle.Observer { mlmu ->
            mIdUser  = mlmu.mUserId
            mNameUser  = mlmu.mNameUser
            mEmailUser  = mlmu.mEmailUser
            mPhotoUser  = mlmu.mPhotoUser
            mUserEvent = mlmu.mEventUser })
    }

}



