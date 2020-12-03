package com.so.dingbring.view.detail

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.CameraUpdateFactory.zoomIn
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.so.dingbring.ItemSelectionAnimator
import com.so.dingbring.R
import com.so.dingbring.data.MyItem
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.dialog_layout_detail.*
import kotlinx.android.synthetic.main.dialog_layout_profil.*
import java.util.ArrayList


class DetailAdapter(
    var mContext: Context,
    var mListMyItem: ArrayList<ArrayList<String>>
) : RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {



    val itemClickN: BehaviorSubject<HashMap<Int,ArrayList<String>>> = BehaviorSubject.create()


    private val itemSelectionAnimator: ItemSelectionAnimator = ItemSelectionAnimator(mContext)
    var mItemStatus = ""
    var mItemName = ""
    var mItemQty = ""
    var mItemId = ""
    var mItemUserId = ""
    var mItemEventId = ""
    var mUserPic = ""
    var mUserName = ""



    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {

        with(holder) {

            mDetailEditMinus.visibility = View.GONE
            mDetailEditPlus.visibility = View.GONE
            mDetailEditDelete.visibility = View.GONE
            mDetailEditStatus.visibility = View.GONE
           // mDetailEditCache.visibility = View.GONE
             mItemStatus = mListMyItem[position][1]
             mItemName = mListMyItem[position][2]
             mItemQty = mListMyItem[position][3]
             mItemId = mListMyItem[position][4]
             mItemUserId = ""
             mItemEventId = mListMyItem[position][6]
             mUserPic = mListMyItem[position][7]
             mUserName =  mListMyItem[position][8]


            mDetailItem.text = mItemName
            mDetailQuantity.text = mItemQty
           // mDetailUser.text = mListMyItem[position].mItemUser


            Glide.with(mContext)
                .load(mUserPic)
                .apply(RequestOptions.circleCropTransform())
                .into(mDetailImageUser)

               if (mItemStatus== "I need") {warningVisible() }

               else {warningInvisible()} } }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.detail_item, parent, false)
        return DetailViewHolder(view) }

    override fun getItemCount(): Int {
        return if (mListMyItem.size > 0) { mListMyItem.size }
        else { 0 } }

    inner class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var animShow= false
        var mDetailItem: TextView = itemView.findViewById(R.id.detail_item_type)
        var mDetailQuantity: TextView = itemView.findViewById(R.id.detail_item_quantity)
        var mDetailImageUser: ImageView = itemView.findViewById(R.id.detail_item_photo)
        var mDetailB1: ImageView = itemView.findViewById(R.id.detail_item_b)
        var mDetailB2: ImageView = itemView.findViewById(R.id.detail_item_band)
        var mDetailB3: ImageView = itemView.findViewById(R.id.detail_item_bandb)

        var mDetailEdit: FloatingActionButton = itemView.findViewById(R.id.detail_item_global)
        var mDetailEditStatus: FloatingActionButton = itemView.findViewById(R.id.detail_item_status)
        var mDetailEditPlus: FloatingActionButton = itemView.findViewById(R.id.detail_item_plus)
        var mDetailEditMinus: FloatingActionButton = itemView.findViewById(R.id.detail_item_minus)
        var mDetailEditDelete: FloatingActionButton = itemView.findViewById(R.id.detail_item_delete)
        var mDetailEditSub: ImageView = itemView.findViewById(R.id.detail_item_sub)

        init {


        mDetailEdit.setOnClickListener {
         if (!animShow) {
             val animation = AnimationUtils.loadAnimation(mContext, R.anim.zoomout)
             mDetailEdit.startAnimation(animation)
             animShow = true}

         else { val animation = AnimationUtils.loadAnimation(mContext, R.anim.zoomin)
             mDetailEdit.startAnimation(animation)
             animShow = false}

            if (mDetailEditMinus.isVisible) { iconInvisible()}

                else { iconVisible()
                mDetailEditPlus.setOnClickListener {initMapUp(1)
                    iconInvisible() }
                mDetailEditMinus.setOnClickListener{initMapUp(2)
                    iconInvisible() }
                mDetailEditDelete.setOnClickListener {initMapUp(3)
                    iconInvisible() }

            } } }

         fun initMapUp(i: Int) {
            var mChangeHashMap = hashMapOf<Int, ArrayList<String>>()
            mChangeHashMap.clear()
            mChangeHashMap[i] = mListMyItem[position]
            itemClickN.onNext(mChangeHashMap)

        }
        fun warningInvisible(){
            mDetailB1.visibility = View.INVISIBLE
            mDetailB2.visibility = View.INVISIBLE
            mDetailB3.visibility = View.INVISIBLE
        }

         fun warningVisible(){
            mDetailB1.visibility = View.VISIBLE
            mDetailB2.visibility = View.VISIBLE
            mDetailB3.visibility = View.VISIBLE

             mDetailB3.setOnClickListener { initMapUp(0) }
         }

        fun iconVisible() {
            val animation100right = AnimationUtils.loadAnimation(mContext, R.anim.slideright100)
            val animation100 = AnimationUtils.loadAnimation(mContext, R.anim.slideleft100)
            val animation200 = AnimationUtils.loadAnimation(mContext, R.anim.slideleft200)
            val animation300 = AnimationUtils.loadAnimation(mContext, R.anim.slideleft300)
            val animation400 = AnimationUtils.loadAnimation(mContext, R.anim.slideleft400)
            mDetailEditMinus.visibility = View.VISIBLE
            mDetailEditPlus.visibility = View.VISIBLE
            mDetailEditDelete.visibility = View.VISIBLE
            mDetailEditStatus.visibility = View.VISIBLE
            mDetailEditSub.visibility = View.VISIBLE
            mDetailEditStatus.startAnimation(animation400)
            mDetailEditPlus.startAnimation(animation300)
            mDetailEditMinus.startAnimation(animation200)
            mDetailEditDelete.startAnimation(animation100)
            mDetailEditSub.startAnimation(animation100)
            mDetailImageUser.startAnimation(animation100right)
            mDetailImageUser.visibility = View.INVISIBLE }

        private fun iconInvisible() {
            val animation100left = AnimationUtils.loadAnimation(mContext, R.anim.slideleft100)
            val animation100 = AnimationUtils.loadAnimation(mContext, R.anim.slideright100)
            val animation200 = AnimationUtils.loadAnimation(mContext, R.anim.slideright200)
            val animation300 = AnimationUtils.loadAnimation(mContext, R.anim.slideright300)
            val animation400 = AnimationUtils.loadAnimation(mContext, R.anim.slideright400)
            mDetailImageUser.startAnimation(animation100left)
            mDetailEditStatus.startAnimation(animation100)
            mDetailEditPlus.startAnimation(animation200)
            mDetailEditMinus.startAnimation(animation300)
            mDetailEditDelete.startAnimation(animation400)
            mDetailEditSub.startAnimation(animation400)
            mDetailEditMinus.visibility = View.GONE
            mDetailEditPlus.visibility = View.GONE
            mDetailEditDelete.visibility = View.GONE
            mDetailEditStatus.visibility = View.GONE
            mDetailEditSub.visibility = View.GONE
            mDetailImageUser.visibility = View.VISIBLE }

    }


}





