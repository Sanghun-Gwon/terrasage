# ── Stage 1: Build ───────────────────────────────────────────────────────────
# 빌드 컨텍스트는 terrasage-web/ 폴더입니다.
FROM node:22-alpine AS build
WORKDIR /app

COPY package.json package-lock.json* ./
RUN npm ci

COPY . .

# NEXT_PUBLIC_* 값은 빌드 시점에 클라이언트 번들로 인라인되므로 build-arg로 주입
ARG NEXT_PUBLIC_API_URL=http://localhost:8080
ENV NEXT_PUBLIC_API_URL=$NEXT_PUBLIC_API_URL

RUN npm run build

# ── Stage 2: Runtime ─────────────────────────────────────────────────────────
# next.config.ts의 output: "standalone" 덕분에 node_modules 없이 실행 가능
FROM node:22-alpine AS runtime
WORKDIR /app

ENV NODE_ENV=production
ENV PORT=3000

RUN addgroup -S nextjs && adduser -S nextjs -G nextjs
USER nextjs

COPY --from=build --chown=nextjs:nextjs /app/public ./public
COPY --from=build --chown=nextjs:nextjs /app/.next/standalone ./
COPY --from=build --chown=nextjs:nextjs /app/.next/static ./.next/static

EXPOSE 3000
CMD ["node", "server.js"]
