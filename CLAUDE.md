# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

TerraSage - 생물 백과사전 및 사육 관리 플랫폼

## Core Features

- 생물 백과사전 (분류학 기반, 아종/변이 관리)
- 사육자 커뮤니티
- 개인 사육환경 관리 (온도, 습도, 먹이, 광량 시각화)
- AI 사육 가이드 및 알림
- 건강 진단 (AI 분석, 스마트 모니터링)
- 마켓플레이스 (거래/경매, PG 결제)

---

## Tech Stack

> 공식 릴리즈 기준 (2026-06 확인)

| 항목 | 스펙 | 비고 |
|------|------|------|
| Language | Kotlin 2.4.0 | https://kotlinlang.org/docs/releases.html |
| JVM | JDK 21 (LTS) | Spring Boot 4.x 최소 요구사항 |
| Framework | Spring Boot 4.1.0 | https://spring.io/projects/spring-boot |
| Build Tool | Gradle 8.x (Kotlin DSL) | build.gradle.kts |
| Primary DB | PostgreSQL 16 | JSONB 지원, 관계형 데이터 적합 |
| Search | Elasticsearch 9.x | 한국어 nori analyzer — **2차 도입** |
| ORM | Spring Data JPA (Hibernate 7) | Spring Boot 4.x 기본 포함 |
| Dynamic Query | Spring Data JPA Specifications | QueryDSL 대신 사용 (Kotlin 2.x KAPT 지원 종료 예정) |
| Test | JUnit 5 + MockK + Testcontainers | MockK: Kotlin 전용 mock 라이브러리 |
| Linter | ktlint | Kotlin 공식 코딩 컨벤션 자동 적용 |

> ES는 MVP에서 PG 단독으로 시작. Repository 인터페이스로 추상화하여 ES 전환 시 영향 최소화.

---

## Module Structure

| 모듈 | 설명 |
|------|------|
| terrasage-api | REST API 서버 (메인 개발 대상) |
| terrasage-admin | 관리자 페이지 (후순위) |
| terrasage-web | 사용자 웹 페이지 (후순위) |

---

## Architecture

### 기본 원칙

**표준 레이어드 아키텍처를 따른다. 과한 추상화, 불필요한 패턴 적용 금지.**

```
Controller → Service → Repository → DB
```

- Controller: HTTP 요청/응답 처리, 입력 유효성 검증
- Service: 비즈니스 로직
- Repository: Spring Data JPA 인터페이스, DB 접근
- Entity: DB 테이블 매핑 전용 (Controller 직접 노출 금지)
- DTO: Request / Response 분리

### 지켜야 할 것

- **인터페이스 최소화**: Service 인터페이스는 복수 구현체가 있을 때만 생성. 1개면 클래스 직접 사용.
- **Spring 기능 신뢰**: Spring의 DI, 트랜잭션, 예외 처리를 그대로 활용. 위에 레이어 추가 금지.
- **DTO 변환 위치**: Entity → DTO 변환은 Service 레이어에서 처리.
- **단순한 것 우선**: Repository method naming → JPQL `@Query` → Specifications 순서로 복잡도를 올린다.

### 피해야 할 것

- 구현체 1개인데 인터페이스 + Impl 분리
- 모든 클래스에 Factory/Builder 패턴 강제 적용
- 사용하지 않는 기능을 위한 확장 구조 미리 설계
- `@Autowired` 필드 주입 (생성자 주입 사용)
- Kotlin에서 Java 스타일 코드 작성 (getter/setter, null 체크 if-else 등)

---

## Package Structure

```
com.terrasage
└── api
    ├── {domain}
    │   ├── controller       # @RestController
    │   ├── service          # 비즈니스 로직
    │   ├── repository       # Spring Data JPA Repository
    │   ├── entity           # @Entity
    │   └── dto              # Request / Response DTO
    ├── common
    │   ├── config           # Spring 설정 (@Configuration)
    │   ├── exception        # 예외 클래스 + @RestControllerAdvice
    │   └── response         # ApiResponse 공통 응답 래퍼
    └── TerrasageApiApplication.kt
```

도메인 예시: `encyclopedia`, `community`, `care`, `marketplace`

---

## Code Style

Kotlin 공식 코딩 컨벤션 준수 (ktlint로 자동 강제).

### 네이밍

| 대상 | 규칙 | 예시 |
|------|------|------|
| 클래스/인터페이스/enum | PascalCase | `SpeciesService`, `DifficultyLevel` |
| 함수/변수/프로퍼티 | camelCase | `findAllSpecies()`, `commonNameKo` |
| 상수 (`companion object`) | SCREAMING_SNAKE_CASE | `MAX_PAGE_SIZE = 100` |
| 패키지 | lowercase | `com.terrasage.api.encyclopedia` |
| DB 테이블/컬럼 | snake_case | `care_guide`, `temp_hot_zone` |
| API endpoint | kebab-case, 복수형 명사 | `GET /api/v1/species/{id}/care-guide` |

### 공통 응답 형식

```kotlin
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: ErrorResponse? = null,
)

data class ErrorResponse(
    val code: String,
    val message: String,
)
```

### 예외 처리

```kotlin
// 베이스 예외
open class TerrasageException(
    val code: String,
    message: String,
) : RuntimeException(message)

// 도메인별 예외
class NotFoundException(resource: String, id: Any) :
    TerrasageException("NOT_FOUND", "$resource($id)을 찾을 수 없습니다")

class DuplicateException(resource: String) :
    TerrasageException("DUPLICATE", "$resource이 이미 존재합니다")
```

- 전역 처리: `@RestControllerAdvice`
- HTTP 상태: 200 / 201 / 400 / 404 / 409 / 500

### Kotlin 관용 코드

```kotlin
// ❌ Java 스타일
val name = if (species.commonNameKo != null) species.commonNameKo else "Unknown"

// ✅ Kotlin 스타일
val name = species.commonNameKo ?: "Unknown"

// ❌ getter/setter
class Species {
    var name: String = ""
    fun getName() = name
}

// ✅ data class
data class SpeciesResponse(
    val id: Long,
    val commonNameKo: String,
    val scientificName: String,
)
```

### 기타

- `var` 대신 `val` 우선
- nullable(`?`) 은 실제로 null이 가능한 경우에만
- 함수 길이 30줄 이하 권장
- 주석은 WHY가 명확할 때만 (WHAT 설명 주석 금지)

---

## Documentation

- [프로젝트 개요](./docs/overview.md)
- [백과사전 기능](./docs/features/encyclopedia.md)
- [사육환경 관리](./docs/features/care-management.md)
- [커뮤니티](./docs/features/community.md)
- [건강 진단](./docs/features/health-diagnosis.md)
- [마켓플레이스](./docs/features/marketplace.md)
