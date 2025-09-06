package com.test.schedulerapp.workmanager

import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.test.schedulerapp.SchedulerApp
import com.test.schedulerapp.commonutils.GlobalConstant
import java.util.concurrent.TimeUnit

object WorkController {
    private val TAG = "[WorkController]"
    private val appContext = SchedulerApp.getAppContext()
    private val workManager = WorkManager.getInstance(appContext)


    fun initWork(pkg: String, timeInterval: Long) {
        Log.i(TAG, "initWork()::pkg - $pkg")
        //cancel existing work first by the same tag.
        cancelWork(pkg)

        //create work
        val inputData = workDataOf(
            GlobalConstant.WORK_TYPE_PACKAGE_NAME to pkg
        )

        val workRequest = OneTimeWorkRequestBuilder<BackgroundWorker>()
            .setInputData(inputData)
            .setInitialDelay(15, TimeUnit.MINUTES)
            .addTag(pkg)
            .build()

        workManager.enqueue(workRequest)
    }

    fun cancelWork(pkg: String) {
        Log.i(TAG, "cancelWork()::pkg-$pkg")
        workManager.cancelAllWorkByTag(pkg)
    }

    fun cancelAllWork() {
        //TODO::
    }
}