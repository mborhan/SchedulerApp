package com.test.schedulerapp.ui.updateschedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.test.schedulerapp.R
import com.test.schedulerapp.data.model.ScheduleItem

class UpdateScheduleAdapter(
    private var items: List<ScheduleItem>,
    private val onEditClick: (position: Int) -> Unit
) : RecyclerView.Adapter<UpdateScheduleAdapter.ScheduleViewHolder>() {

    fun updateData(newList: List<ScheduleItem>) {
        items = newList
        notifyDataSetChanged()
    }

    inner class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTime: TextView = itemView.findViewById(R.id.textTime)
        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.update_schedule_item, parent, false)
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val item = items[position]
        holder.textTime.text = "hour: ${item.time}"

        holder.btnEdit.setOnClickListener {
            onEditClick(position)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateTime(position: Int, newTime: String) {
        items[position].time = newTime
        notifyItemChanged(position)
    }
}
