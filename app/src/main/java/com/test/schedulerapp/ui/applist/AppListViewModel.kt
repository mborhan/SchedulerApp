package com.test.schedulerapp.ui.applist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.schedulerapp.data.model.AppData
import com.test.schedulerapp.data.repository.AppListRepository
import com.test.schedulerapp.db.data.model.AppListInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppListViewModel(private val repository: AppListRepository) : ViewModel() {

    private val _apps = MutableLiveData<List<AppData>>()
    val apps: LiveData<List<AppData>> get() = _apps

    val allAppList = repository.allAppList

    fun insert(appInfo: AppListInfo) = viewModelScope.launch {
        repository.insert(appInfo)
    }

    fun deleteByPackageName(pkgName: String) = viewModelScope.launch {
        repository.deleteByPackageName(pkgName)
    }

    fun loadApps() {
        viewModelScope.launch(Dispatchers.IO) {
            val appList = repository.getGetInstalledAppList()
            Log.i("TAG", "appList size - ${appList.size}")
            _apps.postValue(appList)   // update LiveData
        }
    }
}
