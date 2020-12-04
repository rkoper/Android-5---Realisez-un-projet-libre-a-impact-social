package com.so.dingbring.view.login

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.so.dingbring.R
import com.so.dingbring.view.main.MainActivity


class LoginActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 19840521
    var mNameUser = " ... "
    private var mIdUser = "..."


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        showSignInOptions()


        val mUser = FirebaseAuth.getInstance().currentUser
        mUser?.let{
            launchMainActivity()
        } ?: showSignInOptions()



    }

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
            .build(), RC_SIGN_IN)  }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == RC_SIGN_IN) {
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
            mIntent.putExtra("GlobalIdUser", mIdUser)
            startActivity(mIntent)

        }

    }


    companion object {

        var mIdUser= FirebaseAuth.getInstance().currentUser?.uid.toString()

    }


}

