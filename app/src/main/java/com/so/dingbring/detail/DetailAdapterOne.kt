package com.so.dingbring.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.so.dingbring.R
import kotlinx.android.synthetic.main.detail_item.view.*

class DetailAdapterOne(var mContext: Context): RecyclerView.Adapter<DetailAdapterOne.DetailOneViewHolder>() {

    private var dataList= mutableListOf<MyDetail>()

    fun setListDetail(data:MutableList<MyDetail>){ dataList=data }

    inner class DetailOneViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindView(mydetail: MyDetail){
                itemView.create_num_item_wait.isVisible = true
                itemView.detail_item_quantity.text = mydetail.mItemQty
                itemView.detail_item_type.text = mydetail.mItemType
                itemView.detail_item_personn.text = mydetail.mItemUser
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailOneViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.detail_item,parent,false)
        return DetailOneViewHolder(view)

    }

    override fun getItemCount(): Int {
        return if(dataList.size>0){ dataList.size }
        else { 0 }
    }

    override fun onBindViewHolder(holder: DetailOneViewHolder, position: Int) {
        val userDetails: MyDetail = dataList[position]
        holder.bindView(userDetails)
    }



}