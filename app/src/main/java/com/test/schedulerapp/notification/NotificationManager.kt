package com.test.schedulerapp.notification

import android.Manifest
import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationMgr(private val context: Context) {

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "my_channel_id",
                "MyChannel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel for demo notifications"
            }
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showNotification(message: String) {
        // Intent for BroadcastReceiver
        val broadcastIntent = Intent(context, NotificationBroadcastReceiver::class.java).apply {
            putExtra("message_body", message)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            broadcastIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, "my_channel_id")
            .setSmallIcon(R.drawable.ic_dialog_info)
            .setContentTitle("Notification Demo")
            .setContentText(message)
            .setContentIntent(pendingIntent)  // Tap action
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1001, builder.build())
    }
}


