package com.test.schedulerapp.ui.applist

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
import com.test.schedulerapp.data.repository.AppListRepository
import com.test.schedulerapp.databinding.FragmentAppListLayoutBinding
import com.test.schedulerapp.db.AppDatabase
import com.test.schedulerapp.ui.timepicker.TimePickerFragment
import com.test.schedulerapp.ui.utils.Navigator
import com.test.schedulerapp.ui.utils.SharedViewModel
import com.test.schedulerapp.ui.utils.ViewModelFactory

class AppListFragment : Fragment() {
    private val TAG = "[AppListFragment]"

    private lateinit var binding: FragmentAppListLayoutBinding
    private var recyclerViewAdapter: AppItemViewAdapter? = null
    private lateinit var viewModel: SharedViewModel
    private lateinit var currentContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        currentContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppListLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setRecyclerview()
    }

    private fun initViewModel() {
        val dao = AppDatabase.getDatabase(SchedulerApp.getAppContext()).appListInfoDao()
        val repository = AppListRepository(currentContext, dao)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(requireActivity(), factory).get(SharedViewModel::class.java)
    }

    private fun handleItemClicked(position: Int) {
        // Toast.makeText(context, "positin - $position", Toast.LENGTH_LONG).show()
        val clickedApp = viewModel.apps.value?.get(position)
        viewModel.appInfo = clickedApp

        clickedApp?.let {
            Navigator.startSecondLVL(
                requireActivity() as AppCompatActivity,
                TimePickerFragment(),
                true
            )
        }
    }

    private fun setRecyclerview() {
        binding.appItemRecyclerview.layoutManager = LinearLayoutManager(context)

        recyclerViewAdapter = AppItemViewAdapter(emptyList()) { position ->
            Log.i("TAG", "positin - $position")
            handleItemClicked(position)
        }

        // Setting the Adapter with the recyclerview
        binding.appItemRecyclerview.adapter = recyclerViewAdapter

        // observe LiveData
        viewModel.apps.observe(viewLifecycleOwner) { appList ->
            recyclerViewAdapter?.updateData(appList)
            if (appList.isNotEmpty()) {
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.loadApps()
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Installed apps"
    }
}