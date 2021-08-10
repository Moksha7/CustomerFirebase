package com.example.customerfirebase.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.example.customerfirebase.model.RemainderDetails
import com.example.customerfirebase.service.AlarmService
import com.example.customerfirebase.service.RescheduleAlarmsService
import com.example.customerfirebase.utils.Constant


class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            val toastText = String.format("Alarm Reboot")
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
            startRescheduleAlarmsService(context)
        } else {
            val toastText = String.format("Alarm Received")
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
            startAlarmService(context, intent)


        }
    }


    private fun startAlarmService(context: Context, intent: Intent) {
        val intentService = Intent(context, AlarmService::class.java)
        intentService.apply {
            putExtra(Constant.INTENT_EXTRA_REMINDER_IDENTIFIER,
                intent.getStringExtra(Constant.INTENT_EXTRA_REMINDER_IDENTIFIER))
            putExtra(Constant.INTENT_EXTRA_ROW_ID,
                intent.getStringExtra(Constant.INTENT_EXTRA_ROW_ID))
            putExtra(Constant.INTENT_EXTRA_TITLE,
                intent.getStringExtra(Constant.INTENT_EXTRA_TITLE))
            putExtra(Constant.INTENT_EXTRA_DESC_PRO_NAME,
                intent.getStringExtra(Constant.INTENT_EXTRA_DESC_PRO_NAME))
            putExtra(Constant.INTENT_EXTRA_DESC_PRO_CATEGORY,
                intent.getStringExtra(Constant.INTENT_EXTRA_DESC_PRO_CATEGORY))
            putExtra(Constant.INTENT_EXTRA_DESC_PRO_QUANTITY,
                intent.getStringExtra(Constant.INTENT_EXTRA_DESC_PRO_QUANTITY))
            putExtra(Constant.INTENT_EXTRA_DESC_PRO_PRICE,
                intent.getStringExtra(Constant.INTENT_EXTRA_DESC_PRO_PRICE))
            putExtra(Constant.INTENT_EXTRA_DESC_PRO_TOTAL,
                intent.getStringExtra(Constant.INTENT_EXTRA_DESC_PRO_TOTAL))
            putExtra(Constant.INTENT_EXTRA_DESC_PRO_IMAGE,
                intent.getStringExtra(Constant.INTENT_EXTRA_DESC_PRO_IMAGE))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }

    private fun startRescheduleAlarmsService(context: Context) {
        val intentService = Intent(context, RescheduleAlarmsService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }

    companion object {

        private const val TAG = "AlarmReceiver"

        fun getAlarmPendingIntent(
            pckgContext: Context,
            reminder: RemainderDetails,
        ): PendingIntent {
            val intent = Intent(pckgContext, AlarmReceiver::class.java)
            intent.apply {
                putExtra(Constant.INTENT_EXTRA_REMINDER_IDENTIFIER, reminder.reminderIndentifier)
                putExtra(Constant.INTENT_EXTRA_ROW_ID, reminder.remainderId.toString().toInt())
                putExtra(Constant.INTENT_EXTRA_TITLE, reminder.customerName)
                putExtra(Constant.INTENT_EXTRA_DESC_PRO_NAME, reminder.productName)
                putExtra(Constant.INTENT_EXTRA_DESC_PRO_CATEGORY, reminder.productCategory)
                putExtra(Constant.INTENT_EXTRA_DESC_PRO_QUANTITY, reminder.productQuantity)
                putExtra(Constant.INTENT_EXTRA_DESC_PRO_PRICE, reminder.productPrice)
                putExtra(Constant.INTENT_EXTRA_DESC_PRO_TOTAL, reminder.productTotal)
                putExtra(Constant.INTENT_EXTRA_DESC_PRO_IMAGE, reminder.productImageUrl)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                pckgContext,
                reminder.remainderId.toString().toInt(),
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
            Log.d(TAG, "getAlarmPendingIntent: ${reminder.remainderId.toString().toInt()}")
            return pendingIntent
        }


    }


}