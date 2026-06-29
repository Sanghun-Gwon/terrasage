package com.terrasage.api.auth.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UpdateProfileRequest(
    @field:NotBlank @field:Size(max = 20)
    val name: String,

    @field:Pattern(regexp = "^01[016789][- ]?(\\d{3,4})[- ]?(\\d{4})$", message = "올바른 전화번호 형식이 아닙니다")
    val phoneNumber: String? = null,
)
