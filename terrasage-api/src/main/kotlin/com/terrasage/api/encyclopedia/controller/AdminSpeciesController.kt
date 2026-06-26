package com.terrasage.api.encyclopedia.controller

import com.terrasage.api.common.response.ApiResponse
import com.terrasage.api.encyclopedia.dto.SpeciesCreateRequest
import com.terrasage.api.encyclopedia.dto.SpeciesDetailResponse
import com.terrasage.api.encyclopedia.dto.SpeciesListResponse
import com.terrasage.api.encyclopedia.dto.SpeciesSearchRequest
import com.terrasage.api.encyclopedia.dto.SpeciesUpdateRequest
import com.terrasage.api.encyclopedia.service.SpeciesService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

// 종(Species) 관리자 API — 추후 @PreAuthorize("hasRole('ADMIN')") 추가 예정
@RestController
@RequestMapping("/api/v1/admin/species")
class AdminSpeciesController(
    private val speciesService: SpeciesService,
) {
    // 종 목록 조회 (관리자용) — status 필터 없이 DRAFT/PUBLISHED/ARCHIVED 전체 조회
    @GetMapping
    fun getSpeciesList(pageable: Pageable): ApiResponse<Page<SpeciesListResponse>> =
        ApiResponse.ok(speciesService.getSpeciesList(SpeciesSearchRequest(status = null), pageable))

    // 종 등록 — 초기 상태 DRAFT (관리자가 검토 후 PUBLISHED로 변경)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createSpecies(@Valid @RequestBody request: SpeciesCreateRequest): ApiResponse<SpeciesDetailResponse> =
        ApiResponse.ok(speciesService.createSpecies(request))

    // 종 수정 — 정보 변경 및 상태 전환 (DRAFT → PUBLISHED → ARCHIVED)
    @PutMapping("/{id}")
    fun updateSpecies(
        @PathVariable id: Long,
        @Valid @RequestBody request: SpeciesUpdateRequest,
    ): ApiResponse<SpeciesDetailResponse> =
        ApiResponse.ok(speciesService.updateSpecies(id, request))

    // 종 삭제 — 하드삭제 (잘못된 정보, 서비스 종료 종 제거용)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteSpecies(@PathVariable id: Long) =
        speciesService.deleteSpecies(id)
}
