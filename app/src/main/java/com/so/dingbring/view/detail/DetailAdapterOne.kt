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

class DetailAdapterOne(var mContext: Context, private val itemSelectedListener: ItemSelectedListener)
    : RecyclerView.Adapter<DetailAdapterOne.DetailOneViewHolder>() {

    private var dataList : MutableList<MyItem> = mutableListOf()
    interface ItemSelectedListener { fun onItemSelected(mItemID: String, mItemStatus: String) }

    fun setListDetail(data: MutableList<MyItem>){ dataList = data }

    inner class DetailOneViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bindView(mydetail: MyItem){
            if (mydetail.mItemStatus == "I need")
            { itemView.create_num_item_wait.isVisible = true
                itemView.detail_item_cl.setBackgroundColor(mContext.resources.getColor(R.color.Red))}
            else { itemView.create_num_item_ok.isVisible = true
                itemView.detail_item_cl.setBackgroundColor(mContext.resources.getColor(R.color.Green))}

            itemView.detail_item_quantity.text = mydetail.mItemQty
            itemView.detail_item_type.text = mydetail.mItemType
            itemView.detail_item_personn.text = mydetail.mItemUser
        }
        init{ itemView.setOnClickListener {
            itemSelectedListener.onItemSelected(dataList?.get(adapterPosition)?.mItemId.toString(), dataList?.get(adapterPosition).mItemStatus)
        dataList.clear()} }
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
        val userDetails: MyItem = dataList!![position]
        holder.bindView(userDetails)


    }



}