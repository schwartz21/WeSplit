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
    fun showNotification(name: String, amountOfMoneyOwed: String, groupName: String, nameOfDeptPerson: String) {

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

<<<<<<< HEAD
        val doMagic = PendingIntent.getBroadcast(
=======
        val fuck = PendingIntent.getBroadcast(
>>>>>>> ca992e84d75e20e1c143ba62aa2c143d3f239156
            context,
            2,  // Use a unique request code
            Intent(context, NotificationDismissReceiver::class.java),
            // flag to control version
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText("you owe $amountOfMoneyOwed to $name in group: $groupName ")
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("$nameOfDeptPerson!!!!!")
            .setContentText("you owe $amountOfMoneyOwed to $name in group: $groupName ")
            .setContentIntent(activityPendingIntent)
            .addAction(R.drawable.notification, "DISMISS", dismissIntent)  // Add dismiss action
<<<<<<< HEAD
            .addAction(R.drawable.notification, "THIS Button does nothing", doMagic )
=======
            .addAction(R.drawable.notification, "FUCK YOU", fuck)
>>>>>>> ca992e84d75e20e1c143ba62aa2c143d3f239156
            .setStyle(bigTextStyle)
            .build()

        notificationManager.notify(1, notification)
    }



    companion object {
        const val CHANNEL_ID = "channel"
    }
}