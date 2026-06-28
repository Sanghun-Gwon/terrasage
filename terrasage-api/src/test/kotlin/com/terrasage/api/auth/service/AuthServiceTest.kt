package com.terrasage.api.auth.service

import com.terrasage.api.auth.dto.LoginRequest
import com.terrasage.api.auth.dto.SignupRequest
import com.terrasage.api.auth.entity.User
import com.terrasage.api.auth.entity.UserRole
import com.terrasage.api.auth.repository.UserRepository
import com.terrasage.api.common.exception.DuplicateException
import com.terrasage.api.common.exception.TerrasageException
import com.terrasage.api.common.security.JwtProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.crypto.password.PasswordEncoder

class AuthServiceTest {

    private val userRepository: UserRepository = mockk()
    private val passwordEncoder: PasswordEncoder = mockk()
    private val jwtProvider: JwtProvider = mockk()

    private lateinit var authService: AuthService

    @BeforeEach
    fun setUp() {
        authService = AuthService(userRepository, passwordEncoder, jwtProvider)
    }

    // ── Signup ──────────────────────────────────────────────────────────────

    @Test
    fun `signup - 정상 요청이면 User 저장`() {
        every { userRepository.existsByEmail("new@test.com") } returns false
        every { passwordEncoder.encode("password123") } returns "hashed"
        every { userRepository.save(any()) } answers { firstArg() }

        authService.signup(SignupRequest("new@test.com", "password123", "신규회원"))

        verify { userRepository.save(match { it.email == "new@test.com" && it.password == "hashed" }) }
    }

    @Test
    fun `signup - 중복 이메일이면 DuplicateException 발생`() {
        every { userRepository.existsByEmail("dup@test.com") } returns true

        assertThrows<DuplicateException> {
            authService.signup(SignupRequest("dup@test.com", "password123", "중복회원"))
        }
    }

    @Test
    fun `signup - 기본 역할은 USER`() {
        every { userRepository.existsByEmail(any()) } returns false
        every { passwordEncoder.encode(any()) } returns "hashed"
        every { userRepository.save(any()) } answers { firstArg() }

        authService.signup(SignupRequest("role@test.com", "password123", "일반회원"))

        verify { userRepository.save(match { it.role == UserRole.USER }) }
    }

    // ── Login ───────────────────────────────────────────────────────────────

    @Test
    fun `login - 올바른 자격증명이면 JWT 반환`() {
        val user = User(email = "user@test.com", password = "hashed", name = "회원")
        every { userRepository.findByEmail("user@test.com") } returns user
        every { passwordEncoder.matches("password123", "hashed") } returns true
        every { jwtProvider.generate("user@test.com", "USER") } returns "jwt.token.here"

        val result = authService.login(LoginRequest("user@test.com", "password123"))

        assertThat(result.accessToken).isEqualTo("jwt.token.here")
        assertThat(result.email).isEqualTo("user@test.com")
        assertThat(result.tokenType).isEqualTo("Bearer")
    }

    @Test
    fun `login - 존재하지 않는 이메일이면 예외 발생`() {
        every { userRepository.findByEmail("none@test.com") } returns null

        val ex = assertThrows<TerrasageException> {
            authService.login(LoginRequest("none@test.com", "password123"))
        }
        assertThat(ex.code).isEqualTo("INVALID_CREDENTIALS")
    }

    @Test
    fun `login - 비밀번호가 틀리면 예외 발생`() {
        val user = User(email = "user@test.com", password = "hashed", name = "회원")
        every { userRepository.findByEmail("user@test.com") } returns user
        every { passwordEncoder.matches("wrongpw", "hashed") } returns false

        val ex = assertThrows<TerrasageException> {
            authService.login(LoginRequest("user@test.com", "wrongpw"))
        }
        assertThat(ex.code).isEqualTo("INVALID_CREDENTIALS")
    }

    @Test
    fun `login - 어드민 계정 로그인 시 role이 ADMIN`() {
        val admin = User(email = "admin@test.com", password = "hashed", name = "관리자", role = UserRole.ADMIN)
        every { userRepository.findByEmail("admin@test.com") } returns admin
        every { passwordEncoder.matches("admin123", "hashed") } returns true
        every { jwtProvider.generate("admin@test.com", "ADMIN") } returns "admin.jwt.token"

        val result = authService.login(LoginRequest("admin@test.com", "admin123"))

        assertThat(result.role).isEqualTo(UserRole.ADMIN)
        verify { jwtProvider.generate("admin@test.com", "ADMIN") }
    }
}
