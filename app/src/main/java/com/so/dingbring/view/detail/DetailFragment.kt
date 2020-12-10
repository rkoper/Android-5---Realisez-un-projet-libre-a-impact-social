package com.so.dingbring.view.detail

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.gauravk.bubblenavigation.BubbleNavigationLinearView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.so.dingbring.R
import com.so.dingbring.data.MyEventViewModel
import com.so.dingbring.data.MyItem
import com.so.dingbring.data.MyItemViewModel
import com.so.dingbring.view.base.BaseFragment
import com.so.dingbring.view.main.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_item.*
import kotlinx.android.synthetic.main.dialog_layout_detail.*
import kotlinx.android.synthetic.main.dialog_layout_detail_info.*
import kotlinx.android.synthetic.main.fragment_detail.*
import org.koin.android.viewmodel.ext.android.viewModel
import render.animations.Attention
import render.animations.Bounce
import render.animations.Render
import java.util.*


class DetailFragment : BaseFragment() {

    private var mEventId = ""
    private lateinit var mDetailAdapter: DetailAdapter
    private val mItemVM by viewModel<MyItemViewModel>()
    private val mEventVM by viewModel<MyEventViewModel>()
    private var mItemStatus: String = "I bring"
    private var mItemName: String = "<3"
    private var mItemUniqueID = UUID.randomUUID().toString()
    private var mListMyItem: ArrayList<ArrayList<String>> = arrayListOf()
    var mNameUser = "..."
    private var mIdUser  = FirebaseAuth.getInstance().currentUser?.uid.toString()
    var mStatusBool = true
    private var mBubble : BubbleNavigationLinearView? = null
    private var mBubbleDetail : BubbleNavigationLinearView? = null
    private var mTopBarTxt : TextView? = null
    private var mFloat_back : FloatingActionButton? = null
    private var mFloat_action : FloatingActionButton? = null
    private var mItemQuantity = 1
    private lateinit var d_detail:Dialog
    private var thisView: View? = null
    private var mTextName: TextView? = null
    private var mPosBottomBar: BubbleNavigationLinearView? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FirebaseApp.initializeApp(requireActivity())
        mEventId = arguments?.get("GlobalIdEvent").toString()
        initTopBottomBar()
        thisView =  inflater.inflate(R.layout.fragment_detail, container, false)
        return thisView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mTextName = view.findViewById(R.id.detail_name_txt)
        initCreateItem()
        initRetrieveItem()
        prepareToShare()
        onBackPressed()
        onBackBarPressed()
        initBottom()
        initHeader()
    }


    private fun onBackPressed() {
        mPosBottomBar = activity?.findViewById(R.id.float_bottom_bar)
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navToHome() } })}


    private fun onBackBarPressed() {
        mFloat_back = activity?.findViewById(R.id.item_tb_fb_back)
        mFloat_back?.setOnClickListener { navToHome() } }

    private fun navToHome() {
        Navigation.findNavController(requireActivity(), R.id.hostFragment).navigate(R.id.event_fragment)
        initBubbleDetailInvisible()
        initBubbleVisible() }


    private fun initTopBottomBar() {
        initBubbleInvisible()
        initBubbleDetailVisible()
        iniTopBar()
    }


    private fun iniTopBar() {
        mTopBarTxt = activity?.findViewById(R.id.item_tool_bar)
        mTopBarTxt?.text  = getString(R.string.detail)
        mTopBarTxt?.setTextColor(resources.getColor(R.color.yellow_900))

        mFloat_back = activity?.findViewById(R.id.item_tb_fb_back)
        mFloat_back?.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.yellow_900))
        mFloat_back?.setColorFilter(Color.argb(255, 255, 255, 255))

        mFloat_action = activity?.findViewById(R.id.item_tb_fb_action)
        mFloat_action?.visibility = View.VISIBLE
        mFloat_action?.setImageResource(R.drawable.logo_share)
        mFloat_action?.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.yellow_900))
    }

    private fun initBubbleDetailVisible() {
        mBubbleDetail = activity?.findViewById(R.id.float_bottom_bar_detail)
        mBubbleDetail?.setCurrentActiveItem(1)
        mBubbleDetail?.visibility = View.VISIBLE
    }

    private fun initBubbleInvisible() {
        mBubble = activity?.findViewById(R.id.float_bottom_bar)
        mBubble?.visibility = View.INVISIBLE
    }

    private fun initBubbleDetailInvisible() {
        mBubbleDetail = activity?.findViewById(R.id.float_bottom_bar_detail)
        mBubbleDetail?.visibility = View.INVISIBLE
    }

    private fun initBubbleVisible() {
        mBubble = activity?.findViewById(R.id.float_bottom_bar)
        mBubble?.visibility = View.VISIBLE
        mBubble?.setCurrentActiveItem(1)
        mTopBarTxt?.text  = getString(R.string.event)
        mTopBarTxt?.setTextColor(resources.getColor(R.color.yellow_900))
    }

    private fun initBottom() {
        mBubbleDetail?.setNavigationChangeListener { view, position ->
            when (position) {
                0 ->  navToHome()
                2 -> navToFrag() } } }


    private fun navToFrag() {
        Navigation.findNavController(requireActivity(), R.id.hostFragment).navigate(R.id.event_fragment)
        mTopBarTxt?.text  = getString(R.string.event)
        mFloat_back?.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.red_300))
        mFloat_action?.visibility = View.INVISIBLE
        mTopBarTxt?.text  = getString(R.string.event)
        mTopBarTxt?.text  = getString(R.string.event)
        mBubbleDetail!!.visibility = View.INVISIBLE
        mBubble!!.visibility = View.VISIBLE
        mBubble?.setCurrentActiveItem(1) }



    private fun prepareToShare() {
        mFloat_action?.setOnClickListener {
            val dynamicLink = Firebase.dynamicLinks.dynamicLink {
                link = Uri.parse("https://dingbring.page.link/$mEventId")
                domainUriPrefix = "https://dingbring.page.link/"
                androidParameters { } }

            val dynamicLinkUri = dynamicLink.uri
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, dynamicLinkUri.toString())
            startActivity(Intent.createChooser(intent, "Share Link")) } }

    @SuppressLint("CheckResult")
    private fun initRetrieveItem() {
        mDetailAdapter = DetailAdapter(requireContext(), mListMyItem)
        detail_recyclerView.layoutManager = LinearLayoutManager(context)
        detail_recyclerView.adapter = mDetailAdapter
        initRVObserver()
        mDetailAdapter.itemClickN.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe { data ->

                    if (data.containsKey(0)) {
                        d_detail= Dialog(requireContext())
                        d_detail.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        d_detail.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        d_detail.setContentView(R.layout.dialog_layout_detail)
                        d_detail.show()

                        d_detail.dialog_up.setOnClickListener { mItemVM.updateStatus(data)
                            initRVObserver()
                            d_detail.dismiss()}

                        d_detail.dialog_down.setOnClickListener { d_detail.dismiss()} }
                    else { mItemVM.updateStatus(data)
                    initRVObserver()  }} }




    private fun initRVObserver() {
        mItemVM.getItem(mEventId).observe(requireActivity(), androidx.lifecycle.Observer {mlmi ->
            mlmi.sortByDescending { it[1] }
            mListMyItem.clear()
            mListMyItem.addAll(mlmi)
            mDetailAdapter.notifyDataSetChanged()}) }


     fun initHeader() : String{
         FirebaseApp.initializeApp(requireActivity())
        var mEventAdress = ""
        var mEventDate = ""
        var mEventHour = ""
        var mEventName = ""
        var mEventDesc = ""
        mEventVM.getEventrById(mEventId).observe(requireActivity(), androidx.lifecycle.Observer { myevent ->
             mEventAdress = myevent.mEventAdress
             mEventDate = myevent.mEventDate
             mEventHour = myevent.mEventHour
             mEventName = myevent.mEventName
             mEventDesc = myevent.mEventDesc

            mTextName!!.text = mEventName
            detail_button_info.setOnClickListener {

                val d = Dialog(requireContext())
                d.requestWindowFeature(Window.FEATURE_NO_TITLE)
                d.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                d.setContentView(R.layout.dialog_layout_detail_info)
                d.dialog_det_info_addressevent.text = mEventAdress
                d.dialog_det_info_dateevent.text = mEventDate
                d.dialog_det_info_nameevent.text = mEventName
                d.dialog_det_info_hourevent.text = mEventHour
                d.dialog_det_info_descevent.text = mEventDesc
                d.dialog_det_info_cancel.setOnClickListener { d.dismiss() }
                d.show() } })
    return mEventName
    }

    @SuppressLint("WrongConstant")
    private fun initStatus() {

        val renderLeft = Render(requireContext())
        val renderRight = Render(requireContext())

        renderRight.setAnimation(Attention().Shake(detail_status_bring))
        renderRight.setDuration(2000)
        renderRight.start()

        renderLeft.setAnimation(Attention().Shake(detail_status_need))
        renderLeft.setDuration(2000)
        renderLeft.start()

        detail_status_bring?.setOnClickListener {
            mStatusBool = true
            mItemStatus = "I bring"
            detail_status_bring.setBackgroundColor(activity?.resources?.getColor(R.color.yellow_900)!!)
            detail_status_need.setBackgroundColor(activity?.resources?.getColor(R.color.grey_400)!!) }

            detail_status_need?.setOnClickListener {
                mStatusBool = false
            mItemStatus = "I need"
                detail_status_need.setBackgroundColor(activity?.resources?.getColor(R.color.yellow_900)!!)
                detail_status_bring.setBackgroundColor(activity?.resources?.getColor(R.color.grey_400)!!) } }

    private fun initItem() {
        detail_item_edit.doOnTextChanged { text, start, before, count ->
            mItemName= detail_item_edit.text.toString() } }

    private fun initQuantity() {
        detail_quantity_txt.text =  mItemQuantity.toString()
        detail_quantity_minus.setOnClickListener {

            if (mItemQuantity > 1){
                mItemQuantity -= 1
                detail_quantity_txt.text = mItemQuantity.toString()} }

        detail_quantity_plus .setOnClickListener {
            mItemQuantity += 1
             detail_quantity_txt.text = mItemQuantity.toString()} }

    private fun initCreateItem() {
        initItem()
        initQuantity()
        initStatus()

        detail_check?.setOnClickListener {
            initItem()
            if (mItemName == "") { Toast.makeText(requireContext(), "Please add item <3", Toast.LENGTH_LONG).show() }
            else { val mMyItem = MyItem(mItemUniqueID, mItemName, mItemStatus, mItemQuantity.toString(), mIdUser, mEventId)
               mItemVM.createUniqueItem(mMyItem)
                initRVObserver()

                detail_status_bring.setBackgroundColor(activity?.resources?.getColor(R.color.grey_400)!!)
                detail_status_need.setBackgroundColor(activity?.resources?.getColor(R.color.grey_400)!!)
                detail_quantity_txt.text = "1"
                detail_item_edit.setText("")
            } } }

}
