package com.example.eventapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventapp.R
import com.example.eventapp.ui.DetailActivity
import com.example.eventapp.ui.adapter.EventAdapter
import com.example.eventapp.ui.model.EventNonActiveModel
import com.example.eventapp.ui.model.factory.EventNonActiveModelFactory
import com.example.eventapp.utils.DialogUtil.showNoInternetDialog
import networkCheck


class EventNonActiveFragment : Fragment(), View.OnClickListener {
    private lateinit var eventNonActiveModel: EventNonActiveModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_event_non_active, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventNonActiveModel = EventNonActiveModelFactory.getInstance(requireContext())
            .create(EventNonActiveModel::class.java)

        if (networkCheck(requireContext())) {
            setupRecyclerView(view)
            observeViewModels()
        } else {
            showNoInternetDialog(requireContext(), layoutInflater)
        }
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_event_nonactive)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModels() {
        eventNonActiveModel.listEvent.observe(viewLifecycleOwner, Observer { eventList ->
            val eventAdapter = EventAdapter(eventList) { eventId ->
                // Intent untuk pindah ke DetailActivity sambil membawa eventId
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("EVENT_ID", eventId)
                startActivity(intent)
            }
            view?.findViewById<RecyclerView>(R.id.rv_event_nonactive)?.adapter = eventAdapter
        })

        eventNonActiveModel.isLoading.observe(viewLifecycleOwner, Observer { updateLoadingState() })
    }

    private fun updateLoadingState() {
        val isLoading = eventNonActiveModel.isLoading.value ?: false
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


    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }
}