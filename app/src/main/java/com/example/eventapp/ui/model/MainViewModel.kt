package com.example.eventapp.ui.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val currentFragment = MutableLiveData<Int>()
}