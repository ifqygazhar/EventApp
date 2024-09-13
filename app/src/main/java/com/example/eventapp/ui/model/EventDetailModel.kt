package com.example.eventapp.ui.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eventapp.data.response.EventDetailResponse
import com.example.eventapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventDetailModel : ViewModel() {

    companion object {
        private const val TAG = "EventDetailModel"
    }

    // LiveData untuk menyimpan data detail event
    private val _eventDetail = MutableLiveData<EventDetailResponse>()
    val eventDetail: LiveData<EventDetailResponse> = _eventDetail

    // LiveData untuk status loading
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LiveData untuk error message
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    // Fungsi untuk mengambil detail event berdasarkan id
    fun fetchEventDetail(id: Int) {
        _isLoading.value = true // Memulai proses loading
        val client = ApiConfig.getApiService().getEventDetail(id)

        client.enqueue(object : Callback<EventDetailResponse> {
            override fun onResponse(
                call: Call<EventDetailResponse>,
                response: Response<EventDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _eventDetail.value = responseBody!!
                    } else {
                        _errorMessage.value = responseBody?.message ?: "Unknown error"
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _errorMessage.value = "Failed to load event details: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<EventDetailResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error: ${t.message.toString()}"
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}
