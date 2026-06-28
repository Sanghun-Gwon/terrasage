package com.terrasage.api.support

import com.terrasage.api.auth.entity.User
import com.terrasage.api.auth.entity.UserRole
import com.terrasage.api.auth.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.expectBody
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
abstract class IntegrationTestBase {

    companion object {
        private val postgres: GenericContainer<*> = GenericContainer("postgres:16").apply {
            withExposedPorts(5432)
            withEnv("POSTGRES_DB", "terrasage_test")
            withEnv("POSTGRES_USER", "test")
            withEnv("POSTGRES_PASSWORD", "test")
            waitingFor(Wait.forListeningPort())
            start()
        }

        @DynamicPropertySource
        @JvmStatic
        fun configureDataSource(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") {
                "jdbc:postgresql://localhost:${postgres.getMappedPort(5432)}/terrasage_test"
            }
            registry.add("spring.datasource.username") { "test" }
            registry.add("spring.datasource.password") { "test" }
        }
    }

    @LocalServerPort protected var port: Int = 0

    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var passwordEncoder: PasswordEncoder

    protected lateinit var client: RestTestClient

    @BeforeEach
    fun initClient() {
        client = RestTestClient.bindToServer()
            .baseUrl("http://localhost:$port")
            .build()
    }

    fun createUserAndGetToken(email: String, role: UserRole = UserRole.USER): String {
        val password = "test1234"
        if (!userRepository.existsByEmail(email)) {
            userRepository.save(
                User(email = email, password = passwordEncoder.encode(password)!!, name = "테스터", role = role)
            )
        }
        // jwtProvider.generate()가 실제 서버 필터와 동일한 빈을 사용하지만,
        // 테스트 격리를 위해 실제 로그인 엔드포인트를 통해 토큰을 받음
        return loginAndGetToken(email, password)
    }

    private fun loginAndGetToken(email: String, password: String): String {
        @Suppress("UNCHECKED_CAST")
        val data = client.post().uri("/api/v1/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .body(mapOf("email" to email, "password" to password))
            .exchange()
            .expectStatus().isOk
            .expectBody<Map<String, Any>>()
            .returnResult().responseBody!!["data"] as Map<String, Any>
        return data["accessToken"] as String
    }

    protected fun bearerToken(token: String) = "Bearer $token"
}
