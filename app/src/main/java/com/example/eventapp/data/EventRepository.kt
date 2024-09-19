package com.example.eventapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.eventapp.data.remote.response.EventResponse
import com.example.eventapp.data.remote.response.ListEventsItem
import com.example.eventapp.data.remote.retrofit.ApiService
import com.example.eventapp.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventRepository private constructor(
    private val apiService: ApiService,
    private val appExecutors: AppExecutors
) {
    private val result = MediatorLiveData<Result<List<ListEventsItem>>>()

    fun fetchActiveEvents(): LiveData<Result<List<ListEventsItem>>> {
        val result = MutableLiveData<Result<List<ListEventsItem>>>()
        result.value = Result.Loading

        appExecutors.networkIO.execute {
            apiService.getEventActive().enqueue(object : Callback<EventResponse> {
                override fun onResponse(
                    call: Call<EventResponse>,
                    response: Response<EventResponse>
                ) {
                    if (response.isSuccessful) {
                        result.value = Result.Success(response.body()?.listEvents ?: emptyList())
                    } else {
                        result.value = Result.Error("Error: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                    result.value = Result.Error(t.message.toString())
                }
            })
        }

        return result
    }

    fun fetchNonActiveEvents(): LiveData<Result<List<ListEventsItem>>> {
        val result = MutableLiveData<Result<List<ListEventsItem>>>()
        result.value = Result.Loading

        appExecutors.networkIO.execute {
            apiService.getEventNonActive().enqueue(object : Callback<EventResponse> {
                override fun onResponse(
                    call: Call<EventResponse>,
                    response: Response<EventResponse>
                ) {
                    if (response.isSuccessful) {

                        result.value = Result.Success(response.body()?.listEvents ?: emptyList())
                    } else {
                        result.value = Result.Error("Error: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                    result.value = Result.Error(t.message.toString())
                }
            })
        }

        return result
    }

    fun fetchSearchEvent(query: String): LiveData<Result<List<ListEventsItem>>> {
        val result = MutableLiveData<Result<List<ListEventsItem>>>()
        result.value = Result.Loading

        appExecutors.networkIO.execute {
            apiService.getEventSearch(-1, query).enqueue(object : Callback<EventResponse> {
                override fun onResponse(
                    call: Call<EventResponse>,
                    response: Response<EventResponse>
                ) {
                    if (response.isSuccessful) {
                        result.value = Result.Success(response.body()?.listEvents ?: emptyList())
                    } else {
                        result.value = Result.Error("Error: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                    result.value = Result.Error(t.message.toString())
                }
            })
        }

        return result
    }


    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            appExecutors: AppExecutors
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, appExecutors)
            }.also { instance = it }
    }
}
