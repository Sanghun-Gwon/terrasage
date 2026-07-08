package com.terrasage.api.image.dto

import jakarta.validation.constraints.NotBlank

data class UploadUrlRequest(
    @field:NotBlank(message = "contentType은 필수입니다")
    val contentType: String,
)
