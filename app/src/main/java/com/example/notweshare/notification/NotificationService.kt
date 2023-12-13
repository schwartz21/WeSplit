package com.example.notweshare.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.notweshare.MainActivity
import com.example.notweshare.R
import com.example.notweshare.models.Group
import com.example.notweshare.models.User


class NotificationService (private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    fun DEPRECATEDshowNotification(name: String, amountOfMoneyOwed: String, groupName: String, nameOfOwingPerson: String) {

        val activityIntent = Intent(context, MainActivity::class.java)

        val openAppIntent = Intent().apply {
            setPackage("dk.danskebank.mobilepay");
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT + PendingIntent.FLAG_IMMUTABLE
        )

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

        val notificationText = "Hi $nameOfOwingPerson, $name is asking you to pay the $amountOfMoneyOwed kr. that you owe in the group: $groupName "

        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(notificationText)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("You owe money!")
            .setContentText(notificationText)
            .setContentIntent(activityPendingIntent)
            .addAction(R.drawable.notification, "Not now", dismissIntent)  // Add dismiss action
            .addAction(R.drawable.notification, "Pay up", pendingIntent )
            .setStyle(bigTextStyle)
            .build()

        notificationManager.notify(1, notification)
    }

    fun showNotification(name: String, amountOfMoneyOwed: String, group: Group, owingPerson: User){
        val intent = Intent(context, NotificationJobIntentService::class.java)

        val notificationText = "Hi ${owingPerson.name}, $name is asking you to pay the $amountOfMoneyOwed kr. that you owe in the group: ${group.name} "
        val title = "You owe money!"

        intent.putExtra("title", title)
        intent.putExtra("body", notificationText)
        if(owingPerson.userToken.isNotEmpty()){
            intent.putExtra("token", owingPerson.userToken)
            // Enqueue the work to be done by the service
            NotificationJobIntentService.enqueueWork(context, intent)
        }
    }



    companion object {
        const val CHANNEL_ID = "channel"
    }
}