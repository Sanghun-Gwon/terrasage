package com.terrasage.api.encyclopedia.dto

import com.terrasage.api.encyclopedia.entity.CareGuide
import com.terrasage.api.encyclopedia.entity.HandlingLevel

// 사육 가이드 응답 DTO
data class CareGuideResponse(
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
        fun from(careGuide: CareGuide) = CareGuideResponse(
            enclosureType = careGuide.enclosureType,
            enclosureSizeCm = careGuide.enclosureSizeCm,
            substrate = careGuide.substrate,
            tempHotZone = careGuide.tempHotZone,
            tempCoolZone = careGuide.tempCoolZone,
            tempNight = careGuide.tempNight,
            humidityMin = careGuide.humidityMin,
            humidityMax = careGuide.humidityMax,
            uvbRequired = careGuide.uvbRequired,
            photoperiodHours = careGuide.photoperiodHours,
            feedType = careGuide.feedType,
            feedFrequency = careGuide.feedFrequency,
            supplements = careGuide.supplements,
            handlingLevel = careGuide.handlingLevel,
            cohabitationNote = careGuide.cohabitationNote,
        )
    }
}
