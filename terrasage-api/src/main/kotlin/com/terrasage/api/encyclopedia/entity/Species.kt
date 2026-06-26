package com.terrasage.api.encyclopedia.entity

import jakarta.persistence.*
import java.time.LocalDateTime

// 생물 종 정보 (린네 분류 체계 기반)
@Entity
@Table(name = "species")
class Species(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    // 학명 (국제 표준, 종 식별의 유일 키)
    @Column(nullable = false, unique = true)
    var scientificName: String,

    @Column(nullable = false)
    var commonNameKo: String,

    var commonNameEn: String? = null,

    // 린네 분류 계층: 계 > 문 > 강 > 목 > 과 > 속
    // 'class'/'order'는 Kotlin 예약어이므로 taxonomy 접두어 사용
    @Column(nullable = false)
    var kingdom: String,

    @Column(nullable = false)
    var phylum: String,

    @Column(name = "taxonomy_class", nullable = false)
    var taxonomyClass: String,

    @Column(name = "taxonomy_order", nullable = false)
    var taxonomyOrder: String,

    @Column(nullable = false)
    var family: String,

    @Column(nullable = false)
    var genus: String,

    // 서식 정보
    var origin: String? = null,
    var habitat: String? = null,

    // 수명 (년 단위)
    var lifespanCaptive: Int? = null,
    var lifespanWild: Int? = null,

    // 평균 크기/무게
    var avgSizeCm: Double? = null,
    var avgWeightG: Double? = null,

    // 사육 난이도
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var difficultyLevel: DifficultyLevel,

    // CITES (멸종위기종 국제거래 협약) 등급
    @Enumerated(EnumType.STRING)
    var citesLevel: CitesLevel? = null,

    var legalStatusNote: String? = null,
    var thumbnailUrl: String? = null,

    // 취미 원예/사육 분류 카테고리 (taxonomy와 별개 — 검색/필터 UI용)
    @Enumerated(EnumType.STRING)
    var category: SpeciesCategory? = null,

    // 등록 → DRAFT, 관리자 승인 → PUBLISHED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: SpeciesStatus = SpeciesStatus.DRAFT,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)
