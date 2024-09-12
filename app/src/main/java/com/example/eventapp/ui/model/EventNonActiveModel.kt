package com.example.eventapp.ui.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eventapp.data.response.EventResponse
import com.example.eventapp.data.response.ListEventsItem
import com.example.eventapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventNonActiveModel : ViewModel() {

    companion object {
        private const val TAG = "EventActiveModel"
    }

    private val _listEvent = MutableLiveData<List<ListEventsItem>>()
    val listEvent: LiveData<List<ListEventsItem>> = _listEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        fetchNonActiveEvent()
    }

    fun refreshNonActiveEvents() {
        fetchNonActiveEvent()
    }

    private fun fetchNonActiveEvent() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventNonActive()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                val responseBody = response.body()
                _isLoading.value = false
                if (response.isSuccessful) {

                    _listEvent.value = responseBody?.listEvents
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")

            }
        })
    }
}
