package com.example.eventapp.ui.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.eventapp.data.local.pref.SettingPreferences
import com.example.eventapp.utils.ReminderWorker
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SettingModel(private val pref: SettingPreferences) : ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }


    fun getReminderSetting(): LiveData<Boolean> {
        return pref.getReminderSetting().asLiveData()
    }


    fun saveReminderSetting(isReminderActive: Boolean) {
        viewModelScope.launch {
            pref.saveReminderSetting(isReminderActive)
        }
    }

    fun scheduleDailyReminder(context: Context) {
        val workManager = WorkManager.getInstance(context)
        val reminderRequest = PeriodicWorkRequest.Builder(
            ReminderWorker::class.java,
            15, TimeUnit.MINUTES
        ).build()

        workManager.enqueueUniquePeriodicWork(
            "DailyReminder",
            ExistingPeriodicWorkPolicy.REPLACE,
            reminderRequest
        )
    }


    fun cancelDailyReminder(context: Context) {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelUniqueWork("DailyReminder")
    }
}
