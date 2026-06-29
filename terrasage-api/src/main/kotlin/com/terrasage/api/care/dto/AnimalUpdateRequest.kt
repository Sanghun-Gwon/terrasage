package com.terrasage.api.care.dto

import com.terrasage.api.care.entity.Gender
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDate

data class AnimalUpdateRequest(
    val speciesId: Long? = null,

    @field:Size(max = 100)
    val speciesName: String? = null,

    @field:NotBlank @field:Size(max = 50)
    val name: String,

    @field:Size(max = 50)
    val nickname: String? = null,

    val birthDate: LocalDate? = null,
    val gender: Gender = Gender.UNKNOWN,
    val notes: String? = null,
    val isPublic: Boolean = false,
)
