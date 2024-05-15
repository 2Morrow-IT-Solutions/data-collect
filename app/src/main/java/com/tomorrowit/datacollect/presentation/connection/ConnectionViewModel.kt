package com.tomorrowit.datacollect.presentation.connection

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.net.ConnectivityManager
import android.nfc.NfcAdapter
import android.os.Build
import android.provider.Settings
import android.telephony.ServiceState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomorrowit.datacollect.domain.models.BasicDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ConnectionViewModel @Inject constructor() : ViewModel() {

    private val _state: MutableStateFlow<ConnectionState> =
        MutableStateFlow(ConnectionState.Init)
    val state: StateFlow<ConnectionState> get() = _state

    fun loadConnectionData(context: Context) {
        viewModelScope.launch {
            _state.value = ConnectionState.Init
            val wifiOn =
                Settings.Global.getInt(context.contentResolver, Settings.Global.WIFI_ON, 0) != 0

            val airplaneMode =
                Settings.Global.getInt(
                    context.contentResolver,
                    Settings.Global.AIRPLANE_MODE_ON,
                    0
                ) != 0

            val mobileDataOn =
                Settings.Global.getInt(context.contentResolver, "mobile_data", 0) != 0

            val basicDataList = mutableListOf<BasicDataModel>(
                BasicDataModel(
                    "Is bluetooth available on device",
                    ConnectivityManager.isNetworkTypeValid(ConnectivityManager.TYPE_BLUETOOTH)
                        .toString()
                ),
                BasicDataModel("Is bluetooth enabled", isBluetoothEnabled()),
                BasicDataModel("Is Nfc enabled", isNfcEnabled(context).toString()),
                BasicDataModel(
                    "Is VPN enabled",
                    ConnectivityManager.isNetworkTypeValid(ConnectivityManager.TYPE_VPN)
                        .toString()
                ),
                BasicDataModel("Is Roaming enabled", ServiceState().roaming.toString()),
                BasicDataModel(
                    "Is WI-FI available on device",
                    ConnectivityManager.isNetworkTypeValid(ConnectivityManager.TYPE_WIFI)
                        .toString()
                ),
                BasicDataModel(
                    "Is WI-FI enabled",
                    wifiOn.toString()
                ),
                BasicDataModel(
                    "Is ethernet available on device",
                    ConnectivityManager.isNetworkTypeValid(ConnectivityManager.TYPE_ETHERNET)
                        .toString()
                ),
                BasicDataModel(
                    "Is mobile data enabled",
                    mobileDataOn.toString()
                ),
                BasicDataModel(
                    "Is sim slot available on device",
                    ConnectivityManager.isNetworkTypeValid(ConnectivityManager.TYPE_MOBILE)
                        .toString()
                ),
                BasicDataModel(
                    "Is Wimax available on device",
                    ConnectivityManager.isNetworkTypeValid(ConnectivityManager.TYPE_WIMAX)
                        .toString()
                ),
                BasicDataModel(
                    "Is airplane mode enabled",
                    airplaneMode.toString()
                ),
            )
            _state.value = ConnectionState.ShowList(basicDataList)

        }
    }

    private fun isBluetoothEnabled(): String {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            return "Unknown"
        }
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        return if (bluetoothAdapter?.isEnabled == true) {
            "true"
        } else {
            "false"
        }
    }


    private fun isNfcEnabled(context: Context): Boolean {
        val nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(context)
        return nfcAdapter?.isEnabled ?: false
    }

}

sealed class ConnectionState {
    data object Init : ConnectionState()
    data class ShowList(val list: List<BasicDataModel>) : ConnectionState()
}