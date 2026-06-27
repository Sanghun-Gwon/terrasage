package com.terrasage.api.encyclopedia.dto

import com.terrasage.api.encyclopedia.entity.GeneticPattern
import jakarta.validation.constraints.NotBlank

data class VariantCreateRequest(
    @field:NotBlank val name: String,
    val geneticPattern: GeneticPattern,
    val description: String? = null,
    val imageUrl: String? = null,
)
