package com.terrasage.api.auth.controller

import com.terrasage.api.auth.dto.*
import com.terrasage.api.auth.service.AuthService
import com.terrasage.api.common.response.ApiResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class AuthController(private val authService: AuthService) {

    @PostMapping("/api/v1/auth/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun signup(@Valid @RequestBody request: SignupRequest): ApiResponse<Unit> {
        authService.signup(request)
        return ApiResponse.ok(Unit)
    }

    @PostMapping("/api/v1/auth/login")
    fun login(@Valid @RequestBody request: LoginRequest): ApiResponse<LoginResponse> =
        ApiResponse.ok(authService.login(request))

    @GetMapping("/api/v1/me")
    fun getMyProfile(@AuthenticationPrincipal email: String): ApiResponse<MyProfileResponse> =
        ApiResponse.ok(authService.getMyProfile(email))

    @PutMapping("/api/v1/me")
    fun updateProfile(
        @Valid @RequestBody request: UpdateProfileRequest,
        @AuthenticationPrincipal email: String,
    ): ApiResponse<MyProfileResponse> =
        ApiResponse.ok(authService.updateProfile(email, request))

    @PutMapping("/api/v1/me/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changePassword(
        @Valid @RequestBody request: ChangePasswordRequest,
        @AuthenticationPrincipal email: String,
    ) = authService.changePassword(email, request)
}
