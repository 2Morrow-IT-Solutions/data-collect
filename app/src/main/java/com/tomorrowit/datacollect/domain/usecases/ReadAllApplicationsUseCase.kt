package com.tomorrowit.datacollect.domain.usecases

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.tomorrowit.datacollect.domain.models.ApplicationInfoModel
import com.tomorrowit.datacollect.utils.Logic
import com.tomorrowit.datacollect.utils.TimeUtility
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ReadAllApplicationsUseCase(
    private val packageManager: PackageManager,
    private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): List<ApplicationInfoModel> = withContext(coroutineDispatcher) {
        suspendCoroutine { continuation ->
            val packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
            val appInfoList = packageInfoList.filter { packageInfo ->
                (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0
            }.map { packageInfo ->
                val applicationInfo = packageInfo.applicationInfo
                val appName = packageManager.getApplicationLabel(applicationInfo).toString()
                val icon = packageManager.getApplicationIcon(applicationInfo)
                val size = File(applicationInfo.sourceDir).length()
                ApplicationInfoModel(
                    icon = icon,
                    packageName = packageInfo.packageName,
                    appName = appName,
                    versionCode = packageInfo.versionCode,
                    versionName = packageInfo.versionName,
                    size = Logic.formatFileSize(size),
                    firstInstall = TimeUtility.fromMillisecondsToDateAndTimeStringDots(packageInfo.firstInstallTime),
                    lastUpdate = TimeUtility.fromMillisecondsToDateAndTimeStringDots(packageInfo.lastUpdateTime)
                )
            }
            continuation.resume(appInfoList)
        }
    }

}

