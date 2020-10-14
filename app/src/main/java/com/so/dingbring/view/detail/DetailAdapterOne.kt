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
    interface ItemSelectedListener { fun onItemSelected(mItemID: String, mItemStatus: String, mCase:Int) }

    fun setListDetail(data: MutableList<MyItem>){ dataList = data }

    inner class DetailOneViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bindView(mydetail: MyItem){
            if (mydetail.mItemStatus == "I need")
            { itemView.detail_num_item_wait.isVisible = true
                itemView.detail_item_cl.setBackgroundColor(mContext.resources.getColor(R.color.Red))}
            else { itemView.detail_num_item_ok.isVisible = true
                itemView.detail_item_cl.setBackgroundColor(mContext.resources.getColor(R.color.Green))}

            itemView.detail_item_quantity.text = mydetail.mItemQty
            itemView.detail_item_type.text = mydetail.mItemName
            itemView.detail_item_personn.text = mydetail.mItemUser
        }
        init{ itemView.detail_num_item_wait.setOnClickListener {
            itemSelectedListener.onItemSelected(dataList[adapterPosition].mItemId,  dataList[adapterPosition].mItemStatus, 1)
            dataList.clear()}

            itemView.detail_num_item_ok.setOnClickListener {
                itemSelectedListener.onItemSelected(dataList[adapterPosition].mItemId.toString(), dataList[adapterPosition].mItemStatus, 2)
                dataList.clear()}

            itemView.detail_item_add_one.setOnClickListener {
                itemSelectedListener.onItemSelected   (dataList[adapterPosition].mItemId.toString(), dataList[adapterPosition].mItemQty, 3)
                dataList.clear()}
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
        val userDetails: MyItem = dataList!![position]
        holder.bindView(userDetails)


    }



}