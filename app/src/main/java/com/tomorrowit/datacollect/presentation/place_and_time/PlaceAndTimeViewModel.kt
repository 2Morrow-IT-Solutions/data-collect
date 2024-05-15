package com.tomorrowit.datacollect.presentation.place_and_time

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomorrowit.datacollect.domain.models.BasicDataModel
import com.tomorrowit.datacollect.utils.TimeUtility
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class PlaceAndTimeViewModel @Inject constructor() : ViewModel() {

    private val _state: MutableStateFlow<PlaceAndTimeState> =
        MutableStateFlow(PlaceAndTimeState.Init)
    val state: StateFlow<PlaceAndTimeState> get() = _state

    init {
        viewModelScope.launch {
            _state.value = PlaceAndTimeState.Init
            val basicDataList = mutableListOf<BasicDataModel>(
                BasicDataModel(
                    "Time since last boot", TimeUtility.fromMillisecondsToHoursAndMinutes(
                        System.currentTimeMillis() - SystemClock.currentThreadTimeMillis()
                    )
                ),
                BasicDataModel(
                    "Realtime since boot without sleep",
                    TimeUtility.fromMillisecondsToDurationString(
                        System.currentTimeMillis() - SystemClock.uptimeMillis()
                    )
                ),
                BasicDataModel(
                    "Realtime since boot with sleep",
                    TimeUtility.fromMillisecondsToDurationString(
                        System.currentTimeMillis() - SystemClock.elapsedRealtime()
                    )
                ),
                BasicDataModel("Time zone location", TimeZone.getDefault().id),
                BasicDataModel("Time zone", TimeZone.getDefault().displayName),
                BasicDataModel(
                    "Time zone offset",
                    (TimeZone.getDefault().rawOffset / 3_600_000.0).toString()
                ),
                BasicDataModel(
                    "Time zone savings",
                    (TimeZone.getDefault().dstSavings / 3_600_000.0).toString()
                ),
                BasicDataModel("Locale", Locale.getDefault().toString()),
                BasicDataModel(
                    "Currency",
                    Currency.getInstance(Locale.getDefault()).toString()
                ),
                BasicDataModel("Full country name", Locale.getDefault().displayCountry),
                BasicDataModel("Full language name", Locale.getDefault().displayLanguage),
                BasicDataModel("Country", Locale.getDefault().country),
                BasicDataModel("Language", Locale.getDefault().language),
                BasicDataModel("Language tag", Locale.getDefault().toLanguageTag()),
            )
            _state.value = PlaceAndTimeState.ShowList(basicDataList)
        }
    }
}

sealed class PlaceAndTimeState {
    data object Init : PlaceAndTimeState()
    data class ShowList(val list: List<BasicDataModel>) : PlaceAndTimeState()
}