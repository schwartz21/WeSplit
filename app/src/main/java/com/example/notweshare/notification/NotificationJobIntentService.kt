package com.example.notweshare.notification

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.google.firebase.functions.FirebaseFunctions

class NotificationJobIntentService : JobIntentService() {

    companion object {
        private const val JOB_ID = 1000

        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, NotificationJobIntentService::class.java, JOB_ID, work)
        }
    }

    override fun onHandleWork(intent: Intent) {
        val notFound = "null"
        val title = (intent.getStringExtra("title")?: notFound)
        val body = (intent.getStringExtra("body")?: notFound)

        Log.d("NotificationService", "title: $title")
        Log.d("NotificationService", "body: $body")

        if (body == notFound || title == notFound){
            Log.e("NotificationService","Notification was sent without title or body" )
            return
        }

        val recipientToken = listOf("c51PEdblRh6cSrBWm6wLn5:APA91bFUnkm-TMBgl8nOYMk4NTj7ydqqftRv15FTCm8YiCo2DZWHWIAlQOvMa9CKnZAtwURHC187IZnSDkoZreR-6YGMWk5Bp6XvyRINLC30R3MjotQA-lAEb0d3ZbdLSCHbapVm9I4q")

        sendFCMMessage(recipientToken,title, body)
    }

    private fun sendFCMMessage(token: List<String>, title: String, body: String) {
        try {

            val data = hashMapOf(
                "title" to title,
                "body" to body,
                "tokens" to token
            )
            FirebaseFunctions
                .getInstance()
                .getHttpsCallable("sendNotification")
                .call(data)

            Log.d("NotificationService", "FCM message sent successfully")
        } catch (e: Exception) {
            Log.e("NotificationService", "Error sending FCM message", e)
        }
    }
}
