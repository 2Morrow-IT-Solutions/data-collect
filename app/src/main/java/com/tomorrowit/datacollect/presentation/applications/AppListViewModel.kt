package com.tomorrowit.datacollect.presentation.applications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomorrowit.datacollect.domain.models.ApplicationInfoModel
import com.tomorrowit.datacollect.domain.usecases.ReadAllApplicationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplicationsViewModel @Inject constructor(
    private val readAllApplicationsUseCase: ReadAllApplicationsUseCase,
) : ViewModel() {

    private val _apps: MutableStateFlow<ApplicationsState> =
        MutableStateFlow(ApplicationsState.IsLoading)
    val apps: StateFlow<ApplicationsState> get() = _apps

    init {
        viewModelScope.launch {
            val packageInfoList = readAllApplicationsUseCase.invoke()
            if (packageInfoList.isNotEmpty()) {
                _apps.value = ApplicationsState.ShowList(packageInfoList)
            }
        }
    }
}

sealed class ApplicationsState {
    data object IsLoading : ApplicationsState()
    data class ShowList(val list: List<ApplicationInfoModel>) : ApplicationsState()
}