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
        val body = "BODY YESYT"
        val title = "TEST"

        val recipientToken = listOf("fNOlTce2QVSK4DjOO6uCLb:APA91bEODu_pKqkGLgjYulyFRwEti_kE_C402BSSM1rDq_g2hpc3xAb2_373iM9pPpPLSYvlCiHmKofAzCFPZGYXWrpI8WhO1yTioG80ylc3QI_eRWfP2ltrEsbKrTiMUWPKXEbQGoZD")

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
