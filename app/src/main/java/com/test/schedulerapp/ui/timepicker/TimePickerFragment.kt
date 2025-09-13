package com.test.schedulerapp.ui.timepicker

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.test.schedulerapp.R
import com.test.schedulerapp.SchedulerApp
import com.test.schedulerapp.commonutils.ScheduleState
import com.test.schedulerapp.commonutils.TimeCalculation
import com.test.schedulerapp.data.repository.AppListRepository
import com.test.schedulerapp.databinding.FragmentTimePickerBinding
import com.test.schedulerapp.db.AppDatabase
import com.test.schedulerapp.db.data.model.AppListInfo
import com.test.schedulerapp.ui.changeschedule.ChangeScheduleFragment
import com.test.schedulerapp.ui.utils.Navigator
import com.test.schedulerapp.ui.utils.SharedViewModel
import com.test.schedulerapp.ui.utils.ViewModelFactory
import com.test.schedulerapp.workmanager.WorkController

class TimePickerFragment : Fragment() {
    private val TAG = "[TimePickerFragment]"

    private lateinit var binding: FragmentTimePickerBinding
    private lateinit var viewModel: SharedViewModel
    private lateinit var currentContext: Context
    private var nextScheduleTime: Long = 0
    private var hourOfDay: Int = 0
    private var minute: Int = 0

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
            this.hourOfDay = hourOfDay
            this.minute = minute

            val msg = getString(R.string.time_is) + " " + time
            binding.timeText.text = msg

            nextScheduleTime = TimeCalculation.calculateDelayInMinutes(hourOfDay, minute)
            Log.i(TAG, "nextScheduleTime - $nextScheduleTime")
        }

        binding.setupButton.setOnClickListener {
            if (nextScheduleTime <= 0) {
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
        Navigator.startSecondLVL(
            requireActivity() as AppCompatActivity,
            ChangeScheduleFragment(),
            true
        )
    }

    private fun handleSetupSchedule() {
        Log.i(TAG, "handleSetupButtonClicked: current time - $nextScheduleTime")
        viewModel.appInfo?.let { app ->
            val workerTagName = app.packageName + "${TimeCalculation.getCurrentTimeStamp()}"
            WorkController.initWork(workerTagName, nextScheduleTime, app.packageName)

            val appInfo = AppListInfo(
                packageName = app.packageName,
                appName = app.text,
                status = ScheduleState.SET.name,
                workerTag = workerTagName,
                hourOfDay = hourOfDay,
                minute = minute
            )

            Log.i(TAG, "app name: ${app.text}")
            viewModel.insert(appInfo)

            Toast.makeText(
                currentContext,
                "Save the schedule successfully!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Time picker"
    }
}