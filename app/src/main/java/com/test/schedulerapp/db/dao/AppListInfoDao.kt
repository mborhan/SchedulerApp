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

    @Query("SELECT * FROM app_list_info")
    fun getAllAppInfo(): LiveData<List<AppListInfo>>
}