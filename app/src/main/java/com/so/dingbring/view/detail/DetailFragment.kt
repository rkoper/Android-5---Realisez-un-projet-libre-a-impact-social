package com.so.dingbring.view.detail

import android.animation.ValueAnimator
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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.iosParameters
import com.google.firebase.ktx.Firebase
import com.so.dingbring.R
import com.so.dingbring.data.MyEventViewModel
import com.so.dingbring.data.MyItem
import com.so.dingbring.data.MyItemViewModel
import com.so.dingbring.data.MyUserViewModel
import com.so.dingbring.view.login.LoginActivity
import com.so.dingbring.view.main.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_detail.*
import nl.dionsegijn.steppertouch.OnStepCallback
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class DetailFragment : Fragment() {
    var mEventId = ""

    private lateinit var mDetailAdapter: DetailAdapter

    private val mItemVM by viewModel<MyItemViewModel>()
    private val mEventVM by viewModel<MyEventViewModel>()
    private val mUserVM by viewModel<MyUserViewModel>()

    var i: Int = 1
    var mItemStatus: String = "I bring"
    var mItemName: String = "<3"
    var mItemQuantity: String = "1"
    var mItemUniqueID = UUID.randomUUID().toString()
    var mListMyItem = arrayListOf<MyItem>()
    var mNameUser = "..."
    var mPhotoUser = "..."
    var mIdUser = "////"

    var mSelected = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mEventId = arguments?.get("GlobalIdEvent").toString()
        mIdUser = arguments?.get("GlobalIdUSer").toString()
        initHeader()

        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRetrieveItem()
        initCreateItem()
        detail_status_bring.visibility = View.INVISIBLE
        detail_status_need.visibility = View.INVISIBLE
        detail_txtV_item.visibility = View.INVISIBLE
        detail_edit_item.visibility = View.INVISIBLE
        detail_qty_cl.visibility = View.INVISIBLE
        detail_view.visibility = View.INVISIBLE
        detail_add.visibility = View.INVISIBLE
        prepareToShare()

    }


    private fun prepareToShare() {


        detail_share.setOnClickListener {
            val dynamicLink = Firebase.dynamicLinks.dynamicLink {
                link = Uri.parse(   "https://dingbring.page.link/" + mEventId )

                domainUriPrefix = "https://example.page.link"
                // Open links with this app on Android
                androidParameters { }
                // Open links with com.example.ios on iOS
            //    iosParameters("com.example.ios") { }
            }




            val dynamicLinkUri = dynamicLink.uri

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, dynamicLinkUri.toString())

            startActivity(Intent.createChooser(intent, "Share Link"))



        }



    }




    @SuppressLint("CheckResult")
    private fun initRetrieveItem() {
        mDetailAdapter = DetailAdapter(requireContext(), mListMyItem)
        detail_recyclerView.layoutManager = LinearLayoutManager(context)
        detail_recyclerView.adapter = mDetailAdapter
        initRVObserver()

        mDetailAdapter.itemClickN.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe { data ->
                    mItemVM.updateStatus(data)
                    initRVObserver()
                }

        }


    private fun initRVObserver() {
        mItemVM.getTestItem(mEventId).observeForever { mlmi ->
            mlmi.sortBy { it.mItemStatus }
            with(mListMyItem){
                clear()
                addAll(mlmi)}

            mListMyItem.let {
        mDetailAdapter.notifyDataSetChanged() }

        } }

  private fun initHeader() {
        mEventVM.getEventrById(mEventId).observe(requireActivity(),androidx.lifecycle.Observer { myevent ->
            detail_name_txt.text = myevent.mEventName

            // RAPEL

        }) }


    private fun initButton() {
        detail_button_add.setOnClickListener {
            val zoom1 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomin_v2)
            val zoom2 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomout_2)
            detail_status_bring.startAnimation(zoom1)
            detail_status_bring.startAnimation(zoom2)
            detail_status_need.startAnimation(zoom1)
            detail_status_need.startAnimation(zoom2)
        }
    }


    private fun initStatus() {
        detail_status_bring?.setOnClickListener {
            detail_status_bring.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.blue_800))
            mItemStatus = "I bring"
            detail_status_need.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.blue_200))

            goAnimTxt(detail_txtV_item)
            goAnimEdit(detail_edit_item)
            goAnimView(detail_view)
            detail_edit_item.visibility = View.VISIBLE

        }

        detail_status_need?.setOnClickListener {
            detail_status_need.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.blue_800
                )
            )
            mItemStatus = "I need"
            detail_status_bring.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.blue_200))

            goAnimTxt(detail_txtV_item)
            goAnimEdit(detail_edit_item)
            goAnimView(detail_view)
            detail_edit_item.visibility = View.VISIBLE
        }
    }

    private fun initItem() {
        detail_edit_item.doOnTextChanged { text, start, before, count ->
            if (count ==1)
            {    goAnimLayout(detail_qty_cl)
                goAnimImage(detail_add)
        }
        mItemName = detail_edit_item.text.toString() }
    }

    private fun initQuantity() {
        detail_quantity_item.count = 1
        detail_quantity_item.minValue = 1
        detail_quantity_item.maxValue = 50
        detail_quantity_item.sideTapEnabled = true
        detail_quantity_item.addStepCallback(object : OnStepCallback {
            override fun onStep(qty: Int, positive: Boolean) {
                mItemQuantity = qty.toString() }}) }

    private fun goAnimImage(mLink: ImageView?) {
        val zoom1 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomin_v2)
        val zoom2 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomout_2)
        mLink?.startAnimation(zoom1)
        mLink?.startAnimation(zoom2) }




    @SuppressLint("ResourceType")
    private fun initCreateItem() {
        initButton()
        initItem()
        initQuantity()
        initStatus()

        detail_add?.setOnClickListener {
            initItem()
            if (mItemName == "") { Toast.makeText(
                requireContext(),
                "Please add item <3",
                Toast.LENGTH_LONG
            ).show() }
            else { val mMyItem = MyItem(
                mItemStatus,
                mItemQuantity,
                mItemName,
                mNameUser,
                mItemUniqueID,
                mEventId,
                mPhotoUser
            )
                mItemVM.createUniqueItem(mMyItem)
                initRVObserver() }

            detail_status_bring.visibility = View.INVISIBLE
            detail_status_need.visibility = View.INVISIBLE
            detail_txtV_item.visibility = View.INVISIBLE
            detail_edit_item.visibility = View.INVISIBLE
            detail_qty_cl.visibility = View.INVISIBLE
            detail_view.visibility = View.INVISIBLE
            detail_add.visibility = View.INVISIBLE
        }
    }
    private fun goAnimLayout(mLink: ConstraintLayout?) {
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

    private fun goAnimEdit(mLink: EditText?) {
        val zoom1 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomin_v2)
        val zoom2 = AnimationUtils.loadAnimation(requireContext(), R.anim.zoomout_2)
        mLink?.startAnimation(zoom1)
        mLink?.startAnimation(zoom2) }




}
