#!/usr/bin/env bash
# TerraSage — Cloud Run 배포 스크립트
# 사전 준비: gcloud auth login, gcloud config set project <PROJECT_ID>
#
# 사용법:
#   ./deploy.sh api     # API 서버만 배포
#   ./deploy.sh web     # Web 서버만 배포
#   ./deploy.sh all     # 둘 다 배포

set -e

# ── 설정값 (배포 전 실제 값으로 수정) ────────────────────────────────────────
GCP_PROJECT="your-gcp-project-id"
GCP_REGION="asia-northeast3"           # 서울 리전
API_SERVICE="terrasage-api"
WEB_SERVICE="terrasage-web"

# ── API 배포 ──────────────────────────────────────────────────────────────────
deploy_api() {
  echo "▶ API 배포 중..."
  gcloud run deploy "$API_SERVICE" \
    --source . \
    --dockerfile terrasage-api/Dockerfile \
    --region "$GCP_REGION" \
    --project "$GCP_PROJECT" \
    --platform managed \
    --allow-unauthenticated \
    --max-instances 1 \
    --set-env-vars "SPRING_PROFILES_ACTIVE=prod" \
    --set-secrets \
      "DATABASE_URL=TERRASAGE_DATABASE_URL:latest,\
JWT_SECRET=TERRASAGE_JWT_SECRET:latest,\
ENCRYPT_SECRET=TERRASAGE_ENCRYPT_SECRET:latest,\
ENCRYPT_SALT=TERRASAGE_ENCRYPT_SALT:latest,\
CORS_ALLOWED_ORIGINS=TERRASAGE_CORS_ALLOWED_ORIGINS:latest"
  echo "✓ API 배포 완료"
}

# ── Web 배포 ─────────────────────────────────────────────────────────────────
deploy_web() {
  echo "▶ Web 배포 중..."
  gcloud run deploy "$WEB_SERVICE" \
    --source terrasage-web \
    --region "$GCP_REGION" \
    --project "$GCP_PROJECT" \
    --platform managed \
    --allow-unauthenticated \
    --max-instances 1
  echo "✓ Web 배포 완료"
}

# ── 실행 ──────────────────────────────────────────────────────────────────────
case "${1:-all}" in
  api) deploy_api ;;
  web) deploy_web ;;
  all) deploy_api && deploy_web ;;
  *) echo "사용법: ./deploy.sh [api|web|all]"; exit 1 ;;
esac
