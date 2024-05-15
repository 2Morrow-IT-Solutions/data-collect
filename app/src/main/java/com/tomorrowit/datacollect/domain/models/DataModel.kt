package com.tomorrowit.datacollect.domain.models

data class DataModel(
    val title: String = "",
    val insideData: List<BasicDataModel> = listOf()
)