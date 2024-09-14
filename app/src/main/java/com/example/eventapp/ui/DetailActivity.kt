package com.example.eventapp.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.eventapp.R
import com.example.eventapp.databinding.ActivityDetailBinding
import com.example.eventapp.ui.model.EventDetailModel
import com.example.eventapp.util.LoadImage


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    // Menggunakan viewModels untuk mendapatkan instance ViewModel
    private val eventDetailModel: EventDetailModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflate layout dengan binding
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set toolbar dengan back button
        setSupportActionBar(binding.includeToolbar.toolbar)
        supportActionBar?.title = getString(R.string.detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        binding.includeToolbar.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Atur padding untuk inset
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Ambil event ID dari intent
        val eventId = intent.getIntExtra("EVENT_ID", 1)


        // Observasi data event detail dari ViewModel
        eventDetailModel.eventDetail.observe(this, Observer { eventDetailResponse ->
            val event = eventDetailResponse.event

            val tvCategory = binding.includeCard.tvCategory
            val tvQouta = binding.includeCard.tvQouta
            val tvRegister = binding.includeCard.tvRegister


            binding.tvTitle.text = event.name
            binding.tvOwner.text = event.ownerName
            binding.tvCity.text = event.cityName
            binding.tvStart.text = event.beginTime
            binding.tvEnd.text = event.endTime
            binding.tvSisa.text = "Sisa Qouta Registrasi : ${(event.quota - event.registrants)}"

            tvCategory.text = event.category
            tvQouta.text = event.quota.toString()
            tvRegister.text = event.registrants.toString()

            LoadImage.load(this, binding.imgLogo, event.mediaCover, R.color.placeholder)

            binding.btnPoster.setOnClickListener {
                showWebViewModal(event.description)
            }

            binding.btnRegister.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                startActivity(browserIntent)
            }

        })

        // Observasi status loading
        eventDetailModel.isLoading.observe(this, Observer { isLoading ->
            binding.includeProgressBar.progressBar.visibility =
                if (isLoading) View.VISIBLE else View.GONE
        })

        // Observasi error message
        eventDetailModel.errorMessage.observe(this, Observer { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        })

        // Panggil fetchEventDetail untuk memulai fetch data
        eventDetailModel.fetchEventDetail(eventId)
    }

    private fun showWebViewModal(description: String) {
        // Buat instance dari Dialog
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.modal_webview)

        // Inisialisasi WebView dari layout modal
        val webView = dialog.findViewById<WebView>(R.id.webView)

        // Atur pengaturan WebView
        webView.settings.apply {
            javaScriptEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            builtInZoomControls = true
            displayZoomControls = false
            setSupportZoom(true)
        }

        // Atur WebViewClient untuk memastikan konten dimuat di WebView, bukan di browser eksternal
        webView.webViewClient = WebViewClient()

        // Muat deskripsi HTML di WebView
        webView.loadDataWithBaseURL(null, description, "text/html", "UTF-8", null)

        // Atur dialog agar transparan di bagian luar WebView
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Tampilkan dialog
        dialog.show()
    }

}
