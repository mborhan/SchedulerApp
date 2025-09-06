package com.test.schedulerapp.ui.applist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.schedulerapp.data.model.AppData
import com.test.schedulerapp.data.repository.AppListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AppListViewModel(private val repository: AppListRepository) : ViewModel() {

    private val _apps = MutableLiveData<List<AppData>>()
    val apps: LiveData<List<AppData>> get() = _apps

    fun loadApps() {
        GlobalScope.launch(Dispatchers.IO) {
            val appList = repository.getGetInstalledAppList()
            Log.i("TAG", "appList size - ${appList.size}")
            _apps.postValue(appList)   // update LiveData
        }
    }
}
