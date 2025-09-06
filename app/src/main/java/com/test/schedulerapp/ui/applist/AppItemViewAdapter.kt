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

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.app_item_view, parent, false)

        return ItemViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val item = list[position]

        // sets the image to the imageview from our itemHolder class
       // holder.imageView.setImageResource(item.image)
        holder.imageView.setImageDrawable(item.icon)

        // sets the text to the textview from our itemHolder class
        holder.textView.text = item.text

        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }

    // Holds the views for adding it to image and text
    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val textView: TextView = itemView.findViewById(R.id.textView)
    }
}