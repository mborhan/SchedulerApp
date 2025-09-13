package com.test.schedulerapp.ui.applist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.test.schedulerapp.R
import com.test.schedulerapp.data.model.AppData

class AppItemViewAdapter(private var list: List<AppData>, private val onItemClick: (Int) -> Unit) :
    RecyclerView.Adapter<AppItemViewAdapter.ItemViewHolder>() {

    fun updateData(newList: List<AppData>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.app_item_view, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val item = list[position]
        holder.imageView.setImageDrawable(item.icon)
        holder.textView.text = item.text

        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val textView: TextView = itemView.findViewById(R.id.textView)
    }
}