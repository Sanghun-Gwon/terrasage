package com.terrasage.api.auth.controller

import com.terrasage.api.auth.dto.LoginRequest
import com.terrasage.api.auth.dto.LoginResponse
import com.terrasage.api.auth.dto.SignupRequest
import com.terrasage.api.auth.service.AuthService
import com.terrasage.api.common.response.ApiResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun signup(@Valid @RequestBody request: SignupRequest): ApiResponse<Unit> {
        authService.signup(request)
        return ApiResponse.ok(Unit)
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ApiResponse<LoginResponse> =
        ApiResponse.ok(authService.login(request))
}
