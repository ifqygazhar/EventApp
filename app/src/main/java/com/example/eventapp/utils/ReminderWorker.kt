package com.example.eventapp.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.eventapp.R
import com.example.eventapp.data.remote.retrofit.ApiConfig
import com.example.eventapp.ui.MainActivity

class ReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val sharedPref = context.getSharedPreferences("reminder_prefs", Context.MODE_PRIVATE)
        val isReminderActive = sharedPref.getBoolean("daily_reminder", false)

        if (isReminderActive) {
            try {
                // Memanggil API untuk mendapatkan event terdekat
                val apiService = ApiConfig.getApiService()
                val response = apiService.getEventReminder()

                // Mengecek apakah respons berhasil dan memiliki data event
                if (!response.error && response.listEvents.isNotEmpty()) {
                    val event = response.listEvents.first()
                    showNotification(event.name, event.beginTime)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                return Result.retry()
            }
        }

        return Result.success()
    }

    private fun showNotification(eventName: String, eventTime: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, "reminder_channel")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Event Terdekat")
            .setContentText("Event: $eventName pada $eventTime")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(1001, builder.build())
    }
}
