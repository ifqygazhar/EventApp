package com.example.eventapp.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eventapp.data.EventRepository
import com.example.eventapp.data.local.entity.EventEntity

class EventFavoriteModel(private val eventRepository: EventRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    fun getEventFavorite() = eventRepository.getEventFavorite()

    fun deleteEvent(event: EventEntity) {
        eventRepository.setEventFavorite(event, false)
    }

    fun saveEvent(event: EventEntity) {
        eventRepository.setEventFavorite(event, true)
    }

}