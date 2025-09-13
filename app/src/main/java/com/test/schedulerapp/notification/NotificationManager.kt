package com.test.schedulerapp.notification

//import android.Manifest
//import android.R
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Intent
//import android.os.Build
//import androidx.annotation.RequiresPermission
//import androidx.core.app.NotificationCompat
//import androidx.core.app.NotificationManagerCompat
//
//class NotificationMgr(private val context: Context) {
//
//    fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                "my_channel_id",
//                "MyChannel",
//                NotificationManager.IMPORTANCE_DEFAULT
//            ).apply {
//                description = "Channel for demo notifications"
//            }
//            val manager =
//                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            manager.createNotificationChannel(channel)
//        }
//    }
//
//    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
//    fun showNotification(message: String) {
//        // Intent for BroadcastReceiver
//        val broadcastIntent = Intent(context, NotificationBroadcastReceiver::class.java).apply {
//            putExtra("message_body", message)
//        }
//
//        val pendingIntent = PendingIntent.getBroadcast(
//            context,
//            0,
//            broadcastIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        val builder = NotificationCompat.Builder(context, "my_channel_id")
//            .setSmallIcon(R.drawable.ic_dialog_info)
//            .setContentTitle("Notification Demo")
//            .setContentText(message)
//            .setContentIntent(pendingIntent)  // Tap action
//            .setAutoCancel(true)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//        val notificationManager = NotificationManagerCompat.from(context)
//        notificationManager.notify(1001, builder.build())
//    }
//}

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.test.schedulerapp.R

object NotificationMgr {

    private const val CHANNEL_ID = "my_channel_id"
    private const val CHANNEL_NAME = "My Notifications"
    private const val CHANNEL_DESC = "Channel for demo notifications"
    private const val NOTIFICATION_ID = 1001

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESC
            }

            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    fun showNotification(context: Context, message: String) {
        // Intent for BroadcastReceiver (or Activity if you want)
        val broadcastIntent = Intent(context, NotificationBroadcastReceiver::class.java).apply {
            putExtra("message_body", message)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            broadcastIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // use your own app icon
            .setContentTitle("Scheduler app demo")
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }
}



