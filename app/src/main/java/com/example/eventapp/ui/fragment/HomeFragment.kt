package com.example.eventapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventapp.R
import com.example.eventapp.ui.adapter.EventAdapter
import com.example.eventapp.ui.adapter.EventSmallAdapter
import com.example.eventapp.ui.model.EventActiveModel
import com.example.eventapp.ui.model.EventNonActiveModel

class HomeFragment : Fragment(), View.OnClickListener {

    private val eventActiveModel: EventActiveModel by viewModels()
    private val eventNonActiveModel: EventNonActiveModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvEventSoon = view.findViewById<RecyclerView>(R.id.rv_event_soon)
        val rvEvent = view.findViewById<RecyclerView>(R.id.rv_event)
        val progressBar = view.findViewById<View>(R.id.includeProgressBar)
            .findViewById<ProgressBar>(R.id.progressBar)

        // Setup horizontal RecyclerView for "soon" events
        rvEventSoon.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Setup vertical RecyclerView for "regular" events
        rvEvent.layoutManager = LinearLayoutManager(requireContext())

        // Observe active events (upcoming)
        eventActiveModel.listEvent.observe(viewLifecycleOwner, Observer { eventList ->
            val eventSoonList = eventList.take(5)
            val eventSoonAdapter = EventSmallAdapter(eventSoonList)
            rvEventSoon.adapter = eventSoonAdapter
        })

        // Observe non-active events (finished)
        eventNonActiveModel.listEvent.observe(viewLifecycleOwner, Observer { eventList ->
            val eventAdapter = EventAdapter(eventList)
            rvEvent.adapter = eventAdapter
        })

        // Observe loading states from both models
        eventActiveModel.isLoading.observe(viewLifecycleOwner, Observer { updateLoadingState() })
        eventNonActiveModel.isLoading.observe(viewLifecycleOwner, Observer { updateLoadingState() })
    }

    private fun updateLoadingState() {
        val isLoadingActive = eventActiveModel.isLoading.value ?: false
        val isLoadingNonActive = eventNonActiveModel.isLoading.value ?: false

        val progressBar = view?.findViewById<View>(R.id.includeProgressBar)
            ?.findViewById<ProgressBar>(R.id.progressBar)
        val rvEvent = view?.findViewById<RecyclerView>(R.id.rv_event)
        val rvEventSoon = view?.findViewById<RecyclerView>(R.id.rv_event_soon)

        // Show loading if either of the ViewModel is loading
        if (isLoadingActive || isLoadingNonActive) {
            progressBar?.visibility = View.VISIBLE
            rvEvent?.visibility = View.GONE
            rvEventSoon?.visibility = View.GONE
        } else {
            progressBar?.visibility = View.GONE
            rvEvent?.visibility = View.VISIBLE
            rvEventSoon?.visibility = View.VISIBLE
        }
    }

    override fun onClick(p0: View?) {
        // Handle click events here
    }
}

