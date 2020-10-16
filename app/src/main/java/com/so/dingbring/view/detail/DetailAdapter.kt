package com.so.dingbring.view.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.so.dingbring.R
import com.so.dingbring.data.MyItem
import io.reactivex.subjects.BehaviorSubject

class DetailAdapter(
    var mContext: Context,
    var mListMyItem: ArrayList<MyItem>
) : RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {

    val itemClickEmpty: BehaviorSubject<MyItem> = BehaviorSubject.create()
    val itemClickFull: BehaviorSubject<MyItem> = BehaviorSubject.create()
    val itemClickN: BehaviorSubject<MyItem> = BehaviorSubject.create()

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.mDetailItem.text = mListMyItem[position].mItemName
        holder.mDetailQuantity.text = mListMyItem[position].mItemQty
        holder.mDetailUser.text = mListMyItem[position].mItemUser


        if (mListMyItem[position].mItemStatus == "I need") {
            holder.mDetailEmptyButton.visibility = View.VISIBLE
            holder.mDetailColor.setBackgroundColor(mContext.resources.getColor(R.color.Red))
        } else {
            holder.mDetailFullButton.visibility = View.VISIBLE
            holder.mDetailColor.setBackgroundColor(mContext.resources.getColor(R.color.Green))
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
        var mDetailEmptyButton: ImageView = itemView.findViewById(R.id.detail_num_item_wait)
        var mDetailFullButton: ImageView = itemView.findViewById(R.id.detail_num_item_ok)
        var mDetailColor: ConstraintLayout = itemView.findViewById(R.id.detail_item_cl)
        var mDetailUser: TextView = itemView.findViewById(R.id.detail_item_personn)
        var mDetailAddOne: ImageView = itemView.findViewById(R.id.detail_item_add_one)


        init {
            mDetailEmptyButton.setOnClickListener {
                itemClickEmpty.onNext(mListMyItem[position])
               //    mDetailFullButton.visibility = View.VISIBLE
               //  mDetailEmptyButton.visibility = View.INVISIBLE
               //  mDetailColor.setBackgroundColor(mContext.resources.getColor(R.color.Green))
            }

            mDetailFullButton.setOnClickListener {
                itemClickFull.onNext(mListMyItem[position])
              //  mDetailEmptyButton.visibility = View.VISIBLE
              //  mDetailFullButton.visibility = View.INVISIBLE
               // mDetailColor.setBackgroundColor(mContext.resources.getColor(R.color.Red))
            }

            mDetailAddOne.setOnClickListener {
                itemClickN.onNext(mListMyItem[position])
                // mDetailQuantity.text = mListMyItem[position].mItemQty.toInt().plus(1).toString()

            }

        }
    }
}



