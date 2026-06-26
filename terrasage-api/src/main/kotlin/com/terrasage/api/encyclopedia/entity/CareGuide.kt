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
    var enclosureType: String? = null,
    var enclosureSizeCm: String? = null,
    var substrate: String? = null,

    // 온도 구배 (°C) — 파충류는 핫존/쿨존 온도 차이가 핵심
    var tempHotZone: Double? = null,
    var tempCoolZone: Double? = null,
    var tempNight: Double? = null,

    // 습도 범위 (%)
    var humidityMin: Int? = null,
    var humidityMax: Int? = null,

    // 조명 설정
    var uvbRequired: Boolean = false,
    var photoperiodHours: Int? = null,

    // 먹이/영양
    var feedType: String? = null,
    var feedFrequency: String? = null,
    var supplements: String? = null,

    // 핸들링 난이도
    @Enumerated(EnumType.STRING)
    var handlingLevel: HandlingLevel? = null,

    // 합사 가능 여부 및 주의사항
    var cohabitationNote: String? = null,

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)
