package com.so.dingbring.view.detail

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Point
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.imangazaliev.circlemenu.CircleMenu
import com.so.dingbring.R
import com.so.dingbring.data.MyItem
import io.reactivex.subjects.BehaviorSubject


class DetailAdapter(
    var mContext: Context,
    var mListMyItem: ArrayList<MyItem>
) : RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {

    val itemClickEmpty: BehaviorSubject<MyItem> = BehaviorSubject.create()
    val itemClickFull: BehaviorSubject<MyItem> = BehaviorSubject.create()
    val itemClickN: BehaviorSubject<HashMap<Int,MyItem>> = BehaviorSubject.create()

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        with(holder) {
            mDetailItem.text = mListMyItem[position].mItemName
            mDetailQuantity.text = mListMyItem[position].mItemQty
            mDetailUser.text = mListMyItem[position].mItemUser

            Glide.with(mContext)
                .load(mListMyItem[position].mItemUserPhoto)
                .apply(RequestOptions.circleCropTransform())
                .into(mDetailImageUser)

            println("----|mItemUserPhoto|-----" + mListMyItem[position].mItemUserPhoto)
            println("----|mItemUser|-----" + mListMyItem[position].mItemUser)



            if (mListMyItem[position].mItemStatus == "I need") {
                mDetailBand.visibility = View.VISIBLE
                mDetailColor.setBackgroundColor(mContext.resources.getColor(R.color.orange_50))
            } else {
                mDetailBand.visibility = View.GONE
                mDetailColor.setBackgroundColor(mContext.resources.getColor(R.color.orange_300))
            }
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.detail_item, parent, false)
        return DetailViewHolder(view)
    }

    override fun getItemCount(): Int {
        println("mListMyItem.size------" + mListMyItem.size)
        return if (mListMyItem.size > 0) {
            mListMyItem.size
        } else {
            0
        }
    }

    inner class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mDetailItem: TextView = itemView.findViewById(R.id.detail_item_type)
        var mDetailQuantity: TextView = itemView.findViewById(R.id.detail_item_quantity)
        var mDetailColor: ConstraintLayout = itemView.findViewById(R.id.detail_item_cl)
        var mDetailUser: TextView = itemView.findViewById(R.id.detail_item_personn)
        var mDetailAddOne: CircleMenu = itemView.findViewById(R.id.circleMenu)
        var mDetailImageUser: ImageView = itemView.findViewById(R.id.detail_item_photo)
        var mDetailBand: TextView = itemView.findViewById(R.id.detail_item_band)


        init {
            mDetailAddOne.setOnItemClickListener { buttonIndex ->
                var mChangeHashMap = hashMapOf< Int,MyItem>()
                mChangeHashMap[buttonIndex] = mListMyItem[position]
            itemClickN.onNext(mChangeHashMap) }} }


}



