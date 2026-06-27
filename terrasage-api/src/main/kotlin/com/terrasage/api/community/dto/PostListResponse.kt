package com.terrasage.api.community.dto

import com.terrasage.api.community.entity.BoardType
import com.terrasage.api.community.entity.Post
import java.time.LocalDateTime

data class PostListResponse(
    val id: Long,
    val boardType: BoardType,
    val title: String,
    val authorName: String,
    val thumbnailUrl: String?,  // imageUrls 첫번째 항목 (목록용)
    val likeCount: Long,
    val commentCount: Long,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(post: Post, likeCount: Long, commentCount: Long) = PostListResponse(
            id = post.id,
            boardType = post.boardType,
            title = post.title,
            authorName = post.author.name,
            thumbnailUrl = post.imageUrls?.split(",")?.firstOrNull { it.isNotBlank() },
            likeCount = likeCount,
            commentCount = commentCount,
            createdAt = post.createdAt,
        )
    }
}
