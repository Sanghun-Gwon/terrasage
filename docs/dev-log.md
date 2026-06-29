# TerraSage 개발 로그

> 작업 기록 및 설계 결정 사항 정리

---

## 2026-06 세션 1 — 백과사전 기초

### 완료 내용
- 프로젝트 초기화 (Kotlin 2.4, Spring Boot 4.1, Gradle 9.5)
- `terrasage-api` 백과사전 CRUD API 구현
- `terrasage-web` 종 목록/상세 페이지
- `terrasage-admin` 종 관리 페이지

### 주요 파일
| 위치 | 설명 |
|------|------|
| `terrasage-api/src/main/kotlin/.../encyclopedia/` | 백과사전 도메인 (entity/repo/service/controller) |
| `terrasage-web/src/app/species/` | 웹 종 목록·상세 |
| `terrasage-admin/src/app/species/` | 어드민 종 관리 |

---

## 2026-06 세션 2 — 백과사전 개선 + 인증 + 커뮤니티

### 1. 백과사전 개선

#### Morph → Variant 리네임
기존 `Morph` (모프) 용어가 파충류에 국한된 표현이라 `Variant` (변이/품종)로 전체 리네임.

- `Morph.kt` → `Variant.kt` (파일은 유지, 빈 stub으로 전환)
- API 엔드포인트: `/{speciesId}/morphs` → `/{speciesId}/variants`
- 프론트엔드 타입: `Morph` → `Variant`

#### CareGuide 엔티티 분리
동물/식물의 사육 환경 표현 방식이 달라 단일 테이블 → 도메인별 분리.

| 전 | 후 |
|----|-----|
| `CareGuide` (단일) | `AnimalCareGuide` + `PlantCareGuide` (분리) |
| `care_guide` 테이블 | `animal_care_guide` + `plant_care_guide` 테이블 |
| `/care-guide` | `/animal-care-guide` + `/plant-care-guide` |

**AnimalCareGuide 주요 필드**
```
enclosureType, enclosureSizeCm, substrate
tempHotZone, tempCoolZone, tempNight
humidityMin, humidityMax, uvbRequired, photoperiodHours
feedType, feedFrequency, supplements
handlingLevel, cohabitationNote
```

**PlantCareGuide 주요 필드**
```
potType, growingMedium
lightRequirement, lightHoursPerDay
tempMin, tempMax, humidityMin, humidityMax
wateringFrequency, wateringMethod
fertilizerType, fertilizerFrequency
repottingNote, pruningNote
```

#### 샘플 데이터 확장
- 총 27종 (파충류 12, 양서류 3, 수생생물 5, 수생식물 3, 분재 2, 식충식물 2)
- 각 종의 사육/재배 가이드 데이터

---

### 2. 인증/보안

#### 구성 요소

```
com.terrasage.api
├── auth/
│   ├── entity/     User, UserRole
│   ├── repository/ UserRepository
│   ├── dto/        LoginRequest, LoginResponse, SignupRequest
│   ├── service/    AuthService
│   └── controller/ AuthController
└── common/security/
    ├── JwtProvider      # JWT 생성/검증 (jjwt 0.12.6)
    ├── JwtAuthFilter    # Bearer 토큰 추출, SecurityContext 설정
    └── SecurityConfig   # 라우트 보호, CORS, BCrypt
```

#### 공개/보호 라우트
| 라우트 | 인증 |
|--------|------|
| `POST /api/v1/auth/**` | 없음 |
| `GET /api/v1/species/**` | 없음 |
| `GET /api/v1/posts/**` | 없음 |
| `POST/PUT/DELETE /api/v1/posts/**` | 로그인 필요 |
| `/api/v1/admin/**` | ADMIN 역할 필요 |

#### JWT 구조
- 알고리즘: HS384 (HMAC-SHA384)
- 페이로드: `sub` (email), `role`, `iat`, `exp`
- 만료: 1시간 (설정: `jwt.expiration-ms`)
- 환경변수: `JWT_SECRET` (없으면 개발용 기본값 사용)

#### 테스트 계정
| 이메일 | 비밀번호 | 역할 | 닉네임 |
|--------|----------|------|--------|
| `admin@terrasage.com` | `admin1234` | ADMIN | 관리자 |
| `user@terrasage.com` | `user1234` | USER | 레오게코덕후 |

---

### 3. 커뮤니티 도메인

#### 엔티티 구조
```
com.terrasage.api.community
├── entity/
│   ├── BoardType    # SHOWCASE / TIPS / MORPH / QNA / FREE
│   ├── Post         # 게시글 (imageUrls: 쉼표 구분 URL)
│   ├── Comment      # 댓글 (parent_id로 대댓글 지원)
│   └── PostLike     # 좋아요 (post_id + user_id 유니크 제약)
├── repository/      PostRepository, CommentRepository, PostLikeRepository
├── dto/             PostListResponse, PostDetailResponse, CommentResponse, ...
├── service/         PostService
└── controller/      PostController
```

#### 게시판 타입
| 타입 | 설명 | 특이사항 |
|------|------|----------|
| SHOWCASE | 개체 자랑 | imageUrls 활용 (썸네일 표시) |
| TIPS | 사육팁/환경 공유 | |
| MORPH | 모프/변이종 정보 | |
| QNA | 질문/답변 | 댓글로 토론 |
| FREE | 자유게시판 | |

#### API 엔드포인트
```
GET    /api/v1/posts               게시글 목록 (boardType 필터, 페이지)
GET    /api/v1/posts/{id}          게시글 상세 (댓글 포함)
POST   /api/v1/posts               게시글 작성 [로그인]
PUT    /api/v1/posts/{id}          게시글 수정 [작성자]
DELETE /api/v1/posts/{id}          게시글 삭제 [작성자/ADMIN]
GET    /api/v1/posts/{id}/comments 댓글 목록
POST   /api/v1/posts/{id}/comments 댓글 작성 [로그인]
DELETE /api/v1/posts/{id}/comments/{commentId}  댓글 삭제 [작성자/ADMIN]
POST   /api/v1/posts/{id}/likes    좋아요 토글 [로그인]
```

#### imageUrls 설계 결정
단순성 우선. 실제 이미지 업로드(S3 등) 없이 URL 문자열만 저장.
`Post.imageUrls` = `TEXT` 컬럼에 쉼표 구분 URL 저장.
목록 API에서는 첫 번째 URL만 `thumbnailUrl`로 노출.
나중에 별도 `post_image` 테이블로 마이그레이션 예정.

---

### 4. 프론트엔드 개편

#### terrasage-web (port 3000)

**인증 흐름**
- `proxy.ts`: 모든 라우트 보호, 쿠키(`terrasage_token`) 없으면 `/login` 리다이렉트
- 로그인 성공 → ADMIN이면 `localhost:3001`로 이동, USER이면 홈(`/`)으로 이동
- JWT를 쿠키에 저장 (SameSite=Lax, max-age=3600)
- 서버 컴포넌트: `cookies()` from `next/headers`로 토큰 읽기
- 클라이언트 컴포넌트: `document.cookie` 파싱으로 토큰 읽기

> **Next.js 16 변경사항**: `middleware.ts` → `proxy.ts`, 함수명도 `middleware` → `proxy`

**페이지 구성**
```
/login          로그인 페이지 (공개)
/               홈 — 메뉴 카드 네비게이션
/species        백과사전 목록
/species/{id}   종 상세
/community      커뮤니티 목록 (게시판 탭 필터)
/community/{id} 게시글 상세 (이미지, 좋아요, 댓글)
/community/new  글쓰기
```

**컴포넌트 역할 분리**
| 컴포넌트 | 방식 | 이유 |
|---------|------|------|
| 목록/상세 페이지 | 서버 컴포넌트 | SEO, 초기 로딩 속도 |
| LikeButton | 클라이언트 | 상태 변경 (optimistic update) |
| CommentSection | 클라이언트 | 폼 상호작용, 실시간 추가 |
| HeaderUser | 클라이언트 | 쿠키에서 사용자 정보 파싱 |

#### terrasage-admin (port 3001)

- `/login` 페이지 추가
- 모든 admin API 요청에 `Authorization: Bearer {token}` 헤더 포함
- 서버 컴포넌트에서 401 받으면 `redirect("/login")`
- 클라이언트 컴포넌트에서 401 받으면 `window.location.href = "/login"`

---

## 설계 결정 기록

### CareGuide 분리 선택 이유
선택지: ① 단일 테이블 유지 → ② nullable 컬럼 추가 → ③ **엔티티 완전 분리**

"과하더라도 나중에 수정 생각하면 이게 맞는거같다" — 유저 의견.
식물과 동물의 사육 개념이 근본적으로 달라(사육장 vs 화분, 먹이 vs 비료) 분리가 맞다.

### imageUrls 저장 방식
선택지: ① `@ElementCollection` (별도 테이블) → ② JSONB → ③ **TEXT 쉼표 구분**

MVP 단계에서 추가 테이블이나 JSONB 쿼리 복잡성 없이 단순하게 처리.
이미지 개수가 많거나 정렬이 필요해지면 `post_image` 테이블로 마이그레이션.

### QueryDSL 대신 Specifications
Kotlin 2.x에서 KAPT 지원이 불안정해질 예정.
단순한 검색은 method naming → `@Query` → Specifications 순으로 복잡도 상승.

---

---

## 2026-06 세션 3 — 테스트 코드 작성 및 Spring Security 7.x 이슈 해결

### 1. 테스트 구성

#### 단위 테스트 (MockK)
| 파일 | 테스트 수 | 내용 |
|------|-----------|------|
| `AuthServiceTest` | 7 | signup/login 시나리오, 예외 케이스 |
| `PostServiceTest` | 10 | 게시글 CRUD, 권한 검사, 좋아요, 댓글 |

#### 통합 테스트 (Testcontainers + RestTestClient)
| 파일 | 테스트 수 | 내용 |
|------|-----------|------|
| `AuthApiTest` | 6 | 회원가입/로그인 HTTP API |
| `CommunityApiTest` | 11 | 게시글/댓글/좋아요 HTTP API |
| `SpeciesApiTest` | 6 | 종 조회, Admin API 인가 |

**총 40개 테스트 전체 통과**

---

### 2. Testcontainers 2.x 마이그레이션

Spring Boot 4.1.0이 Testcontainers 2.x를 사용. 1.x와 API 차이 있음.

| 항목 | 1.x | 2.x |
|------|-----|-----|
| PostgreSQL 모듈 | `org.testcontainers:postgresql` | 제거됨 |
| PostgreSQL 컨테이너 | `PostgreSQLContainer()` | `GenericContainer("postgres:16")` |
| JUnit 연동 | `@Testcontainers` + `@Container` | 제거됨 |
| 컨테이너 시작 | 자동 (JUnit extension) | `companion object { ... .start() }` |

```kotlin
// ✅ Testcontainers 2.x 패턴
companion object {
    private val postgres: GenericContainer<*> = GenericContainer("postgres:16").apply {
        withExposedPorts(5432)
        withEnv("POSTGRES_DB", "terrasage_test")
        withEnv("POSTGRES_USER", "test")
        withEnv("POSTGRES_PASSWORD", "test")
        waitingFor(Wait.forListeningPort())
        start()  // JVM shutdown hook으로 자동 종료
    }

    @DynamicPropertySource
    @JvmStatic
    fun configureDataSource(registry: DynamicPropertyRegistry) {
        registry.add("spring.datasource.url") {
            "jdbc:postgresql://localhost:${postgres.getMappedPort(5432)}/terrasage_test"
        }
    }
}
```

---

### 3. TestRestTemplate → RestTestClient

Spring Boot 4.x에서 `TestRestTemplate` 제거됨. 대체재: `RestTestClient` (Spring 7.x 신규).

```kotlin
// build.gradle.kts — 별도 의존성 불필요 (spring-boot-starter-test 내 포함)

// 테스트 클라이언트 초기화
@BeforeEach
fun initClient() {
    client = RestTestClient.bindToServer()
        .baseUrl("http://localhost:$port")
        .build()
}

// 요청/응답 패턴
import org.springframework.test.web.servlet.client.expectBody  // Kotlin reified extension

val data = client.post().uri("/api/v1/auth/login")
    .contentType(MediaType.APPLICATION_JSON)
    .body(mapOf("email" to email, "password" to password))  // bodyValue()가 아닌 body()
    .exchange()
    .expectStatus().isOk
    .expectBody<Map<String, Any>>()
    .returnResult().responseBody!!
```

---

### 4. Spring Security 7.x STATELESS 환경 이슈 (핵심)

자세한 내용: [troubleshooting.md](./troubleshooting.md)

#### 이슈 1: `getContext().authentication = auth` 동작 안 함

STATELESS 세션에서 `SecurityContextHolder.getContext()`는 호출마다 새 인스턴스를 반환.
→ `SecurityContextHolder.setContext(newContext)`로 교체해야 이후 필터에 전달됨.

#### 이슈 2: `response.sendError()` → `/error` 재진입 → 401

`accessDeniedHandler`에서 `sendError(403)`을 호출하면 `/error`로 포워드.
`/error`가 `permitAll()` 처리 안 되어 있으면 새 Security 체인 실행 → anonymous → 401.
→ 직접 JSON 응답 작성으로 해결.

---

---

## 암호화 키 관리 지침 (운영 전환 시 참고)

> 현재: 로컬 개발 단계 → application.yml 기본값 사용. 운영 전 반드시 전환.

### 관련 법령·기준

| 구분 | 기준 | 핵심 요구사항 |
|------|------|--------------|
| 국내 | 개인정보보호법 시행령 제30조 | 고유식별정보·비밀번호 암호화 의무 |
| 국내 | KISA 암호키 관리 안내서 | 키 분리 보관, 대칭키 유효기간 최대 2년 |
| 국제 | NIST SP 800-57 Part 1 Rev.5 | 키 전 주기(생성→폐기) 관리 |
| 국제 | PCI DSS v4.0 Req. 3.6~3.7 | 결제 기능 추가 시, 연 1회 이상 교체 |

### 운영 전환 체크리스트

- [ ] `application.yml` 기본값 제거 → `ENCRYPT_SECRET`, `ENCRYPT_SALT` 환경변수 필수화
- [ ] AWS KMS / HashiCorp Vault 등 전용 키 관리 서비스 연동 (`EncryptionConfig`만 교체)
- [ ] 키 버전 관리 도입 (`ENCRYPT_KEY_VERSION`) → 교체 시 구버전 키 병행 운용
- [ ] 키 접근 감사 로그 활성화
- [ ] 키 교체 주기 정책 수립 및 캘린더 등록

### 키 교체 시 주의사항

기존 암호화 데이터를 새 키로 **재암호화하거나**, 이전 키를 별도 보관하여 복호화 가능하게 유지해야 합니다. 키만 교체하면 기존 데이터를 읽을 수 없습니다.

---

## 다음 작업 목록

- [ ] 회원가입 페이지 (terrasage-web)
- [ ] 커뮤니티 페이지 — 수정/삭제 버튼 (작성자 본인만)
- [ ] 내 프로필 페이지
- [ ] 사육환경 관리 도메인
- [ ] 이미지 업로드 (S3 or 로컬)
- [ ] 검색 기능 강화 (Elasticsearch 2차 도입)
- [ ] 마켓플레이스 도메인
