package com.terrasage.api.encyclopedia.dto

import com.terrasage.api.encyclopedia.entity.CitesLevel
import com.terrasage.api.encyclopedia.entity.DifficultyLevel
import com.terrasage.api.encyclopedia.entity.SpeciesStatus
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

// 종 수정 요청 DTO (관리자 전용)
// 등록과 달리 status도 변경 가능 (DRAFT → PUBLISHED → ARCHIVED)
data class SpeciesUpdateRequest(
    @field:NotBlank
    val scientificName: String,

    @field:NotBlank
    val commonNameKo: String,

    val commonNameEn: String? = null,

    @field:NotBlank
    val kingdom: String,

    @field:NotBlank
    val phylum: String,

    @field:NotBlank
    val taxonomyClass: String,

    @field:NotBlank
    val taxonomyOrder: String,

    @field:NotBlank
    val family: String,

    @field:NotBlank
    val genus: String,

    val origin: String? = null,
    val habitat: String? = null,

    @field:Positive
    val lifespanCaptive: Int? = null,

    @field:Positive
    val lifespanWild: Int? = null,

    @field:Positive
    val avgSizeCm: Double? = null,

    @field:Positive
    val avgWeightG: Double? = null,

    val difficultyLevel: DifficultyLevel,
    val citesLevel: CitesLevel? = null,
    val legalStatusNote: String? = null,
    val thumbnailUrl: String? = null,
    val status: SpeciesStatus,
)
