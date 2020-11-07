package com.so.dingbring.view.event

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.so.dingbring.R
import com.so.dingbring.data.MyEvent
import io.reactivex.subjects.BehaviorSubject

class EventAdapter(
    var context: Context,
    mDataEvent: MutableList<MyEvent>
): RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
    private var mDataEvent = mDataEvent
    private var mContext = context

    val itemClick: BehaviorSubject<MyEvent> = BehaviorSubject.create()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return EventViewHolder(view)
    }


    override fun getItemCount(): Int {
        return if (mDataEvent.size > 0) {
            mDataEvent.size
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.mDisEventAddress.text = mDataEvent[position].mEventAdress
        holder.mDisEventDate.text = mDataEvent[position].mEventDate
        holder.mDisEventName.text = mDataEvent[position].mEventName
        holder.mDisEventUser.text = mDataEvent[position].mEventUserName
        Glide.with(holder.itemView.context)
            .load(mDataEvent[position].mEventUserPhoto).centerCrop().thumbnail(1f).into(holder.mDisEventPhoto);

        holder.mDetailItem.setOnClickListener {
           println("click----------" + mDataEvent[position].mEventName)
            itemClick.onNext(mDataEvent[position])
        }


        //  holder.mDisplayQuantity.text = mListMyItem[position].mItemQty
    }



    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mDisEventAddress: TextView = itemView.findViewById(R.id.home_address)
        var mDisEventCity: TextView = itemView.findViewById(R.id.home_city)
        var mDisEventDate: TextView = itemView.findViewById(R.id.home_date)
        var mDisEventName: TextView = itemView.findViewById(R.id.home_name)
        var mDisEventUser: TextView = itemView.findViewById(R.id.home_pers)
        var mDisEventPhoto: ImageView = itemView.findViewById(R.id.home_item_img)
        var mDetailItem: ConstraintLayout = itemView.findViewById(R.id.event_rv)


        // var mDisEvent: TextView = itemView.findViewById(R.id.home_name)
        //  var mDisplayColor : ConstraintLayout = itemView.findViewById(R.id.create_item_all)




}
}