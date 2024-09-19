package com.example.eventapp.ui.model.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eventapp.data.EventRepository
import com.example.eventapp.di.Injection
import com.example.eventapp.ui.model.EventNonActiveModel

class EventNonActiveModelFactory private constructor(private val eventRepository: EventRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventNonActiveModel::class.java)) {
            return EventNonActiveModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: EventNonActiveModelFactory? = null
        fun getInstance(context: Context): EventNonActiveModelFactory =
            instance ?: synchronized(this) {
                instance ?: EventNonActiveModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}