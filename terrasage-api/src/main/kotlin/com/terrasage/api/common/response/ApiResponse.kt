package com.terrasage.api.common.response

// 전체 API 공통 응답 래퍼 — 성공/실패 여부를 일관된 구조로 반환
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: ErrorResponse? = null,
) {
    companion object {
        fun <T> ok(data: T) = ApiResponse(success = true, data = data)

        fun fail(code: String, message: String) = ApiResponse<Nothing>(
            success = false,
            error = ErrorResponse(code, message),
        )
    }
}

data class ErrorResponse(
    val code: String,
    val message: String,
)
