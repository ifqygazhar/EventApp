package com.example.eventapp.di

import android.content.Context
import com.example.eventapp.data.EventRepository
import com.example.eventapp.data.remote.retrofit.ApiConfig
import com.example.eventapp.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val appExecutors = AppExecutors()
        return EventRepository.getInstance(apiService, appExecutors)
    }
}