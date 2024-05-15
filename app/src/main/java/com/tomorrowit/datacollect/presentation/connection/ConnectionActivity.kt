package com.tomorrowit.datacollect.presentation.connection

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.tomorrowit.datacollect.databinding.ActivityConnectionBinding
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
class ConnectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConnectionBinding
    private val viewModel: ConnectionViewModel by viewModels()

    private lateinit var recyclerViewAdapter: RecyclerViewAdapterBasicDataModel
    private val arrayData: MutableList<BasicDataModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConnectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeState()
        viewModel.loadConnectionData(this@ConnectionActivity)

        recyclerViewAdapter = RecyclerViewAdapterBasicDataModel(
            this,
            arrayData
        )

        binding.activityConnectionRv.adapter = recyclerViewAdapter

        binding.activityConnectionToolbar.setNavigationOnClickListener {
            this.finish()
        }
    }

    private fun observeState() {
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).distinctUntilChanged()
            .onEach { state ->
                when (state) {
                    is ConnectionState.Init -> {}

                    is ConnectionState.ShowList -> {
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

