package com.terrasage.api.encyclopedia.entity

import jakarta.persistence.*
import java.time.LocalDateTime

// 종별 사육 가이드 (Species와 1:1 관계)
@Entity
@Table(name = "care_guide")
class CareGuide(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "species_id", nullable = false, unique = true)
    val species: Species,

    // 사육장 환경
    val enclosureType: String? = null,
    val enclosureSizeCm: String? = null,
    val substrate: String? = null,

    // 온도 구배 (°C) — 파충류는 핫존/쿨존 온도 차이가 핵심
    val tempHotZone: Double? = null,
    val tempCoolZone: Double? = null,
    val tempNight: Double? = null,

    // 습도 범위 (%)
    val humidityMin: Int? = null,
    val humidityMax: Int? = null,

    // 조명 설정
    val uvbRequired: Boolean = false,
    val photoperiodHours: Int? = null,

    // 먹이/영양
    val feedType: String? = null,
    val feedFrequency: String? = null,
    val supplements: String? = null,

    // 핸들링 난이도
    @Enumerated(EnumType.STRING)
    val handlingLevel: HandlingLevel? = null,

    // 합사 가능 여부 및 주의사항
    val cohabitationNote: String? = null,

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)
