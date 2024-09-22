package com.example.eventapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
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
import com.example.eventapp.ui.model.EventNonActiveModel
import com.example.eventapp.ui.model.factory.EventNonActiveModelFactory
import com.example.eventapp.utils.DialogUtil.showNoInternetDialog
import kotlinx.coroutines.launch
import networkCheck


class EventNonActiveFragment : Fragment() {
    private val eventNonActiveModel: EventNonActiveModel by viewModels {
        EventNonActiveModelFactory.getInstance(
            requireActivity()
        )
    }

    private lateinit var eventAdapter: EventAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_event_non_active, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (networkCheck(requireContext())) {
            setupRecyclerView(view)
            observeViewModels()
        } else {
            showNoInternetDialog(requireContext(), layoutInflater)
        }
    }

    private fun setupRecyclerView(view: View) {
        val rvEvent = view.findViewById<RecyclerView>(R.id.rv_event_nonactive)
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
        eventNonActiveModel.listEvent.observe(viewLifecycleOwner, Observer { eventList ->
            eventAdapter.submitList(eventList)
        })
        eventNonActiveModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            updateLoadingState(isLoading)
        })
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
            eventNonActiveModel.deleteEvent(event)
        } else {
            eventNonActiveModel.saveEvent(event)
        }
    }

    private fun navigateToDetail(eventId: Int) {
        val intent = Intent(requireContext(), DetailActivity::class.java).apply {
            putExtra("EVENT_ID", eventId)
        }
        startActivity(intent)
    }
}