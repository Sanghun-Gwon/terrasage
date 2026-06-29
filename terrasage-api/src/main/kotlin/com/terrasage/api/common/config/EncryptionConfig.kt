package com.terrasage.api.common.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.encrypt.Encryptors
import org.springframework.security.crypto.encrypt.TextEncryptor

@Configuration
class EncryptionConfig(
    @Value("\${encrypt.secret}") private val secret: String,
    @Value("\${encrypt.salt}") private val salt: String,
) {
    @Bean
    fun textEncryptor(): TextEncryptor = Encryptors.text(secret, salt)
}
