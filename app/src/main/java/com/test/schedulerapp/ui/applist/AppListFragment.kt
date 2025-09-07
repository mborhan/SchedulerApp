package com.test.schedulerapp.ui.applist

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.schedulerapp.SchedulerApp
import com.test.schedulerapp.data.repository.AppListRepository
import com.test.schedulerapp.databinding.FragmentAppListLayoutBinding
import com.test.schedulerapp.db.AppDatabase
import com.test.schedulerapp.db.data.model.AppListInfo
import com.test.schedulerapp.ui.utils.ViewModelFactory
import com.test.schedulerapp.workmanager.WorkController

class AppListFragment : Fragment() {
    private val TAG = "[AppListFragment]"

    private lateinit var binding: FragmentAppListLayoutBinding
    private var recyclerViewAdapter: AppItemViewAdapter? = null
    private lateinit var viewModel: AppListViewModel
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
        viewModel = ViewModelProvider(this, factory).get(AppListViewModel::class.java)
    }

    private fun handleItemClicked(position: Int) {
        Toast.makeText(context, "positin - $position", Toast.LENGTH_LONG).show()
        val clickedApp = viewModel.apps.value?.get(position)
        clickedApp?.let {
            WorkController.initWork(clickedApp.packageName, 15)
            val appInfo = AppListInfo(
                packageName = clickedApp.packageName,
                appName = clickedApp.text,
                status = "None"
            )
            viewModel.isPackageExists(clickedApp.packageName) {
                if (it) {
                    Log.i(TAG, "app info is already added in database.")
                } else {
                    viewModel.insert(appInfo)
                }
            }
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
            if(appList.isNotEmpty()){
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.loadApps()
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "AppListView"
    }
}