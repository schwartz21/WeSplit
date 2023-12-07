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
            val recipientToken = "c5ROfnZGScuI_8OP2QLfxQ:APA91bGrLcDs9fp_yTwbsm23oZbsyC29_aZyrl6NVK4920hBn7xFW7_isT9MGU28azMXC0Oz3B521pBD9u9Flspp4MWBDpiTm0ths1xbONfrBK1XPBBixmqML8O-ksC_OXI_OqrnDPK8\n"

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
