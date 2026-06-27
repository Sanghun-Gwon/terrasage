package com.terrasage.api.encyclopedia.dto

import com.terrasage.api.encyclopedia.entity.GeneticPattern
import com.terrasage.api.encyclopedia.entity.Variant

data class VariantResponse(
    val id: Long,
    val name: String,
    val geneticPattern: GeneticPattern,
    val description: String?,
    val imageUrl: String?,
) {
    companion object {
        fun from(variant: Variant) = VariantResponse(
            id = variant.id,
            name = variant.name,
            geneticPattern = variant.geneticPattern,
            description = variant.description,
            imageUrl = variant.imageUrl,
        )
    }
}
