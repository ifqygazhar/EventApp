package com.example.eventapp.ui.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eventapp.data.EventRepository
import com.example.eventapp.data.Result
import com.example.eventapp.data.remote.response.EventDetailResponse


class EventDetailModel(private val eventRepository: EventRepository) : ViewModel() {
    companion object {
        private const val TAG = "EventDetailModel"
    }

    // LiveData untuk menampung event detail
    private val _eventDetail = MutableLiveData<EventDetailResponse>()
    val eventDetail: LiveData<EventDetailResponse> = _eventDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchEventDetail(id: Int) {
        _isLoading.value = true

        eventRepository.fetchEventDetail(id).observeForever { result ->
            when (result) {
                is Result.Loading -> _isLoading.value = true
                is Result.Success -> {
                    _isLoading.value = false
                    _eventDetail.value = result.data
                }

                is Result.Error -> {
                    _isLoading.value = false
                    Log.e(TAG, "Error: ${result.error}")
                }
            }
        }
    }
}
