package com.so.dingbring.view.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.so.dingbring.R
import com.so.dingbring.data.MyItem
import kotlinx.android.synthetic.main.detail_item.view.*

class DetailAdapterTwo(var mContext: Context): RecyclerView.Adapter<DetailAdapterTwo.DetailTwoViewHolder>() {

    private var dataList= mutableListOf<MyItem>()

    fun setListDetail(data:MutableList<MyItem>){ dataList=data }

    inner class DetailTwoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindView(mydetail: MyItem){

            itemView.detail_num_item_ok.isVisible = true
            itemView.detail_item_quantity.text = mydetail.mItemQty
            itemView.detail_item_type.text = mydetail.mItemName
            itemView.detail_item_personn.text = mydetail.mItemUser }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailTwoViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.detail_item,parent,false)
        return DetailTwoViewHolder(view)

    }

    override fun getItemCount(): Int {
        return if(dataList.size>0){ dataList.size }
        else { 0 }
    }

    override fun onBindViewHolder(holder: DetailTwoViewHolder, position: Int) {
        val userDetails: MyItem = dataList[position]
        holder.bindView(userDetails)
    }



}