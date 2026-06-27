# TerraSage 개발 이슈 기록

개발 중 발생한 문제와 해결 방법을 기록합니다.

---

## 빠른 해결 명령어

| 상황 | 명령어 |
|------|--------|
| DB 스키마 변경 / data.sql 수정 반영 | `docker compose down -v && docker compose up -d` |
| API 서버 재시작 | `lsof -ti:8080 \| xargs kill -9 ; ./gradlew :terrasage-api:bootRun` |
| Web 서버 재시작 | `lsof -ti:3000 \| xargs kill -9 ; cd terrasage-web && npm run dev` |
| Admin 서버 재시작 | `lsof -ti:3001 \| xargs kill -9 ; cd terrasage-admin && npm run dev` |
| Kotlin 컴파일 확인 | `./gradlew :terrasage-api:compileKotlin` |
| npm PATH 없을 때 (nvm 환경) | `export PATH="$PATH:$HOME/.nvm/versions/node/v22.23.1/bin"` |
| 전체 서버 상태 확인 | `lsof -i:8080,3000,3001 -sTCP:LISTEN` |
| API 종 목록 확인 | `curl -s http://localhost:8080/api/v1/species?size=5 \| python3 -m json.tool` |

---

## 목차

1. [PostgreSQL VALUES 절 타입 추론 오류](#1-postgresql-values-절-타입-추론-오류)
2. [HandlingLevel enum 불일치 (체크 제약 위반)](#2-handlinglevel-enum-불일치-체크-제약-위반)
3. [Spring - Enum 파라미터 다중값 파싱 실패](#3-spring---enum-파라미터-다중값-파싱-실패)
4. [Next.js 16 - params/searchParams Promise 타입 변경](#4-nextjs-16---paramssearchparams-promise-타입-변경)
5. [Next.js - 빌드 타임 API 프리렌더 실패](#5-nextjs---빌드-타임-api-프리렌더-실패)
6. [shadcn Select - onValueChange null 전달 타입 에러](#6-shadcn-select---onvaluechange-null-전달-타입-에러)
7. [Hibernate 컬럼 명명 규칙 - 단일 대문자 접미사 미분리](#7-hibernate-컬럼-명명-규칙---단일-대문자-접미사-미분리)
8. [DB 스키마 변경 시 볼륨 리셋 필요](#8-db-스키마-변경-시-볼륨-리셋-필요)
9. [리팩토링 후 구 변수명 참조로 서버 500 에러](#9-리팩토링-후-구-변수명-참조로-서버-500-에러)
10. [백그라운드 셸 환경에서 npm PATH 누락](#10-백그라운드-셸-환경에서-npm-path-누락)

---

## 1. PostgreSQL VALUES 절 타입 추론 오류

**증상**
```
ERROR: column "temp_night" is of type double precision but expression is of type text
```

**원인**  
`INSERT INTO ... SELECT FROM (VALUES ...)` 구문에서 PostgreSQL은 첫 번째 행의 값으로 컬럼 타입을 추론한다. 식물 재배 가이드 데이터의 첫 행에서 `NULL`을 그대로 쓰면 `text`로 추론되어, 실제 컬럼 타입(`double precision`)과 충돌.

**해결**  
첫 번째 행의 NULL에 명시적 타입 캐스트 추가:
```sql
-- ❌ 문제
('Echeveria laui', ..., NULL, ...)
-- ✅ 해결
('Echeveria laui', ..., NULL::double precision, ...)
```

**교훈**  
VALUES 절에서 NULL을 사용하는 컬럼은 반드시 첫 번째 행에 타입 캐스트를 명시한다.

---

## 2. HandlingLevel enum 불일치 (체크 제약 위반)

**증상**
```
ERROR: insert or update on table "animal_care_guide" violates check constraint
```

**원인**  
data.sql에서 `'ADVANCED'`를 사용했지만, 실제 `HandlingLevel` enum 값은 `EASY / MODERATE / DIFFICULT / EXPERT_ONLY`.

**해결**  
`'ADVANCED'` → `'DIFFICULT'`로 수정.

**교훈**  
Kotlin enum 변경 시 data.sql의 리터럴 값도 함께 확인한다.

---

## 3. Spring - Enum 파라미터 다중값 파싱 실패

**증상**  
Admin 종 목록 조회 시 `400 Bad Request` 또는 enum 변환 실패.

**원인**  
프론트엔드에서 `status=DRAFT,PUBLISHED,ARCHIVED`를 단일 문자열로 전송했고, Spring이 이를 하나의 enum 값으로 파싱하려다 실패.

**해결**  
Admin 전용 `GET /api/v1/admin/species` 엔드포인트를 별도로 추가하여 `status = null` (전체 조회)로 고정:
```kotlin
@GetMapping
fun getSpeciesList(pageable: Pageable) =
    ApiResponse.ok(speciesService.getSpeciesList(SpeciesSearchRequest(status = null), pageable))
```

프론트엔드도 admin 전용 엔드포인트만 호출하도록 변경.

---

## 4. Next.js 16 - params/searchParams Promise 타입 변경

**증상**
```
Type error: Property 'id' does not exist on type 'Promise<{ id: string }>'
```

**원인**  
Next.js 16에서 App Router의 `params`와 `searchParams`가 `Promise<T>` 타입으로 변경됨. 이전 버전처럼 직접 구조분해하면 타입 에러.

**해결**  
```typescript
// ❌ 이전 방식
export default function Page({ params }: { params: { id: string } }) {
  const { id } = params;
}

// ✅ Next.js 16 방식
export default async function Page({ params }: { params: Promise<{ id: string }> }) {
  const { id } = await params;
}
```

**교훈**  
`terrasage-web/AGENTS.md` 참고 — "This is NOT the Next.js you know". 버전별 Breaking Change를 반드시 확인.

---

## 5. Next.js - 빌드 타임 API 프리렌더 실패

**증상**
```
Error: fetch failed — ECONNREFUSED 127.0.0.1:8080
```

빌드 시 API를 호출하는 페이지가 실패.

**원인**  
Next.js 빌드 타임에 API 서버가 없는 상태에서 서버 컴포넌트가 데이터를 프리렌더하려고 시도함.

**해결**  
API 호출이 있는 모든 서버 컴포넌트에 추가:
```typescript
export const dynamic = "force-dynamic";
```

적용 대상: `app/species/page.tsx`, `app/species/[id]/page.tsx`, `app/species/[id]/edit/page.tsx`

---

## 6. shadcn Select - onValueChange null 전달 타입 에러

**증상**
```
Type 'null' is not assignable to type 'string'
```

**원인**  
shadcn `Select`의 `onValueChange`가 선택 해제 시 `null`을 전달하는데, 폼 상태 setter 타입이 `string`만 허용.

**해결**  
setter 타입을 `string | number | null | undefined`로 확장하고, `null`은 `undefined`로 변환:
```typescript
const set = (key: keyof SpeciesFormData, value: string | number | null | undefined) =>
  setForm((prev) => ({ ...prev, [key]: value ?? undefined }));
```

---

## 7. Hibernate 컬럼 명명 규칙 - 단일 대문자 접미사 미분리

**증상**  
`avgWeightG` 필드가 DB에 `avg_weightg`로 생성됨. 예상한 `avg_weight_g`와 다름.

**원인**  
Hibernate의 `SpringPhysicalNamingStrategy`는 연속된 대문자를 구분하지 않고, 단독 대문자 접미사(`G`)도 분리하지 않음. 반면 `photoperiodHours` → `photoperiod_hours`는 정상 변환됨.

**해결**  
컬럼명을 명시적으로 지정:
```kotlin
@Column(name = "avg_weight_g")
var avgWeightG: Double? = null
```

또는 필드명을 `avgWeightGrams`처럼 자연스럽게 변경.

**교훈**  
단일 대문자로 끝나는 약어(`G`, `W`, `ID` 등) 필드는 `@Column(name = ...)` 명시 또는 필드명 풀네임 사용 권장.

---

## 8. DB 스키마 변경 시 볼륨 리셋 필요

**발생 시점**  
- `SpeciesCategory` 컬럼 추가
- `care_guide` → `animal_care_guide` / `plant_care_guide` 테이블 분리
- data.sql SQL 오류 수정 반영

**원인**  
`ddl-auto: update`는 새 컬럼/테이블을 추가할 수 있지만, 기존 데이터의 충돌(타입 불일치, 제약 조건 위반)이나 삭제된 테이블은 자동으로 처리하지 않음.

**해결**  
```bash
docker compose down -v    # 볼륨 포함 완전 삭제
docker compose up -d      # DB 재생성
```

**교훈**  
- 개발 중 스키마 변경이 잦을 때는 볼륨 리셋을 주저하지 않는다
- data.sql 수정 후에도 항상 볼륨 리셋으로 검증

---

## 9. 리팩토링 후 구 변수명 참조로 서버 500 에러

**증상**
```
ReferenceError: cg is not defined
  at SpeciesDetailPage (src/app/species/[id]/page.tsx:117:8)
```

**원인**  
`CareGuide` → `AnimalCareGuide` / `PlantCareGuide` 분리 리팩토링 후, 이전에 실행 중이던 Next.js 서버가 소스 변경을 일부만 반영한 상태에서 오류 발생. 서버가 크래시하여 종료됨.

**해결**  
서버 재시작. 파일 자체에는 문제 없었음 (편집은 정상 완료된 상태).

**교훈**  
큰 리팩토링 후에는 dev 서버를 수동으로 재시작하여 확인.

---

## 10. 백그라운드 셸 환경에서 npm PATH 누락

**증상**
```
(eval):2: command not found: npm
```

**원인**  
백그라운드 셸(`run_in_background`)은 사용자 셸 프로필(`.zshrc`, `.nvm/nvm.sh` 등)을 로드하지 않아 `nvm`으로 설치된 Node.js가 PATH에 없음.

**해결**  
실행 전 PATH를 명시적으로 설정:
```bash
export PATH="$PATH:$HOME/.nvm/versions/node/v22.23.1/bin"
cd terrasage-web && npm run dev > /tmp/web.log 2>&1 &
```

**교훈**  
백그라운드 프로세스 또는 비대화형 셸에서 nvm/pyenv 등 버전 매니저 기반 툴 실행 시 PATH를 명시한다.
