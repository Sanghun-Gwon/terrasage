package com.terrasage.api.encyclopedia.repository

import com.terrasage.api.encyclopedia.entity.Variant
import org.springframework.data.jpa.repository.JpaRepository

interface VariantRepository : JpaRepository<Variant, Long> {
    fun findBySpeciesId(speciesId: Long): List<Variant>
    fun deleteBySpeciesId(speciesId: Long)
}
