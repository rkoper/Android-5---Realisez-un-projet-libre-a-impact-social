package com.so.dingbring.view.activity

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.so.dingbring.R
import java.util.*


class LoginActivity : AppCompatActivity() {

    private val RCSIGNIN = 19840521
    var mNameUser = " ... "
    private var mIdUser = "..."
    private lateinit var sharedPref: SharedPreferences
    private var mCurrentLanguage :String? = ""
    private lateinit var locale: Locale
    private var PRIVATEMODE = 0
    private val PREFNAME = "-"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initLang()
        showSignInOptions()

/*
        val mUser = FirebaseAuth.getInstance().currentUser
        mUser?.let{
            launchMainActivity()
        } ?: showSignInOptions()



 */


    }

    private fun initLang() {
        sharedPref = this.getSharedPreferences(PREFNAME, PRIVATEMODE)
        mCurrentLanguage =  sharedPref.getString("localeName","en")

        if (mCurrentLanguage=="en") {setLocale("en")}
        if (mCurrentLanguage=="fr") {setLocale("fr")}
        if (mCurrentLanguage=="es") {setLocale("es")}
        if (mCurrentLanguage=="pt") {setLocale("pt")}



    }

    private fun setLocale(localeName: String) {
        locale = Locale(localeName)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = locale
        res.updateConfiguration(conf, dm)
        val editor = sharedPref.edit()
        editor.putString("localeName",localeName)
        editor.apply() }

    private fun showSignInOptions() {

        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().build()
        )



        startActivityForResult(AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.LoginTheme)
            .setIsSmartLockEnabled(false)
            .build(), RCSIGNIN)  }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == RCSIGNIN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                launchMainActivity()
            }
        }
    }


    private fun launchMainActivity() {
        if (FirebaseAuth.getInstance().currentUser?.uid != null)
        { mIdUser = FirebaseAuth.getInstance().currentUser?.uid.toString()

            val mIntent = Intent(this, MainActivity::class.java)

            println("mIdUser-------------Login------------------->> $mIdUser")
            mIntent.putExtra("GlobalIdUser", mIdUser)
            startActivity(mIntent)

        }

    }



}

