package com.terrasage.api.image.dto

data class UploadUrlResponse(
    // GCS에 PUT으로 직접 업로드할 서명된 URL (10분 유효)
    val uploadUrl: String,
    // 업로드 완료 후 조회에 사용할 영구 공개 URL — DB에는 이 값을 저장
    val publicUrl: String,
)
