package com.example.eventapp.ui.fragment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventapp.R
import com.example.eventapp.ui.DetailActivity
import com.example.eventapp.ui.adapter.EventAdapter
import com.example.eventapp.ui.adapter.EventSmallAdapter
import com.example.eventapp.ui.model.EventActiveModel
import com.example.eventapp.ui.model.EventNonActiveModel
import com.example.eventapp.util.DialogUtil.showNoInternetDialog
import networkCheck

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

        // Cek apakah ada koneksi internet
        if (networkCheck(requireContext())) {
            // Jika ada koneksi internet, lakukan fetching data
            setupRecyclerViews(view)
            observeViewModels()
        } else {
            // Jika tidak ada koneksi, tampilkan AlertDialog
            val tvUpcomingEvent = view.findViewById<TextView>(R.id.tv_upcoming_event)
            val tvFinishEvent = view.findViewById<TextView>(R.id.tv_finish_event)
            tvUpcomingEvent.visibility = View.GONE
            tvFinishEvent.visibility = View.GONE
            showNoInternetDialog(requireContext(), layoutInflater)
        }
    }


    private fun setupRecyclerViews(view: View) {
        val rvEventSoon = view.findViewById<RecyclerView>(R.id.rv_event_soon)
        val rvEvent = view.findViewById<RecyclerView>(R.id.rv_event)

        // Setup horizontal RecyclerView for "soon" events
        rvEventSoon.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Setup vertical RecyclerView for "regular" events
        rvEvent.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModels() {
        // Observe active events (upcoming)
        eventActiveModel.listEvent.observe(viewLifecycleOwner, Observer { eventList ->
            val eventSoonList = eventList.take(5)
            val eventSoonAdapter = EventSmallAdapter(eventSoonList)
            view?.findViewById<RecyclerView>(R.id.rv_event_soon)?.adapter = eventSoonAdapter
        })

        // Observe non-active events (finished)
        eventNonActiveModel.listEvent.observe(viewLifecycleOwner, Observer { eventList ->
            val eventAdapter = EventAdapter(eventList) { eventId ->
                // Intent untuk pindah ke DetailActivity sambil membawa eventId
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("EVENT_ID", eventId)
                startActivity(intent)
            }
            view?.findViewById<RecyclerView>(R.id.rv_event)?.adapter = eventAdapter
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
        val tvUpcomingEvent = view?.findViewById<TextView>(R.id.tv_upcoming_event)
        val tvFinishEvent = view?.findViewById<TextView>(R.id.tv_finish_event)

        // Show loading if either of the ViewModel is loading
        if (isLoadingActive || isLoadingNonActive) {
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

    override fun onClick(p0: View?) {
        // Handle click events here
    }
}