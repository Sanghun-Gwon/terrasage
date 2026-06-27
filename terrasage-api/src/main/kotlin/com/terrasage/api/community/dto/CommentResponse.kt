package com.terrasage.api.community.dto

import com.terrasage.api.community.entity.Comment
import java.time.LocalDateTime

data class CommentResponse(
    val id: Long,
    val content: String,
    val authorName: String,
    val parentId: Long?,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(comment: Comment) = CommentResponse(
            id = comment.id,
            content = comment.content,
            authorName = comment.author.name,
            parentId = comment.parent?.id,
            createdAt = comment.createdAt,
        )
    }
}
