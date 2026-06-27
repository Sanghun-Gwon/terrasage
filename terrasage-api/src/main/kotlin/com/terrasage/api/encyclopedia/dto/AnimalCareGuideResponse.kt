package com.terrasage.api.encyclopedia.dto

import com.terrasage.api.encyclopedia.entity.AnimalCareGuide
import com.terrasage.api.encyclopedia.entity.HandlingLevel

data class AnimalCareGuideResponse(
    val enclosureType: String?,
    val enclosureSizeCm: String?,
    val substrate: String?,
    val tempHotZone: Double?,
    val tempCoolZone: Double?,
    val tempNight: Double?,
    val humidityMin: Int?,
    val humidityMax: Int?,
    val uvbRequired: Boolean,
    val photoperiodHours: Int?,
    val feedType: String?,
    val feedFrequency: String?,
    val supplements: String?,
    val handlingLevel: HandlingLevel?,
    val cohabitationNote: String?,
) {
    companion object {
        fun from(g: AnimalCareGuide) = AnimalCareGuideResponse(
            enclosureType = g.enclosureType,
            enclosureSizeCm = g.enclosureSizeCm,
            substrate = g.substrate,
            tempHotZone = g.tempHotZone,
            tempCoolZone = g.tempCoolZone,
            tempNight = g.tempNight,
            humidityMin = g.humidityMin,
            humidityMax = g.humidityMax,
            uvbRequired = g.uvbRequired,
            photoperiodHours = g.photoperiodHours,
            feedType = g.feedType,
            feedFrequency = g.feedFrequency,
            supplements = g.supplements,
            handlingLevel = g.handlingLevel,
            cohabitationNote = g.cohabitationNote,
        )
    }
}
