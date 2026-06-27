package com.terrasage.api.community.repository

import com.terrasage.api.community.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findByPostIdOrderByCreatedAtAsc(postId: Long): List<Comment>
    fun countByPostId(postId: Long): Long
    fun deleteAllByPostId(postId: Long)
}
