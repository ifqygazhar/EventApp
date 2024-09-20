package com.example.eventapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.eventapp.R
import com.example.eventapp.data.local.entity.EventEntity
import com.example.eventapp.utils.LoadImage
import com.google.android.material.button.MaterialButton

class EventAdapter(
    private val onFavoriteClick: (EventEntity) -> Unit,
    private val onItemClick: (Int) -> Unit,
) :
    ListAdapter<EventEntity, EventAdapter.EventViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        val btnFavorite = holder.itemView.findViewById<MaterialButton>(R.id.btnFavorite)

        if (event.isFavorite) {
            btnFavorite.setIconResource(R.drawable.ic_favorite)
        } else {
            btnFavorite.setIconResource(R.drawable.ic_no_favorite)
        }
        btnFavorite.setOnClickListener {
            onFavoriteClick(event)
        }
    }

    class EventViewHolder(
        itemView: View,
        private val onItemClick: (Int) -> Unit,
    ) :
        RecyclerView.ViewHolder(itemView) {
        private val tvName = itemView.findViewById<TextView>(R.id.tvName)
        private val tvOwner = itemView.findViewById<TextView>(R.id.tvOwner)
        private val tvCity = itemView.findViewById<TextView>(R.id.tvCity)
        private val tvCategory = itemView.findViewById<TextView>(R.id.tvCategory)
        private val tvQouta = itemView.findViewById<TextView>(R.id.tvQouta)
        private val tvRegister = itemView.findViewById<TextView>(R.id.tvRegister)
        private val ivEventCover = itemView.findViewById<ImageView>(R.id.ivPicture)

        fun bind(event: EventEntity) {
            tvName.text = event.title
            tvOwner.text = event.owner
            tvCity.text = event.city
            tvCategory.text = event.category
            tvQouta.text = event.quota.toString()
            tvRegister.text = event.register.toString()

            LoadImage.load(itemView.context, ivEventCover, event.mediaCover, R.color.placeholder)

            // Set click listener on itemView and pass the event id
            itemView.setOnClickListener {
                onItemClick(event.id)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<EventEntity> =
            object : DiffUtil.ItemCallback<EventEntity>() {
                override fun areItemsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                    return oldItem.title == newItem.title
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: EventEntity,
                    newItem: EventEntity
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}

