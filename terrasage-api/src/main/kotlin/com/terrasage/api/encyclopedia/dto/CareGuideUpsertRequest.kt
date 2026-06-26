package com.terrasage.api.encyclopedia.dto

import com.terrasage.api.encyclopedia.entity.HandlingLevel

// 사육가이드 등록/수정 통합 요청 (없으면 생성, 있으면 전체 덮어씀)
data class CareGuideUpsertRequest(
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
