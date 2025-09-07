package com.test.schedulerapp.data.repository

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.LiveData
import com.test.schedulerapp.data.model.AppData
import com.test.schedulerapp.db.dao.AppListInfoDao
import com.test.schedulerapp.db.data.model.AppListInfo

class AppListRepository(private val context: Context, private val appListInfoDao: AppListInfoDao) {

    val allAppList: LiveData<List<AppListInfo>> = appListInfoDao.getAllAppInfo()

    suspend fun insert(appInfo: AppListInfo) = appListInfoDao.insert(appInfo)

    //suspend fun delete(appListInfo: AppListInfo) = appListInfoDao.delete(appListInfo)
    suspend fun deleteByPackageName(pkgName: String) = appListInfoDao.deleteByPackageName(pkgName)

    suspend fun updateStatus(pkgName: String, newStatus: String) = appListInfoDao.updateStatus(pkgName, newStatus)

    suspend fun isPackageExists(pkgName: String): Boolean = appListInfoDao.isPackageExists(pkgName)

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