package com.so.dingbring.view.settings

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.auth.AuthUI
import com.gauravk.bubblenavigation.BubbleNavigationLinearView
import com.google.firebase.auth.FirebaseAuth
import com.so.dingbring.R
import com.so.dingbring.data.MyUserViewModel
import com.so.dingbring.view.base.BaseFragment
import com.so.dingbring.view.login.LoginActivity
import kotlinx.android.synthetic.main.dialog_layout_language.*
import kotlinx.android.synthetic.main.dialog_layout_profil.*
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class SettingsFragment : BaseFragment() {
    lateinit var locale: Locale
    private var mCurrentLanguage :String? = ""
    private var mCurrentLang = ""
    private var mCheckedID = 1
    lateinit var d_contact:Dialog
    lateinit var d_profil:Dialog
    lateinit var d_lang:Dialog
    var mNameUser = "..."
    var mEmailUser = "..."
    var mPhotoUser = "..."
    var mNbUser = 0
    var mIdUser  = FirebaseAuth.getInstance().currentUser?.uid.toString()
    var fragmentStatus = ""
    var varbutton: BubbleNavigationLinearView? = null
    private val mUserVM by viewModel<MyUserViewModel>()
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "-"
    lateinit var sharedPref:SharedPreferences
    lateinit var mDrawable: Drawable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_settings, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        varbutton = activity?.findViewById(R.id.float_bottom_bar)
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                 //   view?.findNavController()?.navigate(R.id.action_settings_to_home)
                    varbutton?.setCurrentActiveItem(1) } })
        goHeader()


        onBackPressed()
    }

    private fun onBackPressed() {
        varbutton = activity?.findViewById(R.id.float_bottom_bar)
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Navigation.findNavController(requireActivity(), R.id.hostFragment).navigate(R.id.event_fragment)
                    varbutton?.setCurrentActiveItem(1) } })
    }




    private fun goHeader() {
        mUserVM.getUserById(mIdUser)
            ?.observe(requireActivity(), androidx.lifecycle.Observer { mlmu ->
                mIdUser = mlmu.mUserId
                mNameUser = mlmu.mNameUser
                mEmailUser = mlmu.mEmailUser
                mPhotoUser = mlmu.mPhotoUser
                mNbUser = mlmu.mNbEvent!!.toInt()
                card_t_settings_name.text = mNameUser
                Glide.with(this).load(mPhotoUser).apply(RequestOptions.circleCropTransform()).into(card_t_settings_photo)
                initMedal(mNbUser)
                card_t_setting_personn?.setOnClickListener { loadProfil() }
                card_t_setting_logOut?.setOnClickListener { loadLogOut() }
                card_t_setting_lang?.setOnClickListener { loadLanguage() }
                card_t_setting_contact?.setOnClickListener { loadContact() } }) }


    private fun initMedal(mNbUser: Int) {
        if(mNbUser in 0..4)   { mDrawable = resources.getDrawable(R.drawable.medalone)
            card_t_settings_status.text = "Newbie"
            Glide.with(this).load(mDrawable).apply(RequestOptions.circleCropTransform()).into(settings_medal) }
        if(mNbUser in 5..9)   { mDrawable = resources.getDrawable(R.drawable.medaltwo)
            card_t_settings_status.text = "Beginner"
            Glide.with(this).load(mDrawable).apply(RequestOptions.circleCropTransform()).into(settings_medal) }
        if(mNbUser in 10..14) { mDrawable = resources.getDrawable(R.drawable.medalthree)
            card_t_settings_status.text = "Intermediate"
            Glide.with(this).load(mDrawable).apply(RequestOptions.circleCropTransform()).into(settings_medal) }
        if(mNbUser in 15..19)   { mDrawable = resources.getDrawable(R.drawable.medalfour)
            card_t_settings_status.text = "Experienced"
            Glide.with(this).load(mDrawable).apply(RequestOptions.circleCropTransform()).into(settings_medal) }
        if(mNbUser in 20..24)   { mDrawable = resources.getDrawable(R.drawable.medalfive)
            card_t_settings_status.text = "Advanced"
            Glide.with(this).load(mDrawable).apply(RequestOptions.circleCropTransform()).into(settings_medal) }
        if(mNbUser > 24)   { mDrawable = resources.getDrawable(R.drawable.medalsix)
            card_t_settings_status.text = "Expert"
            Glide.with(this).load(mDrawable).apply(RequestOptions.circleCropTransform()).into(settings_medal)}
    }



    private fun loadProfil() {
        d_profil= Dialog(requireContext())
        d_profil.requestWindowFeature(Window.FEATURE_NO_TITLE)
        d_profil.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        d_profil.setContentView(R.layout.dialog_layout_profil)
        d_profil.setting_profil_edit.hint = mNameUser
        d_profil.show()
        initProfil() }


    private fun loadLanguage() {
        d_lang= Dialog(requireContext())
        d_lang.requestWindowFeature(Window.FEATURE_NO_TITLE)
        d_lang.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        d_lang.setContentView(R.layout.dialog_layout_language)
        d_lang.show()
        initLang()
        d_lang.settings_cancel_lang.setOnClickListener { d_lang.dismiss() } }

    private fun loadContact() {
        d_contact = Dialog(requireContext())
        d_contact.requestWindowFeature(Window.FEATURE_NO_TITLE)
        d_contact.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        d_contact.setContentView(R.layout.dialog_layout_contact)
        d_contact.show()
        initContact() }


    private fun loadLogOut() {
        AuthUI.getInstance().signOut(requireContext()).addOnCompleteListener {
            Toast.makeText(requireContext(), "Bye bye", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent) } }


    private fun initProfil() {
        d_profil.setting_profil_edit.hint = "  "
        Glide.with(requireContext()).load(mPhotoUser).apply(RequestOptions.circleCropTransform()).into(d_profil.dialog_setting_img_profil)
        editprofil() }


    private fun initLang() {
        sharedPref = requireActivity().getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        mCurrentLanguage =  sharedPref.getString("localeName","en")
        if (mCurrentLanguage=="en") {changecolor(d_lang.setting_en) ; d_lang.dialog_lang_current_lang.text = "English" }
        if (mCurrentLanguage=="fr") {changecolor(d_lang.setting_fr) ; d_lang.dialog_lang_current_lang.text = "Français" }
       // if (mCurrentLanguage=="it") {changecolor(d_lang.setting_it) ; d_lang.dialog_lang_current_lang.text = "Italiano" }
        if (mCurrentLanguage=="sp") {changecolor(d_lang.setting_sp) ; d_lang.dialog_lang_current_lang.text = "Español" }
       // if (mCurrentLanguage=="de") {changecolor(d_lang.setting_de) ; d_lang.dialog_lang_current_lang.text = "Duitse" }
       // if (mCurrentLanguage=="zh") {changecolor(d_lang.setting_ch) ; d_lang.dialog_lang_current_lang.text = "中文" }
        if (mCurrentLanguage=="pt") {changecolor(d_lang.setting_pt) ; d_lang.dialog_lang_current_lang.text = "Português" }
        // if (mCurrentLanguage=="ar") {changecolor(d_lang.setting_ar) ; d_lang.dialog_lang_current_lang.text = "عربى" }
        goLanguage() }

    private fun changecolor(mTxtLang: TextView?) {
        initcolor()
        mTxtLang?.setBackgroundColor(Color.BLACK)
        mTxtLang?.setTextColor(Color.WHITE)
        d_lang.dialog_lang_current_lang.text = mCurrentLang }

    private fun initcolor() {
        d_lang.setting_en.setBackgroundColor(Color.GRAY) ; d_lang.setting_en.setTextColor(Color.BLACK)
        d_lang.setting_fr.setBackgroundColor(Color.GRAY) ; d_lang.setting_fr.setTextColor(Color.BLACK)
      //  d_lang.setting_it.setBackgroundColor(Color.GRAY) ; d_lang.setting_it.setTextColor(Color.BLACK)
        d_lang.setting_sp.setBackgroundColor(Color.GRAY) ; d_lang.setting_sp.setTextColor(Color.BLACK)
       // d_lang.setting_de.setBackgroundColor(Color.GRAY) ; d_lang.setting_de.setTextColor(Color.BLACK)
       // d_lang.setting_ch.setBackgroundColor(Color.GRAY) ; d_lang.setting_ch.setTextColor(Color.BLACK)
        d_lang.setting_pt.setBackgroundColor(Color.GRAY) ; d_lang.setting_pt.setTextColor(Color.BLACK)
       // d_lang.setting_ar.setBackgroundColor(Color.GRAY) ; d_lang.setting_ar.setTextColor(Color.BLACK)
        }

    private fun goLanguage( ) {
            d_lang.setting_en.setOnClickListener { setLocale("en") }
            d_lang.setting_fr.setOnClickListener { setLocale("fr") }
          //  d_lang.setting_it.setOnClickListener { setLocale("it") }
            d_lang.setting_sp.setOnClickListener { setLocale("es") }
        //    d_lang.setting_de.setOnClickListener { setLocale("de") }
      //      d_lang.setting_ch.setOnClickListener { setLocale("zh") }
            d_lang.setting_pt.setOnClickListener { setLocale("pt") }
    //        d_lang.setting_ar.setOnClickListener { setLocale("ar") }
    }


    private fun setLocale(localeName: String) {
        settings_check_lang.setOnClickListener {
            if (localeName != mCurrentLang) {
             locale = Locale(localeName)
             val res = resources
             val dm = res.displayMetrics
             val conf = res.configuration
             conf.locale = locale
             res.updateConfiguration(conf, dm)
        var editor = sharedPref.edit()
        editor.putString("localeName",localeName)
        editor.apply()
        initcolor() }
         else { Toast.makeText(requireContext(), "Language, , already, , selected)!", Toast.LENGTH_SHORT).show() }
    d_lang.dismiss() } }

    private fun editprofil() {
        val c = HashMap<String, ImageView>()

        var mLstDrawable = arrayListOf(
            "https://i.ibb.co/Vtwz7tL/C6.png", "https://i.ibb.co/PN1dfmb/C5.png",
            "https://i.ibb.co/SywTtCy/C4.png", "https://i.ibb.co/XjFxB00/C3.png",
            "https://i.ibb.co/nsbNLwq/c2.png", "https://i.ibb.co/WDjtfWR/c1.png",
            "https://i.ibb.co/7YHdHKt/C7.png")

        var mLstImageV = arrayListOf(d_profil.setting_img_profil_1, d_profil.setting_img_profil_2, d_profil.setting_img_profil_3,
            d_profil.setting_img_profil_4, d_profil.setting_img_profil_5, d_profil.setting_img_profil_6, d_profil.setting_img_profil_7)

        for (i in 0..6) { c[mLstDrawable[i]] = mLstImageV[i] }

        c.forEach { map -> loadUserChange(map) } }


            private fun loadUserChange(map: Map.Entry<String, ImageView>) {
                var key = map.key
                Glide.with(requireContext()).load(map.key).apply(RequestOptions.circleCropTransform()).into(map.value)

                map.value.setOnClickListener {
                    Glide.with(requireContext()).load(key).apply(RequestOptions.circleCropTransform()).into(d_profil.dialog_setting_img_profil)
                    mPhotoUser = key
                   }
                saveNewUserInfo() }

            private fun saveNewUserInfo() {
                d_profil.settings_check.setOnClickListener {
                    mNameUser = d_profil.setting_profil_edit.text.toString()

                    card_t_settings_name.text = mNameUser
                    Glide.with(requireActivity()).load(mPhotoUser).apply(RequestOptions.circleCropTransform()).into(card_t_settings_photo)
                    mUserVM.updateUserName(mIdUser, mNameUser)
                    mUserVM.updateUserPhoto(mIdUser, mPhotoUser)
                    d_profil.dismiss() } }

    private fun initContact() {
        /*
        val recipient = "sofianem75018@gmail.com"
        val subject = d.custom_dialog_subject.text.toString().trim()
        val message = d.custom_dialog_message.text.toString().trim()
        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        mIntent.putExtra(Intent.EXTRA_TEXT, message)


        try {
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
    }

         */
    }
}



/*
            private fun goContactUs() {


                card_t_setting_contact?.setOnClickListener {




                    d.settings_send_mail.setOnClickListener {

                        Toast.makeText(requireContext(), "Send eMail", Toast.LENGTH_SHORT).show()


                        }
                    }
                }


            }


    }

 */


