package com.example.customerfirebase.service

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.customerfirebase.model.RemainderDetails
import com.example.customerfirebase.receiver.BootReceiver
import com.example.customerfirebase.utils.AlarmUtil
import com.example.customerfirebase.utils.Constant
import com.google.firebase.firestore.FirebaseFirestore


class RescheduleAlarmsService : LifecycleService() {
    private var reminders: List<RemainderDetails>? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("alarm service", "reschedule Alarm service called")
        super.onStartCommand(intent, flags, startId)
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        reminders = getReminderList()
        Log.d(BootReceiver.TAG, "reminders $reminders")
        val listOfReminders = reminders //Read, so we can smartCast
        if (!listOfReminders.isNullOrEmpty()) {
            listOfReminders.forEach { reminder ->
                if (reminder.remainderIsActive) {
                    AlarmUtil.createAlarm(
                        this.applicationContext,
                        reminder,
                        alarmManager
                    )
                }
            }
        }
        return START_STICKY
    }


    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    private fun getReminderList(): List<RemainderDetails> {
        val remainderList = MutableLiveData<ArrayList<RemainderDetails>>()
        val newRemainderList = arrayListOf<RemainderDetails>()
        val remainderRef = FirebaseFirestore.getInstance().collection(Constant.REMAINDER)
        remainderRef.get().addOnSuccessListener { documents ->
            if (documents != null) {
                Log.d(BootReceiver.TAG, "Remainder List ")
                for (document in documents) {
                    Log.d(BootReceiver.TAG, "${document.id} => ${document.data}")
                    val remainder = document.toObject(RemainderDetails::class.java)
                    newRemainderList.add(remainder)
                    remainderList.value = newRemainderList
                    Log.d(BootReceiver.TAG, "Remainder List " + newRemainderList.size)
                }
            }
            Log.d(BootReceiver.TAG, "Remainder List Document Snapshot")
        }.addOnFailureListener { exception ->
            Log.d(BootReceiver.TAG, "get failed with", exception)
        }
        Log.d(BootReceiver.TAG, "remainder List : " + remainderList.value)
        return newRemainderList
    }
}