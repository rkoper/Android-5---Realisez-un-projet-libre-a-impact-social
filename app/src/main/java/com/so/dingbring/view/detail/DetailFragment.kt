package com.so.dingbring.view.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gauravk.bubblenavigation.BubbleNavigationLinearView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.so.dingbring.R
import com.so.dingbring.data.MyEventViewModel
import com.so.dingbring.data.MyItem
import com.so.dingbring.data.MyItemViewModel
import com.so.dingbring.data.MyUserViewModel
import com.so.dingbring.view.base.BaseFragment
import com.so.dingbring.view.login.LoginActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_detail.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class DetailFragment : BaseFragment() {

    var mEventId = ""
    private lateinit var mDetailAdapter: DetailAdapter
    private val mItemVM by viewModel<MyItemViewModel>()
    private val mEventVM by viewModel<MyEventViewModel>()
    var mItemStatus: String = "I bring"
    var mItemName: String = "<3"
    var mItemUniqueID = UUID.randomUUID().toString()
    var mListMyItem: ArrayList<ArrayList<String>> = arrayListOf()
    var mNameUser = "..."
    var mIdUser = "////"
    var mBubble : BubbleNavigationLinearView? = null
    var mItemQuantity = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mEventId = arguments?.get("GlobalIdEvent").toString()
        mIdUser = LoginActivity.mIdUser
        initHeader()
        return inflater.inflate(R.layout.fragment_detail, container, false) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRetrieveItem()
        initCreateItem()
        mBubble = activity?.findViewById(R.id.floating_top_bar_navigation)
        mBubble!!.visibility = View.INVISIBLE
        invisible()
        prepareToShare()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    view?.findNavController()?.navigate(R.id.action_detail_to_home)
                    val aniSlide = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomin_main)
                    mBubble!!.startAnimation(aniSlide)
                    mBubble!!.visibility = View.VISIBLE
                    mBubble?.setCurrentActiveItem(1) } }) }



    private fun prepareToShare() { detail_share.setOnClickListener {
            val dynamicLink = Firebase.dynamicLinks.dynamicLink {
                link = Uri.parse(   "https://dingbring.page.link/" + mEventId )
                domainUriPrefix = "https://example.page.link"
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
                   mItemVM.updateStatus(data)
                    initRVObserver() } }

    private fun initRVObserver() {
        mItemVM.getItem(mEventId).observe(requireActivity(), androidx.lifecycle.Observer {   mlmi ->
            mListMyItem.clear()
            mListMyItem.addAll(mlmi)
            mDetailAdapter.notifyDataSetChanged() }) }


    private fun initHeader() {
        mEventVM.getEventrById(mEventId).observe(requireActivity(),androidx.lifecycle.Observer { myevent ->
            detail_name_txt.text = myevent.mEventName }) }

    private fun initButton() {
        detail_button_add.setOnClickListener {
            val zoom1 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomin_v2)
            val zoom2 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomout_2)
            detail_status_bring.startAnimation(zoom1)
            detail_status_bring.startAnimation(zoom2)
            detail_status_need.startAnimation(zoom1)
            detail_status_need.startAnimation(zoom2) } }

    private fun initStatus() {
        detail_status_bring?.setOnClickListener {
            detail_status_bring.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.purple_400))
            mItemStatus = "I bring"
            detail_status_need.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.fui_transparent))
            initAnimafterClickAdd()}



            detail_status_need?.setOnClickListener {
            detail_status_need.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.purple_400))
            mItemStatus = "I need"
            detail_status_bring.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.fui_transparent))
                initAnimafterClickAdd()}}

    private fun initAnimafterClickAdd() {
        goAnimTxtLayout(detail_item_txt)
        goAnimEdit(detail_item_edit)
        detail_item_edit.visibility = View.VISIBLE
    }

    private fun initItem() {
        detail_item_edit.doOnTextChanged { text, start, before, count ->
            if (start == 1) {
                goAnimTxt(detail_qty_txt)
            //    goAnimImage(detail_check)
                goAnimView(detail_quantity_item)
                goAnimView(detail_quantity_minus)
                goAnimView(detail_quantity_plus)



            }

            mItemName= detail_item_edit.text.toString()
        } }

    private fun initQuantity() {
        detail_quantity_txt.text =  mItemQuantity.toString()

        detail_quantity_minus.setOnClickListener {
            goAnimImage(detail_check)
            if (mItemQuantity > 1){
                mItemQuantity = mItemQuantity - 1
                detail_quantity_txt.text = mItemQuantity.toString()} }

        detail_quantity_plus .setOnClickListener {
            goAnimImage(detail_check)
            mItemQuantity = mItemQuantity + 1
             detail_quantity_txt.text = mItemQuantity.toString()} }

    @SuppressLint("ResourceType")
    private fun initCreateItem() {
        initButton()
        initItem()
        initQuantity()
        initStatus()

        detail_check?.setOnClickListener {
            initItem()
            if (mItemName == "") { Toast.makeText(requireContext(), "Please add item <3", Toast.LENGTH_LONG).show() }
            else { val mMyItem = MyItem(mItemUniqueID, mItemName, mItemStatus, mItemQuantity.toString(), mIdUser, mEventId)

               mItemVM.createUniqueItem(mMyItem)
                invisible()
                initRVObserver() }

         //  initVisible()

        } }
    private fun invisible() {
        detail_status_bring.visibility = View.INVISIBLE
        detail_status_need.visibility = View.INVISIBLE
        detail_qty_txt.visibility = View.INVISIBLE
        detail_item_txt.visibility = View.INVISIBLE
        detail_item_edit.visibility = View.INVISIBLE
        detail_quantity_item.visibility = View.INVISIBLE
        detail_quantity_minus.visibility = View.INVISIBLE
        detail_quantity_plus.visibility = View.INVISIBLE
        detail_check.visibility = View.INVISIBLE }


    private fun goAnimImage(mLink: ImageView?) {
        val zoom1 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomin_v2)
        val zoom2 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomout_2)
        mLink?.startAnimation(zoom1)
        mLink?.startAnimation(zoom2) }

    private fun goAnimView(mLink: View?) {
        val zoom1 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomin_v2)
        val zoom2 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomout_2)
        mLink?.startAnimation(zoom1)
        mLink?.startAnimation(zoom2) }

    private fun goAnimTxt(mLink: TextView?) {
        val zoom1 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomin_v2)
        val zoom2 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomout_2)
        mLink?.startAnimation(zoom1)
        mLink?.startAnimation(zoom2) }

    private fun goAnimTxtLayout(mLink: TextInputLayout?) {
        val zoom1 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomin_v2)
        val zoom2 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomout_2)
        mLink?.startAnimation(zoom1)
        mLink?.startAnimation(zoom2) }

    private fun goAnimEdit(mLink: EditText?) {
        val zoom1 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomin_v2)
        val zoom2 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomout_2)
        mLink?.startAnimation(zoom1)
        mLink?.startAnimation(zoom2) }

}
