package com.so.dingbring

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.so.dingbring.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_image.view.*
import kotlinx.android.synthetic.main.info_news.view.*
import kotlinx.android.synthetic.main.loading_layout.view.*
import kotlinx.android.synthetic.main.loading_layout.view.details

class InfoAdapter(var mContext: Context): RecyclerView.Adapter<InfoAdapter.InfoViewHolder>() {

    private var dataList= mutableListOf<MyData>()

    fun setList(data:MutableList<MyData>){ dataList=data }

    inner class InfoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindView(myData: MyData){
            itemView.details.text = myData.eventName
            itemView.details1.text = myData.eventDate
            itemView.setOnClickListener {

                var bundle = bundleOf( "eventName" to myData.eventName, "eventDate" to myData.eventDate)
                it.findNavController().navigate(R.id.action_homeFragment_detail_fragment, bundle)


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.info_news,parent,false)
        return InfoViewHolder(view)

    }

    override fun getItemCount(): Int {
        return if(dataList.size>0){ dataList.size }
        else { 0 }
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {

        val userDetails: MyData = dataList[position]
        holder.bindView(userDetails)

    }



}