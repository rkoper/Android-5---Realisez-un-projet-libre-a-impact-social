package com.so.dingbring.view.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.so.dingbring.ExpandableLayout
import com.so.dingbring.R
import com.so.dingbring.Utils
import com.so.dingbring.data.MyEvent
import kotlinx.android.synthetic.main.home_item.view.*

class HomeAdapter(var context: Context, mDataEvent: MutableList<MyEvent>): RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    private var dataList= mDataEvent
    private var mContext = context
    private val expandedPositionSet: HashSet<Int> = HashSet()

    inner class HomeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindView(myEvent: MyEvent){
            itemView.home_name.text = myEvent.mEventName
            itemView.home_date.text = myEvent.mEventDate
            itemView.home_address.text = Utils.formatAdress(myEvent.mEventAdress, true)
            itemView.home_city.text = Utils.formatAdress(myEvent.mEventAdress, false)
            itemView.home_orga.text = myEvent.mEventOrga
            itemView.home_item_round.text = position.plus(1).toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.home_item, parent, false)
        val vh = HomeViewHolder(v)
        context = parent.context
        return vh

    }

    override fun getItemCount(): Int {
        return if(dataList.size>0){ dataList.size }
        else { 0 }
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

        val mHome: MyEvent = dataList[position]
        holder.bindView(mHome)
        holder.itemView.home_item_expand_button.setOnClickListener {
            (object :
                ExpandableLayout.OnExpandListener {
                override fun onExpand(expanded: Boolean) {
                    if (expandedPositionSet.contains(position)) {
                        expandedPositionSet.remove(position)
                    } else {
                        expandedPositionSet.add(position)
                    }
                }
            })
            holder.itemView.expand_layout.setExpand(expandedPositionSet.contains(position))
        }


        holder.itemView.cl2.setOnClickListener {
            println("    Click 1  ")
            var bundle = bundleOf("eventId" to dataList[position].mEventId)
            it.findNavController().navigate(R.id.action_homeFragment_detail_fragment, bundle)
        }


    }



}