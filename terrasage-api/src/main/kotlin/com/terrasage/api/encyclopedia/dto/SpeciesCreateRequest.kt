package com.terrasage.api.encyclopedia.dto

import com.terrasage.api.encyclopedia.entity.CitesLevel
import com.terrasage.api.encyclopedia.entity.DifficultyLevel
import com.terrasage.api.encyclopedia.entity.Species
import com.terrasage.api.encyclopedia.entity.SpeciesCategory
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

// 종 등록 요청 DTO
// @field: — Kotlin data class는 생성자 파라미터이므로, 필드 레벨 검증을 위해 use-site target 필수
data class SpeciesCreateRequest(
    @field:NotBlank
    val scientificName: String,

    @field:NotBlank
    val commonNameKo: String,

    val commonNameEn: String? = null,

    // 분류 정보 (필수)
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

    // 선택 정보
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
    val category: SpeciesCategory? = null,
    val citesLevel: CitesLevel? = null,
    val legalStatusNote: String? = null,
    val thumbnailUrl: String? = null,
) {
    // Request → Entity 변환 (status는 항상 DRAFT로 시작)
    fun toEntity() = Species(
        scientificName = scientificName,
        commonNameKo = commonNameKo,
        commonNameEn = commonNameEn,
        kingdom = kingdom,
        phylum = phylum,
        taxonomyClass = taxonomyClass,
        taxonomyOrder = taxonomyOrder,
        family = family,
        genus = genus,
        origin = origin,
        habitat = habitat,
        lifespanCaptive = lifespanCaptive,
        lifespanWild = lifespanWild,
        avgSizeCm = avgSizeCm,
        avgWeightG = avgWeightG,
        difficultyLevel = difficultyLevel,
        category = category,
        citesLevel = citesLevel,
        legalStatusNote = legalStatusNote,
        thumbnailUrl = thumbnailUrl,
    )
}
