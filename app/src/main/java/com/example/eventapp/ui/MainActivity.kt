package com.example.eventapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eventapp.R
import com.example.eventapp.databinding.ActivityMainBinding
import com.example.eventapp.ui.fragment.EventActiveFragment
import com.example.eventapp.ui.fragment.EventNonActiveFragment
import com.example.eventapp.ui.fragment.FavoriteFragment
import com.example.eventapp.ui.fragment.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // Key untuk menyimpan fragment yang dipilih
    private val SELECTED_FRAGMENT_KEY = "selected_fragment"

    // Variabel untuk menyimpan posisi fragment yang terakhir dipilih
    private var selectedFragmentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.includeToolbar.toolbar)

        // Jika ada savedInstanceState, ambil fragment yang dipilih sebelumnya
        if (savedInstanceState != null) {
            selectedFragmentIndex = savedInstanceState.getInt(SELECTED_FRAGMENT_KEY, 0)
        } else {
            // Jika tidak ada state yang tersimpan, tampilkan HomeFragment sebagai default
            selectedFragmentIndex = 0
        }

        // Set fragment yang sudah dipilih sebelumnya
        loadFragment(selectedFragmentIndex)

        // Set item active di bottom bar sesuai fragment yang dipilih
        binding.bottomBar.itemActiveIndex = selectedFragmentIndex

        // Set up bottom bar listener, namun hanya update jika selection berubah
        binding.bottomBar.setOnItemSelectedListener { position ->
            if (position != selectedFragmentIndex) {
                selectedFragmentIndex = position
                loadFragment(selectedFragmentIndex)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.frame_container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom,
            )
            insets
        }
    }

    // Simpan fragment yang dipilih saat orientasi berubah
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SELECTED_FRAGMENT_KEY, selectedFragmentIndex)
    }

    // Fungsi untuk mengganti fragment
    private fun loadFragment(index: Int) {
        val selectedFragment = when (index) {
            0 -> HomeFragment()
            1 -> EventActiveFragment()
            2 -> EventNonActiveFragment()
            3 -> FavoriteFragment()
            else -> HomeFragment()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment, selectedFragment)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_search -> {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}


