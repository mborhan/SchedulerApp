package com.test.schedulerapp.ui.timepicker

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.test.schedulerapp.R
import com.test.schedulerapp.SchedulerApp
import com.test.schedulerapp.data.repository.AppListRepository
import com.test.schedulerapp.databinding.FragmentTimePickerBinding
import com.test.schedulerapp.db.AppDatabase
import com.test.schedulerapp.ui.applist.AppListViewModel
import com.test.schedulerapp.ui.utils.ViewModelFactory

class TimePickerFragment : Fragment() {
    private val TAG = "[TimePickerFragment]"

    private lateinit var binding: FragmentTimePickerBinding
    private lateinit var viewModel: AppListViewModel
    private lateinit var currentContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        currentContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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
            val msg = getString(R.string.time_is) + " " + time
            binding.timeText.text = msg
        }

        binding.setupButton.setOnClickListener {
            handleSetupButtonClicked()
        }
    }

    private fun initViewModel() {
        val dao = AppDatabase.getDatabase(SchedulerApp.getAppContext()).appListInfoDao()
        val repository = AppListRepository(currentContext, dao)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(requireActivity(), factory).get(AppListViewModel::class.java)
    }

    private fun handleSetupButtonClicked() {
        //TODO::
    }
}