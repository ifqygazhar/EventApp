package com.example.eventapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventapp.R
import com.example.eventapp.databinding.ActivitySearchBinding
import com.example.eventapp.ui.adapter.EventSearchAdapter
import com.example.eventapp.ui.model.EventSearchModel
import com.example.eventapp.ui.model.factory.EventSearchModelFactory
import com.example.eventapp.utils.DialogUtil
import networkCheck

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var eventSearchModel: EventSearchModel
    private lateinit var eventSearchAdapter: EventSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.includeToolbar.toolbar)
        supportActionBar?.title = getString(R.string.search)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        binding.includeToolbar.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        val factory = EventSearchModelFactory.getInstance(this)
        eventSearchModel = ViewModelProvider(this, factory)[EventSearchModel::class.java]

        setupRecyclerView()

        // Set observer untuk search query
        eventSearchModel.searchQuery.observe(this) { query ->
            if (query != binding.tfSearch.editText?.text.toString()) {
                binding.tfSearch.editText?.setText(query)
            }
        }

        // Saat user menekan enter, lakukan pengecekan internet terlebih dahulu sebelum pencarian
        binding.tfSearch.editText?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                val query = binding.tfSearch.editText?.text.toString()
                if (query.isNotEmpty()) {
                    // Cek koneksi internet
                    if (networkCheck(this)) {
                        eventSearchModel.setSearchQuery(query)
                        eventSearchModel.fetchSearchEvent(query)
                    } else {
                        // Tampilkan dialog no internet
                        DialogUtil.showNoInternetDialog(this, layoutInflater)
                    }
                }
                true
            } else {
                false
            }
        }

        eventSearchModel.listEvent.observe(this) { eventList ->
            if (eventList.isEmpty()) {
                binding.imgEmpty.visibility = View.VISIBLE
                binding.rvEventSearch.visibility = View.GONE
            } else {
                binding.imgEmpty.visibility = View.GONE
                binding.rvEventSearch.visibility = View.VISIBLE
                eventSearchAdapter.setData(eventList)
            }
        }

        eventSearchModel.isLoading.observe(this) {
            showLoading(it)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupRecyclerView() {
        eventSearchAdapter = EventSearchAdapter(emptyList()) { eventId ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("EVENT_ID", eventId)
            startActivity(intent)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvEventSearch.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvEventSearch.addItemDecoration(itemDecoration)
        binding.rvEventSearch.adapter = eventSearchAdapter
    }

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
