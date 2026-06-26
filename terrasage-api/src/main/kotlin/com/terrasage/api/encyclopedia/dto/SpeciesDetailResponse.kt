package com.terrasage.api.encyclopedia.dto

import com.terrasage.api.encyclopedia.entity.*

// 종 상세 조회 응답 — CareGuide, Morph 목록 포함
data class SpeciesDetailResponse(
    val id: Long,
    val scientificName: String,
    val commonNameKo: String,
    val commonNameEn: String?,
    val kingdom: String,
    val phylum: String,
    val taxonomyClass: String,
    val taxonomyOrder: String,
    val family: String,
    val genus: String,
    val origin: String?,
    val habitat: String?,
    val lifespanCaptive: Int?,
    val lifespanWild: Int?,
    val avgSizeCm: Double?,
    val avgWeightG: Double?,
    val difficultyLevel: DifficultyLevel,
    val citesLevel: CitesLevel?,
    val legalStatusNote: String?,
    val thumbnailUrl: String?,
    val status: SpeciesStatus,
    val careGuide: CareGuideResponse?,
    val morphs: List<MorphResponse>,
) {
    companion object {
        fun from(
            species: Species,
            careGuide: CareGuide?,
            morphs: List<Morph>,
        ) = SpeciesDetailResponse(
            id = species.id,
            scientificName = species.scientificName,
            commonNameKo = species.commonNameKo,
            commonNameEn = species.commonNameEn,
            kingdom = species.kingdom,
            phylum = species.phylum,
            taxonomyClass = species.taxonomyClass,
            taxonomyOrder = species.taxonomyOrder,
            family = species.family,
            genus = species.genus,
            origin = species.origin,
            habitat = species.habitat,
            lifespanCaptive = species.lifespanCaptive,
            lifespanWild = species.lifespanWild,
            avgSizeCm = species.avgSizeCm,
            avgWeightG = species.avgWeightG,
            difficultyLevel = species.difficultyLevel,
            citesLevel = species.citesLevel,
            legalStatusNote = species.legalStatusNote,
            thumbnailUrl = species.thumbnailUrl,
            status = species.status,
            careGuide = careGuide?.let { CareGuideResponse.from(it) },
            morphs = morphs.map { MorphResponse.from(it) },
        )
    }
}
