package com.terrasage.api.image.service

import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.HttpMethod
import com.google.cloud.storage.Storage
import com.terrasage.api.common.exception.TerrasageException
import com.terrasage.api.image.dto.UploadUrlRequest
import com.terrasage.api.image.dto.UploadUrlResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID
import java.util.concurrent.TimeUnit

@Service
class ImageService(
    private val storage: Storage,
    @Value("\${gcs.bucket}")
    private val bucket: String,
) {
    companion object {
        // 확장자는 클라이언트 파일명이 아닌 contentType에서 결정 (파일명 위조 방지)
        private val ALLOWED_CONTENT_TYPES = mapOf(
            "image/jpeg" to "jpg",
            "image/png" to "png",
            "image/webp" to "webp",
            "image/gif" to "gif",
        )
        private const val EXPIRE_MINUTES = 10L
        private val DATE_PATH = DateTimeFormatter.ofPattern("yyyy/MM")
    }

    fun createUploadUrl(request: UploadUrlRequest): UploadUrlResponse {
        val extension = ALLOWED_CONTENT_TYPES[request.contentType]
            ?: throw TerrasageException(
                "UNSUPPORTED_IMAGE_TYPE",
                "지원하지 않는 이미지 형식입니다: ${request.contentType}",
            )

        val objectName = "uploads/${LocalDate.now().format(DATE_PATH)}/${UUID.randomUUID()}.$extension"
        val blobInfo = BlobInfo.newBuilder(bucket, objectName)
            .setContentType(request.contentType)
            .build()

        // withContentType(): Content-Type을 서명에 포함 → 클라이언트가 같은 헤더로 PUT해야 유효
        val uploadUrl = storage.signUrl(
            blobInfo,
            EXPIRE_MINUTES,
            TimeUnit.MINUTES,
            Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
            Storage.SignUrlOption.withContentType(),
            Storage.SignUrlOption.withV4Signature(),
        )

        return UploadUrlResponse(
            uploadUrl = uploadUrl.toString(),
            publicUrl = "https://storage.googleapis.com/$bucket/$objectName",
        )
    }
}
