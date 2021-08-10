package com.example.customerfirebase.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.Vibrator
import android.util.Log
import androidx.annotation.Nullable
import androidx.core.app.NotificationCompat
import com.example.customerfirebase.ui.MainActivity
import com.example.customerfirebase.utils.Constant


class AlarmService : Service() {
    // private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null

    override fun onCreate() {
        super.onCreate()
        /*mediaPlayer = MediaPlayer.create(this,getFi)
        mediaPlayer!!.isLooping = true*/
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("alarm service", "Alarm service called")
        val notificationId = intent?.extras?.getInt(Constant.INTENT_EXTRA_ROW_ID) ?: 0
        val reminderIdentifier =
            intent?.extras?.getString(Constant.INTENT_EXTRA_REMINDER_IDENTIFIER)
        val title = intent?.extras?.getString(Constant.INTENT_EXTRA_TITLE)
        val productName = intent?.extras?.getString(Constant.INTENT_EXTRA_DESC_PRO_NAME)
        val productCategory = intent?.extras?.getString(Constant.INTENT_EXTRA_DESC_PRO_CATEGORY)
        val productQuantity = intent?.extras?.getString(Constant.INTENT_EXTRA_DESC_PRO_QUANTITY)
        val productPrice = intent?.extras?.getString(Constant.INTENT_EXTRA_DESC_PRO_PRICE)
        val productTotal = intent?.extras?.getString(Constant.INTENT_EXTRA_DESC_PRO_TOTAL)
        val productImage = intent?.extras?.getString(Constant.INTENT_EXTRA_DESC_PRO_IMAGE)

        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.putExtra(Constant.INTENT_EXTRA_REMINDER_IDENTIFIER, reminderIdentifier)

        val pendingIntent = PendingIntent.getActivity(this,
            notificationId,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)


        val notification: Notification = NotificationCompat.Builder(this,
            getString(com.example.customerfirebase.R.string.reminder_notification_channel_id))
            .setSmallIcon(com.example.customerfirebase.R.drawable.ic_notifications_smallicon)
            .setContentTitle(title)
            .setContentText(productName)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(productName + "\n" + "Category : " + productCategory + "\nQuantity : " + productQuantity + "\nPrice : " + productPrice +
                        "\nTotal : " + productTotal))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        //mediaPlayer!!.start()
        val pattern = longArrayOf(0, 100, 1000)
        vibrator!!.vibrate(pattern, 0)
        startForeground(1, notification)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        // mediaPlayer!!.stop()
        vibrator!!.cancel()
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}