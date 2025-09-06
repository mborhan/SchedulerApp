package com.test.schedulerapp

import android.app.Application
import android.content.Context

class SchedulerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private lateinit var instance: SchedulerApp

        fun getAppContext(): Context = instance.applicationContext
    }
}
