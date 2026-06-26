package com.terrasage.api.encyclopedia.repository

import com.terrasage.api.encyclopedia.entity.Species
import com.terrasage.api.encyclopedia.entity.SpeciesStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

// JpaSpecificationExecutor — 동적 검색용 Specification 패턴 지원
interface SpeciesRepository : JpaRepository<Species, Long>, JpaSpecificationExecutor<Species> {
    fun findByStatus(status: SpeciesStatus, pageable: Pageable): Page<Species>
    fun existsByScientificName(scientificName: String): Boolean
}
