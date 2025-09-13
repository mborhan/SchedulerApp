package com.test.schedulerapp.ui.utils

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

class SharedViewModel(private val repository: AppListRepository) : ViewModel() {

    private val _apps = MutableLiveData<List<AppData>>()
    val apps: LiveData<List<AppData>> get() = _apps
    var appInfo: AppData? = null

    val allAppList = repository.allAppList
    var latestApplist: MutableList<AppListInfo> = mutableListOf()

    fun insert(appInfo: AppListInfo) = viewModelScope.launch {
        repository.insert(appInfo)
    }

    fun deleteByWorkerTagName(workerTagName: String) = viewModelScope.launch {
        repository.deleteByWorkerTagName(workerTagName)
    }

    fun deleteByPackageName(pkgName: String) = viewModelScope.launch {
        repository.deleteByPackageName(pkgName)
    }

    fun updateStatus(pkgName: String, newStatus: String) = viewModelScope.launch {
        repository.updateStatus(pkgName, newStatus)
    }

    fun isPackageExists(pkgName: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isExist = repository.isPackageExists(pkgName)
            onResult(isExist)
        }
    }

    fun loadApps() {
        viewModelScope.launch(Dispatchers.IO) {
            val appList = repository.getGetInstalledAppList()
            Log.i("TAG", "appList size - ${appList.size}")
            _apps.postValue(appList)   // update LiveData
        }
    }
}
