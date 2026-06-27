package com.terrasage.api.encyclopedia.repository

import com.terrasage.api.encyclopedia.entity.AnimalCareGuide
import org.springframework.data.jpa.repository.JpaRepository

interface AnimalCareGuideRepository : JpaRepository<AnimalCareGuide, Long> {
    fun findBySpeciesId(speciesId: Long): AnimalCareGuide?
}
