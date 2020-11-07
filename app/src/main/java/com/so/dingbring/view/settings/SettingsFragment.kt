package com.so.dingbring.view.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.firebase.ui.auth.AuthUI
import com.google.android.material.chip.Chip
import com.so.dingbring.R
import com.so.dingbring.view.login.LoginActivity
import com.so.dingbring.view.main.MainActivity
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.*


class SettingsFragment : Fragment() {

    lateinit var locale: Locale
    private var currentLanguage = "en"
    private var mCheckedID = 1
    var mNameUser = "..."
    var mEmailUser = "..."
    var mPhotoUser = "..."
    var mIdUser = "..."
    var fragmentStatus = ""
    var varbutton : ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_settings, container, false)

        mIdUser  = MainActivity.mIdUser
        mNameUser  = MainActivity.mNameUser
        mEmailUser  = MainActivity.mEmailUser
        mPhotoUser  = MainActivity.mPhotoUser



      varbutton = activity?.findViewById(R.id.main_button)

        return  view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      //  varbutton = activity?.findViewById(R.id.main_button)
        varbutton?.visibility  = View.VISIBLE
        goLanguage()
        goContactUs()
        goLogOut()
        fragmentStatus = "onViewCreated"
   }

    private fun goLogOut() {
        settings_logout_button.setOnClickListener {
            AuthUI.getInstance().signOut(requireContext()).addOnCompleteListener {
                Toast.makeText(requireContext(), "Bye bye", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent) } } }

    private fun goContactUs() {

        val recipient = "sofianem75018@gmail.com"
        val subject = settings_mail_object.text.toString().trim()
        val message = settings_mail_txt.text.toString().trim()

        settings_send.setOnClickListener {

            Toast.makeText(requireContext(), "Send eMail", Toast.LENGTH_SHORT).show()

            val mIntent = Intent(Intent.ACTION_SEND)
            mIntent.data = Uri.parse("mailto:")
            mIntent.type = "text/plain"
            mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            mIntent.putExtra(Intent.EXTRA_TEXT, message)


            try { startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
            } catch (e: Exception) { Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show() }
        }
    }


    private fun goLanguage() {

        settings_language_chip.setOnCheckedChangeListener { group, Id ->
            settings_language_chip.isSingleSelection
           when (group.checkedChipId) {
               setting_en.id  ->   setLocale("en")
               setting_fr.id   ->  setLocale("fr")
               setting_it.id   ->  setLocale("it")
               setting_sp.id   ->  setLocale("es")
               setting_de.id   ->  setLocale("de")
               setting_pt.id   ->  setLocale("pt")
               setting_ch.id   ->  setLocale("zh")
               setting_ar.id   ->  setLocale("ar")
           }
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
                currentLanguage = localeName

                 Navigation.findNavController(requireActivity(), R.id.hostFragment).navigate(R.id.settings_fragment) }

            else { Toast.makeText(requireContext(), "Language, , already, , selected)!", Toast.LENGTH_SHORT).show()
            }
        }
}