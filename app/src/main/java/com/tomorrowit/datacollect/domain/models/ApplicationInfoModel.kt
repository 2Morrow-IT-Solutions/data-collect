package com.tomorrowit.datacollect.domain.models

import android.graphics.drawable.Drawable

data class ApplicationInfoModel(
    val icon: Drawable?,
    val packageName: String,
    val appName: String,
    val versionCode: Int,
    val versionName: String,
    val size: String,
    val firstInstall: String,
    val lastUpdate: String
)
