package com.test.schedulerapp.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.test.schedulerapp.R
import com.test.schedulerapp.SchedulerApp
import com.test.schedulerapp.data.repository.AppListRepository
import com.test.schedulerapp.databinding.FragmentHomeBinding
import com.test.schedulerapp.db.AppDatabase
import com.test.schedulerapp.ui.applist.AppListFragment

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
    }

    private fun initVar() {
        binding.showInstallApp.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AppListFragment())
                .addToBackStack(null)
                .commit()
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

