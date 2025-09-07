package com.test.schedulerapp.ui.timepicker

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.test.schedulerapp.R
import com.test.schedulerapp.SchedulerApp
import com.test.schedulerapp.data.repository.AppListRepository
import com.test.schedulerapp.databinding.FragmentTimePickerBinding
import com.test.schedulerapp.db.AppDatabase
import com.test.schedulerapp.db.data.model.AppListInfo
import com.test.schedulerapp.ui.utils.SharedViewModel
import com.test.schedulerapp.ui.utils.ViewModelFactory
import com.test.schedulerapp.workmanager.WorkController

class TimePickerFragment : Fragment() {
    private val TAG = "[TimePickerFragment]"

    private lateinit var binding: FragmentTimePickerBinding
    private lateinit var viewModel: SharedViewModel
    private lateinit var currentContext: Context
    private var currentTime: Long = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        currentContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimePickerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()

        Log.i(TAG, "onViewCreated")
        viewModel.appInfo?.let {
            Log.i(TAG, "package: ${it.packageName}")
            binding.topImage.setImageDrawable(it.icon)
            binding.appName.text = it.text
        }

        binding.timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            val time = "$hourOfDay" + " : " + "$minute"
            currentTime = (hourOfDay * 60 + minute).toLong()
            val msg = getString(R.string.time_is) + " " + time
            binding.timeText.text = msg
        }

        binding.setupButton.setOnClickListener {
            if (currentTime <= 0) {
                Toast.makeText(currentContext, "Please pick the time first!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                handleSetupSchedule()
            }
        }

        binding.cancelButton.setOnClickListener {
            handleCancelSchedule()
        }
    }

    private fun initViewModel() {
        val dao = AppDatabase.getDatabase(SchedulerApp.getAppContext()).appListInfoDao()
        val repository = AppListRepository(currentContext, dao)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(requireActivity(), factory).get(SharedViewModel::class.java)
    }

    private fun handleCancelSchedule() {
        Log.i(TAG, "handleCancelSchedule")

        viewModel.appInfo?.let { app ->
            WorkController.cancelWork(app.packageName)
            Log.i(TAG, "app name: ${app.text}")
            viewModel.isPackageExists(app.packageName) {
                if (it) {
                    Log.i(TAG, "app info is already available in database.")
                    viewModel.deleteByPackageName(app.packageName)
                    Toast.makeText(currentContext, "Cancel the schedule successfully!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun handleSetupSchedule() {
        Log.i(TAG, "handleSetupButtonClicked: current time - $currentTime")
        viewModel.appInfo?.let { app ->
            WorkController.initWork(app.packageName, currentTime)
            val appInfo = AppListInfo(
                packageName = app.packageName, appName = app.text, status = "Set"
            )
            Log.i(TAG, "app name: ${app.text}")
            viewModel.isPackageExists(app.packageName) {
                if (it) {
                    Log.i(TAG, "app info is already added in database.")
                } else {
                    Log.i(TAG, "app info added in db.")
                    viewModel.insert(appInfo)
                    Toast.makeText(currentContext, "Save the schedule successfully!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}