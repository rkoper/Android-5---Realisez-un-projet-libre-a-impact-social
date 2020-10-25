package com.so.dingbring.view.settings

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.firebase.ui.auth.AuthUI
import com.so.dingbring.R
import com.so.dingbring.databinding.FragmentDetailBinding
import com.so.dingbring.databinding.FragmentSettingsBinding
import com.so.dingbring.view.login.LoginActivity
import com.so.dingbring.view.main.MainActivity
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.*

class SettingsFragment : Fragment() {

    private lateinit var mBinding: FragmentSettingsBinding
    lateinit var locale: Locale
    private var currentLanguage = "en"
    private var currentLang: String? = null
    var mNameUser = "..."
    var mEmailUser = "..."
    var mPhotoUser = "..."
    var mIdUser = "..."

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        mIdUser  = MainActivity.mIdUser
        mNameUser  = MainActivity.mNameUser
        mEmailUser  = MainActivity.mEmailUser
        mPhotoUser  = MainActivity.mPhotoUser

        println("( Settings )" + mIdUser + "( - )" + mNameUser  + "( - )" + mEmailUser + "( - )" + mPhotoUser )

        testprintln()

        return mBinding.root}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        goLanguage()
        goContactUs()
        goLogOut()
        super.onViewCreated(view, savedInstanceState) }

    private fun goLogOut() {

        mBinding.settingsLogoutButton.setOnClickListener {
            AuthUI.getInstance().signOut(requireContext()).addOnCompleteListener {
                Toast.makeText(requireContext(), "Bye bye", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            } } }

    private fun goContactUs() {

        val recipient = "sofianem75018@gmail.com"
        val subject = settings_mail_object.text.toString().trim()
        val message = settings_mail_txt.text.toString().trim()

        mBinding.settingsSend.setOnClickListener {

            Toast.makeText(requireContext(), "Send eMail", Toast.LENGTH_SHORT).show()

            val mIntent = Intent(Intent.ACTION_SEND)
            mIntent.data = Uri.parse("mailto:")
            mIntent.type = "text/plain"
            mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            mIntent.putExtra(Intent.EXTRA_TEXT, message)


            try { startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }

        }
    }


    private fun goLanguage() {
        Log.d(TAG,"getDisplayLanguage = " + Locale.getDefault().getDisplayLanguage());
        Log.d(TAG,"getLanguage = " + Locale.getDefault().getLanguage());
        Log.d(TAG,"Resources.getLanguage = " + Resources.getSystem().getConfiguration().locale.getLanguage());
        Log.d(TAG,"getResources.getLanguage = " + resources.configuration.locale);
        mBinding.settingsLanguageChip.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == 6) {   setLocale("en")}
            if (checkedId == 7) {  setLocale("fr") }
            if (checkedId == 8) {   setLocale("es")}
            if (checkedId == 9) {   setLocale("hi")}
            if (checkedId == 10) {   setLocale("it")}
            if (checkedId == 11) {   setLocale("zh")}
            if (checkedId == 12) {   setLocale("de")}
            if (checkedId == 13) {   setLocale("pt")}
        }
    }

        private fun setLocale(localeName: String) {
            if (localeName != currentLanguage) {
                locale = Locale(localeName)
                val res = resources
                val dm = res.displayMetrics
                val conf = res.configuration
                conf.locale = locale
                res.updateConfiguration(conf, dm)
                val refresh = Intent(
                    requireContext(),
                    MainActivity::class.java
                )
                refresh.putExtra(currentLang, localeName)
                startActivity(refresh)
            } else {
                Toast.makeText(
                    requireContext(), "Language, , already, , selected)!", Toast.LENGTH_SHORT).show();
            }
        }

    private fun testprintln() {
        println("--7--mIdUser----" + mIdUser)
    }
}