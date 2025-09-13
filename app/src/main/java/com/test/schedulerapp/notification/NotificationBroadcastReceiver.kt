package com.test.schedulerapp.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import com.test.schedulerapp.SchedulerApp
import com.test.schedulerapp.commonutils.GlobalConstant

class NotificationBroadcastReceiver : BroadcastReceiver() {
    private val TAG = "[NotificationBroadcastReceiver]"

    override fun onReceive(context: Context, intent: Intent) {
        val pkg = intent.getStringExtra(GlobalConstant.PACKAGE_NAME)
        Log.d(TAG, "Notification clicked with pkg: $pkg")
//        Toast.makeText(context, "Class event received", Toast.LENGTH_LONG).show()
        handleLaunching(intent)
    }

    private fun handleLaunching(intent: Intent) {
        val pkg = intent.getStringExtra(GlobalConstant.PACKAGE_NAME)
        Log.i(TAG, "pkg: $pkg")

        if (pkg == null) return

        val context = SchedulerApp.getAppContext()
        val pm: PackageManager = context.packageManager
        val launcherIntent = pm.getLaunchIntentForPackage(pkg)
        Log.i(TAG, "launcherIntent - $launcherIntent")

        try {
            launcherIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(launcherIntent)
            Log.i(TAG, "Launching success!")
        } catch (e: Exception) {
            Log.i(TAG, "error - $e")
        }
    }
}