package com.tomorrowit.datacollect.domain.usecases

import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.Context
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.tomorrowit.datacollect.domain.models.BasicDataModel
import com.tomorrowit.datacollect.domain.models.DataModel
import com.tomorrowit.datacollect.utils.ResourcesUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GetSoftwareUseCase(
    private val context: Context,
    private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(): List<DataModel> = withContext(coroutineDispatcher) {
        suspendCoroutine { continuation ->
            var arrayData = mutableListOf<DataModel>()

            val contentResolver: ContentResolver = context.contentResolver
            val clipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = clipboardManager.primaryClip

            val hasText =
                clipData != null && clipData.itemCount > 0 && clipData.getItemAt(0).text != null

            val locationManager =
                context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager

            val isGpsEnabled: Boolean =
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            val isNetworkEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            val screenBrightness = Settings.System.getInt(
                contentResolver,
                Settings.System.SCREEN_BRIGHTNESS,
                -1
            )

            val locationMode = Settings.Secure.getInt(
                contentResolver,
                Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF
            )

            val fontScale = Settings.System.getFloat(
                contentResolver,
                Settings.System.FONT_SCALE,
                1.0f
            )

            val toneName = Settings.System.getString(
                contentResolver,
                Settings.System.RINGTONE
            )

            val androidId = Settings.Secure.getString(
                contentResolver,
                Settings.Secure.ANDROID_ID
            )

            val fontScalePercentage = (fontScale * 100).toInt()

            val screenBrightnessStr: String = if (screenBrightness == -1) {
                "Unknown"
            } else if (screenBrightness < 0 || screenBrightness > 255) {
                "Invalid value"
            } else {
                "$screenBrightness"
            }

            val deviceName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                Settings.Global.getString(
                    contentResolver,
                    Settings.Global.DEVICE_NAME
                )
            } else {
                "Unknown"
            }

            val bootCount = Settings.Global.getInt(
                contentResolver,
                Settings.Global.BOOT_COUNT
            )

            val developerOptionsOn =
                Settings.Global.getInt(
                    contentResolver,
                    Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
                    0
                ) != 0

            val locationModeStr: String = when (locationMode) {
                Settings.Secure.LOCATION_MODE_OFF -> "OFF"
                Settings.Secure.LOCATION_MODE_SENSORS_ONLY -> "Device Only"
                Settings.Secure.LOCATION_MODE_BATTERY_SAVING -> "Battery Saving"
                Settings.Secure.LOCATION_MODE_HIGH_ACCURACY -> "High Accuracy"
                else -> "Unknown"
            }

            val securityManagerInstalled = System.getSecurityManager() != null

            val canReadFiles =
                ResourcesUtils.hasPermission("java.io.FilePermission", "data", "read")
            val canConnectToLocalhost =
                ResourcesUtils.hasPermission(
                    "java.net.SocketPermission",
                    "localhost:8080",
                    "connect"
                )
            val canLoadClasses =
                ResourcesUtils.hasPermission("java.security.SecurityPermission", "getClassLoader")

            val isStaticIpEnabled =
                Settings.System.getInt(contentResolver, Settings.System.WIFI_USE_STATIC_IP, 0) == 1

            val getWifiStaticIp =
                Settings.System.getString(contentResolver, Settings.System.WIFI_STATIC_IP)

            val getWifiStaticGateway =
                Settings.System.getString(contentResolver, Settings.System.WIFI_STATIC_GATEWAY)

            val getWifiStaticNetmask =
                Settings.System.getString(contentResolver, Settings.System.WIFI_STATIC_NETMASK)

            val getWifiStaticDns1 =
                Settings.System.getString(contentResolver, Settings.System.WIFI_STATIC_DNS1)

            val getWifiStaticDns2 =
                Settings.System.getString(contentResolver, Settings.System.WIFI_STATIC_DNS2)

            arrayData = mutableListOf(
                DataModel(
                    title = "Clipboard",
                    insideData = mutableListOf(
                        BasicDataModel("Clipboard has text", hasText.toString()),
                        BasicDataModel(
                            "Text from clipboard",
                            ResourcesUtils.getClipBoardText(context)
                        )
                    )
                ),
                DataModel(
                    title = "System",
                    insideData = mutableListOf(
                        BasicDataModel(
                            "os.name",
                            (System.getProperty("os.name")?.toString() ?: "")
                        ),
                        BasicDataModel(
                            "os.version",
                            (System.getProperty("os.version")?.toString() ?: "")
                        ),
                        BasicDataModel(
                            "os.arch",
                            (System.getProperty("os.arch")?.toString() ?: "")
                        ),
                        BasicDataModel(
                            "user.name",
                            (System.getProperty("user.name")?.toString() ?: "")
                        )
                    )
                ),
                DataModel(
                    title = "Settings data",
                    insideData = mutableListOf(
                        BasicDataModel("Screen brightness", "$screenBrightnessStr out of 255"),
                        BasicDataModel("Location mode", locationModeStr),
                        BasicDataModel("Font scale percent", fontScalePercentage.toString()),
                        BasicDataModel(
                            "Ringtone name", ResourcesUtils.extractTitleFromUrl(toneName)
                                .toString()
                        ),
                        BasicDataModel(
                            "Sound and vibration mode",
                            ResourcesUtils.getRingerModeString(context)
                        ),
                        BasicDataModel("Is gps enabled", isGpsEnabled.toString()),
                        BasicDataModel("Is network enabled", isNetworkEnabled.toString())
                    )
                ),
                DataModel(
                    title = "Wifi data",
                    insideData = mutableListOf(
                        BasicDataModel("Wifi use static ip", isStaticIpEnabled.toString()),
                        //  BasicDataModel("Wifi static ip", getWifiStaticIp),
                        //  BasicDataModel("Wifi static gateway", getWifiStaticGateway),
                        //  BasicDataModel("Wifi static netmask", getWifiStaticNetmask),
                        //  BasicDataModel("Wifi static dns1", getWifiStaticDns1),
                        //  BasicDataModel("Wifi static dns2", getWifiStaticDns2)
                    )
                ),
                DataModel(
                    title = "Info about security",
                    insideData = mutableListOf(
                        BasicDataModel(
                            "SecurityManager is installed",
                            securityManagerInstalled.toString()
                        ),
                        BasicDataModel("Can read files", canReadFiles.toString()),
                        BasicDataModel(
                            "Can connect,localhost:8080",
                            canConnectToLocalhost.toString()
                        ),
                        BasicDataModel("Can load classes", canLoadClasses.toString())
                    )
                ),
                DataModel(
                    title = "Secure and global data",
                    insideData = mutableListOf(
                        BasicDataModel("Device name", deviceName.toString()),
                        BasicDataModel("Android id", androidId.toString()),
                        BasicDataModel("Boot count", bootCount.toString()),
                        BasicDataModel("Is developer mode on", developerOptionsOn.toString()),
                        BasicDataModel(
                            "Device has lock screen set", ResourcesUtils.getDeviceSecurityType(
                                context
                            )
                        )
                    )
                )
            )
            continuation.resume(arrayData)
        }
    }
}

