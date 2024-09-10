package com.example.eventapp.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
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
}