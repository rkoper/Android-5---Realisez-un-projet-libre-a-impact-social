package com.so.dingbring.view.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.firebase.FirebaseApp
import com.so.dingbring.R
import com.so.dingbring.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(){

    lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initBottom(mBinding)

    }


    private fun initBottom(mBinding: ActivityMainBinding) {
            mBinding.floatingTopBarNavigation.setTypeface(ResourcesCompat.getFont(this, R.font.adventpro))
            mBinding.floatingTopBarNavigation.setNavigationChangeListener { view , position ->
                when (position) {
                    0 -> Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.homeFragment)
                    1 -> Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.createFragment)
                    2 -> Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.calendar_fragment)
                    3 ->



                        Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.profil_fragment)
                    4 -> Navigation.findNavController(this, R.id.hostFragment).navigate(R.id.settings_fragment)
                    else -> { print("Error")}
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