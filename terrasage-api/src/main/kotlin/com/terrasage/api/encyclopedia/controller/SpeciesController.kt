package com.terrasage.api.encyclopedia.controller

import com.terrasage.api.common.response.ApiResponse
import com.terrasage.api.encyclopedia.dto.SpeciesDetailResponse
import com.terrasage.api.encyclopedia.dto.SpeciesListResponse
import com.terrasage.api.encyclopedia.dto.SpeciesSearchRequest
import com.terrasage.api.encyclopedia.service.SpeciesService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

// 종(Species) 공개 API — 누구나 접근 가능
@RestController
@RequestMapping("/api/v1/species")
class SpeciesController(
    private val speciesService: SpeciesService,
) {
    // 종 목록 조회 + 검색/필터 (쿼리 파라미터: keyword, taxonomyClass, family 등)
    @GetMapping
    fun getSpeciesList(search: SpeciesSearchRequest, pageable: Pageable): ApiResponse<Page<SpeciesListResponse>> =
        ApiResponse.ok(speciesService.getSpeciesList(search, pageable))

    // 종 상세 조회 (CareGuide + Morph 포함)
    @GetMapping("/{id}")
    fun getSpeciesDetail(@PathVariable id: Long): ApiResponse<SpeciesDetailResponse> =
        ApiResponse.ok(speciesService.getSpeciesDetail(id))
}
