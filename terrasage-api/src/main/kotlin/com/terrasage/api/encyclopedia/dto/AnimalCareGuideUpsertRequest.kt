package com.terrasage.api.encyclopedia.dto

import com.terrasage.api.encyclopedia.entity.HandlingLevel

data class AnimalCareGuideUpsertRequest(
    val enclosureType: String? = null,
    val enclosureSizeCm: String? = null,
    val substrate: String? = null,
    val tempHotZone: Double? = null,
    val tempCoolZone: Double? = null,
    val tempNight: Double? = null,
    val humidityMin: Int? = null,
    val humidityMax: Int? = null,
    val uvbRequired: Boolean = false,
    val photoperiodHours: Int? = null,
    val feedType: String? = null,
    val feedFrequency: String? = null,
    val supplements: String? = null,
    val handlingLevel: HandlingLevel? = null,
    val cohabitationNote: String? = null,
)
