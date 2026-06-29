package com.terrasage.api.common.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.encrypt.Encryptors
import org.springframework.security.crypto.encrypt.TextEncryptor

/**
 * 개인정보(전화번호 등) 암호화 설정 — AES-256-GCM
 *
 * ── 현재 상태 ────────────────────────────────────────────────────────────────
 * 로컬 개발 단계: application.yml 기본값 사용 (소스 보관).
 * 운영 전 반드시 아래 키 관리 지침에 따라 전환할 것.
 *
 * ── 키 관리 법적/보안 요구사항 ──────────────────────────────────────────────
 * [국내]
 * - 개인정보보호법 시행령 제30조: 고유식별정보·비밀번호·바이오정보 암호화 의무
 * - KISA "암호이용안내서" / "암호키 관리 안내서":
 *   https://www.kisa.or.kr/2060204/form?postSeq=14&lang_type=KO
 *   · 암호키는 소스코드·설정파일과 분리 보관
 *   · 키 유효기간: 대칭키 권고 최대 2년 (민감도에 따라 단축)
 *   · 키 교체(Rotation) 후 이전 키로 암호화된 데이터 재암호화 또는 이전 키 이력 보관
 *
 * [국제]
 * - NIST SP 800-57 Part 1 Rev.5 (Key Management Recommendation):
 *   https://csrc.nist.gov/publications/detail/sp/800-57-part-1/rev-5/final
 *   · 키 생성(Generate) → 배포(Distribute) → 저장(Store) → 사용(Use)
 *     → 교체(Rotate) → 폐기(Destroy) 전 주기 관리
 * - PCI DSS v4.0 Req. 3.6~3.7 (결제 기능 추가 시 준수 필요):
 *   · 암호화 키는 최소 권한 원칙으로 접근 제어
 *   · 키 교체는 연 1회 이상
 *
 * ── 운영 전환 시 체크리스트 ─────────────────────────────────────────────────
 * TODO(운영 전):
 *  1. application.yml 기본값 제거 → 환경변수(ENCRYPT_SECRET, ENCRYPT_SALT) 필수화
 *  2. AWS KMS / HashiCorp Vault / Azure Key Vault 등 전용 키 관리 서비스 도입
 *     - 현재 Encryptors.text() → KMS 기반 구현체로 교체 (이 클래스만 수정)
 *  3. 키 버전 관리: ENCRYPT_KEY_VERSION 도입 → 교체 시 구버전 키 병행 운용
 *     (구버전 키로 암호화된 데이터 복호화 가능하도록 유지)
 *  4. 키 접근 감사 로그 활성화
 *  5. 키 교체 주기 정책 수립 (KISA 권고: 최대 2년, 결제 포함 시 연 1회)
 */
@Configuration
class EncryptionConfig(
    @Value("\${encrypt.secret}") private val secret: String,
    @Value("\${encrypt.salt}") private val salt: String,
) {
    @Bean
    fun textEncryptor(): TextEncryptor = Encryptors.text(secret, salt)
}
