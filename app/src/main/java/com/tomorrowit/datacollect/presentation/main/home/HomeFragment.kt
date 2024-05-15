package com.tomorrowit.datacollect.presentation.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.tomorrowit.datacollect.databinding.FragmentHomeBinding
import com.tomorrowit.datacollect.domain.listeners.ItemClicked
import com.tomorrowit.datacollect.domain.models.CollectedDataModel
import com.tomorrowit.datacollect.presentation.adapters.RecyclerViewAdapterCollectedData
import com.tomorrowit.datacollect.presentation.applications.AppListActivity
import com.tomorrowit.datacollect.presentation.connection.ConnectionActivity
import com.tomorrowit.datacollect.presentation.general.GeneralActivity
import com.tomorrowit.datacollect.presentation.place_and_time.PlaceAndTimeActivity
import com.tomorrowit.datacollect.presentation.software.SoftwareActivity
import com.tomorrowit.datacollect.utils.extensions.openActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HomeFragment : Fragment(), ItemClicked {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var recyclerViewAdapter: RecyclerViewAdapterCollectedData
    private var dataList: MutableList<CollectedDataModel> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentHomeBinding.inflate(inflater, container, false)
        .apply { binding = this }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()

        recyclerViewAdapter = RecyclerViewAdapterCollectedData(
            this@HomeFragment.requireContext(),
            dataList,
            this@HomeFragment
        )

        binding.homeRv.adapter = recyclerViewAdapter
    }

    private fun observeState() {
        viewModel.state
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                when (state) {
                    is HomeState.Init -> {}
                    is HomeState.ShowList -> {
                        setUi(state.list)
                    }
                }

            }.launchIn(lifecycleScope)
    }

    private suspend fun setUi(list: List<CollectedDataModel>) {
        withContext(Dispatchers.Main) {
            dataList.clear()
            dataList.addAll(list)
        }
    }

    override fun onItemClicked(position: Int) {
        when (position) {
            0 -> {
                activity?.openActivity(GeneralActivity())
            }

            1 -> {
                activity?.openActivity(AppListActivity())
            }

            2 -> {
                activity?.openActivity(PlaceAndTimeActivity())
            }

            3 -> {
                activity?.openActivity(ConnectionActivity())
            }

            4 -> {
                activity?.openActivity(SoftwareActivity())
            }
        }
    }
}