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
import com.example.eventapp.ui.model.EventNonActiveModel


class EventNonActiveFragment : Fragment(), View.OnClickListener {
    private val eventNonActiveModel: EventNonActiveModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_event_non_active, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_event_nonactive)
        val progressBar = view.findViewById<View>(R.id.includeProgressBar)
            .findViewById<ProgressBar>(R.id.progressBar)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        eventNonActiveModel.listEvent.observe(viewLifecycleOwner, Observer { eventList ->
            val adapter = EventAdapter(eventList)
            recyclerView.adapter = adapter
        })

        eventNonActiveModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        })
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }
}