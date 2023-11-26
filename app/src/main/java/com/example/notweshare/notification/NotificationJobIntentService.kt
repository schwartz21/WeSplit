package com.example.notweshare.notification

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.FirebaseMessaging

class NotificationJobIntentService : JobIntentService() {

    companion object {
        private const val JOB_ID = 1000

        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, NotificationJobIntentService::class.java, JOB_ID, work)
        }
    }

    override fun onHandleWork(intent: Intent) {
        // Get data from the intent
        val title = intent.getStringExtra("title")
        val body = intent.getStringExtra("body")

        // Send FCM message
        sendFCMMessage(title, body)
    }

    private fun sendFCMMessage(title: String?, body: String?) {
        try {
            // The FCM token of the target device
            val recipientToken = "foUVV2vTTAmVDnXrg8m6vb:APA91bGVqyPpOr7xacmoR3m1Hl-kHMP0vWQG-eQy1BK2edcGjePpDaQ4KaypcdH4hzEL6YqmVqXaiELkZa36G8nCkgYX-q7vyZ6pObRHS-cRTdCDM3Vh6l_pmYgVuflLvBFFvP9i9dQP"

            // Create a RemoteMessage
            val message = RemoteMessage.Builder(recipientToken)
                .setMessageId(java.lang.String.valueOf(System.currentTimeMillis()))
                .setData(mapOf("title" to title, "body" to body))
                .build()

            // Send the FCM message
            FirebaseMessaging.getInstance().send(message)

            Log.d("NotificationService", "FCM message sent successfully")
        } catch (e: Exception) {
            Log.e("NotificationService", "Error sending FCM message", e)
        }
    }
}
