package com.example.eventapp.ui.model.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eventapp.data.EventRepository
import com.example.eventapp.di.Injection
import com.example.eventapp.ui.model.EventActiveAndNonActiveModel

class EventActiveAndNonActiveModelFactory private constructor(private val eventRepository: EventRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventActiveAndNonActiveModel::class.java)) {
            return EventActiveAndNonActiveModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: EventActiveAndNonActiveModelFactory? = null

        fun getInstance(context: Context): EventActiveAndNonActiveModelFactory =
            instance ?: synchronized(this) {
                instance
                    ?: EventActiveAndNonActiveModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}
