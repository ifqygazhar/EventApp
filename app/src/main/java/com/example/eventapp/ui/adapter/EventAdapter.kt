package com.example.eventapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventapp.R
import com.example.eventapp.data.response.ListEventsItem
import com.example.eventapp.util.LoadImage

class EventAdapter(private var eventList: List<ListEventsItem>) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    // Method to update data in the adapter
    fun setData(newEventList: List<ListEventsItem>) {
        eventList = newEventList
        notifyDataSetChanged()  // Notify RecyclerView that data has changed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.bind(event)
    }

    override fun getItemCount(): Int = eventList.size

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName = itemView.findViewById<TextView>(R.id.tvName)
        private val tvOwner = itemView.findViewById<TextView>(R.id.tvOwner)
        private val tvCity = itemView.findViewById<TextView>(R.id.tvCity)
        private val tvCategory = itemView.findViewById<TextView>(R.id.tvCategory)
        private val tvQouta = itemView.findViewById<TextView>(R.id.tvQouta)
        private val tvRegister = itemView.findViewById<TextView>(R.id.tvRegister)
        private val ivEventCover = itemView.findViewById<ImageView>(R.id.ivPicture)

        fun bind(event: ListEventsItem) {
            tvName.text = event.name
            tvOwner.text = event.ownerName
            tvCity.text = event.cityName
            tvCategory.text = event.category
            tvQouta.text = event.quota.toString()
            tvRegister.text = event.registrants.toString()

            LoadImage.load(itemView.context, ivEventCover, event.mediaCover, R.color.placeholder)
        }
    }
}
