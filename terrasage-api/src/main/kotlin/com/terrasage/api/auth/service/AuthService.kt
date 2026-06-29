package com.terrasage.api.auth.service

import com.terrasage.api.auth.dto.LoginRequest
import com.terrasage.api.auth.dto.LoginResponse
import com.terrasage.api.auth.dto.SignupRequest
import com.terrasage.api.auth.entity.User
import com.terrasage.api.auth.repository.UserRepository
import com.terrasage.api.common.exception.DuplicateException
import com.terrasage.api.common.exception.TerrasageException
import com.terrasage.api.common.security.JwtProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtProvider: JwtProvider,
) {
    @Transactional
    fun signup(request: SignupRequest) {
        if (userRepository.existsByEmail(request.email)) {
            throw DuplicateException("이메일 '${request.email}'")
        }
        userRepository.save(
            User(
                email = request.email,
                password = passwordEncoder.encode(request.password)!!,
                name = request.name,
                phoneNumber = request.phoneNumber,
            )
        )
    }

    fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw TerrasageException("INVALID_CREDENTIALS", "이메일 또는 비밀번호가 올바르지 않습니다")

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw TerrasageException("INVALID_CREDENTIALS", "이메일 또는 비밀번호가 올바르지 않습니다")
        }

        val token = jwtProvider.generate(user.email, user.role.name)
        return LoginResponse(
            accessToken = token,
            email = user.email,
            name = user.name,
            role = user.role,
        )
    }
}
