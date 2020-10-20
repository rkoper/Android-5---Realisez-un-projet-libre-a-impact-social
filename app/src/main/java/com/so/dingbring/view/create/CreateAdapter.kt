package com.so.dingbring.view.detail.create

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.so.dingbring.R
import com.so.dingbring.data.MyItem

class CreateAdapter(
    var mContext: Context,
    var mListMyItem: ArrayList<MyItem>
): RecyclerView.Adapter<CreateAdapter.CreateViewHolder>() {


    inner class CreateViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var mDisplayStatus: TextView = itemView.findViewById(R.id.create_status_item)
        var mDisplayItem: TextView = itemView.findViewById(R.id.create_item_item)
        var mDisplayQuantity: TextView = itemView.findViewById(R.id.create_quantity_item)
        var mDisplayNum:TextView = itemView.findViewById(R.id.create_num_item)
        var mDisplayColor : ConstraintLayout = itemView.findViewById(R.id.create_item_all)

    }
    override fun onBindViewHolder(holder: CreateViewHolder, position: Int) {
        holder.mDisplayStatus.text = mListMyItem[position].mItemStatus
        holder.mDisplayItem.text = mListMyItem[position].mItemName
        holder.mDisplayQuantity.text = mListMyItem[position].mItemQty
        holder.mDisplayNum.text = position.plus(1).toString()

        if (mListMyItem[position].mItemStatus == "I need")
        {holder.mDisplayColor.setBackgroundColor(mContext.resources.getColor(R.color.blue_50))}
        else
        {holder.mDisplayColor.setBackgroundColor(mContext.resources.getColor(R.color.blue_100))}


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.create_item,parent,false)
        return CreateViewHolder(view)

    }

    override fun getItemCount(): Int {
        return if(mListMyItem.size>0){ mListMyItem.size }
        else { 0 }
    }





}