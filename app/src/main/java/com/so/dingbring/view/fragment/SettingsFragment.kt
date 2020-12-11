package com.so.dingbring.view.fragment

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
import com.gauravk.bubblenavigation.BubbleNavigationLinearView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.so.dingbring.R
import com.so.dingbring.Utils
import com.so.dingbring.data.MyUserViewModel
import com.so.dingbring.view.activity.LoginActivity
import com.so.dingbring.view.activity.MainActivity
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
    private lateinit var mDContact:Dialog
    private lateinit var mDProfil:Dialog
    private lateinit var mDLang:Dialog
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
    private var mFloatBack : FloatingActionButton? = null

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
                override fun handleOnBackPressed() { mPosBottomBar?.setCurrentActiveItem(1) } })
        goHeader()
        onBackPressed()
        onBackBarPressed()
    }

    private fun onBackBarPressed() {
        mFloatBack = activity?.findViewById(R.id.item_tb_fb_back)
        mFloatBack?.setOnClickListener {
            navToHome() } }

    private fun onBackPressed() {
        mPosBottomBar = activity?.findViewById(R.id.float_bottom_bar)
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navToHome() } }) }

    private fun navToHome() {
        Navigation.findNavController(requireActivity(), R.id.hostFragment).navigate(R.id.event_fragment)
        mPosBottomBar?.setCurrentActiveItem(1) }



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



    private fun initMedal(mNbUser: Int) {

            card_t_settings_status.text = mUserVM.displayName(mNbUser, requireActivity())
            Glide.with(this).load( mUserVM.displayImg(mNbUser,requireActivity())).apply(RequestOptions.circleCropTransform()).into(settings_medal)
    }



    private fun loadProfil() {
        mDProfil= Dialog(requireContext())
        mDProfil.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDProfil.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDProfil.setContentView(R.layout.dialog_layout_profil)
        mDProfil.setting_profil_edit.hint = mNameUser
        mDProfil.show()
        initProfil() }


    private fun loadLanguage() {
        mDLang= Dialog(requireContext())
        mDLang.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDLang.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDLang.setContentView(R.layout.dialog_layout_language)
        mDLang.show()
        initLang()
        mDLang.settings_cancel_lang.setOnClickListener { mDLang.dismiss() } }

    private fun loadContact() {
        mDContact = Dialog(requireContext())
        mDContact.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDContact.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDContact.setContentView(R.layout.dialog_layout_contact)
        mDContact.show()
        initContact() }


    private fun loadLogOut() {
        Firebase.auth.signOut()
        activity?.finish()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)


    }


    private fun initProfil() {
        mDProfil.setting_profil_edit.hint = ""
        Glide.with(requireContext()).load(mPhotoUser).apply(RequestOptions.circleCropTransform()).into(mDProfil.dialog_setting_img_profil)
        editprofil() }


    private fun initLang() {
        sharedPref = requireActivity().getSharedPreferences(PREFNAME, PRIVATEMODE)
        mCurrentLanguage =  sharedPref.getString("localeName","en")
        if (mCurrentLanguage=="en") {
            changecolor(mDLang.setting_en)
            mDLang.dialog_lang_current_lang.text = getString(R.string.eng) }
        if (mCurrentLanguage=="fr") {
            changecolor(mDLang.setting_fr)
            mDLang.dialog_lang_current_lang.text = getString(R.string.fra) }
        if (mCurrentLanguage=="es") {
            changecolor(mDLang.setting_sp)
            mDLang.dialog_lang_current_lang.text = getString(R.string.spa) }
        if (mCurrentLanguage=="pt") {
            changecolor(mDLang.setting_pt)
            mDLang.dialog_lang_current_lang.text = getString(R.string.pot) }
        goLanguage() }



    private fun initcolor() {
        mDLang.setting_en.setBackgroundColor(Color.GRAY)
        mDLang.setting_en.setTextColor(Color.WHITE)
        mDLang.setting_fr.setBackgroundColor(Color.GRAY)
        mDLang.setting_fr.setTextColor(Color.WHITE)
        mDLang.setting_sp.setBackgroundColor(Color.GRAY)
        mDLang.setting_sp.setTextColor(Color.WHITE)
        mDLang.setting_pt.setBackgroundColor(Color.GRAY)
        mDLang.setting_pt.setTextColor(Color.WHITE)
        }

    private fun changecolor(mTxtLang: TextView?) {
        initcolor()
        mTxtLang?.setBackgroundColor(resources.getColor(R.color.orange_300))
        mTxtLang?.setTextColor(Color.BLACK)
        mDLang.dialog_lang_current_lang.text = mCurrentLang }


    private fun goLanguage( ) {
            mDLang.setting_en.setOnClickListener { upadateColor(mDLang.setting_en, R.string.eng)
                setLocale("en") }
            mDLang.setting_fr.setOnClickListener { upadateColor(mDLang.setting_fr, R.string.fra)
                setLocale("fr") }
            mDLang.setting_sp.setOnClickListener { upadateColor(mDLang.setting_sp, R.string.spa)
                setLocale("es") }
            mDLang.setting_pt.setOnClickListener { upadateColor(mDLang.setting_pt, R.string.pot)
                setLocale("pt") }
    }

    private fun upadateColor (mTxtLang: TextView?, lang: Int) {
        initcolor()
        mTxtLang?.setBackgroundColor(resources.getColor(R.color.orange_300))
        mTxtLang?.setTextColor(Color.BLACK)
        mDLang.dialog_lang_current_lang.text = getString(lang) }


    private fun setLocale(localeName: String) {
        mDLang.settings_check_lang.setOnClickListener {
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
                activity?.startActivity(intent) }
         else { Toast.makeText(requireContext(), "Language, , already, , selected)!", Toast.LENGTH_SHORT).show() }
    mDLang.dismiss() } }

    private fun editprofil() {
        val mMapPhoto = HashMap<String, ImageView>()
        val mLstDrawable = Utils.listDrawable()
        val mLstImageV = Utils.listImageV(mDProfil)
        for (i in 0..6) { mMapPhoto[mLstDrawable[i]] = mLstImageV[i] }
        mMapPhoto.forEach { map -> loadUserChange(map) } }


            private fun loadUserChange(map: Map.Entry<String, ImageView>) {
                val key = map.key
                Glide.with(requireContext()).load(map.key).apply(RequestOptions.circleCropTransform()).into(map.value)

                map.value.setOnClickListener {
                    Glide.with(requireContext()).load(key).apply(RequestOptions.circleCropTransform()).into(mDProfil.dialog_setting_img_profil)
                    mPhotoUser = key }


                mDProfil.setting_profil_edit.doOnTextChanged { text, start, before, count ->
                    if (start > 1) { mNameUser = mDProfil.setting_profil_edit.text.toString() } }
                saveNewUserInfo() }

            private fun saveNewUserInfo() {
                mDProfil.settings_check.setOnClickListener {
                    card_t_settings_name.text = mNameUser

                    Glide.with(requireActivity()).load(mPhotoUser).apply(RequestOptions.circleCropTransform()).into(card_t_settings_photo)
                    mUserVM.updateUserName(mIdUser, mNameUser)
                    mUserVM.updateUserPhoto(mIdUser, mPhotoUser)
                    mDProfil.dismiss() }

                mDProfil.settings_cancel.setOnClickListener { mDProfil.dismiss() } }

    private fun initContact() {
        mDContact.settings_send_mail.setOnClickListener {
        val recipient = "sofianem75018@gmail.com"
        val subject = mDContact.custom_dialog_subject.text.toString().trim()
        val message = mDContact.custom_dialog_message.text.toString().trim()
        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        mIntent.putExtra(Intent.EXTRA_TEXT, message)
            try { startActivity(Intent.createChooser(mIntent, "Choose Email Client...")) }
            catch (e: Exception) { Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show() }}

        mDContact.settings_cancel_mail.setOnClickListener { mDContact.dismiss()}


    }
}


