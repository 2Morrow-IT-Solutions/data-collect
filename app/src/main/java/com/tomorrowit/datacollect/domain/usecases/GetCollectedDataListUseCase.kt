package com.tomorrowit.datacollect.domain.usecases

import com.tomorrowit.datacollect.R
import com.tomorrowit.datacollect.domain.models.CollectedDataModel

class GetCollectedDataListUseCase {
    private val _list: List<CollectedDataModel> =
        listOf(
            CollectedDataModel(
                title = "General information",
                description = "Access device specifications, manufacturer data, and more.",
                icon = R.drawable.ic_phone
            ),
            CollectedDataModel(
                title = "Application information",
                description = "Have a look on the apps you've got installed, like version, size, and more.",
                icon = R.drawable.ic_apps
            ),
            CollectedDataModel(
                title = "Place and time",
                description = "Reveal device location, time zone, and related data.",
                icon = R.drawable.ic_time
            ),
            CollectedDataModel(
                title = "Connection information",
                description = "Examine network, Wi-Fi, and other connection details.",
                icon = R.drawable.ic_usb
            ),
            CollectedDataModel(
                title = "Software information",
                description = "Investigate operating system, firmware, and software versions for your device.",
                icon = R.drawable.ic_software
            )
        )

    operator fun invoke(): List<CollectedDataModel> {
        return _list
    }
}