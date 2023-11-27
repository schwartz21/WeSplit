package com.example.notweshare.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.example.notweshare.R
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.notweshare.MainActivity

class NotificationService (private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    fun showNotification(name: String, amountOfMoneyOwed: String, groupName: String) {

        val activityIntent = Intent(context, MainActivity::class.java)

        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            //flag to control version
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val dismissIntent = PendingIntent.getBroadcast(
            context,
            1,  // Use a unique request code
            Intent(context, NotificationDismissReceiver::class.java),
            // flag to control version
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val notification = NotificationCompat.Builder(context, COUNTER_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_airplanemode_active_24)
            .setContentTitle("$groupName")
            .setContentText("you owe $amountOfMoneyOwed to $name")
            .setContentIntent(activityPendingIntent)
            .addAction(R.drawable.baseline_airplanemode_active_24, "DISMISS", dismissIntent)  // Add dismiss action
            .build()

        notificationManager.notify(1, notification)
    }



    companion object {
        const val COUNTER_CHANNEL_ID = "counter_channel"
    }
}