package com.terrasage.api.encyclopedia.repository

import com.terrasage.api.encyclopedia.entity.Morph
import org.springframework.data.jpa.repository.JpaRepository

// Species와 N:1 관계 — 한 종에 여러 모프 존재
interface MorphRepository : JpaRepository<Morph, Long> {
    fun findBySpeciesId(speciesId: Long): List<Morph>
    // 종 하드삭제 전 FK 정리용
    fun deleteBySpeciesId(speciesId: Long)
}
