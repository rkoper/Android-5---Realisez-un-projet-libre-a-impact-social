package com.so.dingbring.view.main

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions.Builder
import androidx.navigation.Navigation
import com.google.android.libraries.places.api.Places
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.so.dingbring.R
import com.so.dingbring.data.MyUserViewModel
import com.so.dingbring.databinding.ActivityMainBinding
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(){

    lateinit var mBinding: ActivityMainBinding
    var mNameUser = " .... "
    var mEmailUser = "...."
    var mPhotoUser = "...."
    var mIdUser = "...."
    private val mUserVM by viewModel<MyUserViewModel>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        mIdUser = intent?.getStringExtra(USERID).toString()
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mUserVM.getifUserExist(mIdUser)?.observe(this, { condition ->
            println("---------|mIdUser|--------" + mIdUser)
            println("---------| New User ? |--------" + condition.toString())
            if (condition){createFireStoreUser()} })

        initBottom(mBinding)
        hideBottomBar(false)

    }



    private fun createFireStoreUser() {
        println("----------------| createFireStoreUser |------------------")
        val emptylist = arrayListOf<String>()
        val mDataUser: MutableMap<String, Any> = HashMap()
        mDataUser["NameUser"] = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        mDataUser["EmailUser"] = FirebaseAuth.getInstance().currentUser?.email.toString()
        mDataUser["PhotoUser"] = FirebaseAuth.getInstance().currentUser?.photoUrl.toString()
        mDataUser["DocIdUser"] = FirebaseAuth.getInstance().currentUser?.uid.toString()
        mDataUser["eventUser"] = emptylist
        mUserVM.createUser(mDataUser)

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
                val bundle = Bundle()
                bundle.putString(USERID, mIdUser)



                when (position) {
                    0 -> Navigation.findNavController(this, R.id.hostFragment)
                        .navigate(R.id.homeFragment, bundle, navBuilder.build())
                    1 ->  Navigation.findNavController(this, R.id.hostFragment)
                        .navigate(R.id.createFragment, bundle, navBuilder.build())
                    2 -> Navigation.findNavController(this, R.id.hostFragment)
                        .navigate(R.id.calendar_fragment, bundle, navBuilder.build())
                    3 -> Navigation.findNavController(this, R.id.hostFragment)
                        .navigate(R.id.profil_fragment, bundle, navBuilder.build())
                    4 -> Navigation.findNavController(this, R.id.hostFragment)
                        .navigate(R.id.settings_fragment, bundle, navBuilder.build())
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

    companion object {
        val USERID = "GlobalId"

        val mNameUser = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        val mEmailUser  = FirebaseAuth.getInstance().currentUser?.email.toString()
        val mPhotoUser   = FirebaseAuth.getInstance().currentUser?.photoUrl.toString()
        val mIdUser  = FirebaseAuth.getInstance().currentUser?.uid.toString()
    }
}