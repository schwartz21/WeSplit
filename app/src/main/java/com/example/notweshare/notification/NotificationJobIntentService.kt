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
        val token = (intent.getStringExtra("token")?: notFound)

        Log.d("NotificationService", "title: $title")
        Log.d("NotificationService", "body: $body")

        if (body == notFound || title == notFound || token == notFound){
            Log.e("NotificationService","Notification was sent without title, body or token" )
            return
        }

        sendFCMMessage(token,title, body)
    }

    private fun sendFCMMessage(token: String, title: String, body: String) {
        try {

            val data = hashMapOf(
                "title" to title,
                "body" to body,
                "tokens" to listOf(token)
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
