package com.terrasage.api.community.controller

import com.terrasage.api.common.response.ApiResponse
import com.terrasage.api.community.dto.*
import com.terrasage.api.community.entity.BoardType
import com.terrasage.api.community.service.PostService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/posts")
class PostController(private val postService: PostService) {

    @GetMapping
    fun getPosts(
        @RequestParam(required = false) boardType: BoardType?,
        pageable: Pageable,
    ): ApiResponse<Page<PostListResponse>> =
        ApiResponse.ok(postService.getPostList(boardType, pageable))

    @GetMapping("/{id}")
    fun getPost(@PathVariable id: Long): ApiResponse<PostDetailResponse> =
        ApiResponse.ok(postService.getPost(id))

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    fun createPost(
        @Valid @RequestBody request: PostCreateRequest,
        auth: Authentication,
    ): ApiResponse<PostDetailResponse> =
        ApiResponse.ok(postService.createPost(auth.name, request))

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun updatePost(
        @PathVariable id: Long,
        @Valid @RequestBody request: PostUpdateRequest,
        auth: Authentication,
    ): ApiResponse<PostDetailResponse> =
        ApiResponse.ok(postService.updatePost(id, auth.name, request))

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    fun deletePost(
        @PathVariable id: Long,
        auth: Authentication,
    ) {
        val isAdmin = auth.authorities.any { it.authority == "ROLE_ADMIN" }
        postService.deletePost(id, auth.name, isAdmin)
    }

    @GetMapping("/{postId}/comments")
    fun getComments(@PathVariable postId: Long): ApiResponse<List<CommentResponse>> =
        ApiResponse.ok(postService.getPost(postId).comments)

    @PostMapping("/{postId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    fun addComment(
        @PathVariable postId: Long,
        @Valid @RequestBody request: CommentCreateRequest,
        auth: Authentication,
    ): ApiResponse<CommentResponse> =
        ApiResponse.ok(postService.addComment(postId, auth.name, request))

    @DeleteMapping("/{postId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    fun deleteComment(
        @PathVariable postId: Long,
        @PathVariable commentId: Long,
        auth: Authentication,
    ) {
        val isAdmin = auth.authorities.any { it.authority == "ROLE_ADMIN" }
        postService.deleteComment(postId, commentId, auth.name, isAdmin)
    }

    @PostMapping("/{postId}/likes")
    @PreAuthorize("isAuthenticated()")
    fun toggleLike(
        @PathVariable postId: Long,
        auth: Authentication,
    ): ApiResponse<Map<String, Boolean>> =
        ApiResponse.ok(mapOf("liked" to postService.toggleLike(postId, auth.name)))
}
