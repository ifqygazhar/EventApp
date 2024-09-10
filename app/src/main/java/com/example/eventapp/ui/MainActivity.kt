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

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val eventActiveFragment = EventActiveFragment()
        val fragmentManager = supportFragmentManager
        val fragment = fragmentManager.findFragmentByTag(EventActiveFragment::class.java.simpleName)

        if (fragment !is EventActiveFragment) {
            Log.d(
                "EventActiveFragment",
                "Fragment Name :" + EventActiveFragment::class.java.simpleName
            )
            fragmentManager.commit {
                add(
                    R.id.frame_container,
                    eventActiveFragment,
                    EventActiveFragment::class.java.simpleName
                )
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
}