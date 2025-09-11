package com.test.schedulerapp.db.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_list_info")
data class AppListInfo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val packageName: String,
    val appName: String,
    val status: String,
    val workerTag: String
)
