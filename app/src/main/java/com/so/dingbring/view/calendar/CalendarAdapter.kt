package com.so.dingbring.view.calendar


import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.so.dingbring.R
import com.tejpratapsingh.recyclercalendar.adapter.RecyclerCalendarBaseAdapter
import com.tejpratapsingh.recyclercalendar.model.RecyclerCalendarConfiguration
import com.tejpratapsingh.recyclercalendar.model.RecyclerCalenderViewItem
import com.tejpratapsingh.recyclercalendar.utilities.CalendarUtils
import java.util.*

class CalendarAdapter(
    mCtxt: Context,
    startDate: Date,
    endDate: Date,
    val configuration: RecyclerCalendarConfiguration,
    val mDataPass: MutableList<MutableList<String>>,
    val dateSelectListener: OnDateSelected
) : RecyclerCalendarBaseAdapter(startDate, endDate, configuration) {

    val mContext = mCtxt

    interface OnDateSelected {
        fun onDateSelected(mEvent: MutableList<String>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.calendar_item, parent, false)
        return MonthCalendarViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        calendarItem: RecyclerCalenderViewItem
    ) {
        val monthViewHolder: MonthCalendarViewHolder = holder as MonthCalendarViewHolder
        monthViewHolder.itemView.visibility = View.VISIBLE
        monthViewHolder.viewEvent.visibility = View.GONE

        monthViewHolder.itemView.setOnClickListener(null)

        monthViewHolder.textViewDay.setTextColor(Color.WHITE);
        monthViewHolder.textViewDate.setTextColor(Color.WHITE);

        if (calendarItem.isHeader) {
            val selectedCalendar = Calendar.getInstance(Locale.getDefault())
            selectedCalendar.time = calendarItem.date

            val month: String = CalendarUtils.dateStringFromFormat(
                configuration.calendarLocale,
                selectedCalendar.time,
                CalendarUtils.DISPLAY_MONTH_FORMAT
            ) ?: ""
            val year = selectedCalendar[Calendar.YEAR].toLong()
            monthViewHolder.textViewDay.setPadding(0, 10, 0, 10)
            monthViewHolder.textViewDay.text = year.toString()
            monthViewHolder.textViewDate.setPadding(0, 10, 0, 10)

            monthViewHolder.textViewDate.text = month
        } else if (calendarItem.isEmpty) {
            monthViewHolder.itemView.visibility = View.GONE
            monthViewHolder.textViewDay.text = ""
            monthViewHolder.textViewDate.text = ""

        } else {

            val calendarDate = Calendar.getInstance(Locale.getDefault())
            calendarDate.time = calendarItem.date

            val day: String = CalendarUtils.dateStringFromFormat(
                configuration.calendarLocale,
                calendarDate.time,
                CalendarUtils.DISPLAY_WEEK_DAY_FORMAT
            ) ?: ""

            monthViewHolder.textViewDay.text = day
            val dateInt = (CalendarUtils.dateStringFromFormat(
                configuration.calendarLocale,
                calendarDate.time,
                CalendarUtils.DB_DATE_FORMAT
            ) ?: "0").toString()

            monthViewHolder.textViewDate.text =
                CalendarUtils.dateStringFromFormat(
                    configuration.calendarLocale,
                    calendarDate.time,
                    CalendarUtils.DISPLAY_DATE_FORMAT
                ) ?: ""

            if (mDataPass.isNotEmpty()) {
//                println("----<<<<   2  <<<<data>>>>  2   >>>---" + mDataPass[position][1])
                mDataPass.forEach { data ->

                    val newDate = data[1].split("/")
                    val d = newDate[0]
                    val m = newDate[1]
                    val y = newDate[2]
                    val mCompareDate = y+m+d

                    if (dateInt == mCompareDate) {
                        monthViewHolder.viewEvent.visibility = View.VISIBLE
                       // monthViewHolder.viewEvent.setBackgroundDrawable(mContext.resources.getDrawable(R.drawable.round_small))
                        monthViewHolder.viewEvent.setBackgroundColor(mContext.resources.getColor(R.color.green_300))


                        monthViewHolder.itemView.setOnClickListener { dateSelectListener.onDateSelected(data) }

                    }
                }
            }
        }
    }
}


class MonthCalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textViewDay: TextView = itemView.findViewById(R.id.textCalenderItemVerticalDay)
    val textViewDate: TextView = itemView.findViewById(R.id.textCalenderItemVerticalDate)
    val viewEvent: View = itemView.findViewById(R.id.viewCalenderItemVerticalEvent)

}
