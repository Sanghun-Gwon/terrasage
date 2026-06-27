package com.terrasage.api.community.repository

import com.terrasage.api.community.entity.BoardType
import com.terrasage.api.community.entity.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long> {
    fun findByBoardType(boardType: BoardType, pageable: Pageable): Page<Post>
}
