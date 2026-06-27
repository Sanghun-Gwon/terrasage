package com.terrasage.api.community.service

import com.terrasage.api.auth.repository.UserRepository
import com.terrasage.api.community.dto.*
import com.terrasage.api.community.entity.BoardType
import com.terrasage.api.community.entity.Comment
import com.terrasage.api.community.entity.Post
import com.terrasage.api.community.entity.PostLike
import com.terrasage.api.community.repository.CommentRepository
import com.terrasage.api.community.repository.PostLikeRepository
import com.terrasage.api.community.repository.PostRepository
import com.terrasage.api.common.exception.NotFoundException
import com.terrasage.api.common.exception.TerrasageException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostService(
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val postLikeRepository: PostLikeRepository,
    private val userRepository: UserRepository,
) {
    fun getPostList(boardType: BoardType?, pageable: Pageable): Page<PostListResponse> {
        val posts = if (boardType != null) {
            postRepository.findByBoardType(boardType, pageable)
        } else {
            postRepository.findAll(pageable)
        }
        return posts.map { post ->
            PostListResponse.from(
                post,
                likeCount = postLikeRepository.countByPostId(post.id),
                commentCount = commentRepository.countByPostId(post.id),
            )
        }
    }

    fun getPost(id: Long): PostDetailResponse {
        val post = postRepository.findById(id).orElseThrow { NotFoundException("Post", id) }
        val comments = commentRepository.findByPostIdOrderByCreatedAtAsc(id).map(CommentResponse::from)
        return PostDetailResponse.from(post, postLikeRepository.countByPostId(id), comments)
    }

    @Transactional
    fun createPost(email: String, request: PostCreateRequest): PostDetailResponse {
        val author = userRepository.findByEmail(email) ?: throw NotFoundException("User", email)
        val post = postRepository.save(
            Post(
                author = author,
                boardType = request.boardType,
                title = request.title,
                content = request.content,
                imageUrls = request.imageUrls?.joinToString(","),
            )
        )
        return PostDetailResponse.from(post, 0, emptyList())
    }

    @Transactional
    fun updatePost(id: Long, email: String, request: PostUpdateRequest): PostDetailResponse {
        val post = postRepository.findById(id).orElseThrow { NotFoundException("Post", id) }
        if (post.author.email != email) throw TerrasageException("FORBIDDEN", "수정 권한이 없습니다")

        post.title = request.title
        post.content = request.content
        post.imageUrls = request.imageUrls?.joinToString(",")
        post.updatedAt = java.time.LocalDateTime.now()

        val comments = commentRepository.findByPostIdOrderByCreatedAtAsc(id).map(CommentResponse::from)
        return PostDetailResponse.from(post, postLikeRepository.countByPostId(id), comments)
    }

    @Transactional
    fun deletePost(id: Long, email: String, isAdmin: Boolean) {
        val post = postRepository.findById(id).orElseThrow { NotFoundException("Post", id) }
        if (!isAdmin && post.author.email != email) throw TerrasageException("FORBIDDEN", "삭제 권한이 없습니다")

        postLikeRepository.deleteAllByPostId(id)
        commentRepository.deleteAllByPostId(id)
        postRepository.delete(post)
    }

    @Transactional
    fun addComment(postId: Long, email: String, request: CommentCreateRequest): CommentResponse {
        val post = postRepository.findById(postId).orElseThrow { NotFoundException("Post", postId) }
        val author = userRepository.findByEmail(email) ?: throw NotFoundException("User", email)
        val parent = request.parentId?.let {
            commentRepository.findById(it).orElseThrow { NotFoundException("Comment", it) }
        }
        val comment = commentRepository.save(Comment(post = post, author = author, parent = parent, content = request.content))
        return CommentResponse.from(comment)
    }

    @Transactional
    fun deleteComment(postId: Long, commentId: Long, email: String, isAdmin: Boolean) {
        val comment = commentRepository.findById(commentId).orElseThrow { NotFoundException("Comment", commentId) }
        if (!isAdmin && comment.author.email != email) throw TerrasageException("FORBIDDEN", "삭제 권한이 없습니다")
        commentRepository.delete(comment)
    }

    @Transactional
    fun toggleLike(postId: Long, email: String): Boolean {
        val post = postRepository.findById(postId).orElseThrow { NotFoundException("Post", postId) }
        val user = userRepository.findByEmail(email) ?: throw NotFoundException("User", email)
        val existing = postLikeRepository.findByPostIdAndUserId(postId, user.id)
        return if (existing != null) {
            postLikeRepository.delete(existing)
            false
        } else {
            postLikeRepository.save(PostLike(post = post, user = user))
            true
        }
    }
}
