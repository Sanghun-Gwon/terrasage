package com.terrasage.api.encyclopedia.dto

import com.terrasage.api.encyclopedia.entity.PlantCareGuide

data class PlantCareGuideResponse(
    val potType: String?,
    val growingMedium: String?,
    val lightRequirement: String?,
    val lightHoursPerDay: Int?,
    val tempMin: Double?,
    val tempMax: Double?,
    val humidityMin: Int?,
    val humidityMax: Int?,
    val wateringFrequency: String?,
    val wateringMethod: String?,
    val fertilizerType: String?,
    val fertilizerFrequency: String?,
    val repottingNote: String?,
    val pruningNote: String?,
    val overallNote: String?,
) {
    companion object {
        fun from(g: PlantCareGuide) = PlantCareGuideResponse(
            potType = g.potType,
            growingMedium = g.growingMedium,
            lightRequirement = g.lightRequirement,
            lightHoursPerDay = g.lightHoursPerDay,
            tempMin = g.tempMin,
            tempMax = g.tempMax,
            humidityMin = g.humidityMin,
            humidityMax = g.humidityMax,
            wateringFrequency = g.wateringFrequency,
            wateringMethod = g.wateringMethod,
            fertilizerType = g.fertilizerType,
            fertilizerFrequency = g.fertilizerFrequency,
            repottingNote = g.repottingNote,
            pruningNote = g.pruningNote,
            overallNote = g.overallNote,
        )
    }
}
