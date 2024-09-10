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

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.includeToolbar.toolbar)


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment, EventActiveFragment())
                .commit()
        }
        binding.bottomBar.setOnItemSelectedListener {
            val selectedFragment = when (it) {
                0 -> EventActiveFragment()
                1 -> EventNonActiveFragment()
                else -> EventActiveFragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment, selectedFragment)
                .commit()
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