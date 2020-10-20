package com.so.dingbring.view.main

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavOptions
import androidx.navigation.NavOptions.Builder
import androidx.navigation.Navigation
import com.so.dingbring.R
import com.so.dingbring.data.MyUserViewModel
import com.so.dingbring.databinding.ActivityMainBinding
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(){

    lateinit var mBinding: ActivityMainBinding
    private val mUserVM by viewModel<MyUserViewModel>()
    var name = "......"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initBottom(mBinding)
        hideBottomBar(false)

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
                when (position) {
                    0 -> Navigation.findNavController(this, R.id.hostFragment)
                        .navigate(R.id.homeFragment, null, navBuilder.build())
                    1 -> Navigation.findNavController(this, R.id.hostFragment)
                        .navigate(R.id.createFragment, null, navBuilder.build())
                    2 -> Navigation.findNavController(this, R.id.hostFragment)
                        .navigate(R.id.calendar_fragment, null, navBuilder.build())
                    3 -> Navigation.findNavController(this, R.id.hostFragment)
                        .navigate(R.id.profil_fragment, null, navBuilder.build())
                    4 -> Navigation.findNavController(this, R.id.hostFragment)
                        .navigate(R.id.settings_fragment, null, navBuilder.build())
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