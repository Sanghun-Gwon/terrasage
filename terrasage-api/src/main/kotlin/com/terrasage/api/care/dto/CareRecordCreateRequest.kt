package com.terrasage.api.care.dto

import java.time.LocalDateTime

data class CareRecordCreateRequest(
    val recordedAt: LocalDateTime = LocalDateTime.now(),
    val temperature: Double? = null,
    val humidity: Double? = null,
    val lightHours: Double? = null,
    val weight: Double? = null,
    val feedType: String? = null,
    val feedAmount: String? = null,
    val notes: String? = null,
)
