package com.example.notweshare.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.notweshare.MainActivity
import com.example.notweshare.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FirebaseService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle the incoming message
        println("onMessageRecived")
        Log.d("NotificationService", "Notification received")
        val notificationData = remoteMessage.notification
        // Extract relevant information from notificationData
        // Show a notification
        if (notificationData != null) {
            showNotification(notificationData)
        }
    }

    private fun showNotification(notificationData: RemoteMessage.Notification) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel for Android Oreo and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "channel_id"
            val channelName = "Channel Name"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(channel)
        }

        // Create an Intent for the notification
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val openAppIntent = Intent().apply {
            setPackage("dk.danskebank.mobilepay");
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            1,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT + PendingIntent.FLAG_IMMUTABLE
        )

        val activityPendingIntent = PendingIntent.getActivity(
            this,
            1,
            intent,
            //flag to control version
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val dismissIntent = PendingIntent.getBroadcast(
            this,
            1,  // Use a unique request code
            Intent(this, NotificationDismissReceiver::class.java),
            // flag to control version
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        // Build the notification
        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(notificationData.body)
        val notificationBuilder = NotificationCompat.Builder(this, "channel_id")
            .setSmallIcon(R.drawable.notification)
            .setContentTitle(notificationData.title)
            .setContentText(notificationData.body)
            .setContentIntent(activityPendingIntent)
            .addAction(R.drawable.notification, "Not now", dismissIntent)  // Add dismiss action
            .addAction(R.drawable.notification, "Pay up", pendingIntent )
            .setStyle(bigTextStyle)
            .build()

        // Show the notification
        notificationManager.notify(0, notificationBuilder)

        Log.d("MyFirebaseMessagingService", "onMessageReceived")

    }

}