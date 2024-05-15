package com.tomorrowit.datacollect.presentation.general

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.tomorrowit.datacollect.databinding.ActivityGeneralBinding
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
class GeneralActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGeneralBinding
    private val viewModel: GeneralViewModel by viewModels()

    private lateinit var recyclerViewAdapter: RecyclerViewAdapterData
    private val generalDataList: MutableList<DataModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGeneralBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeState()

        binding.activityGeneralToolbar.setNavigationOnClickListener {
            this.finish()
        }

        recyclerViewAdapter = RecyclerViewAdapterData(
            this,

            generalDataList
        )
        binding.activityGeneralRv.adapter = recyclerViewAdapter
    }

    private fun observeState() {
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).distinctUntilChanged()
            .onEach { state ->
                when (state) {
                    is GeneralState.Init -> {}

                    is GeneralState.ShowList -> {
                        setRv(state.list)
                    }
                }
            }.launchIn(lifecycleScope)
    }

    private suspend fun setRv(list: List<DataModel>) {
        withContext(Dispatchers.Main) {
            generalDataList.clear()
            generalDataList.addAll(list)
        }
    }

    override fun finish() {
        super.finish()
        finishAnimation()
    }
}