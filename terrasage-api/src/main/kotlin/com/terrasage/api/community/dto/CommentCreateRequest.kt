package com.terrasage.api.community.dto

import jakarta.validation.constraints.NotBlank

data class CommentCreateRequest(
    @field:NotBlank val content: String,
    val parentId: Long? = null,
)
