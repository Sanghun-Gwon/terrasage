# ── Stage 1: Build ───────────────────────────────────────────────────────────
# 빌드 컨텍스트는 프로젝트 루트(terrasage/)여야 합니다.
# Cloud Run 배포 시: gcloud run deploy --source . (루트에서 실행)
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /workspace

# 의존성 레이어 캐시: 소스 변경 시 재다운로드 방지
COPY gradlew .
COPY gradle/ gradle/
COPY settings.gradle.kts .
COPY build.gradle.kts .
COPY terrasage-api/build.gradle.kts terrasage-api/

RUN chmod +x gradlew && ./gradlew :terrasage-api:dependencies --no-daemon -q

# 소스 복사 후 빌드
COPY terrasage-api/src/ terrasage-api/src/

RUN ./gradlew :terrasage-api:bootJar --no-daemon -x test

# ── Stage 2: Runtime ─────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 비root 사용자로 실행 (보안)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

COPY --from=build /workspace/terrasage-api/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
