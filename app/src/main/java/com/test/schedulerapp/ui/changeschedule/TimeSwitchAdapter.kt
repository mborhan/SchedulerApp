package com.test.schedulerapp.ui.changeschedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.test.schedulerapp.R
import com.test.schedulerapp.data.model.TimeSwitchItem

class TimeSwitchAdapter(
    private var items: List<TimeSwitchItem>,
    private val onSwitchChanged: (TimeSwitchItem, Boolean, Int) -> Unit
) : RecyclerView.Adapter<TimeSwitchAdapter.TimeSwitchViewHolder>() {

    fun updateData(newList: List<TimeSwitchItem>) {
        items = newList
        notifyDataSetChanged()
    }

    inner class TimeSwitchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val switchButton: Switch = itemView.findViewById(R.id.switchButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSwitchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_time_switch, parent, false)
        return TimeSwitchViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeSwitchViewHolder, position: Int) {
        val item = items[position]
        holder.tvTime.text = item.time
        holder.switchButton.isChecked = item.isEnabled

        // Handle switch toggle
        holder.switchButton.setOnCheckedChangeListener { _, isChecked ->
            item.isEnabled = isChecked
            onSwitchChanged(item, isChecked, position)
        }
    }

    override fun getItemCount(): Int = items.size
}
