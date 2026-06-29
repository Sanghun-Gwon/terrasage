package com.terrasage.api.care.controller

import com.terrasage.api.care.dto.*
import com.terrasage.api.care.service.AnimalService
import com.terrasage.api.common.response.ApiResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/animals")
class AnimalController(private val animalService: AnimalService) {

    @GetMapping
    fun getMyAnimals(@AuthenticationPrincipal email: String): ApiResponse<List<AnimalResponse>> =
        ApiResponse.ok(animalService.getMyAnimals(email))

    @GetMapping("/{id}")
    fun getAnimal(
        @PathVariable id: Long,
        @AuthenticationPrincipal email: String,
    ): ApiResponse<AnimalResponse> =
        ApiResponse.ok(animalService.getAnimal(id, email))

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAnimal(
        @Valid @RequestBody request: AnimalCreateRequest,
        @AuthenticationPrincipal email: String,
    ): ApiResponse<AnimalResponse> =
        ApiResponse.ok(animalService.createAnimal(request, email))

    @PutMapping("/{id}")
    fun updateAnimal(
        @PathVariable id: Long,
        @Valid @RequestBody request: AnimalUpdateRequest,
        @AuthenticationPrincipal email: String,
    ): ApiResponse<AnimalResponse> =
        ApiResponse.ok(animalService.updateAnimal(id, request, email))

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAnimal(
        @PathVariable id: Long,
        @AuthenticationPrincipal email: String,
    ) = animalService.deleteAnimal(id, email)

    // ── CareRecord ──────────────────────────────────────────────────────────

    @GetMapping("/{id}/records")
    fun getRecords(
        @PathVariable id: Long,
        @AuthenticationPrincipal email: String,
    ): ApiResponse<List<CareRecordResponse>> =
        ApiResponse.ok(animalService.getRecords(id, email))

    @PostMapping("/{id}/records")
    @ResponseStatus(HttpStatus.CREATED)
    fun addRecord(
        @PathVariable id: Long,
        @Valid @RequestBody request: CareRecordCreateRequest,
        @AuthenticationPrincipal email: String,
    ): ApiResponse<CareRecordResponse> =
        ApiResponse.ok(animalService.addRecord(id, request, email))

    @DeleteMapping("/{id}/records/{recordId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteRecord(
        @PathVariable id: Long,
        @PathVariable recordId: Long,
        @AuthenticationPrincipal email: String,
    ) = animalService.deleteRecord(id, recordId, email)
}
