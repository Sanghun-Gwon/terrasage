package com.terrasage.api.common.exception

// 애플리케이션 공통 예외 베이스 — code 필드로 클라이언트가 에러 유형 구분
open class TerrasageException(
    val code: String,
    message: String,
) : RuntimeException(message)

// 리소스 조회 실패 (HTTP 404)
class NotFoundException(resource: String, id: Any) :
    TerrasageException("NOT_FOUND", "${resource}($id)을 찾을 수 없습니다")

// 유니크 제약 위반 (HTTP 409)
class DuplicateException(resource: String) :
    TerrasageException("DUPLICATE", "${resource}이(가) 이미 존재합니다")

// 권한 없음 (HTTP 403)
class ForbiddenException(message: String = "권한이 없습니다") :
    TerrasageException("FORBIDDEN", message)
