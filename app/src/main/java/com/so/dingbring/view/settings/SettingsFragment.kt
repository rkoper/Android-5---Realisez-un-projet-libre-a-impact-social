package com.so.dingbring.view.settings

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.auth.AuthUI
import com.gauravk.bubblenavigation.BubbleNavigationLinearView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.so.dingbring.R
import com.so.dingbring.data.MyUserViewModel
import com.so.dingbring.view.base.BaseFragment
import com.so.dingbring.view.login.LoginActivity
import com.so.dingbring.view.main.MainActivity
import kotlinx.android.synthetic.main.dialog_layout_contact.*
import kotlinx.android.synthetic.main.dialog_layout_language.*
import kotlinx.android.synthetic.main.dialog_layout_profil.*
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class SettingsFragment : BaseFragment() {
    private lateinit var locale: Locale
    private var mCurrentLanguage :String? = ""
    private var mCurrentLang = ""
    private lateinit var d_contact:Dialog
    private lateinit var d_profil:Dialog
    private lateinit var d_lang:Dialog
    var mNameUser = "..."
    private var mEmailUser = "..."
    private var mPhotoUser = "..."
    private var mNbUser = 0
    private var mIdUser  = FirebaseAuth.getInstance().currentUser?.uid.toString()
    var mPosBottomBar: BubbleNavigationLinearView? = null
    private val mUserVM by viewModel<MyUserViewModel>()
    private var PRIVATEMODE = 0
    private val PREFNAME = "-"
    private lateinit var sharedPref:SharedPreferences
    private lateinit var mDrawable: Drawable
    private var mFloat_back : FloatingActionButton? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPosBottomBar = activity?.findViewById(R.id.float_bottom_bar)
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                 //   view?.findNavController()?.navigate(R.id.action_settings_to_home)
                    mPosBottomBar?.setCurrentActiveItem(1) } })
        goHeader()


        onBackPressed()
        onBackBarPressed()
    }

    private fun onBackBarPressed() {
        mFloat_back = activity?.findViewById(R.id.item_tb_fb_back)
        mFloat_back?.setOnClickListener {
            navToHome()
        }
    }

    private fun onBackPressed() {
        mPosBottomBar = activity?.findViewById(R.id.float_bottom_bar)
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navToHome() } })
    }

    private fun navToHome() {
        Navigation.findNavController(requireActivity(), R.id.hostFragment).navigate(R.id.event_fragment)
        mPosBottomBar?.setCurrentActiveItem(1)
    }



    private fun goHeader() {
        mUserVM.getUserById(mIdUser)
            .observe(requireActivity(), androidx.lifecycle.Observer { mlmu ->
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
                card_t_setting_contact?.setOnClickListener { loadContact() } })
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initMedal(mNbUser: Int) {
        if(mNbUser in 0..4)   { mDrawable = resources.getDrawable(R.drawable.medalone)
            card_t_settings_status.text = getString(R.string.newbie)
            Glide.with(this).load(mDrawable).apply(RequestOptions.circleCropTransform()).into(settings_medal) }
        if(mNbUser in 5..9)   { mDrawable = resources.getDrawable(R.drawable.medaltwo)
            card_t_settings_status.text = getString(R.string.beginner)
            Glide.with(this).load(mDrawable).apply(RequestOptions.circleCropTransform()).into(settings_medal) }
        if(mNbUser in 10..14) { mDrawable = resources.getDrawable(R.drawable.medalthree)
            card_t_settings_status.text = getString(R.string.intermediate)
            Glide.with(this).load(mDrawable).apply(RequestOptions.circleCropTransform()).into(settings_medal) }
        if(mNbUser in 15..19)   { mDrawable = resources.getDrawable(R.drawable.medalfour)
            card_t_settings_status.text = getString(R.string.experienced)
            Glide.with(this).load(mDrawable).apply(RequestOptions.circleCropTransform()).into(settings_medal) }
        if(mNbUser in 20..24)   { mDrawable = resources.getDrawable(R.drawable.medalfive)
            card_t_settings_status.text = getString(R.string.advanced)
            Glide.with(this).load(mDrawable).apply(RequestOptions.circleCropTransform()).into(settings_medal) }
        if(mNbUser > 24)   { mDrawable = resources.getDrawable(R.drawable.medalsix)
            card_t_settings_status.text = getString(R.string.expert)
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
        d_profil.setting_profil_edit.hint = ""
        Glide.with(requireContext()).load(mPhotoUser).apply(RequestOptions.circleCropTransform()).into(d_profil.dialog_setting_img_profil)
        editprofil() }


    private fun initLang() {
        sharedPref = requireActivity().getSharedPreferences(PREFNAME, PRIVATEMODE)
        mCurrentLanguage =  sharedPref.getString("localeName","en")
        println("mCurrentLanguage------------------>" + mCurrentLanguage)
        if (mCurrentLanguage=="en") {
            changecolor(d_lang.setting_en)

            d_lang.dialog_lang_current_lang.text = getString(R.string.eng) }
        if (mCurrentLanguage=="fr") {
            changecolor(d_lang.setting_fr)
            d_lang.dialog_lang_current_lang.text = getString(R.string.fra) }
        if (mCurrentLanguage=="es") {
            changecolor(d_lang.setting_sp)
            d_lang.dialog_lang_current_lang.text = getString(R.string.spa) }
        if (mCurrentLanguage=="pt") {
            changecolor(d_lang.setting_pt)
            d_lang.dialog_lang_current_lang.text = getString(R.string.pot) }
        goLanguage() }



    private fun initcolor() {
        d_lang.setting_en.setBackgroundColor(Color.GRAY)
        d_lang.setting_en.setTextColor(Color.WHITE)
        d_lang.setting_fr.setBackgroundColor(Color.GRAY)
        d_lang.setting_fr.setTextColor(Color.WHITE)
        d_lang.setting_sp.setBackgroundColor(Color.GRAY)
        d_lang.setting_sp.setTextColor(Color.WHITE)
        d_lang.setting_pt.setBackgroundColor(Color.GRAY)
        d_lang.setting_pt.setTextColor(Color.WHITE)
        }

    private fun changecolor(mTxtLang: TextView?) {

        println("mTxtLang------------------>" + mTxtLang.toString())
        initcolor()
        mTxtLang?.setBackgroundColor(resources.getColor(R.color.orange_300))
        mTxtLang?.setTextColor(Color.BLACK)
        d_lang.dialog_lang_current_lang.text = mCurrentLang }


    private fun goLanguage( ) {
            d_lang.setting_en.setOnClickListener {
                upadateColor(d_lang.setting_en, R.string.eng)
                setLocale("en") }
            d_lang.setting_fr.setOnClickListener {
                upadateColor(d_lang.setting_fr, R.string.fra)
                setLocale("fr") }
            d_lang.setting_sp.setOnClickListener {
                upadateColor(d_lang.setting_sp, R.string.spa)
                setLocale("es") }
            d_lang.setting_pt.setOnClickListener {
                upadateColor(d_lang.setting_pt, R.string.pot)
                setLocale("pt") }
    }

    private fun upadateColor (mTxtLang: TextView?, lang: Int) {
        initcolor()
        mTxtLang?.setBackgroundColor(resources.getColor(R.color.orange_300))
        mTxtLang?.setTextColor(Color.BLACK)
        d_lang.dialog_lang_current_lang.text = getString(lang) }


    private fun setLocale(localeName: String) {

        d_lang.settings_check_lang.setOnClickListener {
            if (localeName != mCurrentLang) {
             locale = Locale(localeName)
             val res = resources
             val dm = res.displayMetrics
             val conf = res.configuration
             conf.locale = locale
             res.updateConfiguration(conf, dm)
        val editor = sharedPref.edit()
        editor.putString("localeName",localeName)
        editor.apply()
        initcolor()

                val intent = Intent (activity, MainActivity::class.java)
                activity?.startActivity(intent)


            }
         else { Toast.makeText(requireContext(), "Language, , already, , selected)!", Toast.LENGTH_SHORT).show() }
    d_lang.dismiss() } }

    private fun editprofil() {
        val c = HashMap<String, ImageView>()

        val mLstDrawable = arrayListOf(
            "https://i.ibb.co/Vtwz7tL/C6.png", "https://i.ibb.co/PN1dfmb/C5.png",
            "https://i.ibb.co/SywTtCy/C4.png", "https://i.ibb.co/XjFxB00/C3.png",
            "https://i.ibb.co/nsbNLwq/c2.png", "https://i.ibb.co/WDjtfWR/c1.png",
            "https://i.ibb.co/7YHdHKt/C7.png")

        val mLstImageV = arrayListOf(d_profil.setting_img_profil_1, d_profil.setting_img_profil_2, d_profil.setting_img_profil_3,
            d_profil.setting_img_profil_4, d_profil.setting_img_profil_5, d_profil.setting_img_profil_6, d_profil.setting_img_profil_7)

        for (i in 0..6) { c[mLstDrawable[i]] = mLstImageV[i] }

        c.forEach { map -> loadUserChange(map) } }


            private fun loadUserChange(map: Map.Entry<String, ImageView>) {
                val key = map.key
                Glide.with(requireContext()).load(map.key).apply(RequestOptions.circleCropTransform()).into(map.value)

                map.value.setOnClickListener {
                    Glide.with(requireContext()).load(key).apply(RequestOptions.circleCropTransform()).into(d_profil.dialog_setting_img_profil)
                    mPhotoUser = key }


                d_profil.setting_profil_edit.doOnTextChanged { text, start, before, count ->
                    if (start > 1) { mNameUser = d_profil.setting_profil_edit.text.toString() } }
                saveNewUserInfo() }

            private fun saveNewUserInfo() {
                d_profil.settings_check.setOnClickListener {



                    card_t_settings_name.text = mNameUser

                    Glide.with(requireActivity()).load(mPhotoUser).apply(RequestOptions.circleCropTransform()).into(card_t_settings_photo)
                    mUserVM.updateUserName(mIdUser, mNameUser)
                    mUserVM.updateUserPhoto(mIdUser, mPhotoUser)
                    d_profil.dismiss() }

                d_profil.settings_cancel.setOnClickListener { d_profil.dismiss() }


            }

    private fun initContact() {

        d_contact.settings_send_mail.setOnClickListener {
        val recipient = "sofianem75018@gmail.com"
        val subject = d_contact.custom_dialog_subject.text.toString().trim()
        val message = d_contact.custom_dialog_message.text.toString().trim()
        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        mIntent.putExtra(Intent.EXTRA_TEXT, message)
            try { startActivity(Intent.createChooser(mIntent, "Choose Email Client...")) }
            catch (e: Exception) { Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show() }}


        d_contact.settings_cancel_mail.setOnClickListener { d_contact.dismiss()}


    }
}


