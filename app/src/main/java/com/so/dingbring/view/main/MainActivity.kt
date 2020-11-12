package com.so.dingbring.view.main


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.so.dingbring.data.MyUserViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import com.so.dingbring.R
import kotlinx.android.synthetic.main.activity_test.*

class MainActivity : AppCompatActivity(){

    var mNameUser = " .... "
    var mEmailUser = "...."
    var mPhotoUser = "...."
    var mIdUser = "...."
    var mIdEvent = "...."
    var mTest = "...."
    private val mUserVM by viewModel<MyUserViewModel>()
    var mUserEvent = arrayListOf("", "")
    var isOpen = false
    var toDetail = true

    @SuppressLint("WrongConstant", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_test)


        mIdUser = intent?.getStringExtra("GlobalIdUser").toString()


        testCloud()





        if (mIdUser != "null"){

            println("--------|Test USER|--------" + mIdUser)
            mUserVM.getifNewUser(mIdUser)?.observe(this, androidx.lifecycle.Observer{ condition ->
                if (condition){

                    mNameUser = FirebaseAuth.getInstance().currentUser?.displayName.toString()
                    mEmailUser  = FirebaseAuth.getInstance().currentUser?.email.toString()
                    mPhotoUser = FirebaseAuth.getInstance().currentUser?.photoUrl.toString()
                    mIdUser= FirebaseAuth.getInstance().currentUser?.uid.toString()
                    initHeader()
                    createFireStoreUser() }

                else{ mUserVM.getUserById(mIdUser)?.observe(this,androidx.lifecycle.Observer {mlmu ->
                    mIdUser  = mlmu.mUserId
                    mNameUser  = mlmu.mNameUser
                    mEmailUser  = mlmu.mEmailUser
                    mPhotoUser  = mlmu.mPhotoUser
                    mUserEvent = mlmu.mEventUser
                    initHeader()})}

            })}


        card_t_event.setOnClickListener {
            val intent = Intent(this, ItemActivity::class.java)
            intent.putExtra("msg", 1)
            intent.putExtra("GlobalIdUser", mIdUser)
            startActivity(intent)
        }
        card_t_create.setOnClickListener {
            val intent = Intent(this, ItemActivity::class.java)
            intent.putExtra("msg", 2)
            intent.putExtra("GlobalIdUser", mIdUser)
            startActivity(intent)
        }
        card_t_calendar.setOnClickListener {
            val intent = Intent(this, ItemActivity::class.java)
            intent.putExtra("msg", 3)
            intent.putExtra("GlobalIdUser", mIdUser)
            startActivity(intent)
        }
        card_t_profil.setOnClickListener {
            val intent = Intent(this, ItemActivity::class.java)
            intent.putExtra("msg", 4)
            intent.putExtra("GlobalIdUser", mIdUser)
            startActivity(intent)
        }
        card_t_setting.setOnClickListener {
            val intent = Intent(this, ItemActivity::class.java)
            intent.putExtra("msg", 5)
            intent.putExtra("GlobalIdUser", mIdUser)
            startActivity(intent)
        }
    }

    private fun testCloud() {
//        TODO("Not yet implemented")
    }

    private fun initHeader() {
        println("--------|Test mNameUser|--------" + mNameUser)
        card_t_profil_name.text = mNameUser
        Glide.with(this).load(mPhotoUser).apply(RequestOptions.circleCropTransform()).into(card_t_profil_photo)
    }


    private fun createFireStoreUser() {
        val emptylist = arrayListOf<String>()
        val mDataUser: MutableMap<String, Any> = HashMap()


        mDataUser["NameUser"] = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        mDataUser["EmailUser"] = FirebaseAuth.getInstance().currentUser?.email.toString()
        mDataUser["PhotoUser"] = FirebaseAuth.getInstance().currentUser?.photoUrl.toString()
        mDataUser["DocIdUser"] = FirebaseAuth.getInstance().currentUser?.uid.toString()
        mDataUser["eventUser"] = emptylist
        mDataUser["eventNbEvent"] = 0


        mUserVM.createUser(mDataUser)

    }

    }
