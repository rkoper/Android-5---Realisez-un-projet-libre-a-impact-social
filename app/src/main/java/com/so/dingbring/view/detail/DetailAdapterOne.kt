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

class DetailAdapterOne(var mContext: Context,
                       private val itemSelectedListener: ItemSelectedListener)
    : RecyclerView.Adapter<DetailAdapterOne.DetailOneViewHolder>() {

    private var mStatusLst : MutableList<String> = mutableListOf()
    private var mQuantityLst : MutableList<String> = mutableListOf()
    private var mNameItemLst : MutableList<String> = mutableListOf()
    private var mUserLst : MutableList<String> = mutableListOf()
    private var mItemIDLst : MutableList<String> = mutableListOf()
    private var mStat : String = ""
    private var mQty : String = ""
    private var mNameIt : String = ""
    private var mUs : String = ""
    private var mItID : String = ""
    interface ItemSelectedListener { fun onItemSelected(mItemID: String, mItemStatus: String, mCase:Int) }

    fun setListDetail   (
        mStatusList : ArrayList<String>,
         mNameItemList  : ArrayList<String>,
        mQuantityList  : ArrayList<String>,
        mUserList :  ArrayList<String>,
        mItemIDList :  ArrayList<String> )
    {
        mStatusLst = mStatusList
        mQuantityLst = mQuantityList
        mNameItemLst =  mNameItemList 
        mUserLst = mUserList
        mItemIDLst =  mItemIDList}


    inner class DetailOneViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bindView() {
            if (mStatusLst[position] == "I need") {
                itemView.detail_num_item_wait.isVisible = true
                itemView.detail_item_cl.setBackgroundColor(mContext.resources.getColor(R.color.Red))
            } else {
                itemView.detail_num_item_ok.isVisible = true
                itemView.detail_item_cl.setBackgroundColor(mContext.resources.getColor(R.color.Green))
            }

            itemView.detail_item_quantity.text = mQuantityLst[position]
            itemView.detail_item_type.text = mNameItemLst[position]
            itemView.detail_item_personn.text = mUserLst[position]
        }

        init {
            itemView.detail_num_item_wait.setOnClickListener {
                println("---| click 1 |---" )
                mStatusLst[adapterPosition] = "I bring"
                itemView.detail_num_item_ok.isVisible = true
                itemView.detail_item_cl.setBackgroundColor(mContext.resources.getColor(R.color.Green))
                itemSelectedListener.onItemSelected(
                    mItemIDLst[adapterPosition],
                    mStatusLst[adapterPosition],
                    1
                )
            }

            itemView.detail_num_item_ok.setOnClickListener {
                println("---| click 2 |---" )
                mStatusLst[adapterPosition] = "I need"
                itemSelectedListener.onItemSelected(
                    mItemIDLst[adapterPosition],
                    mStatusLst[adapterPosition],
                    2
                )
            }

            itemView.detail_item_add_one.setOnClickListener {
                println("---| click 3 |---" )
                itemSelectedListener.onItemSelected(
                    mItemIDLst[adapterPosition],
                    mQuantityLst[adapterPosition],
                    3
                )
                mQuantityLst[adapterPosition]  =  mQuantityLst[adapterPosition].toInt().plus(1).toString()
            }
        }
    }

    fun clearAndReload(){
        mStatusLst.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailOneViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.detail_item,parent,false)
        return DetailOneViewHolder(view)

    }

    override fun getItemCount(): Int {
        return if(mNameItemLst.size>0){ mNameItemLst.size }
        else { 0 }
    }

    override fun onBindViewHolder(holder: DetailOneViewHolder, position: Int) {
       // val userDetails: MyItem = mUserLst!![position]
        holder.bindView()


    }



}