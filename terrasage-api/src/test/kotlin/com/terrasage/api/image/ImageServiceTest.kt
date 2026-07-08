package com.terrasage.api.image

import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.terrasage.api.common.exception.TerrasageException
import com.terrasage.api.image.dto.UploadUrlRequest
import com.terrasage.api.image.service.ImageService
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.net.URI

class ImageServiceTest {

    private val storage = mockk<Storage>()
    private val service = ImageService(storage, "test-bucket")

    @Test
    fun `지원하지 않는 이미지 형식이면 예외가 발생한다`() {
        val exception = assertThrows<TerrasageException> {
            service.createUploadUrl(UploadUrlRequest("image/svg+xml"))
        }
        assertEquals("UNSUPPORTED_IMAGE_TYPE", exception.code)
    }

    @Test
    fun `서명된 업로드 URL과 영구 공개 URL을 반환한다`() {
        val blobSlot = slot<BlobInfo>()
        every {
            storage.signUrl(capture(blobSlot), any(), any(), *anyVararg())
        } returns URI("https://signed.example.com/upload").toURL()

        val response = service.createUploadUrl(UploadUrlRequest("image/jpeg"))

        assertEquals("https://signed.example.com/upload", response.uploadUrl)
        assertTrue(response.publicUrl.startsWith("https://storage.googleapis.com/test-bucket/uploads/"))
        assertTrue(response.publicUrl.endsWith(".jpg"))
        // 확장자는 contentType 기준으로 결정, Content-Type이 서명 대상에 포함되는지 확인
        assertEquals("image/jpeg", blobSlot.captured.contentType)
    }
}
