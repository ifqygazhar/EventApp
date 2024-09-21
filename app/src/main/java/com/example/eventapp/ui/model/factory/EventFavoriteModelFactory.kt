package com.example.eventapp.ui.model.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eventapp.data.EventRepository
import com.example.eventapp.di.Injection
import com.example.eventapp.ui.model.EventFavoriteModel

class EventFavoriteModelFactory private constructor(private val eventRepository: EventRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventFavoriteModel::class.java)) {
            return EventFavoriteModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: EventFavoriteModelFactory? = null
        fun getInstance(context: Context): EventFavoriteModelFactory =
            instance ?: synchronized(this) {
                instance ?: EventFavoriteModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }

}