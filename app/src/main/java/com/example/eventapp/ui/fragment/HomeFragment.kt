package com.example.eventapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventapp.R
import com.example.eventapp.data.local.entity.EventEntity
import com.example.eventapp.ui.DetailActivity
import com.example.eventapp.ui.adapter.EventAdapter
import com.example.eventapp.ui.adapter.EventSmallAdapter
import com.example.eventapp.ui.model.EventActiveAndNonActiveModel
import com.example.eventapp.ui.model.factory.EventActiveAndNonActiveModelFactory
import com.example.eventapp.utils.DialogUtil.showNoInternetDialog
import kotlinx.coroutines.launch
import networkCheck


class HomeFragment : Fragment() {

    private val eventActiveAndNonActiveModel: EventActiveAndNonActiveModel by viewModels {
        EventActiveAndNonActiveModelFactory.getInstance(requireActivity())
    }

    private lateinit var eventSoonAdapter: EventSmallAdapter
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check internet connection
        if (networkCheck(requireContext())) {
            setupRecyclerViews(view)
            observeViewModels()
        } else {
            showNoInternetDialog(requireContext(), layoutInflater)
            view.findViewById<TextView>(R.id.tv_upcoming_event).visibility = View.GONE
            view.findViewById<TextView>(R.id.tv_finish_event).visibility = View.GONE
        }
    }

    private fun setupRecyclerViews(view: View) {
        val rvEventSoon = view.findViewById<RecyclerView>(R.id.rv_event_soon)
        rvEventSoon.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        eventSoonAdapter = EventSmallAdapter(
            onFavoriteClick = { event ->
                viewLifecycleOwner.lifecycleScope.launch {
                    toggleFavorite(event)
                }
            },
            onItemClick = { eventId -> navigateToDetail(eventId) }
        )
        rvEventSoon.adapter = eventSoonAdapter

        val rvEvent = view.findViewById<RecyclerView>(R.id.rv_event)
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

    private fun observeViewModels() {

        // Observe active events (upcoming)
        eventActiveAndNonActiveModel.listEventActive.observe(
            viewLifecycleOwner,
            Observer { eventList ->
                eventSoonAdapter.submitList(eventList.take(5))
                Log.d("LISTACTIVE", eventList.take(5).toString())// Menggunakan eventSoonAdapter
            })

        // Observe non-active events (finished)
        eventActiveAndNonActiveModel.listEventNonActive.observe(
            viewLifecycleOwner,
            Observer { eventList ->
                eventAdapter.submitList(eventList)  // Menggunakan eventAdapter
                Log.d("LISTNONACTIVE", eventList.take(5).toString())
            })

        // Observe loading states from the model
        eventActiveAndNonActiveModel.isLoading.observe(
            viewLifecycleOwner,
            Observer { updateLoadingState() })
    }

    private fun updateLoadingState() {
        val isLoading = eventActiveAndNonActiveModel.isLoading.value ?: false

        val progressBar = view?.findViewById<View>(R.id.includeProgressBar)
            ?.findViewById<ProgressBar>(R.id.progressBar)
        val rvEvent = view?.findViewById<RecyclerView>(R.id.rv_event)
        val rvEventSoon = view?.findViewById<RecyclerView>(R.id.rv_event_soon)
        val tvUpcomingEvent = view?.findViewById<TextView>(R.id.tv_upcoming_event)
        val tvFinishEvent = view?.findViewById<TextView>(R.id.tv_finish_event)

        // Show loading if model is loading
        if (isLoading) {
            progressBar?.visibility = View.VISIBLE
            rvEvent?.visibility = View.GONE
            rvEventSoon?.visibility = View.GONE
            tvUpcomingEvent?.visibility = View.GONE
            tvFinishEvent?.visibility = View.GONE
        } else {
            progressBar?.visibility = View.GONE
            rvEvent?.visibility = View.VISIBLE
            rvEventSoon?.visibility = View.VISIBLE
            tvUpcomingEvent?.visibility = View.VISIBLE
            tvFinishEvent?.visibility = View.VISIBLE
        }
    }

    private suspend fun toggleFavorite(event: EventEntity) {
        if (event.isFavorite) {
            eventActiveAndNonActiveModel.deleteEvent(event)
        } else {
            eventActiveAndNonActiveModel.saveEvent(event)
        }
    }

    private fun navigateToDetail(eventId: Int) {
        val intent = Intent(requireContext(), DetailActivity::class.java).apply {
            putExtra("EVENT_ID", eventId)
        }
        startActivity(intent)
    }
}
