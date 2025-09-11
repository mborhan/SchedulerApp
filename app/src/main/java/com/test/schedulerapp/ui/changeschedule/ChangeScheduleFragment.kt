package com.test.schedulerapp.ui.changeschedule

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.schedulerapp.SchedulerApp
import com.test.schedulerapp.data.model.TimeSwitchItem
import com.test.schedulerapp.data.repository.AppListRepository
import com.test.schedulerapp.databinding.FragmentChangeScheduleBinding
import com.test.schedulerapp.db.AppDatabase
import com.test.schedulerapp.ui.utils.SharedViewModel
import com.test.schedulerapp.ui.utils.ViewModelFactory

class ChangeScheduleFragment : Fragment() {
    private lateinit var binding: FragmentChangeScheduleBinding
    private lateinit var currentContext: Context
    private lateinit var viewModel: SharedViewModel
    private var timeSwitchItemAdapter: TimeSwitchAdapter? = null
    private var TAG = "[ChangeScheduleFragment]"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        currentContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeScheduleBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setRecyclerview()
        viewModel.appInfo?.let {
            binding.topImage.setImageDrawable(it.icon)
            binding.appName.text = it.text
        }

        viewModel.allAppList.observe(viewLifecycleOwner) {
            val timeList: MutableList<TimeSwitchItem> = mutableListOf()

            it.forEach {
                Log.i(TAG, "packagename - ${it.packageName}, workerTag - ${it.workerTag}")
                Log.i(TAG, "hourOfDay: ${it.hourOfDay}, minute: ${it.minute}")
                val item = TimeSwitchItem("${it.hourOfDay}" + ":" + "${it.minute}", true)
                timeList.add(item)
            }

            timeSwitchItemAdapter?.updateData(timeList)
        }
    }

    private fun initViewModel() {
        val dao = AppDatabase.getDatabase(SchedulerApp.getAppContext()).appListInfoDao()
        val repository = AppListRepository(currentContext, dao)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(requireActivity(), factory).get(SharedViewModel::class.java)
    }

    private fun setRecyclerview() {

        binding.itemRecyclerview.layoutManager = LinearLayoutManager(context)
        timeSwitchItemAdapter = TimeSwitchAdapter(emptyList()) { item, isChecked ->
            if (!isChecked) {
                viewModel.appInfo?.let {
                    viewModel.deleteByPackageName(it.packageName)
                }
            }
        }

        binding.itemRecyclerview.adapter = timeSwitchItemAdapter
    }
}