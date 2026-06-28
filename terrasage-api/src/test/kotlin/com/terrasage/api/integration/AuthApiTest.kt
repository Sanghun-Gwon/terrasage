package com.terrasage.api.integration

import com.terrasage.api.auth.dto.SignupRequest
import com.terrasage.api.support.IntegrationTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.client.expectBody

class AuthApiTest : IntegrationTestBase() {

    // ── POST /api/v1/auth/signup ─────────────────────────────────────────────

    @Test
    fun `signup - 201 Created 반환`() {
        val body = SignupRequest("signup_ok@test.com", "password123", "신규회원")

        val result = client.post().uri("/api/v1/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .body(body)
            .exchange()
            .expectStatus().isCreated
            .expectBody<Map<String, Any>>()
            .returnResult().responseBody!!

        assertThat(result["success"]).isEqualTo(true)
    }

    @Test
    fun `signup - 중복 이메일이면 409 Conflict`() {
        val body = SignupRequest("dup_signup@test.com", "password123", "중복")

        client.post().uri("/api/v1/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .body(body)
            .exchange()
            .expectStatus().isCreated

        val result = client.post().uri("/api/v1/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .body(body)
            .exchange()
            .expectStatus().isEqualTo(409)
            .expectBody<Map<String, Any>>()
            .returnResult().responseBody!!

        assertThat(result["success"]).isEqualTo(false)
    }

    @Test
    fun `signup - 유효성 실패(짧은 비밀번호)면 400 Bad Request`() {
        val body = SignupRequest("valid_email@test.com", "short", "회원")

        client.post().uri("/api/v1/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .body(body)
            .exchange()
            .expectStatus().isBadRequest
    }

    // ── POST /api/v1/auth/login ──────────────────────────────────────────────

    @Test
    fun `login - 올바른 자격증명이면 JWT 반환`() {
        client.post().uri("/api/v1/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .body(SignupRequest("login_ok@test.com", "password123", "회원"))
            .exchange()
            .expectStatus().isCreated

        @Suppress("UNCHECKED_CAST")
        val data = client.post().uri("/api/v1/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .body(mapOf("email" to "login_ok@test.com", "password" to "password123"))
            .exchange()
            .expectStatus().isOk
            .expectBody<Map<String, Any>>()
            .returnResult().responseBody!!["data"] as Map<String, Any>

        assertThat(data["accessToken"]).isNotNull()
        assertThat(data["tokenType"]).isEqualTo("Bearer")
        assertThat(data["role"]).isEqualTo("USER")
    }

    @Test
    fun `login - 비밀번호 오류면 success=false`() {
        client.post().uri("/api/v1/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .body(SignupRequest("wrong_pw@test.com", "password123", "회원"))
            .exchange()
            .expectStatus().isCreated

        val result = client.post().uri("/api/v1/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .body(mapOf("email" to "wrong_pw@test.com", "password" to "wrongpassword"))
            .exchange()
            .expectBody<Map<String, Any>>()
            .returnResult().responseBody!!

        assertThat(result["success"]).isEqualTo(false)
    }

    @Test
    fun `login - 존재하지 않는 이메일이면 success=false`() {
        val result = client.post().uri("/api/v1/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .body(mapOf("email" to "ghost@test.com", "password" to "password123"))
            .exchange()
            .expectBody<Map<String, Any>>()
            .returnResult().responseBody!!

        assertThat(result["success"]).isEqualTo(false)
    }
}
