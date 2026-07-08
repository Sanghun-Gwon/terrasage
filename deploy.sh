#!/usr/bin/env bash
# TerraSage — Cloud Run 배포 스크립트 (Cloud Shell에서 실행 권장)
# 사전 준비: gcloud auth login, gcloud config set project terrasage-prod-2026
#
# 사용법:
#   ./deploy.sh api     # API 서버만 배포
#   ./deploy.sh web     # Web 서버만 배포 (API가 먼저 배포되어 있어야 함)
#   ./deploy.sh cors    # CORS 시크릿을 web URL로 갱신 + API 새 리비전
#   ./deploy.sh all     # api → web → cors 순서로 전체 배포

set -e

# ── 설정값 ────────────────────────────────────────────────────────────────────
GCP_PROJECT="terrasage-prod-2026"
GCP_REGION="asia-northeast3"           # 서울 리전
API_SERVICE="terrasage-api"
WEB_SERVICE="terrasage-web"
AR_REPO="cloud-run-source-deploy"      # --source 배포가 자동 생성하는 저장소를 공용 사용

api_url() {
  gcloud run services describe "$API_SERVICE" \
    --region "$GCP_REGION" --project "$GCP_PROJECT" --format "value(status.url)"
}

web_url() {
  gcloud run services describe "$WEB_SERVICE" \
    --region "$GCP_REGION" --project "$GCP_PROJECT" --format "value(status.url)"
}

# ── API 배포 ──────────────────────────────────────────────────────────────────
deploy_api() {
  echo "▶ API 배포 중..."
  # gcloud run deploy --source는 소스 루트의 Dockerfile만 인식하므로 임시 복사
  cp terrasage-api/Dockerfile ./Dockerfile
  trap 'rm -f ./Dockerfile' EXIT

  gcloud run deploy "$API_SERVICE" \
    --source . \
    --region "$GCP_REGION" \
    --project "$GCP_PROJECT" \
    --platform managed \
    --allow-unauthenticated \
    --max-instances 1 \
    --memory 1Gi \
    --set-env-vars "SPRING_PROFILES_ACTIVE=prod" \
    --set-secrets \
      "DATABASE_URL=TERRASAGE_DATABASE_URL:latest,\
JWT_SECRET=TERRASAGE_JWT_SECRET:latest,\
ENCRYPT_SECRET=TERRASAGE_ENCRYPT_SECRET:latest,\
ENCRYPT_SALT=TERRASAGE_ENCRYPT_SALT:latest,\
CORS_ALLOWED_ORIGINS=TERRASAGE_CORS_ALLOWED_ORIGINS:latest"

  rm -f ./Dockerfile
  trap - EXIT
  echo "✓ API 배포 완료: $(api_url)"
}

# ── Web 배포 ─────────────────────────────────────────────────────────────────
deploy_web() {
  local api_url
  api_url=$(api_url)
  if [ -z "$api_url" ]; then
    echo "✗ API 서비스를 찾을 수 없습니다. ./deploy.sh api 를 먼저 실행하세요."
    exit 1
  fi
  echo "▶ Web 배포 중... (API_URL=$api_url)"

  # Artifact Registry 저장소가 없으면 생성 (api --source 배포 시 자동 생성되지만 방어)
  gcloud artifacts repositories describe "$AR_REPO" \
    --location "$GCP_REGION" --project "$GCP_PROJECT" >/dev/null 2>&1 ||
    gcloud artifacts repositories create "$AR_REPO" \
      --repository-format=docker --location "$GCP_REGION" --project "$GCP_PROJECT"

  # NEXT_PUBLIC_API_URL은 빌드 시점 인라인이라 build-arg가 필요 → 별도 Cloud Build
  local image="${GCP_REGION}-docker.pkg.dev/${GCP_PROJECT}/${AR_REPO}/${WEB_SERVICE}"
  gcloud builds submit terrasage-web \
    --project "$GCP_PROJECT" \
    --region "$GCP_REGION" \
    --config terrasage-web/cloudbuild.yaml \
    --substitutions "_API_URL=${api_url},_IMAGE=${image}"

  gcloud run deploy "$WEB_SERVICE" \
    --image "$image" \
    --region "$GCP_REGION" \
    --project "$GCP_PROJECT" \
    --platform managed \
    --allow-unauthenticated \
    --max-instances 1 \
    --set-env-vars "NEXT_PUBLIC_API_URL=${api_url}"

  echo "✓ Web 배포 완료: $(web_url)"
}

# ── CORS 갱신 ─────────────────────────────────────────────────────────────────
# web URL이 배포 후에야 확정되므로, 시크릿 갱신 → API 새 리비전으로 반영
update_cors() {
  local web project_number web_deterministic origins
  web=$(web_url)
  # Cloud Run은 레거시(랜덤 해시)와 결정적(프로젝트 번호) URL 두 개를 가짐.
  # 브라우저 Origin은 접속한 URL 그대로이므로 둘 다 허용해야 함.
  project_number=$(gcloud projects describe "$GCP_PROJECT" --format 'value(projectNumber)')
  web_deterministic="https://${WEB_SERVICE}-${project_number}.${GCP_REGION}.run.app"
  origins="$web"
  [ "$web" != "$web_deterministic" ] && origins="$web,$web_deterministic"
  echo "▶ CORS 허용 출처 갱신: $origins"
  echo -n "$origins" | gcloud secrets versions add TERRASAGE_CORS_ALLOWED_ORIGINS \
    --data-file=- --project "$GCP_PROJECT"
  # :latest 시크릿은 인스턴스 기동 시점에 읽히므로 새 리비전 생성이 필요
  gcloud run services update "$API_SERVICE" \
    --region "$GCP_REGION" --project "$GCP_PROJECT" \
    --update-env-vars "CORS_SYNCED_AT=$(date +%s)"
  echo "✓ CORS 갱신 완료"
}

# ── 실행 ──────────────────────────────────────────────────────────────────────
case "${1:-all}" in
  api)  deploy_api ;;
  web)  deploy_web ;;
  cors) update_cors ;;
  all)  deploy_api && deploy_web && update_cors ;;
  *) echo "사용법: ./deploy.sh [api|web|cors|all]"; exit 1 ;;
esac
