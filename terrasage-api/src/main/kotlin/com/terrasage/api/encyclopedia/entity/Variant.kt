package com.terrasage.api.encyclopedia.entity

import jakarta.persistence.*

@Entity
@Table(name = "variant")
class Variant(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "species_id", nullable = false)
    val species: Species,

    @Column(nullable = false)
    var name: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var geneticPattern: GeneticPattern,

    var description: String? = null,
    var imageUrl: String? = null,
)
