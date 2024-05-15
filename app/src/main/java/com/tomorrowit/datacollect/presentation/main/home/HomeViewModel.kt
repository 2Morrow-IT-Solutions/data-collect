package com.tomorrowit.datacollect.presentation.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomorrowit.datacollect.domain.models.CollectedDataModel
import com.tomorrowit.datacollect.domain.usecases.GetCollectedDataListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCollectedDataListUseCase: GetCollectedDataListUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<HomeState> =
        MutableStateFlow(HomeState.Init)
    val state: StateFlow<HomeState> get() = _state

    init {
        viewModelScope.launch {
            val list = getCollectedDataListUseCase.invoke()
            _state.value = HomeState.ShowList(list)
        }
    }
}

sealed class HomeState {
    data object Init : HomeState()
    data class ShowList(val list: List<CollectedDataModel>) : HomeState()
}