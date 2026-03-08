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

## Tech Stack

> ⚠️ 기획 완료 전까지 스펙 변경 가능. 개발 시작 후 고정.

| 항목 | 스펙 |
|------|------|
| Language | Java |
| Framework | Spring Boot |
| Build Tool | Gradle |
| Database | TBD (기획 중 결정) |

## Module Structure

| 모듈 | 설명 |
|------|------|
| terrasage-api | REST API 서버 |
| terrasage-admin | 관리자 페이지 |
| terrasage-web | 사용자 웹 페이지 |

## Documentation

- [프로젝트 개요](./docs/overview.md)
- [백과사전 기능](./docs/features/encyclopedia.md)
- [사육환경 관리](./docs/features/care-management.md)
- [커뮤니티](./docs/features/community.md)
- [건강 진단](./docs/features/health-diagnosis.md)
- [마켓플레이스](./docs/features/marketplace.md)
