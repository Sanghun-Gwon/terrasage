package com.terrasage.api.encyclopedia.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "plant_care_guide")
class PlantCareGuide(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "species_id", nullable = false, unique = true)
    val species: Species,

    // 재배 환경
    var potType: String? = null,         // 화분/재배 용기 종류
    var growingMedium: String? = null,   // 배양토

    // 광량
    var lightRequirement: String? = null, // 광량 조건 (직사광선/반음지/실내 등)
    var lightHoursPerDay: Int? = null,   // 하루 일조 시간

    // 온도 (°C)
    var tempMin: Double? = null,         // 최저 생육 온도
    var tempMax: Double? = null,         // 최고 생육 온도 (적온)

    // 습도 (%)
    var humidityMin: Int? = null,
    var humidityMax: Int? = null,

    // 물주기
    var wateringFrequency: String? = null,
    var wateringMethod: String? = null,

    // 비료/영양
    var fertilizerType: String? = null,
    var fertilizerFrequency: String? = null,

    // 관리
    var repottingNote: String? = null,   // 분갈이 주기/방법
    var pruningNote: String? = null,     // 전정/관리 팁
    var overallNote: String? = null,     // 전반적 메모

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)
