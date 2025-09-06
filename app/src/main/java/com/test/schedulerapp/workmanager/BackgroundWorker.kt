package com.test.schedulerapp.workmanager

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.test.schedulerapp.commonutils.GlobalConstant

class BackgroundWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    private val TAG = "[BackgroundWorker]"

    override fun doWork(): Result {
        val data = inputData.getString(GlobalConstant.WORK_TYPE_PACKAGE_NAME)
        Log.i(TAG, "data: $data")
        Toast.makeText(applicationContext, "data - $data", Toast.LENGTH_LONG).show()

        return try {
            // Returning success after the operation is done
            Result.success()
        } catch (e: Exception) {
            // Handling failure in case of an exception
            Result.failure()
        }
    }
}

