package com.so.dingbring.view.event

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseApp
import com.so.dingbring.R
import com.so.dingbring.data.MyEvent
import com.so.dingbring.data.MyEventViewModel
import com.so.dingbring.data.MyUserViewModel
import com.so.dingbring.view.main.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_event.*
import org.koin.android.viewmodel.ext.android.viewModel

class EventFragment : Fragment() {

    private val mEventVM by viewModel<MyEventViewModel>()
    private val mUserVM by viewModel<MyUserViewModel>()
    private lateinit var mEventAdapter: EventAdapter
    var mDataEvent: MutableList<MyEvent> = mutableListOf()
    var mNameUser = "///"
    var mEmailUser = " /// "
    var mPhotoUser = " /// "
    var mIdUser = "////"
    var mUserEvent = arrayListOf("", "")
    var varbutton : ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FirebaseApp.initializeApp(requireContext())
        val view: View = inflater.inflate(R.layout.fragment_event, container, false)
        mIdUser  = MainActivity.mIdUser
        mNameUser  = MainActivity.mNameUser
        mEmailUser  = MainActivity.mEmailUser
        mPhotoUser  = MainActivity.mPhotoUser

        mUserVM.getifNewUser(mIdUser)?.observe(requireActivity(), androidx.lifecycle.Observer { condition ->
            if (condition){
                mIdUser  = MainActivity.mIdUser
                mNameUser  = MainActivity.mNameUser
                mEmailUser  = MainActivity.mEmailUser
                mPhotoUser  = MainActivity.mPhotoUser
                initHeader()
                initRV()}

            else{ mUserVM.getUserById(mIdUser).observe(requireActivity(),androidx.lifecycle.Observer {mlmu ->
                mIdUser  = mlmu.mUserId
                mNameUser  = mlmu.mNameUser
                mEmailUser  = mlmu.mEmailUser
                mPhotoUser  = mlmu.mPhotoUser
                mUserEvent = mlmu.mEventUser

                initHeader()
                initRV() })} })


        varbutton = activity?.findViewById(R.id.main_button)
        varbutton?.visibility = View.VISIBLE

        return view }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        varbutton = activity?.findViewById(R.id.main_button)
        varbutton?.visibility = View.VISIBLE
    }

    private fun initHeader() {
        val animation100 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoominbacktofront)
        home_name.startAnimation(animation100)
        home_name.text = mNameUser}


    @SuppressLint("CheckResult")
    private fun initRV() {
        mEventAdapter = EventAdapter(requireActivity(), mDataEvent)
        recyclerview_event.setHasFixedSize(true)
        recyclerview_event.layoutManager = LinearLayoutManager(context)
        recyclerview_event.adapter = mEventAdapter

        mEventAdapter.itemClick.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread()).subscribe { data ->


                var bundle = bundleOf("GlobalIdEvent" to data.mEventId)
                bundle.putString("GlobalIdUSer", mIdUser)
                view?.findNavController()?.navigate(R.id.action_eventFragment_to_detail_fragment, bundle)




            }
        loadRV() }


    private fun loadRV() {
        mEventVM.getUserEvent(mUserEvent).observe(requireActivity(), androidx.lifecycle.Observer{ a ->
            mDataEvent.clear()
            mDataEvent.addAll(a)

            mEventAdapter.notifyDataSetChanged() })}

}