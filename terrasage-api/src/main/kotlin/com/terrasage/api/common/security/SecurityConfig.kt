package com.terrasage.api.common.security

import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

private fun HttpServletResponse.writeJson(status: Int, code: String, message: String) {
    this.status = status
    contentType = MediaType.APPLICATION_JSON_VALUE
    writer.write("""{"success":false,"data":null,"error":{"code":"$code","message":"$message"}}""")
}

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(private val jwtProvider: JwtProvider) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .csrf { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/api/v1/auth/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/v1/species/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/v1/species").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/v1/posts").permitAll()
                    .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            }
            .exceptionHandling { ex ->
                // sendError()는 /error로 포워드하여 Security 필터 체인을 다시 탄다.
                // 직접 응답 작성으로 포워드 없이 즉시 반환.
                ex.authenticationEntryPoint { _, response, _ ->
                    response.writeJson(HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED", "인증이 필요합니다")
                }
                ex.accessDeniedHandler { _, response, _ ->
                    response.writeJson(HttpServletResponse.SC_FORBIDDEN, "FORBIDDEN", "권한이 없습니다")
                }
            }
            .addFilterBefore(JwtAuthFilter(jwtProvider), UsernamePasswordAuthenticationFilter::class.java)
            .build()

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration().apply {
            allowedOrigins = listOf("http://localhost:3000", "http://localhost:3001")
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
            allowedHeaders = listOf("*")
            allowCredentials = true
        }
        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", config)
        }
    }
}
