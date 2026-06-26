package com.terrasage.api.encyclopedia.dto

import com.terrasage.api.encyclopedia.entity.DifficultyLevel
import com.terrasage.api.encyclopedia.entity.Species
import com.terrasage.api.encyclopedia.entity.SpeciesCategory
import com.terrasage.api.encyclopedia.entity.SpeciesStatus

// 종 목록 조회 응답 — 리스트에 필요한 최소 필드만 포함
data class SpeciesListResponse(
    val id: Long,
    val scientificName: String,
    val commonNameKo: String,
    val commonNameEn: String?,
    val thumbnailUrl: String?,
    val category: SpeciesCategory?,
    val difficultyLevel: DifficultyLevel,
    val family: String,
    val status: SpeciesStatus,
) {
    companion object {
        fun from(species: Species) = SpeciesListResponse(
            id = species.id,
            scientificName = species.scientificName,
            commonNameKo = species.commonNameKo,
            commonNameEn = species.commonNameEn,
            thumbnailUrl = species.thumbnailUrl,
            category = species.category,
            difficultyLevel = species.difficultyLevel,
            family = species.family,
            status = species.status,
        )
    }
}
