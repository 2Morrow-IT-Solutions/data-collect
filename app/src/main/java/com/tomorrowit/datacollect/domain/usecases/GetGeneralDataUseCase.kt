package com.tomorrowit.datacollect.domain.usecases

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Resources
import android.hardware.Sensor
import android.hardware.SensorManager
import android.media.AudioManager
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.os.PowerManager
import com.tomorrowit.datacollect.domain.models.BasicDataModel
import com.tomorrowit.datacollect.domain.models.DataModel
import com.tomorrowit.datacollect.utils.ResourcesUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GetGeneralDataUseCase(
    private val context: Context,
    private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(): List<DataModel> = withContext(coroutineDispatcher) {
        suspendCoroutine { continuation ->

            var arrayData = mutableListOf<DataModel>()

            val activityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memoryInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memoryInfo)
            val externalStorageInfo = Environment.getExternalStorageDirectory()
            val internalStorageInfo = Environment.getDataDirectory()

            val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            val batteryIntent =
                context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

            val batteryLevel =
                batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

            val chargingState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS)
            } else {
                batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS)
            }
            val isCharging = chargingState == BatteryManager.BATTERY_STATUS_CHARGING ||
                    chargingState == BatteryManager.BATTERY_STATUS_FULL

            val batteryHealth = batteryIntent?.getStringExtra(BatteryManager.EXTRA_HEALTH)

            val batteryStatus = batteryIntent?.getStringExtra(BatteryManager.EXTRA_STATUS)

            val batteryTechnology = batteryIntent?.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)

            val batteryTemperature = batteryIntent?.getStringExtra(BatteryManager.EXTRA_TEMPERATURE)

            val batteryVoltage = batteryIntent?.getStringExtra(BatteryManager.EXTRA_VOLTAGE)

            val batteryCurrent =
                batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE)

            val batteryPresent = batteryIntent?.getStringExtra(BatteryManager.EXTRA_PRESENT)

            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

            val ringerMode = when (audioManager.ringerMode) {
                AudioManager.RINGER_MODE_NORMAL -> "Normal"
                AudioManager.RINGER_MODE_SILENT -> "Silent"
                AudioManager.RINGER_MODE_VIBRATE -> "Vibrate"
                else -> "Unknown"
            }

            val maxVolumeRingtone = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING)
            val currentVolumeRingtone = audioManager.getStreamVolume(AudioManager.STREAM_RING)

            val maxVolumeMusic = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            val currentVolumeMusic = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager

            val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

            val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

            val gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

            val ambientLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
            val proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

            val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
            val barometer = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

            val packageManager = context.packageManager
            val hasCamera = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
            val hasFrontCamera =
                packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
            val hasNfc = packageManager.hasSystemFeature(PackageManager.FEATURE_NFC)
            val hasFingerprint = packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
            val hasBluetooth = packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)
            val hasGPS = packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)
            val hasUSB = packageManager.hasSystemFeature(PackageManager.FEATURE_USB_HOST)
            val hasIris = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                packageManager.hasSystemFeature(PackageManager.FEATURE_IRIS)
            } else {
                packageManager.hasSystemFeature(PackageManager.FEATURE_IRIS)
            }

            val processorManufacturer = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Build.SOC_MANUFACTURER
            } else {
                "Unknown"
            }
            val processorModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Build.SOC_MODEL
            } else {
                "Unknown"
            }

            arrayData = mutableListOf(
                DataModel(
                    title = "General",
                    insideData = mutableListOf(
                        BasicDataModel("Device brand", Build.BRAND),
                        BasicDataModel("Device board", Build.BOARD),
                        BasicDataModel("Device id", Build.ID),
                        BasicDataModel("Device name", Build.DEVICE),
                        BasicDataModel("Display id", Build.DISPLAY),
                        BasicDataModel("Device hardware", Build.HARDWARE),
                        BasicDataModel("Device host", Build.HOST),
                        BasicDataModel("Device manufacturer", Build.MANUFACTURER),
                        BasicDataModel("Device model", Build.MODEL),
                        BasicDataModel("Device product", Build.PRODUCT),
                        BasicDataModel(
                            "Supported 32-bit", Build.SUPPORTED_32_BIT_ABIS?.joinToString()
                                .toString()
                        ),
                        BasicDataModel(
                            "Supported 64-bit", Build.SUPPORTED_64_BIT_ABIS?.joinToString()
                                .toString()
                        ),
                        BasicDataModel("Device Type", Build.TYPE),
                        BasicDataModel("Device User", Build.USER),
                        BasicDataModel("Device CPU", Build.CPU_ABI),
                        BasicDataModel("Device CPU2", Build.CPU_ABI2),
                        BasicDataModel("Device SDK", Build.VERSION.SDK),
                        BasicDataModel("Device codename", Build.VERSION.CODENAME),
                        BasicDataModel("Device incremental", Build.VERSION.INCREMENTAL),
                        BasicDataModel("Device release", Build.VERSION.RELEASE),
                        BasicDataModel("Last security patch", Build.VERSION.SECURITY_PATCH),
                        BasicDataModel("Processor manufacturer", processorManufacturer),
                        BasicDataModel("Processor model", processorModel),
                    )
                ),
                DataModel(
                    title = "Screen",
                    insideData = mutableListOf(
                        BasicDataModel("Ui Mode", ResourcesUtils.getUiMode()),
                        BasicDataModel(
                            "Is night mode turned on",
                            ResourcesUtils.isNightMode(Resources.getSystem().configuration)
                                .toString()
                        ),
                        BasicDataModel("Font Scale", ResourcesUtils.getFontScale()),
                        BasicDataModel("Navigation", ResourcesUtils.getNavigationMethod()),
                        BasicDataModel("Touchscreen", ResourcesUtils.getTouchscreenType()),
                        BasicDataModel("Keyboard", ResourcesUtils.getKeyboardAvailability()),
                        BasicDataModel("ScreenLayout", ResourcesUtils.getScreenLayout()),
                        BasicDataModel("Orientation", ResourcesUtils.getOrientationString())
                    )
                ),
                DataModel(
                    title = "Ram memory",
                    insideData = mutableListOf(
                        BasicDataModel(
                            "Total memory",
                            "${ResourcesUtils.formatMemorySize(memoryInfo.totalMem)} GB"
                        ),
                        BasicDataModel(
                            "Available memory",
                            "${ResourcesUtils.formatMemorySize(memoryInfo.availMem)} GB"
                        ),
                        BasicDataModel(
                            "Threshold",
                            "${ResourcesUtils.formatMemorySize(memoryInfo.threshold)} GB"
                        ),
                        BasicDataModel("Low Memory", memoryInfo.lowMemory.toString())
                    )
                ),
                DataModel(
                    title = "External storage memory",
                    insideData = mutableListOf(
                        BasicDataModel(
                            "Total external space", "${
                                ResourcesUtils.formatMemorySize(
                                    externalStorageInfo.totalSpace
                                )
                            } GB"
                        ),
                        BasicDataModel(
                            "Free external space", "${
                                ResourcesUtils.formatMemorySize(
                                    externalStorageInfo.freeSpace
                                )
                            } GB"
                        ),
                        BasicDataModel(
                            "Usable external space", "${
                                ResourcesUtils.formatMemorySize(
                                    externalStorageInfo.usableSpace
                                )
                            } GB"
                        ),
                        BasicDataModel("External path", externalStorageInfo.absolutePath),
                        BasicDataModel("External name", externalStorageInfo.name),
                    )
                ),
                DataModel(
                    title = "Internal storage memory",
                    insideData = mutableListOf(
                        BasicDataModel(
                            "Total internal space", "${
                                ResourcesUtils.formatMemorySize(
                                    internalStorageInfo.totalSpace
                                )
                            } GB"
                        ),
                        BasicDataModel(
                            "Free internal space", "${
                                ResourcesUtils.formatMemorySize(
                                    internalStorageInfo.freeSpace
                                )
                            } GB"
                        ),
                        BasicDataModel(
                            "Usable internal space", "${
                                ResourcesUtils.formatMemorySize(
                                    internalStorageInfo.usableSpace
                                )
                            } GB"
                        ),
                        BasicDataModel("Internal path", internalStorageInfo.absolutePath),
                        BasicDataModel("Internal name", internalStorageInfo.name),
                    )

                ),
                DataModel(
                    title = "Battery",
                    insideData = mutableListOf(
                        BasicDataModel("Battery level", batteryLevel.toString()),
                        BasicDataModel(
                            "Charging state",
                            if (isCharging) "Charging" else "Not Charging"
                        ),
                        BasicDataModel("Battery health", batteryHealth.toString()),
                        BasicDataModel("Battery technology", batteryTechnology.toString()),
                        BasicDataModel("Battery temperature", batteryTemperature.toString()),
                        BasicDataModel("Battery voltage", batteryVoltage.toString()),
                        BasicDataModel("Battery current", batteryCurrent.toString()),
                        BasicDataModel("Battery status", batteryStatus.toString()),
                        BasicDataModel("Battery present", batteryPresent.toString())
                    )
                ),
                DataModel(
                    title = "Audio",
                    insideData = mutableListOf(
                        BasicDataModel("Sound mode", ringerMode),
                        BasicDataModel("Max ringtone volume", maxVolumeRingtone.toString()),
                        BasicDataModel(
                            "Current ringtone volume",
                            currentVolumeRingtone.toString()
                        ),
                        BasicDataModel("Max music volume", maxVolumeMusic.toString()),
                        BasicDataModel("Current music volume", currentVolumeMusic.toString()),
                        BasicDataModel(
                            "Microphone is muted",
                            audioManager.isMicrophoneMute.toString()
                        ),
                        BasicDataModel("Music is active", audioManager.isMusicActive.toString())
                    )
                ),
                DataModel(
                    title = "Power",
                    insideData = mutableListOf(
                        BasicDataModel("Screen is on", powerManager.isInteractive.toString()),
                        BasicDataModel(
                            "Device is in idle mode",
                            powerManager.isDeviceIdleMode.toString()
                        ),
                        BasicDataModel(
                            "Device is in power saving mode",
                            powerManager.isPowerSaveMode.toString()
                        ),
                    )
                ),
                DataModel(
                    title = "Sensors",
                    insideData = mutableListOf(
                        BasicDataModel("Accelerometer name", accelerometer?.name.toString()),
                        BasicDataModel("Accelerometer version", accelerometer?.type.toString()),
                        BasicDataModel(
                            "Accelerometer max range",
                            accelerometer?.maximumRange.toString()
                        ),
                        BasicDataModel("Gyroscope name", gyroscope?.name.toString()),
                        BasicDataModel("Gyroscope version", gyroscope?.version.toString()),
                        BasicDataModel(
                            "Gyroscope max range",
                            gyroscope?.maximumRange.toString()
                        ),
                        BasicDataModel("Ambient light name", ambientLight?.name.toString()),
                        BasicDataModel(
                            "Ambient light version",
                            ambientLight?.version.toString()
                        ),
                        BasicDataModel(
                            "Ambient light max range",
                            ambientLight?.maximumRange.toString()
                        ),
                        BasicDataModel("Proximity name", proximity?.name.toString()),
                        BasicDataModel("Proximity version", proximity?.version.toString()),
                        BasicDataModel(
                            "Proximity max range",
                            proximity?.maximumRange.toString()
                        ),
                        BasicDataModel("Barometer name", barometer?.name.toString()),
                        BasicDataModel("Barometer version", barometer?.version.toString()),
                        BasicDataModel(
                            "Barometer max range",
                            barometer?.maximumRange.toString()
                        ),
                        BasicDataModel("Magnetometer name", magnetometer?.name.toString()),
                        BasicDataModel(
                            "Magnetometer version",
                            magnetometer?.version.toString()
                        ),
                        BasicDataModel(
                            "Magnetometer max range",
                            magnetometer?.maximumRange.toString()
                        ),
                    )
                ),
                DataModel(
                    title = "Features",
                    insideData = mutableListOf(
                        BasicDataModel("Has any camera", hasCamera.toString()),
                        BasicDataModel("Has front camera", hasFrontCamera.toString()),
                        BasicDataModel("Has nfc", hasNfc.toString()),
                        BasicDataModel("Has fingerprint", hasFingerprint.toString()),
                        BasicDataModel("Has iris", hasIris.toString()),
                        BasicDataModel("Has bluetooth", hasBluetooth.toString()),
                        BasicDataModel("Has GPS", hasGPS.toString()),
                        BasicDataModel("Has USB", hasUSB.toString()),
                    )
                )
            )
            continuation.resume(arrayData)
        }
    }
}
