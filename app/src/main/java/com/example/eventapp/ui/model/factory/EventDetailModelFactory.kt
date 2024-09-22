package com.example.eventapp.ui.model.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eventapp.data.EventRepository
import com.example.eventapp.di.Injection
import com.example.eventapp.ui.model.EventDetailModel

class EventDetailModelFactory private constructor(private val eventRepository: EventRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventDetailModel::class.java)) {
            return EventDetailModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: EventDetailModelFactory? = null
        fun getInstance(context: Context): EventDetailModelFactory =
            instance ?: synchronized(this) {
                instance ?: EventDetailModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }

}