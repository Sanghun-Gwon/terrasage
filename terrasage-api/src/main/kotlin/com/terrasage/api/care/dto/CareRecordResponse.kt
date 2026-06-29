package com.terrasage.api.care.dto

import com.terrasage.api.care.entity.CareRecord
import java.time.LocalDateTime

data class CareRecordResponse(
    val id: Long,
    val recordedAt: LocalDateTime,
    val temperature: Double?,
    val humidity: Double?,
    val lightHours: Double?,
    val weight: Double?,
    val feedType: String?,
    val feedAmount: String?,
    val notes: String?,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(record: CareRecord) = CareRecordResponse(
            id = record.id,
            recordedAt = record.recordedAt,
            temperature = record.temperature,
            humidity = record.humidity,
            lightHours = record.lightHours,
            weight = record.weight,
            feedType = record.feedType,
            feedAmount = record.feedAmount,
            notes = record.notes,
            createdAt = record.createdAt,
        )
    }
}
