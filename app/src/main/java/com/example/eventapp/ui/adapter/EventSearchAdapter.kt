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
import com.google.android.material.button.MaterialButton

class EventSearchAdapter(
    private var eventList: List<ListEventsItem>,
    private val onItemClick: (Int) -> Unit
) :
    RecyclerView.Adapter<EventSearchAdapter.EventViewHolder>() {

    fun setData(newEventList: List<ListEventsItem>) {
        eventList = newEventList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.bind(event)
        val btnFavorite = holder.itemView.findViewById<MaterialButton>(R.id.btnFavorite)

        btnFavorite.visibility = View.GONE
    }

    override fun getItemCount(): Int = eventList.size

    class EventViewHolder(itemView: View, private val onItemClick: (Int) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
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

            // Set click listener on itemView and pass the event id
            itemView.setOnClickListener {
                onItemClick(event.id)
            }
        }
    }
}
