package com.example.eventapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventapp.R
import com.example.eventapp.data.remote.response.ListEventsItem
import com.example.eventapp.utils.LoadImage

class EventSmallAdapter(
    private var eventList: List<ListEventsItem>,
    private val onItemClick: (Int) -> Unit
) :
    RecyclerView.Adapter<EventSmallAdapter.EventSmallViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventSmallViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_event_small, parent, false)
        return EventSmallViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: EventSmallViewHolder, position: Int) {
        val event = eventList[position]
        holder.bind(event)
    }

    override fun getItemCount(): Int = eventList.size

    class EventSmallViewHolder(itemView: View, private val onItemClick: (Int) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val tvName = itemView.findViewById<TextView>(R.id.tvName)
        private val tvOwner = itemView.findViewById<TextView>(R.id.tvOwner)
        private val tvCity = itemView.findViewById<TextView>(R.id.tvCity)
        private val ivEventCover = itemView.findViewById<ImageView>(R.id.ivPicture)

        fun bind(event: ListEventsItem) {
            tvName.text = event.name
            tvOwner.text = event.ownerName
            tvCity.text = event.cityName

            LoadImage.load(itemView.context, ivEventCover, event.mediaCover, R.color.placeholder)

            // Set click listener on itemView and pass the event id
            itemView.setOnClickListener {
                onItemClick(event.id)
            }
        }
    }
}
