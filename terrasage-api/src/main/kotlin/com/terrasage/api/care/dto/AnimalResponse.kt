package com.terrasage.api.care.dto

import com.terrasage.api.care.entity.Animal
import com.terrasage.api.care.entity.Gender
import java.time.LocalDate
import java.time.LocalDateTime

data class AnimalResponse(
    val id: Long,
    val speciesId: Long?,
    val speciesName: String,
    val name: String,
    val nickname: String?,
    val birthDate: LocalDate?,
    val gender: Gender,
    val notes: String?,
    val isPublic: Boolean,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(animal: Animal) = AnimalResponse(
            id = animal.id,
            speciesId = animal.species?.id,
            speciesName = animal.displaySpeciesName(),
            name = animal.name,
            nickname = animal.nickname,
            birthDate = animal.birthDate,
            gender = animal.gender,
            notes = animal.notes,
            isPublic = animal.isPublic,
            createdAt = animal.createdAt,
        )
    }
}
