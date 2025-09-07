package com.test.schedulerapp.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.test.schedulerapp.db.data.model.AppListInfo

@Dao
interface AppListInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(appListInfo: AppListInfo)

    @Query("DELETE FROM app_list_info WHERE packageName = :pkgName")
    suspend fun deleteByPackageName(pkgName: String)

    @Query("UPDATE app_list_info SET status = :newStatus WHERE packageName = :pkgName")
    suspend fun updateStatus(pkgName: String, newStatus: String)

    @Query("SELECT * FROM app_list_info")
    fun getAllAppInfo(): LiveData<List<AppListInfo>>

    @Query("SELECT EXISTS(SELECT 1 FROM app_list_info WHERE packageName = :pkgName LIMIT 1)")
    suspend fun isPackageExists(pkgName: String): Boolean

}