package com.terrasage.api.encyclopedia.repository

import com.terrasage.api.encyclopedia.dto.SpeciesSearchRequest
import com.terrasage.api.encyclopedia.entity.Species
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification

// JPA Specification 기반 동적 쿼리 빌더
// 검색 조건이 있는 필드만 WHERE 절에 추가 (null인 조건은 무시)
object SpeciesSpecification {

    fun fromSearch(request: SpeciesSearchRequest): Specification<Species> =
        Specification { root: Root<Species>, _: CriteriaQuery<*>?, cb: CriteriaBuilder ->
            val predicates = mutableListOf<Predicate>()

            // 상태 필터 (기본값 PUBLISHED)
            request.status?.let {
                predicates += cb.equal(root.get<Any>("status"), it)
            }

            // 키워드 검색 — 학명, 한글명, 영문명에서 대소문자 무시 LIKE 검색
            request.keyword?.takeIf { it.isNotBlank() }?.let { keyword ->
                val pattern = "%${keyword.lowercase()}%"
                predicates += cb.or(
                    cb.like(cb.lower(root.get("scientificName")), pattern),
                    cb.like(cb.lower(root.get("commonNameKo")), pattern),
                    cb.like(cb.lower(root.get("commonNameEn")), pattern),
                )
            }

            // 분류 계층 필터 (정확히 일치)
            request.taxonomyClass?.let {
                predicates += cb.equal(root.get<Any>("taxonomyClass"), it)
            }

            request.taxonomyOrder?.let {
                predicates += cb.equal(root.get<Any>("taxonomyOrder"), it)
            }

            request.family?.let {
                predicates += cb.equal(root.get<Any>("family"), it)
            }

            request.genus?.let {
                predicates += cb.equal(root.get<Any>("genus"), it)
            }

            // 난이도 필터
            request.difficultyLevel?.let {
                predicates += cb.equal(root.get<Any>("difficultyLevel"), it)
            }

            cb.and(*predicates.toTypedArray())
        }
}
