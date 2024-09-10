package com.example.eventapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventapp.R
import com.example.eventapp.ui.adapter.EventAdapter
import com.example.eventapp.ui.model.EventActiveModel

class EventActiveFragment : Fragment(), View.OnClickListener {

    private val eventActiveModel: EventActiveModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event_active, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_event)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observe the LiveData from ViewModel
        eventActiveModel.listEvent.observe(viewLifecycleOwner, Observer { eventList ->
            val adapter = EventAdapter(eventList)
            recyclerView.adapter = adapter
        })

        // Loading indicator if necessary
        eventActiveModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            // Handle loading state here (e.g., show/hide progress bar)
        })
    }

    override fun onClick(p0: View?) {
        // Handle click events here
    }
}
