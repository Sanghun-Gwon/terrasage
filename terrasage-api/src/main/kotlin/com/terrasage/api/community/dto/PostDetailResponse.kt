package com.terrasage.api.community.dto

import com.terrasage.api.community.entity.BoardType
import com.terrasage.api.community.entity.Post
import java.time.LocalDateTime

data class PostDetailResponse(
    val id: Long,
    val boardType: BoardType,
    val title: String,
    val content: String,
    val imageUrls: List<String>,
    val authorName: String,
    val authorEmail: String,
    val likeCount: Long,
    val comments: List<CommentResponse>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun from(post: Post, likeCount: Long, comments: List<CommentResponse>) = PostDetailResponse(
            id = post.id,
            boardType = post.boardType,
            title = post.title,
            content = post.content,
            imageUrls = post.imageUrls?.split(",")?.filter { it.isNotBlank() } ?: emptyList(),
            authorName = post.author.name,
            authorEmail = post.author.email,
            likeCount = likeCount,
            comments = comments,
            createdAt = post.createdAt,
            updatedAt = post.updatedAt,
        )
    }
}
