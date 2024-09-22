package com.example.eventapp.data.remote.retrofit

import com.example.eventapp.data.remote.response.EventDetailResponse
import com.example.eventapp.data.remote.response.EventResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/events?active=1")
    suspend fun getEventActive(): EventResponse

    @GET("/events?active=0")
    suspend fun getEventNonActive(): EventResponse

    @GET("/events/{id}")
    suspend fun getEventDetail(@Path("id") id: Int): EventDetailResponse

    @GET("/events")
    suspend fun getEventSearch(
        @Query("active") active: Int = -1,
        @Query("q") query: String
    ): EventResponse

}