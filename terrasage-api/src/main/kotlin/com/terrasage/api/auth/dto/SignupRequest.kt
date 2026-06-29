package com.terrasage.api.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class SignupRequest(
    @field:Email @field:NotBlank val email: String,
    @field:NotBlank @field:Size(min = 8) val password: String,
    @field:NotBlank @field:Size(max = 20) val name: String,
    // 010-XXXX-XXXX 또는 01012345678 형식 허용, 선택
    @field:Pattern(regexp = "^01[016789][- ]?(\\d{3,4})[- ]?(\\d{4})$", message = "올바른 전화번호 형식이 아닙니다")
    val phoneNumber: String? = null,
)
