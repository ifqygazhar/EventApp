package com.example.eventapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventapp.R
import com.example.eventapp.data.local.entity.EventEntity
import com.example.eventapp.ui.DetailActivity
import com.example.eventapp.ui.adapter.EventAdapter
import com.example.eventapp.ui.model.EventFavoriteModel
import com.example.eventapp.ui.model.factory.EventFavoriteModelFactory
import kotlinx.coroutines.launch


class FavoriteFragment : Fragment() {
    private val eventFavoriteModel: EventFavoriteModel by viewModels {
        EventFavoriteModelFactory.getInstance(
            requireActivity()
        )
    }

    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)
        viewLifecycleOwner.lifecycleScope.launch {
            observeViewModels()
        }
    }

    private fun setupRecyclerView(view: View) {
        val rvEvent = view.findViewById<RecyclerView>(R.id.rv_favorite)
        rvEvent.layoutManager = LinearLayoutManager(requireContext())
        eventAdapter = EventAdapter(
            onFavoriteClick = { event ->
                viewLifecycleOwner.lifecycleScope.launch {
                    toggleFavorite(event)
                }
            },
            onItemClick = { eventId -> navigateToDetail(eventId) }
        )
        rvEvent.adapter = eventAdapter
    }

    private suspend fun observeViewModels() {
        val emptyView = view?.findViewById<View>(R.id.imgEmpty)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.rv_event_nonactive)
        eventFavoriteModel.getEventFavorite().observe(viewLifecycleOwner) { eventList ->
            if (eventList.isEmpty()) {
                emptyView?.visibility = View.VISIBLE
                recyclerView?.visibility = View.GONE
                eventAdapter.submitList(emptyList())
            } else {
                emptyView?.visibility = View.GONE
                recyclerView?.visibility = View.VISIBLE
                eventAdapter.submitList(eventList)
            }
        }

        eventFavoriteModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            updateLoadingState(isLoading)
        }
    }

    private fun updateLoadingState(isLoading: Boolean) {

        val progressBar = view?.findViewById<View>(R.id.includeProgressBar)
            ?.findViewById<ProgressBar>(R.id.progressBar)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.rv_event_nonactive)

        if (isLoading) {
            progressBar?.visibility = View.VISIBLE
            recyclerView?.visibility = View.GONE
        } else {
            progressBar?.visibility = View.GONE
            recyclerView?.visibility = View.VISIBLE
        }
    }


    private suspend fun toggleFavorite(event: EventEntity) {
        if (event.isFavorite) {
            eventFavoriteModel.deleteEvent(event)
        } else {
            eventFavoriteModel.saveEvent(event)
        }
    }

    private fun navigateToDetail(eventId: Int) {
        val intent = Intent(requireContext(), DetailActivity::class.java).apply {
            putExtra("EVENT_ID", eventId)
        }
        startActivity(intent)
    }
}