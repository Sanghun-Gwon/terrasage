package com.terrasage.api.common.exception

import com.terrasage.api.common.response.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

// 전역 예외 처리 — 구체적인 예외부터 선언해야 상위 핸들러에 먹히지 않음
@RestControllerAdvice
class GlobalExceptionHandler {

    // 404 — 리소스 없음
    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFoundException(e: NotFoundException): ApiResponse<Nothing> =
        ApiResponse.fail(e.code, e.message ?: "Not found")

    // 409 — 중복 리소스 (학명 등 유니크 제약 위반)
    @ExceptionHandler(DuplicateException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleDuplicateException(e: DuplicateException): ApiResponse<Nothing> =
        ApiResponse.fail(e.code, e.message ?: "Duplicate")

    // 400 — @Valid 검증 실패 시 필드별 에러 메시지 조합
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationException(e: MethodArgumentNotValidException): ApiResponse<Nothing> {
        val message = e.bindingResult.fieldErrors
            .joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
        return ApiResponse.fail("VALIDATION_ERROR", message)
    }

    // 403 — 권한 없음
    @ExceptionHandler(ForbiddenException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleForbiddenException(e: ForbiddenException): ApiResponse<Nothing> =
        ApiResponse.fail(e.code, e.message ?: "Forbidden")

    // 400 — 기타 비즈니스 예외 (TerrasageException 하위 클래스)
    @ExceptionHandler(TerrasageException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleTerrasageException(e: TerrasageException): ApiResponse<Nothing> =
        ApiResponse.fail(e.code, e.message ?: "Bad request")

    // 500 — 예상치 못한 서버 오류
    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(e: Exception): ApiResponse<Nothing> {
        org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler::class.java).error("Unhandled exception", e)
        return ApiResponse.fail("INTERNAL_ERROR", "서버 오류가 발생했습니다")
    }
}
