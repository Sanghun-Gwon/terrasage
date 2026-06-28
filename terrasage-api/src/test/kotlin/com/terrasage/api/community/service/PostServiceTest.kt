package com.terrasage.api.community.service

import com.terrasage.api.auth.entity.User
import com.terrasage.api.auth.repository.UserRepository
import com.terrasage.api.common.exception.ForbiddenException
import com.terrasage.api.common.exception.NotFoundException
import com.terrasage.api.community.dto.CommentCreateRequest
import com.terrasage.api.community.dto.PostCreateRequest
import com.terrasage.api.community.entity.BoardType
import com.terrasage.api.community.entity.Comment
import com.terrasage.api.community.entity.Post
import com.terrasage.api.community.entity.PostLike
import com.terrasage.api.community.repository.CommentRepository
import com.terrasage.api.community.repository.PostLikeRepository
import com.terrasage.api.community.repository.PostRepository
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.Optional

class PostServiceTest {

    private val postRepository: PostRepository = mockk()
    private val commentRepository: CommentRepository = mockk()
    private val postLikeRepository: PostLikeRepository = mockk()
    private val userRepository: UserRepository = mockk()

    private lateinit var postService: PostService

    private val author = User(id = 1L, email = "author@test.com", password = "pw", name = "작성자")
    private val other  = User(id = 2L, email = "other@test.com",  password = "pw", name = "타인")

    @BeforeEach
    fun setUp() {
        postService = PostService(postRepository, commentRepository, postLikeRepository, userRepository)
    }

    // ── createPost ───────────────────────────────────────────────────────────

    @Test
    fun `createPost - 게시글이 저장되고 DTO로 반환`() {
        val request = PostCreateRequest("제목", "내용", BoardType.FREE)
        val saved = Post(id = 1L, author = author, boardType = BoardType.FREE, title = "제목", content = "내용")

        every { userRepository.findByEmail("author@test.com") } returns author
        every { postRepository.save(any()) } returns saved
        every { postLikeRepository.countByPostId(1L) } returns 0L
        every { commentRepository.findByPostIdOrderByCreatedAtAsc(1L) } returns emptyList()

        val result = postService.createPost("author@test.com", request)

        assertThat(result.title).isEqualTo("제목")
        assertThat(result.boardType).isEqualTo(BoardType.FREE)
        assertThat(result.likeCount).isEqualTo(0L)
        verify { postRepository.save(any()) }
    }

    @Test
    fun `createPost - 존재하지 않는 이메일이면 NotFoundException`() {
        every { userRepository.findByEmail("ghost@test.com") } returns null

        assertThrows<NotFoundException> {
            postService.createPost("ghost@test.com", PostCreateRequest("제목", "내용", BoardType.FREE))
        }
    }

    // ── deletePost ───────────────────────────────────────────────────────────

    @Test
    fun `deletePost - 작성자 본인은 삭제 가능`() {
        val post = Post(id = 1L, author = author, boardType = BoardType.FREE, title = "제목", content = "내용")
        every { postRepository.findById(1L) } returns Optional.of(post)
        every { postLikeRepository.deleteAllByPostId(1L) } just runs
        every { commentRepository.deleteAllByPostId(1L) } just runs
        every { postRepository.delete(post) } just runs

        postService.deletePost(1L, "author@test.com", isAdmin = false)

        verify { postRepository.delete(post) }
    }

    @Test
    fun `deletePost - 어드민은 다른 사람 글도 삭제 가능`() {
        val post = Post(id = 1L, author = author, boardType = BoardType.FREE, title = "제목", content = "내용")
        every { postRepository.findById(1L) } returns Optional.of(post)
        every { postLikeRepository.deleteAllByPostId(1L) } just runs
        every { commentRepository.deleteAllByPostId(1L) } just runs
        every { postRepository.delete(post) } just runs

        postService.deletePost(1L, "admin@test.com", isAdmin = true)

        verify { postRepository.delete(post) }
    }

    @Test
    fun `deletePost - 관계없는 사람이 삭제 시도하면 FORBIDDEN`() {
        val post = Post(id = 1L, author = author, boardType = BoardType.FREE, title = "제목", content = "내용")
        every { postRepository.findById(1L) } returns Optional.of(post)

        assertThrows<ForbiddenException> {
            postService.deletePost(1L, "other@test.com", isAdmin = false)
        }
    }

    @Test
    fun `deletePost - 존재하지 않는 게시글이면 NotFoundException`() {
        every { postRepository.findById(999L) } returns Optional.empty()

        assertThrows<NotFoundException> {
            postService.deletePost(999L, "author@test.com", isAdmin = false)
        }
    }

    // ── toggleLike ───────────────────────────────────────────────────────────

    @Test
    fun `toggleLike - 좋아요 없으면 생성하고 true 반환`() {
        val post = Post(id = 1L, author = author, boardType = BoardType.FREE, title = "제목", content = "내용")
        every { postRepository.findById(1L) } returns Optional.of(post)
        every { userRepository.findByEmail("other@test.com") } returns other
        every { postLikeRepository.findByPostIdAndUserId(1L, 2L) } returns null
        every { postLikeRepository.save(any()) } answers { firstArg() }

        val liked = postService.toggleLike(1L, "other@test.com")

        assertThat(liked).isTrue()
        verify { postLikeRepository.save(any()) }
    }

    @Test
    fun `toggleLike - 좋아요 있으면 삭제하고 false 반환`() {
        val post = Post(id = 1L, author = author, boardType = BoardType.FREE, title = "제목", content = "내용")
        val like = PostLike(id = 1L, post = post, user = other)
        every { postRepository.findById(1L) } returns Optional.of(post)
        every { userRepository.findByEmail("other@test.com") } returns other
        every { postLikeRepository.findByPostIdAndUserId(1L, 2L) } returns like
        every { postLikeRepository.delete(like) } just runs

        val liked = postService.toggleLike(1L, "other@test.com")

        assertThat(liked).isFalse()
        verify { postLikeRepository.delete(like) }
    }

    // ── addComment ───────────────────────────────────────────────────────────

    @Test
    fun `addComment - 댓글 저장 후 DTO 반환`() {
        val post = Post(id = 1L, author = author, boardType = BoardType.QNA, title = "질문", content = "내용")
        val saved = Comment(id = 1L, post = post, author = other, content = "답변입니다")

        every { postRepository.findById(1L) } returns Optional.of(post)
        every { userRepository.findByEmail("other@test.com") } returns other
        every { commentRepository.save(any()) } returns saved

        val result = postService.addComment(1L, "other@test.com", CommentCreateRequest("답변입니다"))

        assertThat(result.content).isEqualTo("답변입니다")
        assertThat(result.authorName).isEqualTo("타인")
        assertThat(result.parentId).isNull()
    }

    @Test
    fun `addComment - parentId 있으면 대댓글로 저장`() {
        val post = Post(id = 1L, author = author, boardType = BoardType.QNA, title = "질문", content = "내용")
        val parent = Comment(id = 10L, post = post, author = other, content = "부모 댓글")
        val reply = Comment(id = 11L, post = post, author = author, parent = parent, content = "대댓글")

        every { postRepository.findById(1L) } returns Optional.of(post)
        every { userRepository.findByEmail("author@test.com") } returns author
        every { commentRepository.findById(10L) } returns Optional.of(parent)
        every { commentRepository.save(any()) } returns reply

        val result = postService.addComment(1L, "author@test.com", CommentCreateRequest("대댓글", parentId = 10L))

        assertThat(result.parentId).isEqualTo(10L)
    }
}
