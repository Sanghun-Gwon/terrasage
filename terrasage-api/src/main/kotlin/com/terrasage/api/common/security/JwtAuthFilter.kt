package com.terrasage.api.common.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

// @Component 제거: Spring Boot가 자동으로 서블릿 필터로 등록하면
// OncePerRequestFilter의 "이미 실행됨" 표시가 Security 필터 체인 내 실행을 건너뜀
// SecurityConfig에서 직접 생성하여 Security 체인에만 등록
class JwtAuthFilter(private val jwtProvider: JwtProvider) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
    ) {
        val token = resolveToken(request)
        if (token != null && jwtProvider.validate(token)) {
            val email = jwtProvider.getEmail(token)
            val role = jwtProvider.getRole(token)
            val auth = UsernamePasswordAuthenticationToken(
                email, null, listOf(SimpleGrantedAuthority("ROLE_$role"))
            )
            // Spring Security 7.x STATELESS 환경에서 getContext()가 호출마다 새 인스턴스를 반환.
            // setContext()로 thread-local을 교체해야 이후 필터에서 authentication이 보임.
            val context = SecurityContextHolder.createEmptyContext()
            context.authentication = auth
            SecurityContextHolder.setContext(context)
        }
        chain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? =
        request.getHeader("Authorization")
            ?.takeIf { it.startsWith("Bearer ") }
            ?.substring(7)
}
