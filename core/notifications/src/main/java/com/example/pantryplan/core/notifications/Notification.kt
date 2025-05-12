package com.example.pantryplan.core.notifications

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class Notification : BroadcastReceiver() {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) {
            Log.e(
                "Notifications",
                "Either context or intent are null in core.notifications.Notifications.onReceive. Cannot notify"
            )
            return
        }

        val notificationId = intent.getStringExtra("ID") ?: ""
        val channelId = intent.getStringExtra("CHANNEL_ID") ?: ""
        val priority = intent.getIntExtra("PRIORITY", NotificationCompat.PRIORITY_DEFAULT)
        val title = intent.getStringExtra("TITLE") ?: ""
        val body = intent.getStringExtra("BODY") ?: ""

        // Build the notification using NotificationCompat.Builder
        val notification = NotificationCompat.Builder(context, channelId)
            //.setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(intent.getStringExtra(title)) // Set title from intent
            .setContentText(intent.getStringExtra(body)) // Set content text from intent
            .setPriority(priority)
            .build()

        // Get the NotificationManager service
        val manager = NotificationManagerCompat.from(context)

        // Show the notification using the manager
        manager.notify(notificationId.hashCode(), notification)
    }
}