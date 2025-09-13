package com.test.schedulerapp.workmanager

import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.test.schedulerapp.SchedulerApp
import com.test.schedulerapp.commonutils.GlobalConstant
import java.util.concurrent.TimeUnit

object WorkController {
    private val TAG = "[WorkController]" + GlobalConstant.WORK_TAG
    private val appContext = SchedulerApp.getAppContext()
    private val workManager = WorkManager.getInstance(appContext)


    fun initWork(workTag: String, timeInterval: Long, pkg: String) {
        Log.i(TAG, "initWork()::workTag - $workTag, pkg - $pkg, timeInterval - $timeInterval")
        //cancel existing work first by the same tag.
        cancelWork(workTag)

        //create work
        val inputData = workDataOf(
            GlobalConstant.WORK_TYPE_WORK_TAG_NAME to workTag, GlobalConstant.PACKAGE_NAME to pkg
        )

        val workRequest =
            OneTimeWorkRequestBuilder<BackgroundWorker>().setInputData(inputData).setInitialDelay(
                timeInterval, TimeUnit.MINUTES
            ).addTag(workTag).build()

        workManager.enqueue(workRequest)
    }

    fun cancelWork(workTag: String) {
        Log.i(TAG, "cancelWork()::workTag-$workTag")
        workManager.cancelAllWorkByTag(workTag)
    }
}