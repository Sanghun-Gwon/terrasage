package com.terrasage.api.encyclopedia.repository

import com.terrasage.api.encyclopedia.entity.PlantCareGuide
import org.springframework.data.jpa.repository.JpaRepository

interface PlantCareGuideRepository : JpaRepository<PlantCareGuide, Long> {
    fun findBySpeciesId(speciesId: Long): PlantCareGuide?
}
