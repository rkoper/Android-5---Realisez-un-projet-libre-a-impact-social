package com.so.dingbring.view.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.so.dingbring.R
import com.so.dingbring.view.main.MainActivity


class LoginActivity : AppCompatActivity() {


    val RC_SIGN_IN = 19840521
    var mNameUser = " ... "
    var mEmailUser = "..."
    var mPhotoUser = "..."
    var mIdUser = "..."


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        val mUser = FirebaseAuth.getInstance().currentUser
        mUser?.let{
            launchMainActivity()
        } ?: showSignInOptions()

    }

    private fun showSignInOptions() {

        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build()
        )



        startActivityForResult(AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.MyTheme)
            .setIsSmartLockEnabled(false)
            .build(), RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                launchMainActivity()

            } else {
            }
        }
    }

    private fun launchMainActivity() {
        if (FirebaseAuth.getInstance().currentUser?.uid != null)
        { mIdUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
            println("----mIdUser--Login---" + mIdUser)
            val mIntent = Intent(this, MainActivity::class.java)
            mIntent.putExtra("GlobalIdUser", mIdUser)
            startActivity(mIntent)


        }
        else { finish()}


    }


companion object {

        var mIdUser= FirebaseAuth.getInstance().currentUser?.uid.toString()

    }


}

