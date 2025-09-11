package com.test.schedulerapp.ui.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.test.schedulerapp.R
import com.test.schedulerapp.SchedulerApp
import com.test.schedulerapp.data.repository.AppListRepository
import com.test.schedulerapp.databinding.FragmentHomeBinding
import com.test.schedulerapp.db.AppDatabase
import com.test.schedulerapp.notification.NotificationMgr
import com.test.schedulerapp.ui.applist.AppListFragment
import com.test.schedulerapp.ui.utils.Navigator

class HomeFragment : Fragment() {
    private val TAG = "[HomeFragment]"
    private lateinit var binding: FragmentHomeBinding
    private lateinit var currentContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        currentContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initVar()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    101
                )
            }
        }
//Test notification
//        val ins = NotificationMgr(requireContext())
//        ins.createNotificationChannel()
//        ins.showNotification("App launching notification")
    }

    private fun initVar() {
        binding.showInstallApp.setOnClickListener {
            Navigator.startSecondLVL(
                requireActivity() as AppCompatActivity,
                AppListFragment(),
                true
            )
        }

        binding.showLaunchFailAppList.setOnClickListener {
            val dao = AppDatabase.getDatabase(SchedulerApp.getAppContext()).appListInfoDao()
            val repository = AppListRepository(currentContext, dao)

            val appInfo = repository.allAppList
            appInfo.observe(viewLifecycleOwner) { app ->
                Log.i(TAG, "observer is called.")
                app.forEach {
                    Log.i(TAG, "app name - ${it.appName}")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Home"
    }
}

