package com.test.schedulerapp.ui.applist

import android.content.Context
import android.content.pm.PackageManager
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
import com.test.schedulerapp.data.model.AppData
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val view = inflater.inflate(R.layout.fragment_app_list_layout, container, false)
//        return view

        binding = FragmentAppListLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerview()
    }

    private fun setRecyclerview() {
//        // getting the recyclerview by its id
//        val recyclerview: RecyclerView = findViewById(R.id.recyclerview)

        // this creates a vertical layout Manager
//        recyclerview.layoutManager = LinearLayoutManager(this)
        binding.appItemRecyclerview.layoutManager = LinearLayoutManager(context)

        // ArrayList of class ItemsViewModel
        val data = ArrayList<AppData>()

        // This loop will create 20 Views containing
        // the image with the count of view
//        for (i in 1..30) {
//            data.add(AppData(R.drawable.ic_launcher_foreground, "Item $i"))
//        }

        // This will pass the ArrayList to our Adapter
        recyclerViewAdapter = AppItemViewAdapter(emptyList()) { position ->
            Log.i("TAG", "positin - $position")
            Toast.makeText(context, "positin - $position", Toast.LENGTH_LONG).show()
            val clickedApp = viewModel.apps.value?.get(position)
            val pm: PackageManager = currentContext.packageManager
//            clickedApp?.let {
//                val launchIntent = pm.getLaunchIntentForPackage(it.packageName)
//                startActivity(launchIntent)
//            }

            clickedApp?.let {
                WorkController.initWork(clickedApp.packageName, 15)
                val appInfo = AppListInfo(
                    packageName = clickedApp.packageName,
                    appName = clickedApp.text,
                    status = "None"
                )
                viewModel.deleteByPackageName(clickedApp.packageName)
                viewModel.insert(appInfo)
            }
        }

        // Setting the Adapter with the recyclerview
        binding.appItemRecyclerview.adapter = recyclerViewAdapter
        binding.appItemRecyclerview.scrollToPosition(12)
//        binding.appItemRecyclerview.isFocusableInTouchMode = true
//        binding.appItemRecyclerview.requestFocus()


        // init ViewModel
        //context
        val dao = AppDatabase.getDatabase(SchedulerApp.getAppContext()).appListInfoDao()
        val repository = AppListRepository(currentContext, dao)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(AppListViewModel::class.java)

        val appInfo = viewModel.allAppList
        appInfo.observe(viewLifecycleOwner) { app ->
            app.forEach {
                Log.i(TAG, "app name - ${it.appName}")
            }
        }

        // observe LiveData
        viewModel.apps.observe(viewLifecycleOwner) { appList ->
            recyclerViewAdapter?.updateData(appList)

//            //
//            binding.appItemRecyclerview.post {
//                val layoutManager = binding.appItemRecyclerview.layoutManager as LinearLayoutManager
//                val view = layoutManager.findViewByPosition(12)
//                view?.let {
//                    binding.nestedScrollView.smoothScrollTo(0, it.top)
//                }
//            }
        }

        viewModel.loadApps()

    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "AppListView"
    }
}