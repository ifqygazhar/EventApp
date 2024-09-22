package com.example.eventapp.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eventapp.data.EventRepository
import com.example.eventapp.data.Result
import com.example.eventapp.data.local.entity.EventEntity

class EventNonActiveModel(private val eventRepository: EventRepository) : ViewModel() {

    companion object {
        private const val TAG = "EventActiveModel"
    }

    private val _listEvent = MutableLiveData<List<EventEntity>>()
    val listEvent: LiveData<List<EventEntity>> = _listEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        fetchNonActiveEvent()
    }

    private fun fetchNonActiveEvent() {
        _isLoading.value = true

        eventRepository.fetchNonActiveEvents().observeForever { result ->
            when (result) {
                is Result.Loading -> _isLoading.value = true
                is Result.Success -> {
                    _isLoading.value = false
                    _listEvent.value = result.data
                }

                is Result.Error -> {
                    _isLoading.value = false
                }
            }
        }
    }

    suspend fun saveEvent(event: EventEntity) {
        eventRepository.setEventFavorite(event, true)
    }

    suspend fun deleteEvent(event: EventEntity) {
        eventRepository.setEventFavorite(event, false)
    }
}