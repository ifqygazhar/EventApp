package com.example.eventapp.ui.model.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eventapp.data.EventRepository
import com.example.eventapp.di.Injection
import com.example.eventapp.ui.model.EventSearchModel

class EventSearchModelFactory private constructor(private val eventRepository: EventRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventSearchModel::class.java)) {
            return EventSearchModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: EventSearchModelFactory? = null
        fun getInstance(context: Context): EventSearchModelFactory =
            instance ?: synchronized(this) {
                instance ?: EventSearchModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}