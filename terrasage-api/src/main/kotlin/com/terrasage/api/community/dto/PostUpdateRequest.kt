package com.terrasage.api.community.dto

import jakarta.validation.constraints.NotBlank

data class PostUpdateRequest(
    @field:NotBlank val title: String,
    @field:NotBlank val content: String,
    val imageUrls: List<String>? = null,
)
