package com.terrasage.api.encyclopedia.service

import com.terrasage.api.common.exception.DuplicateException
import com.terrasage.api.common.exception.NotFoundException
import com.terrasage.api.encyclopedia.dto.CareGuideUpsertRequest
import com.terrasage.api.encyclopedia.dto.MorphCreateRequest
import com.terrasage.api.encyclopedia.dto.MorphResponse
import com.terrasage.api.encyclopedia.dto.CareGuideResponse
import com.terrasage.api.encyclopedia.dto.SpeciesCreateRequest
import com.terrasage.api.encyclopedia.dto.SpeciesDetailResponse
import com.terrasage.api.encyclopedia.dto.SpeciesListResponse
import com.terrasage.api.encyclopedia.dto.SpeciesSearchRequest
import com.terrasage.api.encyclopedia.dto.SpeciesUpdateRequest
import com.terrasage.api.encyclopedia.entity.CareGuide
import com.terrasage.api.encyclopedia.entity.Morph
import com.terrasage.api.encyclopedia.repository.CareGuideRepository
import com.terrasage.api.encyclopedia.repository.MorphRepository
import com.terrasage.api.encyclopedia.repository.SpeciesRepository
import com.terrasage.api.encyclopedia.repository.SpeciesSpecification
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

// 종(Species) 비즈니스 로직
// 클래스 레벨 readOnly=true → 조회 메서드는 별도 선언 불필요, 쓰기만 @Transactional 오버라이드
@Service
@Transactional(readOnly = true)
class SpeciesService(
    private val speciesRepository: SpeciesRepository,
    private val careGuideRepository: CareGuideRepository,
    private val morphRepository: MorphRepository,
) {
    // Specification 기반 동적 검색 — 조건이 없으면 전체 조회
    fun getSpeciesList(search: SpeciesSearchRequest, pageable: Pageable): Page<SpeciesListResponse> =
        speciesRepository.findAll(SpeciesSpecification.fromSearch(search), pageable)
            .map { SpeciesListResponse.from(it) }

    // 종 + 사육가이드 + 모프 목록을 한번에 조회
    fun getSpeciesDetail(id: Long): SpeciesDetailResponse {
        val species = speciesRepository.findById(id)
            .orElseThrow { NotFoundException("Species", id) }
        val careGuide = careGuideRepository.findBySpeciesId(id)
        val morphs = morphRepository.findBySpeciesId(id)
        return SpeciesDetailResponse.from(species, careGuide, morphs)
    }

    // 학명 중복 체크 후 등록 (신규 종은 DRAFT 상태로 시작)
    @Transactional
    fun createSpecies(request: SpeciesCreateRequest): SpeciesDetailResponse {
        if (speciesRepository.existsByScientificName(request.scientificName)) {
            throw DuplicateException("학명 '${request.scientificName}'")
        }
        val species = speciesRepository.save(request.toEntity())
        return SpeciesDetailResponse.from(species, null, emptyList())
    }

    // 종 정보 전체 수정 — 학명 변경 시 자기 자신 제외하고 중복 체크
    @Transactional
    fun updateSpecies(id: Long, request: SpeciesUpdateRequest): SpeciesDetailResponse {
        val species = speciesRepository.findById(id)
            .orElseThrow { NotFoundException("Species", id) }

        if (species.scientificName != request.scientificName &&
            speciesRepository.existsByScientificName(request.scientificName)) {
            throw DuplicateException("학명 '${request.scientificName}'")
        }

        species.apply {
            scientificName = request.scientificName
            commonNameKo = request.commonNameKo
            commonNameEn = request.commonNameEn
            kingdom = request.kingdom
            phylum = request.phylum
            taxonomyClass = request.taxonomyClass
            taxonomyOrder = request.taxonomyOrder
            family = request.family
            genus = request.genus
            origin = request.origin
            habitat = request.habitat
            lifespanCaptive = request.lifespanCaptive
            lifespanWild = request.lifespanWild
            avgSizeCm = request.avgSizeCm
            avgWeightG = request.avgWeightG
            difficultyLevel = request.difficultyLevel
            category = request.category
            citesLevel = request.citesLevel
            legalStatusNote = request.legalStatusNote
            thumbnailUrl = request.thumbnailUrl
            status = request.status
            updatedAt = LocalDateTime.now()
        }

        val careGuide = careGuideRepository.findBySpeciesId(id)
        val morphs = morphRepository.findBySpeciesId(id)
        return SpeciesDetailResponse.from(species, careGuide, morphs)
    }

    // 종 하드삭제 — CareGuide, Morph FK 먼저 정리 후 삭제
    @Transactional
    fun deleteSpecies(id: Long) {
        if (!speciesRepository.existsById(id)) {
            throw NotFoundException("Species", id)
        }
        careGuideRepository.findBySpeciesId(id)?.let { careGuideRepository.delete(it) }
        morphRepository.deleteBySpeciesId(id)
        speciesRepository.deleteById(id)
    }

    // ── 모프(Morph) ──────────────────────────────────────────

    fun getMorphs(speciesId: Long): List<MorphResponse> {
        if (!speciesRepository.existsById(speciesId)) throw NotFoundException("Species", speciesId)
        return morphRepository.findBySpeciesId(speciesId).map { MorphResponse.from(it) }
    }

    @Transactional
    fun addMorph(speciesId: Long, request: MorphCreateRequest): MorphResponse {
        val species = speciesRepository.findById(speciesId)
            .orElseThrow { NotFoundException("Species", speciesId) }
        val morph = morphRepository.save(
            Morph(
                species = species,
                name = request.name,
                geneticPattern = request.geneticPattern,
                description = request.description,
                imageUrl = request.imageUrl,
            )
        )
        return MorphResponse.from(morph)
    }

    @Transactional
    fun updateMorph(speciesId: Long, morphId: Long, request: MorphCreateRequest): MorphResponse {
        val morph = morphRepository.findById(morphId)
            .orElseThrow { NotFoundException("Morph", morphId) }
        if (morph.species.id != speciesId) throw NotFoundException("Morph", morphId)
        morph.apply {
            name = request.name
            geneticPattern = request.geneticPattern
            description = request.description
            imageUrl = request.imageUrl
        }
        return MorphResponse.from(morph)
    }

    @Transactional
    fun deleteMorph(speciesId: Long, morphId: Long) {
        val morph = morphRepository.findById(morphId)
            .orElseThrow { NotFoundException("Morph", morphId) }
        if (morph.species.id != speciesId) throw NotFoundException("Morph", morphId)
        morphRepository.delete(morph)
    }

    // ── 사육가이드(CareGuide) ─────────────────────────────────

    // 없으면 생성, 있으면 전체 필드 덮어씀 (upsert)
    @Transactional
    fun upsertCareGuide(speciesId: Long, request: CareGuideUpsertRequest): CareGuideResponse {
        val species = speciesRepository.findById(speciesId)
            .orElseThrow { NotFoundException("Species", speciesId) }

        val careGuide = careGuideRepository.findBySpeciesId(speciesId)
            ?: careGuideRepository.save(CareGuide(species = species))

        careGuide.apply {
            enclosureType = request.enclosureType
            enclosureSizeCm = request.enclosureSizeCm
            substrate = request.substrate
            tempHotZone = request.tempHotZone
            tempCoolZone = request.tempCoolZone
            tempNight = request.tempNight
            humidityMin = request.humidityMin
            humidityMax = request.humidityMax
            uvbRequired = request.uvbRequired
            photoperiodHours = request.photoperiodHours
            feedType = request.feedType
            feedFrequency = request.feedFrequency
            supplements = request.supplements
            handlingLevel = request.handlingLevel
            cohabitationNote = request.cohabitationNote
            updatedAt = LocalDateTime.now()
        }

        return CareGuideResponse.from(careGuide)
    }
}
