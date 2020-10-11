package com.so.dingbring.view.detail.create

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.so.dingbring.R

class CreateAdapter(
    var mContext: Context,
    var mStatusList: ArrayList<String>,
    var mItemList: ArrayList<String>,
    var mQuantityList: ArrayList<String>
): RecyclerView.Adapter<CreateAdapter.CreateViewHolder>() {


    inner class CreateViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var mDisplayStatus: TextView = itemView.findViewById(R.id.create_status_item)
        var mDisplayItem: TextView = itemView.findViewById(R.id.create_item_item)
        var mDisplayQuantity: TextView = itemView.findViewById(R.id.create_quantity_item)
        var mDisplayNum:TextView = itemView.findViewById(R.id.create_num_item)
        var mDisplayColor : ConstraintLayout = itemView.findViewById(R.id.create_item_all)

    }
    override fun onBindViewHolder(holder: CreateViewHolder, position: Int) {
        holder.mDisplayStatus.text = mStatusList[position]
        holder.mDisplayItem.text = mItemList[position]
        holder.mDisplayQuantity.text = mQuantityList[position]
        holder.mDisplayNum.text = position.plus(1).toString()

        if (mStatusList[position] == "I need")
        {holder.mDisplayColor.setBackgroundColor(mContext.resources.getColor(R.color.Red))}
        else
        {holder.mDisplayColor.setBackgroundColor(mContext.resources.getColor(R.color.Green))}


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.create_item,parent,false)
        return CreateViewHolder(view)

    }

    override fun getItemCount(): Int {
        return if(mItemList.size>0){ mItemList.size }
        else { 0 }
    }





}