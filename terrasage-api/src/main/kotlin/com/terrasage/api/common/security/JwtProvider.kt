package com.terrasage.api.common.security

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtProvider(
    @Value("\${jwt.secret}") secret: String,
    @Value("\${jwt.expiration-ms}") private val expirationMs: Long,
) {
    private val key: SecretKey = Keys.hmacShaKeyFor(secret.toByteArray())

    fun generate(email: String, role: String): String =
        Jwts.builder()
            .subject(email)
            .claim("role", role)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + expirationMs))
            .signWith(key)
            .compact()

    fun getEmail(token: String): String =
        claims(token).subject

    fun getRole(token: String): String =
        claims(token).get("role", String::class.java)

    fun validate(token: String): Boolean = runCatching { claims(token); true }.getOrDefault(false)

    private fun claims(token: String) =
        Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload
}
