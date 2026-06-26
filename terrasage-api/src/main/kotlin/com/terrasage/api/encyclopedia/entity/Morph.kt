package com.terrasage.api.encyclopedia.entity

import jakarta.persistence.*

// 모프(색상/패턴 변이) — 같은 종 내에서 유전적으로 구분되는 외형 변이
@Entity
@Table(name = "morph")
class Morph(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "species_id", nullable = false)
    val species: Species,

    @Column(nullable = false)
    var name: String,

    // 유전 패턴에 따라 번식 시 발현 확률이 달라짐
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var geneticPattern: GeneticPattern,

    var description: String? = null,
    var imageUrl: String? = null,
)
