package com.so.dingbring.view.detail

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.so.dingbring.R
import com.so.dingbring.Utils
import io.reactivex.subjects.BehaviorSubject
import java.util.ArrayList


class DetailAdapter(
    var mContext: Context,
    var mListMyItem: ArrayList<ArrayList<String>>
) : RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {

    val itemClickN: BehaviorSubject<HashMap<Int,ArrayList<String>>> = BehaviorSubject.create()
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
            Glide.with(mContext).load(mUserPic).apply(RequestOptions.circleCropTransform()).into(mDetailImageUser)

               if (mItemStatus== "I need") {warningVisible() }
               else {warningInvisible()} } }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.detail_item, parent, false)
        return DetailViewHolder(view) }

    override fun getItemCount(): Int {
        return if (mListMyItem.size > 0) { mListMyItem.size }
        else { 0 } }

    inner class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var animShow= false
        var mDetailItem: TextView = itemView.findViewById(R.id.detail_item_type)
        var mDetailQuantity: TextView = itemView.findViewById(R.id.detail_item_quantity)
        var mDetailImageUser: ImageView = itemView.findViewById(R.id.detail_item_photo)
        private var mDetailB1: ImageView = itemView.findViewById(R.id.detail_item_b)
        private var mDetailB2: ImageView = itemView.findViewById(R.id.detail_item_band)
        private var mDetailB3: ImageView = itemView.findViewById(R.id.detail_item_bandb)

        private var mDetailEdit: FloatingActionButton = itemView.findViewById(R.id.detail_item_global)
        var mDetailEditPlus: FloatingActionButton = itemView.findViewById(R.id.detail_item_plus)
        var mDetailEditMinus: FloatingActionButton = itemView.findViewById(R.id.detail_item_minus)
        var mDetailEditDelete: FloatingActionButton = itemView.findViewById(R.id.detail_item_delete)
        private var mDetailEditSub: ImageView = itemView.findViewById(R.id.detail_item_sub)

        init {
        mDetailEdit.setOnClickListener {
            animShow = if (!animShow) {
             val animation = AnimationUtils.loadAnimation(mContext, R.anim.zoomout)
             mDetailEdit.startAnimation(animation)
             true }

            else { val animation = AnimationUtils.loadAnimation(mContext, R.anim.zoomin)
             mDetailEdit.startAnimation(animation)
             false }

            if (mDetailEditMinus.isVisible) { iconInvisible()}
                else { iconVisible()
                mDetailEditPlus.setOnClickListener {initMapUp(1)
                    iconInvisible() }
                mDetailEditMinus.setOnClickListener{initMapUp(2)
                    iconInvisible() }
                mDetailEditDelete.setOnClickListener {initMapUp(3)
                    iconInvisible() } } } }

         private fun initMapUp(i: Int) {
            val mChangeHashMap = hashMapOf<Int, ArrayList<String>>()
            mChangeHashMap.clear()
            mChangeHashMap[i] = mListMyItem[position]
            itemClickN.onNext(mChangeHashMap) }

        fun warningInvisible(){
            mDetailB1.visibility = View.INVISIBLE
            mDetailB2.visibility = View.INVISIBLE
            mDetailB3.visibility = View.INVISIBLE }

         fun warningVisible(){
            mDetailB1.visibility = View.VISIBLE
            mDetailB2.visibility = View.VISIBLE
            mDetailB3.visibility = View.VISIBLE

             mDetailB3.setOnClickListener { initMapUp(0) } }

        private fun iconVisible() { Utils.testAnim(mContext, mDetailEditMinus,mDetailEditPlus,mDetailEditDelete ,mDetailEditSub, mDetailImageUser) }

        private fun iconInvisible()  { Utils.testAnim2(mContext, mDetailEditMinus,mDetailEditPlus,mDetailEditDelete ,mDetailEditSub, mDetailImageUser) }
    }
}





