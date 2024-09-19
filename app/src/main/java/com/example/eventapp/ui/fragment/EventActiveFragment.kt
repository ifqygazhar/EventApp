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
import com.example.eventapp.ui.model.EventActiveModel
import com.example.eventapp.ui.model.factory.EventActiveModelFactory
import com.example.eventapp.utils.DialogUtil.showNoInternetDialog
import networkCheck

class EventActiveFragment : Fragment(), View.OnClickListener {

    private lateinit var eventActiveModel: EventActiveModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event_active, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi ViewModel menggunakan factory
        eventActiveModel = EventActiveModelFactory.getInstance(requireContext())
            .create(EventActiveModel::class.java)

        if (networkCheck(requireContext())) {
            // Jika ada koneksi internet, lakukan fetching data
            setupRecyclerView(view)
            observeViewModels()
        } else {
            // Jika tidak ada koneksi, tampilkan AlertDialog
            showNoInternetDialog(requireContext(), layoutInflater)
        }
    }

    private fun setupRecyclerView(view: View) {
        val rvEvent = view.findViewById<RecyclerView>(R.id.rv_event)
        // Setup vertical RecyclerView for "regular" events
        rvEvent.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModels() {
        eventActiveModel.listEvent.observe(viewLifecycleOwner, Observer { eventList ->
            val eventAdapter = EventAdapter(eventList) { eventId ->
                // Intent untuk pindah ke DetailActivity sambil membawa eventId
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("EVENT_ID", eventId)
                startActivity(intent)
            }
            view?.findViewById<RecyclerView>(R.id.rv_event)?.adapter = eventAdapter
        })
        eventActiveModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            updateLoadingState()
        })
    }

    private fun updateLoadingState() {
        val isLoading = eventActiveModel.isLoading.value ?: false
        val progressBar = view?.findViewById<View>(R.id.includeProgressBar)
            ?.findViewById<ProgressBar>(R.id.progressBar)
        val rvEvent = view?.findViewById<RecyclerView>(R.id.rv_event)

        if (isLoading) {
            progressBar?.visibility = View.VISIBLE
            rvEvent?.visibility = View.GONE
        } else {
            progressBar?.visibility = View.GONE
            rvEvent?.visibility = View.VISIBLE
        }
    }

    override fun onClick(p0: View?) {
        // Handle click events here
    }
}
