package com.terrasage.api.encyclopedia.dto

data class PlantCareGuideUpsertRequest(
    val potType: String? = null,
    val growingMedium: String? = null,
    val lightRequirement: String? = null,
    val lightHoursPerDay: Int? = null,
    val tempMin: Double? = null,
    val tempMax: Double? = null,
    val humidityMin: Int? = null,
    val humidityMax: Int? = null,
    val wateringFrequency: String? = null,
    val wateringMethod: String? = null,
    val fertilizerType: String? = null,
    val fertilizerFrequency: String? = null,
    val repottingNote: String? = null,
    val pruningNote: String? = null,
    val overallNote: String? = null,
)
