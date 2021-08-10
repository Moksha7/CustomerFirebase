package com.example.customerfirebase.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.AlarmManagerCompat
import com.example.customerfirebase.model.RemainderDetails
import com.example.customerfirebase.receiver.AlarmReceiver
import com.example.customerfirebase.receiver.BootReceiver
import java.text.SimpleDateFormat
import java.util.*

class AlarmUtil {
    companion object {
        private const val TAG = "AlarmUtil"

        @SuppressLint("SimpleDateFormat")
        private fun getTriggerTime(dateString: String, timeString: String): Long {
            val date = SimpleDateFormat("EEE, d MMM yyyy").parse(dateString)
            val time = SimpleDateFormat("h:mm a").parse(timeString)
            val calendar = Calendar.getInstance()
            calendar.time = date ?: Date()
            val initialYear = calendar.get(Calendar.YEAR)
            val initialMonth = calendar.get(Calendar.MONTH)
            val initialDate = calendar.get(Calendar.DAY_OF_MONTH)
            calendar.time = time ?: Date()
            val initialHourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
            val initialMinute = calendar.get(Calendar.MINUTE)
            calendar.set(initialYear, initialMonth, initialDate, initialHourOfDay, initialMinute)
            val timeDiff = calendar.timeInMillis - Calendar.getInstance().timeInMillis
            return System.currentTimeMillis() + timeDiff
        }

        private fun getRepeatTime(value: Int, unit: String): Long {
            return when (unit) {
                "Minute" ->
                    (value * 60 * 1000).toLong()
                "Hour" ->
                    (value * 60 * 60 * 1000).toLong()
                "Day" ->
                    (value * 24 * 60 * 60 * 1000).toLong()
                "Week" ->
                    (value * 7 * 24 * 60 * 60 * 1000).toLong()
                "Month" ->
                    (value * 30 * 7 * 24 * 60 * 60 * 1000).toLong()
                else ->
                    (value * 60 * 60 * 1000).toLong()
            }
        }


        fun createAlarm(
            context: Context,
            reminder: RemainderDetails,
            alarmManager: AlarmManager,
        ) {
            val alarmPendingIntent = AlarmReceiver.getAlarmPendingIntent(
                context,
                reminder
            )

            if (reminder.remainderRepeat) {
                if (System.currentTimeMillis() < getTriggerTime(reminder.remainderDate,
                        reminder.remainderTime)
                ) {
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        getTriggerTime(reminder.remainderDate, reminder.remainderTime),
                        getRepeatTime(reminder.remainderRepeatValue, reminder.remainderRepeatUnit),
                        alarmPendingIntent
                    )
                } else {
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        getTriggerTime(reminder.remainderDate, reminder.remainderTime),
                        getRepeatTime(reminder.remainderRepeatValue, reminder.remainderRepeatUnit),
                        alarmPendingIntent
                    )
                }

            } else {
                if (System.currentTimeMillis() < getTriggerTime(reminder.remainderDate,
                        reminder.remainderTime)
                ) {
                    AlarmManagerCompat.setExactAndAllowWhileIdle(
                        alarmManager,
                        AlarmManager.RTC_WAKEUP,
                        getTriggerTime(reminder.remainderDate, reminder.remainderTime),
                        alarmPendingIntent
                    )
                } else {
                    AlarmManagerCompat.setExactAndAllowWhileIdle(
                        alarmManager,
                        AlarmManager.RTC_WAKEUP,
                        getTriggerTime(reminder.remainderDate, reminder.remainderTime),
                        alarmPendingIntent
                    )
                }
            }
            // updateAlarmWhenReboot(context, PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
        }


        fun cancelAlarm(
            context: Context,
            reminder: RemainderDetails,
            alarmManager: AlarmManager,
        ) {
            val alarmPendingIntent = AlarmReceiver.getAlarmPendingIntent(
                context,
                reminder
            )
            Log.d(TAG, "cancelAlarm: ")
            alarmManager.cancel(alarmPendingIntent)
            // updateAlarmWhenReboot(context, PackageManager.COMPONENT_ENABLED_STATE_DISABLED)
        }


        private fun updateAlarmWhenReboot(context: Context, state: Int) {
            val receiver = ComponentName(context, BootReceiver::class.java)
            context.packageManager.setComponentEnabledSetting(
                receiver,
                state,
                PackageManager.DONT_KILL_APP
            )
        }
    }
}