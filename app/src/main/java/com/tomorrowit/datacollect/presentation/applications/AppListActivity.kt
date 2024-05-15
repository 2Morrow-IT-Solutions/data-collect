package com.tomorrowit.datacollect.presentation.applications

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.tomorrowit.datacollect.databinding.ActivityAppListBinding
import com.tomorrowit.datacollect.domain.models.ApplicationInfoModel
import com.tomorrowit.datacollect.presentation.adapters.RecyclerViewAdapterApplications
import com.tomorrowit.datacollect.utils.ViewHelper
import com.tomorrowit.datacollect.utils.extensions.finishAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class AppListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppListBinding
    private val viewModel: ApplicationsViewModel by viewModels()

    private lateinit var recyclerViewAdapter: RecyclerViewAdapterApplications
    private var applicationList: MutableList<ApplicationInfoModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeState()

        recyclerViewAdapter = RecyclerViewAdapterApplications(
            this@AppListActivity,
            applicationList
        )
        binding.activityApplicationsToolbar.setNavigationOnClickListener {
            this.finish()
        }

        binding.activityApplicationsRv.adapter = recyclerViewAdapter
    }

    override fun finish() {
        super.finish()
        finishAnimation()
    }

    private fun observeState() {
        viewModel.apps.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).distinctUntilChanged()
            .onEach { apps ->
                when (apps) {
                    is ApplicationsState.IsLoading -> {
                        showLoading()
                    }

                    is ApplicationsState.ShowList -> {
                        showList()
                        showList(apps.list)
                    }
                }
            }.launchIn(lifecycleScope)
    }

    private suspend fun showList(apps: List<ApplicationInfoModel>) {
        withContext(Dispatchers.Main) {
            applicationList.clear()
            applicationList.addAll(apps)
        }
    }

    private suspend fun showLoading() {
        withContext(Dispatchers.Main) {
            ViewHelper.showView(binding.activityApplicationsLoading.root)
            ViewHelper.hideView(binding.activityApplicationsRv)
        }
    }

    private suspend fun showList() {
        withContext(Dispatchers.Main) {
            ViewHelper.showView(binding.activityApplicationsRv)
            ViewHelper.hideView(binding.activityApplicationsLoading.root)
        }
    }
}