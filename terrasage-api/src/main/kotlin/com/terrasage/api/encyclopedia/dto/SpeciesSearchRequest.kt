package com.terrasage.api.encyclopedia.dto

import com.terrasage.api.encyclopedia.entity.DifficultyLevel
import com.terrasage.api.encyclopedia.entity.SpeciesCategory
import com.terrasage.api.encyclopedia.entity.SpeciesStatus

// 종 검색/필터 조건 — GET /api/v1/species 쿼리 파라미터로 바인딩
// 모든 필드가 nullable이므로 조건이 없으면 전체 조회
data class SpeciesSearchRequest(
    val keyword: String? = null,           // 학명, 한글명, 영문명 통합 검색
    val category: SpeciesCategory? = null, // 취미 카테고리 필터 (REPTILE, SUCCULENT 등)
    val taxonomyClass: String? = null,     // 강(綱) 필터 (예: Reptilia, Amphibia)
    val taxonomyOrder: String? = null,     // 목(目) 필터 (예: Squamata, Testudines)
    val family: String? = null,            // 과(科) 필터 (예: Pythonidae)
    val genus: String? = null,             // 속(屬) 필터
    val difficultyLevel: DifficultyLevel? = null,
    val status: SpeciesStatus? = SpeciesStatus.PUBLISHED, // 기본값: 공개된 종만 조회
)
