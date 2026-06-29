package com.terrasage.api.common.config

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.springframework.security.crypto.encrypt.TextEncryptor
import org.springframework.stereotype.Component

// @Component: Spring이 TextEncryptor를 주입할 수 있도록 Spring-managed converter로 등록
// @Converter(autoApply = false): @Convert로 명시한 컬럼에만 적용
@Component
@Converter
class EncryptedStringConverter(
    private val encryptor: TextEncryptor,
) : AttributeConverter<String?, String?> {

    override fun convertToDatabaseColumn(attribute: String?): String? =
        attribute?.let { encryptor.encrypt(it) }

    override fun convertToEntityAttribute(dbData: String?): String? =
        dbData?.let { encryptor.decrypt(it) }
}
