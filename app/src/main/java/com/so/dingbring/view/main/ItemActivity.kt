package com.so.dingbring.view.main

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavOptions.Builder
import androidx.navigation.Navigation
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.so.dingbring.R
import com.so.dingbring.data.MyEventViewModel
import com.so.dingbring.data.MyUserViewModel
import kotlinx.android.synthetic.main.item_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class ItemActivity : AppCompatActivity() {
    var mUserEvent = arrayListOf("", "")
    var mNameUser = " ..mNameUser.. "
    var mEmailUser = "..mEmailUser.."
    var mPhotoUser = "..mPhotoUser.."
    var mIdUser  = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private val mUserVM by viewModel<MyUserViewModel>()
    var mclick = 0

    @SuppressLint("WrongConstant", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.item_main)
        mclick = intent?.getIntExtra("msg", 99)!!

        retrieveData()
        initClickOnItem()
        initBottomBar() }

    private fun initBottomBar() {
        floating_top_bar_navigation.visibility = View.VISIBLE
        floating_top_bar_navigation.setTypeface(ResourcesCompat.getFont(this, R.font.roboto))
        floating_top_bar_navigation.setNavigationChangeListener { view, position ->
            if (position == 0) { val intent = Intent(this, MainActivity::class.java) ; startActivity(intent) }
            if (position == 1) { Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.event_fragment)
                item_tool_bar.title =  "Event"
                item_tool_bar.setTitleTextColor(resources.getColor(R.color.red_300))}
            if (position == 2) { Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.createFragment)
                item_tool_bar.title =  "Create"
                item_tool_bar.setTitleTextColor(resources.getColor(R.color.blue_600))}
            if (position == 3) { Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.calendar_fragment)
                item_tool_bar.title =  "Calendar"
                item_tool_bar.setTitleTextColor(resources.getColor(R.color.green_300))}
            if (position == 4) { Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.settings_fragment)
                item_tool_bar.title =  "Settings"
                item_tool_bar.setTitleTextColor(resources.getColor(R.color.yellow_900))} }}


    private fun initClickOnItem() {
        val navBuilder = Builder();
        navBuilder.setEnterAnim(R.anim.slideright)
        if (mclick == 1) { mIdUser = intent.getStringExtra("GlobalIdUser").toString()
            floating_top_bar_navigation.setCurrentActiveItem(1)
            item_tool_bar.title = "Event"
            item_tool_bar.setTitleTextColor(resources.getColor(R.color.red_300)) }
        if (mclick == 2) { mIdUser = intent.getStringExtra("GlobalIdUser").toString()
            Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.createFragment)
            floating_top_bar_navigation.setCurrentActiveItem(2)
            item_tool_bar.title = "Create"
            item_tool_bar.setTitleTextColor(resources.getColor(R.color.blue_600)) }
        if (mclick == 3) { mIdUser = intent.getStringExtra("GlobalIdUser").toString()
            Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.calendar_fragment)
            floating_top_bar_navigation.setCurrentActiveItem(3)
            item_tool_bar.title = "Calendar"
            item_tool_bar.setTitleTextColor(resources.getColor(R.color.green_300)) }
        if (mclick == 4) { mIdUser = intent.getStringExtra("GlobalIdUser").toString()
            Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.settings_fragment)
            floating_top_bar_navigation.setCurrentActiveItem(4)
            item_tool_bar.title = "Settings"
            item_tool_bar.setTitleTextColor(resources.getColor(R.color.yellow_900)) } }


    private fun retrieveData() {
        mUserVM.getUserById(mIdUser)?.observe(this,androidx.lifecycle.Observer {mlmu ->
            mIdUser  = mlmu.mUserId
            mNameUser  = mlmu.mNameUser
            mEmailUser  = mlmu.mEmailUser
            mPhotoUser  = mlmu.mPhotoUser
            mUserEvent = mlmu.mEventUser }) }


}



