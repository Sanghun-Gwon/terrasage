package com.terrasage.api.encyclopedia.dto

import com.terrasage.api.encyclopedia.entity.GeneticPattern
import com.terrasage.api.encyclopedia.entity.Morph

// 모프 응답 DTO
data class MorphResponse(
    val id: Long,
    val name: String,
    val geneticPattern: GeneticPattern,
    val description: String?,
    val imageUrl: String?,
) {
    companion object {
        fun from(morph: Morph) = MorphResponse(
            id = morph.id,
            name = morph.name,
            geneticPattern = morph.geneticPattern,
            description = morph.description,
            imageUrl = morph.imageUrl,
        )
    }
}
