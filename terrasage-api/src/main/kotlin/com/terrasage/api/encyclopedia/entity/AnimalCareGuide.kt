package com.terrasage.api.encyclopedia.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "animal_care_guide")
class AnimalCareGuide(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "species_id", nullable = false, unique = true)
    val species: Species,

    // 사육장 환경
    var enclosureType: String? = null,
    var enclosureSizeCm: String? = null,
    var substrate: String? = null,

    // 온도 구배 (°C)
    var tempHotZone: Double? = null,
    var tempCoolZone: Double? = null,
    var tempNight: Double? = null,

    // 습도 범위 (%)
    var humidityMin: Int? = null,
    var humidityMax: Int? = null,

    // 조명
    var uvbRequired: Boolean = false,
    var photoperiodHours: Int? = null,

    // 먹이/영양
    var feedType: String? = null,
    var feedFrequency: String? = null,
    var supplements: String? = null,

    // 핸들링 난이도
    @Enumerated(EnumType.STRING)
    var handlingLevel: HandlingLevel? = null,

    // 합사 주의사항
    var cohabitationNote: String? = null,

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)
