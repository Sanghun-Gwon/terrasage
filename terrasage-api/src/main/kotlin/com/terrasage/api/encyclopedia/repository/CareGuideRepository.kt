package com.terrasage.api.encyclopedia.repository

import com.terrasage.api.encyclopedia.entity.CareGuide
import org.springframework.data.jpa.repository.JpaRepository

// Species와 1:1 관계 — findBySpeciesId는 단일 결과 반환 (nullable)
interface CareGuideRepository : JpaRepository<CareGuide, Long> {
    fun findBySpeciesId(speciesId: Long): CareGuide?
}
