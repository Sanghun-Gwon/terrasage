package com.terrasage.api.care.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "care_records")
class CareRecord(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    val animal: Animal,

    // 수동 입력 시 과거 날짜도 허용 (IoT 연동 시 센서 타임스탬프 사용)
    @Column(nullable = false)
    val recordedAt: LocalDateTime = LocalDateTime.now(),

    @Column val temperature: Double? = null,   // °C
    @Column val humidity: Double? = null,       // %
    @Column val lightHours: Double? = null,     // 시간
    @Column val weight: Double? = null,         // g

    @Column val feedType: String? = null,       // 먹이 종류 (예: 귀뚜라미, 핀키마우스)
    @Column val feedAmount: String? = null,     // 양 (예: 3마리, 5g)

    @Column(columnDefinition = "TEXT")
    val notes: String? = null,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
