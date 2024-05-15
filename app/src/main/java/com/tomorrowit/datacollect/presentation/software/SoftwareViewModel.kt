package com.tomorrowit.datacollect.presentation.software

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomorrowit.datacollect.domain.models.DataModel
import com.tomorrowit.datacollect.domain.usecases.GetSoftwareUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SoftwareViewModel @Inject constructor(
    private val getSoftwareUseCase: GetSoftwareUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<SoftwareState> =
        MutableStateFlow(SoftwareState.Init)
    val state: StateFlow<SoftwareState> get() = _state

    init {
        viewModelScope.launch {
            _state.value = SoftwareState.Init
            val generalDataList = getSoftwareUseCase.invoke()
            if (generalDataList.isNotEmpty()) {
                _state.value = SoftwareState.ShowList(generalDataList)
            }
        }
    }
}

sealed class SoftwareState {
    data object Init : SoftwareState()
    data class ShowList(val list: List<DataModel>) : SoftwareState()
}