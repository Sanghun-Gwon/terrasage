package com.terrasage.api.community.entity

import com.terrasage.api.auth.entity.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "post")
class Post(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    val author: User,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val boardType: BoardType,

    @Column(nullable = false)
    var title: String,

    @Column(columnDefinition = "TEXT", nullable = false)
    var content: String,

    // 개체 자랑(SHOWCASE) 등 이미지 첨부용 — 쉼표 구분 URL 목록
    @Column(columnDefinition = "TEXT")
    var imageUrls: String? = null,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)
