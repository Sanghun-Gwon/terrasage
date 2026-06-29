package com.terrasage.api.care.repository

import com.terrasage.api.care.entity.Animal
import org.springframework.data.jpa.repository.JpaRepository

interface AnimalRepository : JpaRepository<Animal, Long> {
    fun findAllByOwnerIdOrderByCreatedAtDesc(ownerId: Long): List<Animal>
    fun existsByIdAndOwnerId(id: Long, ownerId: Long): Boolean
}
