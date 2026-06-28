package com.terrasage.api.integration

import com.terrasage.api.auth.entity.UserRole
import com.terrasage.api.support.IntegrationTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.client.expectBody

class CommunityApiTest : IntegrationTestBase() {

    // ── 공개 GET ─────────────────────────────────────────────────────────────

    @Test
    fun `GET posts - 토큰 없이 200 반환`() {
        val result = client.get().uri("/api/v1/posts")
            .exchange()
            .expectStatus().isOk
            .expectBody<Map<String, Any>>()
            .returnResult().responseBody!!

        assertThat(result["success"]).isEqualTo(true)
    }

    @Test
    fun `GET posts - boardType 필터 파라미터 정상 작동`() {
        client.get().uri("/api/v1/posts?boardType=QNA")
            .exchange()
            .expectStatus().isOk
    }

    // ── 게시글 작성 ───────────────────────────────────────────────────────────

    @Test
    fun `POST posts - 토큰 없으면 401`() {
        client.post().uri("/api/v1/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .body(mapOf("boardType" to "FREE", "title" to "제목", "content" to "내용"))
            .exchange()
            .expectStatus().isUnauthorized
    }

    @Test
    fun `POST posts - USER 토큰으로 게시글 작성 성공`() {
        val token = createUserAndGetToken("post_user@test.com")

        @Suppress("UNCHECKED_CAST")
        val data = client.post().uri("/api/v1/posts")
            .header("Authorization", bearerToken(token))
            .contentType(MediaType.APPLICATION_JSON)
            .body(mapOf("boardType" to "TIPS", "title" to "레오게코 팁 공유", "content" to "내용"))
            .exchange()
            .expectStatus().isCreated
            .expectBody<Map<String, Any>>()
            .returnResult().responseBody!!["data"] as Map<String, Any>

        assertThat(data["title"]).isEqualTo("레오게코 팁 공유")
        assertThat(data["boardType"]).isEqualTo("TIPS")
    }

    @Test
    fun `POST posts - SHOWCASE에 imageUrls 첨부 가능`() {
        val token = createUserAndGetToken("showcase_user@test.com")

        @Suppress("UNCHECKED_CAST")
        val data = client.post().uri("/api/v1/posts")
            .header("Authorization", bearerToken(token))
            .contentType(MediaType.APPLICATION_JSON)
            .body(mapOf(
                "boardType" to "SHOWCASE",
                "title" to "우리집 레오게코 자랑",
                "content" to "정말 귀엽죠",
                "imageUrls" to listOf("https://example.com/gecko.jpg"),
            ))
            .exchange()
            .expectStatus().isCreated
            .expectBody<Map<String, Any>>()
            .returnResult().responseBody!!["data"] as Map<String, Any>

        @Suppress("UNCHECKED_CAST")
        assertThat(data["imageUrls"] as List<*>).containsExactly("https://example.com/gecko.jpg")
    }

    // ── 좋아요 토글 ───────────────────────────────────────────────────────────

    @Test
    fun `POST likes - 좋아요 토글 첫 번째는 liked=true`() {
        val token = createUserAndGetToken("like_user@test.com")
        val postId = createPost(token, "좋아요 테스트 게시글")

        @Suppress("UNCHECKED_CAST")
        val data = client.post().uri("/api/v1/posts/$postId/likes")
            .header("Authorization", bearerToken(token))
            .exchange()
            .expectStatus().isOk
            .expectBody<Map<String, Any>>()
            .returnResult().responseBody!!["data"] as Map<String, Any>

        assertThat(data["liked"]).isEqualTo(true)
    }

    @Test
    fun `POST likes - 두 번 토글하면 liked=false`() {
        val token = createUserAndGetToken("like_toggle@test.com")
        val postId = createPost(token, "토글 테스트")

        client.post().uri("/api/v1/posts/$postId/likes")
            .header("Authorization", bearerToken(token))
            .exchange()
            .expectStatus().isOk

        @Suppress("UNCHECKED_CAST")
        val data = client.post().uri("/api/v1/posts/$postId/likes")
            .header("Authorization", bearerToken(token))
            .exchange()
            .expectStatus().isOk
            .expectBody<Map<String, Any>>()
            .returnResult().responseBody!!["data"] as Map<String, Any>

        assertThat(data["liked"]).isEqualTo(false)
    }

    // ── 댓글 ─────────────────────────────────────────────────────────────────

    @Test
    fun `POST comments - 댓글 작성 성공`() {
        val token = createUserAndGetToken("comment_user@test.com")
        val postId = createPost(token, "댓글 테스트 게시글")

        @Suppress("UNCHECKED_CAST")
        val data = client.post().uri("/api/v1/posts/$postId/comments")
            .header("Authorization", bearerToken(token))
            .contentType(MediaType.APPLICATION_JSON)
            .body(mapOf("content" to "좋은 정보 감사합니다!"))
            .exchange()
            .expectStatus().isCreated
            .expectBody<Map<String, Any>>()
            .returnResult().responseBody!!["data"] as Map<String, Any>

        assertThat(data["content"]).isEqualTo("좋은 정보 감사합니다!")
        assertThat(data["parentId"]).isNull()
    }

    @Test
    fun `POST comments - 대댓글 작성 (parentId 설정)`() {
        val token = createUserAndGetToken("reply_user@test.com")
        val postId = createPost(token, "대댓글 테스트")

        // 부모 댓글 작성
        @Suppress("UNCHECKED_CAST")
        val parentData = client.post().uri("/api/v1/posts/$postId/comments")
            .header("Authorization", bearerToken(token))
            .contentType(MediaType.APPLICATION_JSON)
            .body(mapOf("content" to "부모 댓글"))
            .exchange()
            .expectStatus().isCreated
            .expectBody<Map<String, Any>>()
            .returnResult().responseBody!!["data"] as Map<String, Any>
        val parentId = (parentData["id"] as Int).toLong()

        // 대댓글 작성
        @Suppress("UNCHECKED_CAST")
        val replyData = client.post().uri("/api/v1/posts/$postId/comments")
            .header("Authorization", bearerToken(token))
            .contentType(MediaType.APPLICATION_JSON)
            .body(mapOf("content" to "대댓글입니다", "parentId" to parentId))
            .exchange()
            .expectStatus().isCreated
            .expectBody<Map<String, Any>>()
            .returnResult().responseBody!!["data"] as Map<String, Any>

        assertThat(replyData["parentId"]).isEqualTo(parentId.toInt())
    }

    // ── 게시글 삭제 ───────────────────────────────────────────────────────────

    @Test
    fun `DELETE post - 다른 사람 글 삭제 시도하면 403`() {
        val author = createUserAndGetToken("del_author@test.com")
        val other  = createUserAndGetToken("del_other@test.com")
        val postId = createPost(author, "삭제 테스트 게시글")

        client.delete().uri("/api/v1/posts/$postId")
            .header("Authorization", bearerToken(other))
            .exchange()
            .expectStatus().isForbidden
    }

    @Test
    fun `DELETE post - 작성자가 삭제하면 204 No Content`() {
        val token = createUserAndGetToken("del_self@test.com")
        val postId = createPost(token, "본인 삭제 테스트")

        client.delete().uri("/api/v1/posts/$postId")
            .header("Authorization", bearerToken(token))
            .exchange()
            .expectStatus().isNoContent
    }

    // ── 헬퍼 ─────────────────────────────────────────────────────────────────

    @Suppress("UNCHECKED_CAST")
    private fun createPost(token: String, title: String): Long {
        val data = client.post().uri("/api/v1/posts")
            .header("Authorization", bearerToken(token))
            .contentType(MediaType.APPLICATION_JSON)
            .body(mapOf("boardType" to "FREE", "title" to title, "content" to "테스트 내용"))
            .exchange()
            .expectStatus().isCreated
            .expectBody<Map<String, Any>>()
            .returnResult().responseBody!!["data"] as Map<String, Any>
        return (data["id"] as Int).toLong()
    }
}
