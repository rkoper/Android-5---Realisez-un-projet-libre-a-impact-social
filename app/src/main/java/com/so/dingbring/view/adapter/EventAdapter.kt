package com.so.dingbring.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.so.dingbring.R
import io.reactivex.subjects.BehaviorSubject

class EventAdapter(
    var context: Context,
   var mDataEvent: MutableList<MutableList<String>>
): RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
    val itemClick: BehaviorSubject<String> = BehaviorSubject.create()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventViewHolder { val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return EventViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (mDataEvent.size > 0) { mDataEvent.size } else { 0 } }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.mDisEventAddress.text = mDataEvent[position][0]
        holder.mDisEventDate.text = mDataEvent[position][1]
        holder.mDisEventName.text = mDataEvent[position][3]
        holder.mDisEventHour.text = mDataEvent[position][7]
        holder.mDisEventUser.text = mDataEvent[position][5]
        holder.mDetailItem.setOnClickListener { itemClick.onNext(mDataEvent[position][2]) }
        Glide.with(holder.itemView.context).load(mDataEvent[position][6]).apply(RequestOptions.circleCropTransform()).into(holder.mDisEventPhoto) }

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mDisEventAddress: TextView = itemView.findViewById(R.id.home_address)
        var mDisEventDate: TextView = itemView.findViewById(R.id.home_date)
        var mDisEventName: TextView = itemView.findViewById(R.id.home_name)
        var mDisEventUser: TextView = itemView.findViewById(R.id.home_pers)
        var mDisEventHour: TextView = itemView.findViewById(R.id.home_hour)
        var mDisEventPhoto: ImageView = itemView.findViewById(R.id.home_item_img)
        var mDetailItem: ConstraintLayout = itemView.findViewById(R.id.item_event) }
}