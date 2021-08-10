package com.example.customerfirebase

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.core.content.ContextCompat
import com.example.customerfirebase.db.CustomerDao
import com.example.customerfirebase.receiver.AlarmReceiver
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class CustomerApplication : Application() {

    @Inject
    lateinit var customerDao: CustomerDao


    companion object {
        lateinit var instance: CustomerApplication
            private set
        lateinit var appContext: Context
        lateinit var customerDaoo: CustomerDao
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appContext = this
        customerDaoo = customerDao
        FirebaseApp.initializeApp(this)
        AlarmReceiver.updateAlarmWhenReboot(instance,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED)
        createNotificationChannel(getString(R.string.reminder_notification_channel_id),
            getString(R.string.reminder_notification_channel_name),
            getString(R.string.reminder_notification_channel_description))
    }

    fun createNotificationChannel(channelId: String, channelName: String, description: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.apply {
                enableLights(true)
                lightColor = Color.BLUE
                enableVibration(true)
            }
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC)
            notificationChannel.description = description
            val notificationManager = ContextCompat.getSystemService(this.applicationContext,
                NotificationManager::class.java
            ) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}