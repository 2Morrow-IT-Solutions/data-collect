package com.tomorrowit.datacollect.utils

import android.os.Build
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit

object TimeUtility {

    private const val DATE_TIME_PATTERN: String = "dd.MM.yyyy 'at' HH:mm"

    @JvmStatic
    fun fromMillisecondsToDateAndTimeStringDots(timestamp: Long): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val instant = Instant.ofEpochMilli(timestamp)
            val zoneId = ZoneId.systemDefault()
            val zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId)
            val formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)
            formatter.format(zonedDateTime)
        } else {
            val oldFormatter = SimpleDateFormat(DATE_TIME_PATTERN, Locale.getDefault())
            oldFormatter.format(timestamp)
        }
    }

    @JvmStatic
    fun fromMillisecondsToDurationString(timestamp: Long): String {
        val currentTime = System.currentTimeMillis()
        val durationMillis = currentTime - timestamp
        val days = TimeUnit.MILLISECONDS.toDays(durationMillis)
        val hours = TimeUnit.MILLISECONDS.toHours(durationMillis) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis) % 60
        return "${days}d ${hours}h ${minutes}m"
    }

    @JvmStatic
    fun fromMillisecondsToHoursAndMinutes(timestamp: Long): String {
        val currentTime = System.currentTimeMillis()
        val durationMillis = currentTime - timestamp
        val hours = TimeUnit.MILLISECONDS.toHours(durationMillis) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis) % 60
        return "${hours}h ${minutes}m"
    }
}