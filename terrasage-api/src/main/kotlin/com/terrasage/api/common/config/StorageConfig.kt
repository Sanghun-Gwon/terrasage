package com.terrasage.api.common.config

import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// Cloud Run: 서비스 계정 ADC(Application Default Credentials)로 자동 인증.
// Signed URL 서명에는 IAM signBlob 권한 필요 → 서비스 계정에 roles/iam.serviceAccountTokenCreator
// 로컬: GOOGLE_APPLICATION_CREDENTIALS 환경변수로 서비스 계정 키 지정 시에만 동작
@Configuration
class StorageConfig {

    @Bean
    fun storage(): Storage = StorageOptions.getDefaultInstance().service
}
