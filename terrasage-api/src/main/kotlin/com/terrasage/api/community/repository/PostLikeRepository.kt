package com.terrasage.api.community.repository

import com.terrasage.api.community.entity.PostLike
import org.springframework.data.jpa.repository.JpaRepository

interface PostLikeRepository : JpaRepository<PostLike, Long> {
    fun findByPostIdAndUserId(postId: Long, userId: Long): PostLike?
    fun countByPostId(postId: Long): Long
    fun deleteAllByPostId(postId: Long)
}
