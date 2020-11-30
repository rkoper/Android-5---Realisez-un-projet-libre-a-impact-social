package com.so.dingbring.view.detail

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.CameraUpdateFactory.zoomIn
import com.so.dingbring.ItemSelectionAnimator
import com.so.dingbring.R
import com.so.dingbring.data.MyItem
import io.reactivex.subjects.BehaviorSubject
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
            mDetailEditCache.visibility = View.GONE
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

               if (mItemStatus== "I need") {
                mDetailBand.visibility = View.VISIBLE
                   mDetailCache.visibility = View.VISIBLE }

               else { mDetailBand.visibility = View.INVISIBLE
                   mDetailCache.visibility = View.INVISIBLE} } }

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
        var mDetailColor: ConstraintLayout = itemView.findViewById(R.id.detail_item_cl)
        var mDetailCache: ImageView = itemView.findViewById(R.id.detail_item_cache)
        var mDetailImageUser: ImageView = itemView.findViewById(R.id.detail_item_photo)
        var mDetailBand: ImageView = itemView.findViewById(R.id.detail_item_band)

        var mDetailEdit: ImageView = itemView.findViewById(R.id.detail_item_global)
        var mDetailEditStatus: ImageView = itemView.findViewById(R.id.detail_item_status)
        var mDetailEditPlus: ImageView = itemView.findViewById(R.id.detail_item_plus)
        var mDetailEditMinus: ImageView = itemView.findViewById(R.id.detail_item_minus)
        var mDetailEditDelete: ImageView = itemView.findViewById(R.id.detail_item_delete)
        var mDetailEditCache: ImageView = itemView.findViewById(R.id.detail_item_cache)






        init {
        mDetailEdit.setOnClickListener {
         if (!animShow) {
             val animation = AnimationUtils.loadAnimation(mContext, R.anim.zoomin)
             mDetailEdit.startAnimation(animation)
             animShow = true}

         else { val animation = AnimationUtils.loadAnimation(mContext, R.anim.zoomout)
             mDetailEdit.startAnimation(animation)
             animShow = false}


            if (mDetailEditMinus.isVisible) { iconInvisible()}
                else { iconVisible()


                     var mChangeHashMap = hashMapOf<Int, ArrayList<String>>()

                     mDetailEditStatus.setOnClickListener {
                         mChangeHashMap.clear()
                         mChangeHashMap[0] = mListMyItem[position]
                         itemClickN.onNext(mChangeHashMap)
                     }

                mDetailEditPlus.setOnClickListener {
                    mChangeHashMap.clear()
                    mChangeHashMap[1] = mListMyItem[position]
                    itemClickN.onNext(mChangeHashMap)
                }
                     mDetailEditMinus.setOnClickListener {
                         itemSelectionAnimator.startSelectAnimation()
                         mChangeHashMap.clear()
                         mChangeHashMap[2] = mListMyItem[position]
                         itemClickN.onNext(mChangeHashMap)
                     }

                     mDetailEditDelete.setOnClickListener {
                         mChangeHashMap.clear()
                         mChangeHashMap[3] = mListMyItem[position]
                         itemClickN.onNext(mChangeHashMap)
                     }


                 }


          //  mDetailEditMinus.visibility = View.GONE
          //  mDetailEditDelete.visibility = View.GONE
           // mDetailEditStatus.visibility = View.GONE



        /*    mDetailAddOne.setOnItemClickListener { buttonIndex ->
                var mChangeHashMap = hashMapOf< Int,MyItem>()
                mChangeHashMap[buttonIndex] = mListMyItem[position]
            itemClickN.onNext(mChangeHashMap) }}

            */
        }




}
        private fun iconVisible() {
            val animation100 = AnimationUtils.loadAnimation(mContext, R.anim.slideleft100)
            val animation200 = AnimationUtils.loadAnimation(mContext, R.anim.slideleft200)
            val animation300 = AnimationUtils.loadAnimation(mContext, R.anim.slideleft300)
            val animation400 = AnimationUtils.loadAnimation(mContext, R.anim.slideleft400)
            mDetailEditMinus.visibility = View.VISIBLE
            mDetailEditPlus.visibility = View.VISIBLE
            mDetailEditDelete.visibility = View.VISIBLE
            mDetailEditStatus.visibility = View.VISIBLE
            mDetailEditCache.visibility = View.VISIBLE
            mDetailEditStatus.startAnimation(animation400)
            mDetailEditPlus.startAnimation(animation300)
            mDetailEditMinus.startAnimation(animation200)
            mDetailEditDelete.startAnimation(animation100)
            mDetailEditCache.startAnimation(animation100)


        }

   private fun zoomIn(mDetailBand: ImageView) {
        val aniSlide = AnimationUtils.loadAnimation(mContext, R.anim.zoomin_main)
        aniSlide.repeatMode = ValueAnimator.INFINITE;
        mDetailBand.startAnimation(aniSlide)
    }


        private fun iconInvisible() {
            val animation100 = AnimationUtils.loadAnimation(mContext, R.anim.slideright100)
            val animation200 = AnimationUtils.loadAnimation(mContext, R.anim.slideright200)
            val animation300 = AnimationUtils.loadAnimation(mContext, R.anim.slideright300)
            val animation400 = AnimationUtils.loadAnimation(mContext, R.anim.slideright400)
            mDetailEditStatus.startAnimation(animation100)
            mDetailEditPlus.startAnimation(animation200)
            mDetailEditMinus.startAnimation(animation300)
            mDetailEditDelete.startAnimation(animation400)
            mDetailEditCache.startAnimation(animation400)
            mDetailEditMinus.visibility = View.GONE
            mDetailEditPlus.visibility = View.GONE
            mDetailEditDelete.visibility = View.GONE
            mDetailEditStatus.visibility = View.GONE
            mDetailEditCache.visibility = View.GONE
        }

        //fun createAnim() {AnimationUtils.loadAnimation(mContext, R.anim.slideright)}
    }


}





