package com.test.schedulerapp.ui.updateschedule

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.schedulerapp.SchedulerApp
import com.test.schedulerapp.commonutils.ScheduleState
import com.test.schedulerapp.commonutils.TimeCalculation
import com.test.schedulerapp.data.model.ScheduleItem
import com.test.schedulerapp.data.repository.AppListRepository
import com.test.schedulerapp.databinding.FragmentUpdateScheduleBinding
import com.test.schedulerapp.db.AppDatabase
import com.test.schedulerapp.db.data.model.AppListInfo
import com.test.schedulerapp.ui.utils.SharedViewModel
import com.test.schedulerapp.ui.utils.ViewModelFactory
import com.test.schedulerapp.workmanager.WorkController
import java.util.Calendar

class UpdateScheduleFragment : Fragment() {

    private lateinit var binding: FragmentUpdateScheduleBinding
    private lateinit var adapter: UpdateScheduleAdapter
    private lateinit var currentContext: Context
    private lateinit var viewModel: SharedViewModel
    private var appName = "appName"
    private var TAG = "[UpdateScheduleFragment]"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        currentContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        setRecyclerview()

        viewModel.appInfo?.let {
            binding.topImage.setImageDrawable(it.icon)
            binding.appName.text = it.text
            appName = it.text
        }

        viewModel.allAppList.observe(viewLifecycleOwner) {
            val scheduleItemList: MutableList<ScheduleItem> = mutableListOf()

            viewModel.latestApplist = mutableListOf()
            it.forEach {
                Log.i(TAG, "packagename - ${it.packageName}, workerTag - ${it.workerTag}")
                Log.i(TAG, "hourOfDay: ${it.hourOfDay}, minute: ${it.minute}")

                if (it.packageName.equals(viewModel.appInfo?.packageName)) {
                    val item = ScheduleItem("${it.hourOfDay}" + ":" + "${it.minute}")
                    scheduleItemList.add(item)
                    viewModel.latestApplist.add(it)
                }
            }

            adapter.updateData(scheduleItemList)
            updateTitleText()
        }
    }

    private fun updateTitleText() {
        if (viewModel.latestApplist.size > 0) {
            binding.titleText.text = "Press edit button for update any below schedule"
        } else {
            binding.titleText.text = "There is no available schedule for updating"
        }
    }

    private fun initViewModel() {
        val dao = AppDatabase.getDatabase(SchedulerApp.getAppContext()).appListInfoDao()
        val repository = AppListRepository(currentContext, dao)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(requireActivity(), factory).get(SharedViewModel::class.java)
    }

    private fun setRecyclerview() {

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = UpdateScheduleAdapter(emptyList()) { position ->
            openTimePicker(position)
        }

        binding.recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Update schedule"
    }

    private fun openTimePicker(position: Int) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                adapter.updateTime(position, formattedTime)

                val app = viewModel.latestApplist.get(position)
                try {
                    val workerTagName = viewModel.latestApplist.get(position).workerTag
                    Log.i(TAG, "workerTagName - $workerTagName")

                    //cancel
                    viewModel.deleteByWorkerTagName(workerTagName)
                    WorkController.cancelWork(workerTagName)

                    //update
                    val nextScheduleTime =
                        TimeCalculation.calculateDelayInMinutes(selectedHour, selectedMinute)
                    Log.i(TAG, "nextScheduleTime - $nextScheduleTime")

                    val updatedWorkerTagName =
                        app.packageName + "${TimeCalculation.getCurrentTimeStamp()}"
                    WorkController.initWork(updatedWorkerTagName, nextScheduleTime, app.packageName)

                    val appInfo = AppListInfo(
                        packageName = app.packageName,
                        appName = appName,
                        status = ScheduleState.SET.name,
                        workerTag = workerTagName,
                        hourOfDay = selectedHour,
                        minute = selectedMinute
                    )
                    viewModel.insert(appInfo)

                } catch (e: Exception) {
                    Log.i(TAG, "error - $e")
                }

            },
            hour,
            minute,
            true
        )
        timePicker.show()
    }
}

