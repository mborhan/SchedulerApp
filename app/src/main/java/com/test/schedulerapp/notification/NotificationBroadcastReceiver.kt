package com.test.schedulerapp.notification

import android.content.Context
import android.content.Intent
import android.util.Log
import android.content.BroadcastReceiver
import android.widget.Toast

class NotificationBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra("message_body")
        Log.d("MyBroadcastReceiver", "Notification clicked with message: $message")
        Toast.makeText(context, "Class event received", Toast.LENGTH_LONG).show()
    }

}