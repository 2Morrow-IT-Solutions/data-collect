package com.tomorrowit.datacollect.presentation.software

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.tomorrowit.datacollect.databinding.ActivitySoftwareBinding
import com.tomorrowit.datacollect.domain.models.DataModel
import com.tomorrowit.datacollect.presentation.adapters.RecyclerViewAdapterData
import com.tomorrowit.datacollect.utils.extensions.finishAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SoftwareActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySoftwareBinding
    private val viewModel: SoftwareViewModel by viewModels()

    private lateinit var recyclerViewAdapter: RecyclerViewAdapterData
    private val softwareDataList: MutableList<DataModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoftwareBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeState()

        binding.activitySoftwareToolbar.setNavigationOnClickListener {
            this.finish()
        }

        recyclerViewAdapter = RecyclerViewAdapterData(
            this,
            softwareDataList
        )
        binding.activitySoftwareRv.adapter = recyclerViewAdapter
    }

    private fun observeState() {
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).distinctUntilChanged()
            .onEach { state ->
                when (state) {
                    is SoftwareState.Init -> {}

                    is SoftwareState.ShowList -> {
                        setRv(state.list)
                    }
                }
            }.launchIn(lifecycleScope)
    }

    private suspend fun setRv(list: List<DataModel>) {
        withContext(Dispatchers.Main) {
            softwareDataList.clear()
            softwareDataList.addAll(list)
        }
    }

    override fun finish() {
        super.finish()
        finishAnimation()
    }

}