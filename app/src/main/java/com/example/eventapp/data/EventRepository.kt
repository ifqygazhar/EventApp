package com.example.eventapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.eventapp.data.local.entity.EventEntity
import com.example.eventapp.data.local.room.EventDao
import com.example.eventapp.data.remote.response.EventResponse
import com.example.eventapp.data.remote.response.ListEventsItem
import com.example.eventapp.data.remote.retrofit.ApiService
import com.example.eventapp.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao,
    private val appExecutors: AppExecutors
) {
    private val activeEventsResult = MediatorLiveData<Result<List<EventEntity>>>()
    private val nonActiveEventsResult = MediatorLiveData<Result<List<EventEntity>>>()

    fun fetchActiveEvents(): LiveData<Result<List<EventEntity>>> {
        activeEventsResult.value = Result.Loading
        appExecutors.networkIO.execute {
            apiService.getEventActive().enqueue(object : Callback<EventResponse> {
                override fun onResponse(
                    call: Call<EventResponse>,
                    response: Response<EventResponse>
                ) {
                    if (response.isSuccessful) {
                        val events = response.body()?.listEvents
                        val eventList = ArrayList<EventEntity>()
                        appExecutors.diskIO.execute {
                            events?.forEach { eventItem ->
                                val isFavorite = eventDao.isEventFavorite(eventItem.id)
                                val event = EventEntity(
                                    eventItem.id,
                                    eventItem.mediaCover,
                                    eventItem.name,
                                    eventItem.ownerName,
                                    eventItem.cityName,
                                    eventItem.category,
                                    eventItem.quota,
                                    eventItem.registrants,
                                    eventItem.beginTime,
                                    true,
                                    isFavorite
                                )
                                eventList.add(event)
                            }
                            eventDao.deleteUpcomingAll()
                            eventDao.insertEvent(eventList)
                        }

                    } else {
                        activeEventsResult.value = Result.Error("Error: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                    activeEventsResult.value = Result.Error(t.message.toString())
                }
            })

        }
        val localData = eventDao.getEventActive()
        activeEventsResult.addSource(localData) { eventDAta: List<EventEntity> ->
            activeEventsResult.value = Result.Success(eventDAta)
        }
        return activeEventsResult
    }

    fun fetchNonActiveEvents(): LiveData<Result<List<EventEntity>>> {
        nonActiveEventsResult.value = Result.Loading
        appExecutors.networkIO.execute {
            apiService.getEventNonActive().enqueue(object : Callback<EventResponse> {
                override fun onResponse(
                    call: Call<EventResponse>,
                    response: Response<EventResponse>
                ) {
                    if (response.isSuccessful) {
                        val events = response.body()?.listEvents
                        val eventList = ArrayList<EventEntity>()
                        appExecutors.diskIO.execute {
                            events?.forEach { eventItem ->
                                val isFavorite = eventDao.isEventFavorite(eventItem.id)
                                val event = EventEntity(
                                    eventItem.id,
                                    eventItem.mediaCover,
                                    eventItem.name,
                                    eventItem.ownerName,
                                    eventItem.cityName,
                                    eventItem.category,
                                    eventItem.quota,
                                    eventItem.registrants,
                                    eventItem.beginTime,
                                    false,
                                    isFavorite
                                )
                                eventList.add(event)
                            }
                            eventDao.deleteFinishedAll()
                            eventDao.insertEvent(eventList)
                        }

                    } else {
                        nonActiveEventsResult.value = Result.Error("Error: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                    nonActiveEventsResult.value = Result.Error(t.message.toString())
                }
            })
        }

        val localData = eventDao.getEventNonActive()
        nonActiveEventsResult.addSource(localData) { eventDAta: List<EventEntity> ->
            nonActiveEventsResult.value = Result.Success(eventDAta)
        }
        return nonActiveEventsResult
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


    fun setEventFavorite(events: EventEntity, eventState: Boolean) {
        appExecutors.diskIO.execute {
            events.isFavorite = eventState
            eventDao.updateEvent(events)
        }
    }


    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao,
            appExecutors: AppExecutors
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao, appExecutors)
            }.also { instance = it }
    }
}
