package com.terrasage.api.care.repository

import com.terrasage.api.care.entity.CareRecord
import org.springframework.data.jpa.repository.JpaRepository

interface CareRecordRepository : JpaRepository<CareRecord, Long> {
    fun findTop100ByAnimalIdOrderByRecordedAtDesc(animalId: Long): List<CareRecord>
    fun existsByIdAndAnimalId(id: Long, animalId: Long): Boolean
}
