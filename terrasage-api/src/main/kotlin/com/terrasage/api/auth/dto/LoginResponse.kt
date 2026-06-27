package com.terrasage.api.auth.dto

import com.terrasage.api.auth.entity.UserRole

data class LoginResponse(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val email: String,
    val name: String,
    val role: UserRole,
)
