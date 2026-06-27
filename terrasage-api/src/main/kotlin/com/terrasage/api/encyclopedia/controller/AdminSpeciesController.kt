package com.terrasage.api.encyclopedia.controller

import com.terrasage.api.common.response.ApiResponse
import com.terrasage.api.encyclopedia.dto.*
import com.terrasage.api.encyclopedia.service.SpeciesService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/admin/species")
@PreAuthorize("hasRole('ADMIN')")
class AdminSpeciesController(
    private val speciesService: SpeciesService,
) {
    @GetMapping
    fun getSpeciesList(pageable: Pageable): ApiResponse<Page<SpeciesListResponse>> =
        ApiResponse.ok(speciesService.getSpeciesList(SpeciesSearchRequest(status = null), pageable))

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createSpecies(@Valid @RequestBody request: SpeciesCreateRequest): ApiResponse<SpeciesDetailResponse> =
        ApiResponse.ok(speciesService.createSpecies(request))

    @PutMapping("/{id}")
    fun updateSpecies(
        @PathVariable id: Long,
        @Valid @RequestBody request: SpeciesUpdateRequest,
    ): ApiResponse<SpeciesDetailResponse> =
        ApiResponse.ok(speciesService.updateSpecies(id, request))

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteSpecies(@PathVariable id: Long) =
        speciesService.deleteSpecies(id)

    // ── 변이/품종(Variant) ──────────────────────────────────────

    @GetMapping("/{speciesId}/variants")
    fun getVariants(@PathVariable speciesId: Long): ApiResponse<List<VariantResponse>> =
        ApiResponse.ok(speciesService.getVariants(speciesId))

    @PostMapping("/{speciesId}/variants")
    @ResponseStatus(HttpStatus.CREATED)
    fun addVariant(
        @PathVariable speciesId: Long,
        @Valid @RequestBody request: VariantCreateRequest,
    ): ApiResponse<VariantResponse> =
        ApiResponse.ok(speciesService.addVariant(speciesId, request))

    @PutMapping("/{speciesId}/variants/{variantId}")
    fun updateVariant(
        @PathVariable speciesId: Long,
        @PathVariable variantId: Long,
        @Valid @RequestBody request: VariantCreateRequest,
    ): ApiResponse<VariantResponse> =
        ApiResponse.ok(speciesService.updateVariant(speciesId, variantId, request))

    @DeleteMapping("/{speciesId}/variants/{variantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteVariant(
        @PathVariable speciesId: Long,
        @PathVariable variantId: Long,
    ) = speciesService.deleteVariant(speciesId, variantId)

    // ── 동물 사육 가이드 ────────────────────────────────────────

    @PutMapping("/{speciesId}/animal-care-guide")
    fun upsertAnimalCareGuide(
        @PathVariable speciesId: Long,
        @RequestBody request: AnimalCareGuideUpsertRequest,
    ): ApiResponse<AnimalCareGuideResponse> =
        ApiResponse.ok(speciesService.upsertAnimalCareGuide(speciesId, request))

    // ── 식물 재배 가이드 ────────────────────────────────────────

    @PutMapping("/{speciesId}/plant-care-guide")
    fun upsertPlantCareGuide(
        @PathVariable speciesId: Long,
        @RequestBody request: PlantCareGuideUpsertRequest,
    ): ApiResponse<PlantCareGuideResponse> =
        ApiResponse.ok(speciesService.upsertPlantCareGuide(speciesId, request))
}
