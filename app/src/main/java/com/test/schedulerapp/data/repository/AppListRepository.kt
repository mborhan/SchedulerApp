package com.test.schedulerapp.data.repository

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import com.test.schedulerapp.data.model.AppData

class AppListRepository(private val context: Context) {

    fun getGetInstalledAppList(): List<AppData> {
        val pm: PackageManager = context.packageManager

        // Query installed applications
        val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)

        val apps = mutableListOf<AppData>()

        for (packageInfo in packages) {
            // Exclude system apps (optional)
            if (pm.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                val appName = pm.getApplicationLabel(packageInfo).toString()
                val appIcon = pm.getApplicationIcon(packageInfo)
                val packageName = packageInfo.packageName

                apps.add(
                    AppData(
                        0,
                        appName,
                        packageName,
                        appIcon
                    )
                )
            }
        }
        Log.i("TAG", "appList size - ${apps.size}")
        return apps
    }
}