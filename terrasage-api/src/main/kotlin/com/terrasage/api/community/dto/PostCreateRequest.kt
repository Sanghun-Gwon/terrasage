package com.terrasage.api.community.dto

import com.terrasage.api.community.entity.BoardType
import jakarta.validation.constraints.NotBlank

data class PostCreateRequest(
    @field:NotBlank val title: String,
    @field:NotBlank val content: String,
    val boardType: BoardType,
    val imageUrls: List<String>? = null,
)
