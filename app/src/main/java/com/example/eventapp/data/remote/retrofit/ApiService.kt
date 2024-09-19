package com.example.eventapp.data.remote.retrofit

import com.example.eventapp.data.remote.response.EventDetailResponse
import com.example.eventapp.data.remote.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/events?active=1")
    fun getEventActive(): Call<EventResponse>

    @GET("/events?active=0")
    fun getEventNonActive(): Call<EventResponse>

    @GET("/events/{id}")
    fun getEventDetail(@Path("id") id: Int): Call<EventDetailResponse>

    @GET("/events")
    fun getEventSearch(
        @Query("active") active: Int = -1,
        @Query("q") query: String
    ): Call<EventResponse>

}