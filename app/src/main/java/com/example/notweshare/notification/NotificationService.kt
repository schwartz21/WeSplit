package com.example.notweshare.notification

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import com.example.notweshare.models.Group
import com.example.notweshare.models.User


class NotificationService (private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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