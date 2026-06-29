package com.terrasage.api.auth.dto

import com.terrasage.api.auth.entity.User
import com.terrasage.api.auth.entity.UserRole
import java.time.LocalDateTime

data class MyProfileResponse(
    val email: String,
    val name: String,
    val phoneNumber: String?,
    val role: UserRole,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(user: User) = MyProfileResponse(
            email = user.email,
            name = user.name,
            phoneNumber = user.phoneNumber?.let { maskPhone(it) },
            role = user.role,
            createdAt = user.createdAt,
        )

        // 010-1234-5678 → 010-****-5678
        private fun maskPhone(phone: String): String {
            val digits = phone.replace(Regex("[- ]"), "")
            return if (digits.length >= 10)
                "${digits.substring(0, 3)}-****-${digits.substring(digits.length - 4)}"
            else phone
        }
    }
}
