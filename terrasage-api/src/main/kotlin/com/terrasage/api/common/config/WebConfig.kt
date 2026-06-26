package com.terrasage.api.common.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

// CORS 설정 — 로컬 개발용 (Next.js 개발 서버 포트 허용)
//
// [Security 연동 시 변경 필요]
// Spring Security 추가 후에는 이 설정 대신 SecurityFilterChain에서 cors() 설정으로 이전:
//   http.cors { it.configurationSource(corsConfigurationSource()) }
// 그렇지 않으면 Spring Security가 CORS 요청을 먼저 차단함.
// 운영 환경에서는 allowedOrigins를 실제 도메인으로 교체할 것.
@Configuration
class WebConfig : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/api/**")
            .allowedOrigins(
                "http://localhost:3000",  // terrasage-web
                "http://localhost:3001",  // terrasage-admin
            )
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .maxAge(3600)
    }
}
