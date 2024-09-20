package com.example.eventapp.ui.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eventapp.data.EventRepository
import com.example.eventapp.data.Result
import com.example.eventapp.data.local.entity.EventEntity

class EventActiveAndNonActiveModel(private val eventRepository: EventRepository) : ViewModel() {

    companion object {
        private const val TAG = "EventActiveModel"
    }

    private val _listEventActive = MutableLiveData<List<EventEntity>>()
    val listEventActive: LiveData<List<EventEntity>> = _listEventActive

    private val _listEventNonActive = MutableLiveData<List<EventEntity>>()
    val listEventNonActive: LiveData<List<EventEntity>> = _listEventNonActive

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        fetchActiveEvent()
        fetchNonActiveEvent()
    }

    private fun fetchActiveEvent() {
        _isLoading.value = true

        eventRepository.fetchActiveEvents().observeForever { result ->
            when (result) {
                is Result.Loading -> _isLoading.value = true
                is Result.Success -> {
                    _isLoading.value = false
                    _listEventActive.value = result.data
                }

                is Result.Error -> {
                    _isLoading.value = false
                    Log.e(TAG, "Error: ${result.error}")
                }
            }
        }
    }

    private fun fetchNonActiveEvent() {
        _isLoading.value = true

        eventRepository.fetchNonActiveEvents().observeForever { result ->
            when (result) {
                is Result.Loading -> _isLoading.value = true
                is Result.Success -> {
                    _isLoading.value = false
                    _listEventNonActive.value = result.data
                }

                is Result.Error -> {
                    _isLoading.value = false
                }
            }
        }
    }


    fun saveEvent(event: EventEntity) {
        eventRepository.setEventFavorite(event, true)
    }

    fun deleteEvent(event: EventEntity) {
        eventRepository.setEventFavorite(event, false)
    }
}