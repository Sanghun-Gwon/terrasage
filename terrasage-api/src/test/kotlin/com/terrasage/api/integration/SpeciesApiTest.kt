package com.terrasage.api.integration

import com.terrasage.api.auth.entity.UserRole
import com.terrasage.api.support.IntegrationTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.client.expectBody

class SpeciesApiTest : IntegrationTestBase() {

    // ── 공개 API (인증 불필요) ────────────────────────────────────────────────

    @Test
    fun `GET species - 토큰 없이 200 반환`() {
        val result = client.get().uri("/api/v1/species")
            .exchange()
            .expectStatus().isOk
            .expectBody<Map<String, Any>>()
            .returnResult().responseBody!!

        assertThat(result["success"]).isEqualTo(true)
    }

    @Test
    fun `GET species - 페이지 파라미터 적용`() {
        client.get().uri("/api/v1/species?size=5&page=0")
            .exchange()
            .expectStatus().isOk
    }

    // ── Admin API 인가 테스트 ─────────────────────────────────────────────────

    @Test
    fun `POST admin species - 토큰 없으면 401`() {
        client.post().uri("/api/v1/admin/species")
            .contentType(MediaType.APPLICATION_JSON)
            .body(minimalSpeciesRequest())
            .exchange()
            .expectStatus().isUnauthorized
    }

    @Test
    fun `POST admin species - USER 토큰이면 403`() {
        val token = createUserAndGetToken("species_user@test.com", UserRole.USER)

        client.post().uri("/api/v1/admin/species")
            .header("Authorization", bearerToken(token))
            .contentType(MediaType.APPLICATION_JSON)
            .body(minimalSpeciesRequest())
            .exchange()
            .expectStatus().isForbidden
    }

    @Test
    fun `POST admin species - ADMIN 토큰이면 201 Created`() {
        val token = createUserAndGetToken("species_admin@test.com", UserRole.ADMIN)

        @Suppress("UNCHECKED_CAST")
        val data = client.post().uri("/api/v1/admin/species")
            .header("Authorization", bearerToken(token))
            .contentType(MediaType.APPLICATION_JSON)
            .body(minimalSpeciesRequest())
            .exchange()
            .expectStatus().isCreated
            .expectBody<Map<String, Any>>()
            .returnResult().responseBody!!["data"] as Map<String, Any>

        assertThat(data["scientificName"]).isEqualTo("Testus testus")
    }

    @Test
    fun `DELETE admin species - 존재하지 않는 ID면 404`() {
        val token = createUserAndGetToken("del_admin@test.com", UserRole.ADMIN)

        client.delete().uri("/api/v1/admin/species/999999")
            .header("Authorization", bearerToken(token))
            .exchange()
            .expectStatus().isNotFound
    }

    // ── 헬퍼 ─────────────────────────────────────────────────────────────────

    private fun minimalSpeciesRequest() = mapOf(
        "scientificName" to "Testus testus",
        "commonNameKo" to "테스트 종",
        "kingdom" to "Animalia",
        "phylum" to "Chordata",
        "taxonomyClass" to "Reptilia",
        "taxonomyOrder" to "Squamata",
        "family" to "Testidae",
        "genus" to "Testus",
        "difficultyLevel" to "BEGINNER",
        "category" to "REPTILE",
    )
}
