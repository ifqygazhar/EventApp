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
import com.example.eventapp.ui.model.EventActiveModel
import com.example.eventapp.ui.model.factory.EventActiveModelFactory
import com.example.eventapp.utils.DialogUtil.showNoInternetDialog
import kotlinx.coroutines.launch
import networkCheck

class EventActiveFragment : Fragment() {

    private val eventActiveModel: EventActiveModel by viewModels {
        EventActiveModelFactory.getInstance(
            requireActivity()
        )
    }
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event_active, container, false)
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
        eventActiveModel.listEvent.observe(viewLifecycleOwner, Observer { eventList ->
            eventAdapter.submitList(eventList)
        })
        eventActiveModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            updateLoadingState(isLoading)
        })
    }

    private fun updateLoadingState(isLoading: Boolean) {
        val progressBarLayout = view?.findViewById<View>(R.id.includeProgressBar)
        val progressBar = progressBarLayout?.findViewById<ProgressBar>(R.id.progressBar)
        val rvEvent = view?.findViewById<RecyclerView>(R.id.rv_event)

        if (isLoading) {
            progressBar?.visibility = View.VISIBLE
            rvEvent?.visibility = View.GONE
        } else {
            progressBar?.visibility = View.GONE
            rvEvent?.visibility = View.VISIBLE
        }
    }


    private suspend fun toggleFavorite(event: EventEntity) {
        if (event.isFavorite) {
            eventActiveModel.deleteEvent(event)
        } else {
            eventActiveModel.saveEvent(event)
        }
    }

    private fun navigateToDetail(eventId: Int) {
        val intent = Intent(requireContext(), DetailActivity::class.java).apply {
            putExtra("EVENT_ID", eventId)
        }
        startActivity(intent)
    }
}
