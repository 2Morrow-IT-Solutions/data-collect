package com.tomorrowit.datacollect.presentation.general

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomorrowit.datacollect.domain.models.DataModel
import com.tomorrowit.datacollect.domain.usecases.GetGeneralDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeneralViewModel @Inject constructor(
    private val getGeneralDataUseCase: GetGeneralDataUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<GeneralState> =
        MutableStateFlow(GeneralState.Init)
    val state: StateFlow<GeneralState> get() = _state

    init {
        viewModelScope.launch {
            _state.value = GeneralState.Init
            val generalDataList = getGeneralDataUseCase.invoke()
            if (generalDataList.isNotEmpty()) {
                _state.value = GeneralState.ShowList(generalDataList)
            }
        }
    }
}

sealed class GeneralState {
    data object Init : GeneralState()
    data class ShowList(val list: List<DataModel>) : GeneralState()
}