package com.so.dingbring.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.so.dingbring.R
import kotlinx.android.synthetic.main.home_item.view.*

class HomeAdapter(var mContext: Context): RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    private var dataList= mutableListOf<MyEvent>()

    fun setList(data:MutableList<MyEvent>){ dataList=data }

    inner class HomeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindView(myEvent: MyEvent){
            itemView.home_name.text = myEvent.eventName
            itemView.home_date.text = myEvent.eventDate
            itemView.home_address.text = myEvent.eventAdress
            itemView.home_orga.text = myEvent.eventOrga
            itemView.setOnClickListener {
                var bundle = bundleOf(
                    "eventName" to myEvent.eventName,
                    "eventDate" to myEvent.eventDate,
                    "eventAdress" to myEvent.eventAdress,
                    "eventId" to myEvent.eventId,
                    "eventOrga" to myEvent.eventOrga
                )
                it.findNavController().navigate(R.id.action_homeFragment_detail_fragment, bundle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.home_item,parent,false)
        return HomeViewHolder(view)

    }

    override fun getItemCount(): Int {
        return if(dataList.size>0){ dataList.size }
        else { 0 }
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

        val mHome: MyEvent = dataList[position]
        holder.bindView(mHome)

    }



}