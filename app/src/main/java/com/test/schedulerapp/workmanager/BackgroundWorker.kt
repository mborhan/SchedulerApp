package com.test.schedulerapp.workmanager

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.test.schedulerapp.SchedulerApp
import com.test.schedulerapp.commonutils.GlobalConstant
import com.test.schedulerapp.notification.NotificationMgr

class BackgroundWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    private val TAG = "[BackgroundWorker]" + GlobalConstant.WORK_TAG

    override fun doWork(): Result {
        val workerTag = inputData.getString(GlobalConstant.WORK_TYPE_WORK_TAG_NAME)
        val pkg = inputData.getString(GlobalConstant.PACKAGE_NAME)
        Log.i(TAG, "workerTag: $workerTag, pkg: $pkg")

        if (pkg == null) return Result.failure()

        val context = SchedulerApp.getAppContext()
        val pm: PackageManager = context.packageManager
        val launcherIntent = pm.getLaunchIntentForPackage(pkg)
        Log.i(TAG, "launcherIntent - $launcherIntent")
        showNotificationForLaunchingIntent(pkg)
        try {
//            launcherIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            context.startActivity(launcherIntent)
//            Log.i(TAG, "Launching success!")
            return Result.success()
        } catch (e: Exception) {
            Log.i(TAG, "error - $e")
            showNotificationForLaunchingIntent(pkg)
            return Result.failure()
        }
    }

    private fun showNotificationForLaunchingIntent(pkg: String) {
        val context = SchedulerApp.getAppContext()
        val message = getAppNameFromPackage(context, pkg)
        NotificationMgr.createNotificationChannel(context)
        NotificationMgr.showNotification(context, "$message, launching error!", pkg)
    }

    private fun getAppNameFromPackage(context: Context, packageName: String): String? {
        return try {
            val pm: PackageManager = context.packageManager
            val applicationInfo = pm.getApplicationInfo(packageName, 0)
            pm.getApplicationLabel(applicationInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }
}

