package com.example.customerfirebase.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.customerfirebase.R
import com.example.customerfirebase.model.RemainderDetails
import com.example.customerfirebase.service.RescheduleAlarmsService
import com.example.customerfirebase.ui.MainActivity
import com.example.customerfirebase.utils.Constant.INTENT_EXTRA_DESC_PRO_CATEGORY
import com.example.customerfirebase.utils.Constant.INTENT_EXTRA_DESC_PRO_IMAGE
import com.example.customerfirebase.utils.Constant.INTENT_EXTRA_DESC_PRO_NAME
import com.example.customerfirebase.utils.Constant.INTENT_EXTRA_DESC_PRO_PRICE
import com.example.customerfirebase.utils.Constant.INTENT_EXTRA_DESC_PRO_QUANTITY
import com.example.customerfirebase.utils.Constant.INTENT_EXTRA_DESC_PRO_TOTAL
import com.example.customerfirebase.utils.Constant.INTENT_EXTRA_REMINDER_IDENTIFIER
import com.example.customerfirebase.utils.Constant.INTENT_EXTRA_ROW_ID
import com.example.customerfirebase.utils.Constant.INTENT_EXTRA_TITLE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL


class AlarmReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
                val toastText = String.format("Alarm Reboot")
                Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()

                if (context != null) {
                    Log.d("Alarm Reciever", "start reschedule service")
                    startRescheduleAlarmsService(context)
                }
            }
        }
        //notification Extras..
        val notificationId = intent?.extras?.getInt(INTENT_EXTRA_ROW_ID) ?: 0
        val reminderIdentifier = intent?.extras?.getString(INTENT_EXTRA_REMINDER_IDENTIFIER)
        val title = intent?.extras?.getString(INTENT_EXTRA_TITLE)
        val productName = intent?.extras?.getString(INTENT_EXTRA_DESC_PRO_NAME)
        val productCategory = intent?.extras?.getString(INTENT_EXTRA_DESC_PRO_CATEGORY)
        val productQuantity = intent?.extras?.getString(INTENT_EXTRA_DESC_PRO_QUANTITY)
        val productPrice = intent?.extras?.getString(INTENT_EXTRA_DESC_PRO_PRICE)
        val productTotal = intent?.extras?.getString(INTENT_EXTRA_DESC_PRO_TOTAL)
        val productImage = intent?.extras?.getString(INTENT_EXTRA_DESC_PRO_IMAGE)


        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.putExtra(INTENT_EXTRA_REMINDER_IDENTIFIER, reminderIdentifier)

        val pendingIntent = PendingIntent.getActivity(context,
            notificationId,
            notificationIntent,
            FLAG_UPDATE_CURRENT)


        Log.d("Alarm Receiver", "alarm received")
        val notificationBuilder = NotificationCompat.Builder(context!!,
            context.getString(R.string.reminder_notification_channel_id))
            .setSmallIcon(R.drawable.ic_notifications_smallicon)
            .setContentTitle(title)
            .setContentText(productName)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(productName + "\n" + "Category : " + productCategory + "\nQuantity : " + productQuantity + "\nPrice : " + productPrice +
                        "\nTotal : " + productTotal))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        productImage?.let { applyImageUrl(notificationBuilder, it) }

        val notificationManager = ContextCompat.getSystemService(context,
            NotificationManager::class.java) as NotificationManager

/*

            val futureTarget = Glide.with(context)
                .asBitmap()
                .load(productImage)
                .submit()


            val bitmap = futureTarget.get()
            notificationBuilder.setLargeIcon(bitmap)


        Glide.with(context).clear(futureTarget)
*/

        notificationManager.notify(notificationId, notificationBuilder.build())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }

    }

    private fun startRescheduleAlarmsService(context: Context) {
        val intentService = Intent(context, RescheduleAlarmsService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
            Log.d("Alarm Reciever", "start reschedule service")
        } else {
            context.startService(intentService)
            Log.d("Alarm Reciever", "start reschedule service")
        }
    }

    fun applyImageUrl(
        builder: NotificationCompat.Builder,
        imageUrl: String,
    ) = runBlocking {
        val url = URL(imageUrl)

        withContext(Dispatchers.IO) {
            try {
                val input = url.openStream()
                BitmapFactory.decodeStream(input)
            } catch (e: IOException) {
                null
            }
        }?.let { bitmap ->
            builder.setLargeIcon(bitmap)
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
                putExtra(INTENT_EXTRA_REMINDER_IDENTIFIER, reminder.reminderIndentifier)
                putExtra(INTENT_EXTRA_ROW_ID, reminder.remainderId.toString().toInt())
                putExtra(INTENT_EXTRA_TITLE, reminder.customerName)
                putExtra(INTENT_EXTRA_DESC_PRO_NAME, reminder.productName)
                putExtra(INTENT_EXTRA_DESC_PRO_CATEGORY, reminder.productCategory)
                putExtra(INTENT_EXTRA_DESC_PRO_QUANTITY, reminder.productQuantity)
                putExtra(INTENT_EXTRA_DESC_PRO_PRICE, reminder.productPrice)
                putExtra(INTENT_EXTRA_DESC_PRO_TOTAL, reminder.productTotal)
                putExtra(INTENT_EXTRA_DESC_PRO_IMAGE, reminder.productImageUrl)
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

        fun updateAlarmWhenReboot(context: Context, state: Int) {
            val receiver = ComponentName(context, BootReceiver::class.java)
            context.packageManager.setComponentEnabledSetting(
                receiver,
                state,
                PackageManager.DONT_KILL_APP
            )
        }


    }



}