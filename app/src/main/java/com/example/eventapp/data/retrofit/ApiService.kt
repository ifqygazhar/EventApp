package com.example.eventapp.data.retrofit

import com.example.eventapp.data.response.EventDetailResponse
import com.example.eventapp.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/events?active=1")
    fun getEventActive(): Call<EventResponse>

    @GET("/events?active=0")
    fun getEventNonActive(): Call<EventResponse>

    @GET("/events/{id}")
    fun getEventDetail(@Path("id") id: Int): Call<EventDetailResponse>

}