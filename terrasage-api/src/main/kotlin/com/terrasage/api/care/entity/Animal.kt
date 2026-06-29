package com.terrasage.api.care.entity

import com.terrasage.api.auth.entity.User
import com.terrasage.api.encyclopedia.entity.Species
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "animals")
class Animal(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    val owner: User,

    // 백과사전 연동 (선택)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "species_id")
    var species: Species? = null,

    // 백과사전 미연동 시 직접 입력
    @Column
    var speciesName: String? = null,

    @Column(nullable = false)
    var name: String,

    @Column
    var nickname: String? = null,

    @Column
    var birthDate: LocalDate? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var gender: Gender = Gender.UNKNOWN,

    @Column(columnDefinition = "TEXT")
    var notes: String? = null,

    @Column(nullable = false)
    var isPublic: Boolean = false,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),
) {
    fun displaySpeciesName(): String =
        species?.commonNameKo ?: speciesName ?: "미분류"
}
