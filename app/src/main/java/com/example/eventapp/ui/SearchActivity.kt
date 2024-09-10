package com.example.eventapp.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventapp.R
import com.example.eventapp.databinding.ActivitySearchBinding
import com.example.eventapp.ui.adapter.EventAdapter
import com.example.eventapp.ui.model.EventSearchModel

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val eventSearchModel: EventSearchModel by viewModels()
    private lateinit var eventAdapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.includeToolbar.toolbar)

        binding.includeToolbar.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setupRecyclerView()

        // Set observer untuk search query
        eventSearchModel.searchQuery.observe(this) { query ->
            if (query != binding.tfSearch.editText?.text.toString()) {
                binding.tfSearch.editText?.setText(query)
            }
        }

        // Saat user menekan enter, lakukan pencarian dan simpan state query
        binding.tfSearch.editText?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                val query = binding.tfSearch.editText?.text.toString()
                if (query.isNotEmpty()) {
                    eventSearchModel.setSearchQuery(query)
                    eventSearchModel.fetchSearchEvent(query)
                }
                true
            } else {
                false
            }
        }

        eventSearchModel.listEvent.observe(this) { eventList ->
            eventAdapter.setData(eventList)
        }

        eventSearchModel.isLoading.observe(this) {
            showLoading(it)
        }

        // Handle window insets for padding adjustments
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Set up RecyclerView with adapter and layout manager
    private fun setupRecyclerView() {
        eventAdapter = EventAdapter(emptyList())
        val layoutManager = LinearLayoutManager(this)
        binding.rvEventSearch.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvEventSearch.addItemDecoration(itemDecoration)
        binding.rvEventSearch.adapter = eventAdapter
    }

    // Show loading indicator while fetching data
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.includeProgressBar.progressBar.visibility = View.VISIBLE
            binding.rvEventSearch.visibility = View.GONE
        } else {
            binding.includeProgressBar.progressBar.visibility = View.GONE
            binding.rvEventSearch.visibility = View.VISIBLE
        }
    }
}
