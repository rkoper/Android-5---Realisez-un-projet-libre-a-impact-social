package com.so.dingbring.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.so.dingbring.R
import com.so.dingbring.data.MyEventViewModel
import com.so.dingbring.data.MyItemViewModel
import com.so.dingbring.data.MyUserViewModel
import com.so.dingbring.view.detail.DetailAdapter
import com.so.dingbring.view.main.MainActivity
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class HomeFragment : Fragment() {
    var mEventId = ""

    private lateinit var mDetailAdapter: DetailAdapter

    private val mItemVM by viewModel<MyItemViewModel>()
    private val mEventVM by viewModel<MyEventViewModel>()
    private val mUserVM by viewModel<MyUserViewModel>()


    var mNameUser = "..."
    var mEmailUser = "..."
    var mPhotoUser = "..."
    var mIdUser = "////"
    var varbutton : ImageView ? = null
    var varBottomBar : ConstraintLayout ? = null
    var mUserEvent = arrayListOf("", "")
    var isOpen = false
    var toDetail = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        mIdUser  = MainActivity.mIdUser
        mNameUser  = MainActivity.mNameUser
        mEmailUser  = MainActivity.mEmailUser
        mPhotoUser  = MainActivity.mPhotoUser

        mUserVM.getifNewUser(mIdUser)?.observe(requireActivity(), androidx.lifecycle.Observer { condition ->
            println("--------mIdUser------" + mIdUser)
            println("--------condition------" + condition)
            if (condition){
                mIdUser  = MainActivity.mIdUser
                mNameUser  = MainActivity.mNameUser
                mEmailUser  = MainActivity.mEmailUser
                mPhotoUser  = MainActivity.mPhotoUser
                createFireStoreUser()
            initHeader()
            }

            else{ mUserVM.getUserById(mIdUser)?.observe(requireActivity(),androidx.lifecycle.Observer {mlmu ->
                mIdUser  = mlmu.mUserId
                mNameUser  = mlmu.mNameUser
                mEmailUser  = mlmu.mEmailUser
                mPhotoUser  = mlmu.mPhotoUser
                mUserEvent = mlmu.mEventUser
                initHeader()})} })
        varbutton = activity?.findViewById(R.id.main_button)
        varBottomBar = activity?.findViewById(R.id.floating_top_bar_navigation)
        return  view }

    private fun initHeader() {
        card_profil_name.text = mNameUser
        Glide.with(requireActivity()).load(mPhotoUser).apply(RequestOptions.circleCropTransform()).into(card_profil_photo)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)











        varBottomBar?.visibility = INVISIBLE
        varbutton?.visibility = INVISIBLE

        card_event_click.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_event_fragment)
            varBottomBar?.visibility = INVISIBLE
            varbutton?.visibility = VISIBLE }

        card_create_click.setOnClickListener {
            varBottomBar?.visibility = INVISIBLE
            varbutton?.visibility = VISIBLE
            findNavController().navigate(R.id.action_homeFragment_to_create_fragment)
            varBottomBar?.visibility = INVISIBLE
            varbutton?.visibility = VISIBLE }

        card_calendar_click.setOnClickListener {
            varBottomBar?.visibility = INVISIBLE
            varbutton?.visibility = VISIBLE
            findNavController().navigate(R.id.action_homeFragment_to_calendar_fragment)
            varBottomBar?.visibility = INVISIBLE
            varbutton?.visibility = VISIBLE }

        card_profil_click.setOnClickListener {
            varBottomBar?.visibility = INVISIBLE
            varbutton?.visibility = VISIBLE
            findNavController().navigate(R.id.action_homeFragment_to_profil_fragment) }

        card_settings_click.setOnClickListener {
            varBottomBar?.visibility = INVISIBLE
            varbutton?.visibility = VISIBLE
            findNavController().navigate(R.id.action_homeFragment_to_settings_fragment)  }




        ic_twitter.setOnClickListener {
            println("----------|ic_twitter.|---------")  }

        ic_facebook.setOnClickListener {
            println("----------|ic_facebook|---------")  }

        ic_linkedin.setOnClickListener {
            println("----------|ic_linkedin|---------")  }

        ic_mail.setOnClickListener {
            println("----------|ic_mail.set|---------")  }

        ic_sms.setOnClickListener {
            println("----------|ic_sms.setO|---------")  }

        ic_youtube.setOnClickListener {
            println("----------|ic_youtube.|---------")  }


    }


     fun createFireStoreUser() {
        val emptylist = arrayListOf<String>()
        val mDataUser: MutableMap<String, Any> = HashMap()
        mDataUser["NameUser"] = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        mDataUser["EmailUser"] = FirebaseAuth.getInstance().currentUser?.email.toString()
        mDataUser["PhotoUser"] = FirebaseAuth.getInstance().currentUser?.photoUrl.toString()
        mDataUser["DocIdUser"] = FirebaseAuth.getInstance().currentUser?.uid.toString()
        mDataUser["eventUser"] = emptylist
        mUserVM.createUser(mDataUser)

    }


   // Newbie

   // Rookie
  //  Skilled
  //  Intermediate
 //   Proficient
  //  Advanced
  //  Expert

}




