package com.terrasage.api.image.controller

import com.terrasage.api.common.response.ApiResponse
import com.terrasage.api.image.dto.UploadUrlRequest
import com.terrasage.api.image.dto.UploadUrlResponse
import com.terrasage.api.image.service.ImageService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// 인증 사용자만 접근 가능 (SecurityConfig의 anyRequest().authenticated())
@RestController
@RequestMapping("/api/v1/images")
class ImageController(private val imageService: ImageService) {

    @PostMapping("/upload-url")
    fun createUploadUrl(
        @Valid @RequestBody request: UploadUrlRequest,
    ): ApiResponse<UploadUrlResponse> = ApiResponse.ok(imageService.createUploadUrl(request))
}
