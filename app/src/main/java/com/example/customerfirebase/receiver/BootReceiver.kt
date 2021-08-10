package com.example.customerfirebase.receiver

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.customerfirebase.model.RemainderDetails
import com.example.customerfirebase.utils.AlarmUtil
import com.example.customerfirebase.utils.Constant
import com.google.firebase.firestore.FirebaseFirestore

class BootReceiver : BroadcastReceiver() {
    private var reminders: List<RemainderDetails>? = null

    companion object {
        const val TAG = "BootReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            Log.d(TAG, "onReceive: intent not null ")
            if (intent.action == "android.intent.action.BOOT_COMPLETED") {
                Log.d(TAG, "onReceive: intent action is correct")
                val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                reminders = getReminderList()
                Log.d(TAG, "reminders $reminders")
                val listOfReminders = reminders //Read, so we can smartCast
                if (!listOfReminders.isNullOrEmpty()) {
                    listOfReminders.forEach { reminder ->
                        if (reminder.remainderIsActive) {
                            AlarmUtil.createAlarm(
                                context.applicationContext,
                                reminder,
                                alarmManager
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getReminderList(): List<RemainderDetails> {
        val remainderList = MutableLiveData<ArrayList<RemainderDetails>>()
        val newRemainderList = arrayListOf<RemainderDetails>()
        val remainderRef = FirebaseFirestore.getInstance().collection(Constant.REMAINDER)
        remainderRef.get().addOnSuccessListener { documents ->
            if (documents != null) {
                Log.d(TAG, "Document Product Snapshot Data: ")
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val remainder = document.toObject(RemainderDetails::class.java)
                    newRemainderList.add(remainder)
                    remainderList.value = newRemainderList
                }
            }
            Log.d(TAG, "Remainder List Document Snapshot")
        }.addOnFailureListener { exception ->
            Log.d(TAG, "get failed with", exception)
        }
        Log.d(TAG, "remainder List : " + remainderList.value)
        return newRemainderList
    }

}