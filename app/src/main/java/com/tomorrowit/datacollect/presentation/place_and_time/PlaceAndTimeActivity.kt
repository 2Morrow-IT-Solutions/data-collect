package com.tomorrowit.datacollect.presentation.place_and_time

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.tomorrowit.datacollect.databinding.ActivityPlaceAndTimeBinding
import com.tomorrowit.datacollect.domain.models.BasicDataModel
import com.tomorrowit.datacollect.presentation.adapters.RecyclerViewAdapterBasicDataModel
import com.tomorrowit.datacollect.utils.extensions.finishAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class PlaceAndTimeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaceAndTimeBinding
    private val viewModel: PlaceAndTimeViewModel by viewModels()

    private lateinit var recyclerViewAdapter: RecyclerViewAdapterBasicDataModel
    private val arrayData: MutableList<BasicDataModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceAndTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeState()

        binding.activitySpaceAndTimeToolbar.setNavigationOnClickListener {
            this.finish()
        }

        recyclerViewAdapter = RecyclerViewAdapterBasicDataModel(
            this,
            arrayData
        )
        binding.activitySpaceAndTimeRv.adapter = recyclerViewAdapter
    }

    private fun observeState() {
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).distinctUntilChanged()
            .onEach { state ->
                when (state) {
                    is PlaceAndTimeState.Init -> {}

                    is PlaceAndTimeState.ShowList -> {
                        setRv(state.list)
                    }
                }
            }.launchIn(lifecycleScope)
    }

    private suspend fun setRv(list: List<BasicDataModel>) {
        withContext(Dispatchers.Main) {
            arrayData.clear()
            arrayData.addAll(list)
        }
    }

    override fun finish() {
        super.finish()
        finishAnimation()
    }
}