package com.terrasage.api.encyclopedia.service

import com.terrasage.api.common.exception.DuplicateException
import com.terrasage.api.common.exception.NotFoundException
import com.terrasage.api.encyclopedia.dto.*
import com.terrasage.api.encyclopedia.entity.AnimalCareGuide
import com.terrasage.api.encyclopedia.entity.PlantCareGuide
import com.terrasage.api.encyclopedia.entity.Variant
import com.terrasage.api.encyclopedia.repository.AnimalCareGuideRepository
import com.terrasage.api.encyclopedia.repository.PlantCareGuideRepository
import com.terrasage.api.encyclopedia.repository.SpeciesRepository
import com.terrasage.api.encyclopedia.repository.SpeciesSpecification
import com.terrasage.api.encyclopedia.repository.VariantRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class SpeciesService(
    private val speciesRepository: SpeciesRepository,
    private val animalCareGuideRepository: AnimalCareGuideRepository,
    private val plantCareGuideRepository: PlantCareGuideRepository,
    private val variantRepository: VariantRepository,
) {
    fun getSpeciesList(search: SpeciesSearchRequest, pageable: Pageable): Page<SpeciesListResponse> =
        speciesRepository.findAll(SpeciesSpecification.fromSearch(search), pageable)
            .map { SpeciesListResponse.from(it) }

    fun getSpeciesDetail(id: Long): SpeciesDetailResponse {
        val species = speciesRepository.findById(id)
            .orElseThrow { NotFoundException("Species", id) }
        val animalCareGuide = animalCareGuideRepository.findBySpeciesId(id)
        val plantCareGuide = plantCareGuideRepository.findBySpeciesId(id)
        val variants = variantRepository.findBySpeciesId(id)
        return SpeciesDetailResponse.from(species, animalCareGuide, plantCareGuide, variants)
    }

    @Transactional
    fun createSpecies(request: SpeciesCreateRequest): SpeciesDetailResponse {
        if (speciesRepository.existsByScientificName(request.scientificName)) {
            throw DuplicateException("학명 '${request.scientificName}'")
        }
        val species = speciesRepository.save(request.toEntity())
        return SpeciesDetailResponse.from(species, null, null, emptyList())
    }

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

        val animalCareGuide = animalCareGuideRepository.findBySpeciesId(id)
        val plantCareGuide = plantCareGuideRepository.findBySpeciesId(id)
        val variants = variantRepository.findBySpeciesId(id)
        return SpeciesDetailResponse.from(species, animalCareGuide, plantCareGuide, variants)
    }

    @Transactional
    fun deleteSpecies(id: Long) {
        if (!speciesRepository.existsById(id)) throw NotFoundException("Species", id)
        animalCareGuideRepository.findBySpeciesId(id)?.let { animalCareGuideRepository.delete(it) }
        plantCareGuideRepository.findBySpeciesId(id)?.let { plantCareGuideRepository.delete(it) }
        variantRepository.deleteBySpeciesId(id)
        speciesRepository.deleteById(id)
    }

    // ── 변이/품종(Variant) ──────────────────────────────────────

    fun getVariants(speciesId: Long): List<VariantResponse> {
        if (!speciesRepository.existsById(speciesId)) throw NotFoundException("Species", speciesId)
        return variantRepository.findBySpeciesId(speciesId).map { VariantResponse.from(it) }
    }

    @Transactional
    fun addVariant(speciesId: Long, request: VariantCreateRequest): VariantResponse {
        val species = speciesRepository.findById(speciesId)
            .orElseThrow { NotFoundException("Species", speciesId) }
        val variant = variantRepository.save(
            Variant(species = species, name = request.name, geneticPattern = request.geneticPattern,
                description = request.description, imageUrl = request.imageUrl)
        )
        return VariantResponse.from(variant)
    }

    @Transactional
    fun updateVariant(speciesId: Long, variantId: Long, request: VariantCreateRequest): VariantResponse {
        val variant = variantRepository.findById(variantId)
            .orElseThrow { NotFoundException("Variant", variantId) }
        if (variant.species.id != speciesId) throw NotFoundException("Variant", variantId)
        variant.apply {
            name = request.name
            geneticPattern = request.geneticPattern
            description = request.description
            imageUrl = request.imageUrl
        }
        return VariantResponse.from(variant)
    }

    @Transactional
    fun deleteVariant(speciesId: Long, variantId: Long) {
        val variant = variantRepository.findById(variantId)
            .orElseThrow { NotFoundException("Variant", variantId) }
        if (variant.species.id != speciesId) throw NotFoundException("Variant", variantId)
        variantRepository.delete(variant)
    }

    // ── 동물 사육 가이드(AnimalCareGuide) ─────────────────────────

    @Transactional
    fun upsertAnimalCareGuide(speciesId: Long, request: AnimalCareGuideUpsertRequest): AnimalCareGuideResponse {
        val species = speciesRepository.findById(speciesId)
            .orElseThrow { NotFoundException("Species", speciesId) }
        val guide = animalCareGuideRepository.findBySpeciesId(speciesId)
            ?: animalCareGuideRepository.save(AnimalCareGuide(species = species))
        guide.apply {
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
        return AnimalCareGuideResponse.from(guide)
    }

    // ── 식물 재배 가이드(PlantCareGuide) ──────────────────────────

    @Transactional
    fun upsertPlantCareGuide(speciesId: Long, request: PlantCareGuideUpsertRequest): PlantCareGuideResponse {
        val species = speciesRepository.findById(speciesId)
            .orElseThrow { NotFoundException("Species", speciesId) }
        val guide = plantCareGuideRepository.findBySpeciesId(speciesId)
            ?: plantCareGuideRepository.save(PlantCareGuide(species = species))
        guide.apply {
            potType = request.potType
            growingMedium = request.growingMedium
            lightRequirement = request.lightRequirement
            lightHoursPerDay = request.lightHoursPerDay
            tempMin = request.tempMin
            tempMax = request.tempMax
            humidityMin = request.humidityMin
            humidityMax = request.humidityMax
            wateringFrequency = request.wateringFrequency
            wateringMethod = request.wateringMethod
            fertilizerType = request.fertilizerType
            fertilizerFrequency = request.fertilizerFrequency
            repottingNote = request.repottingNote
            pruningNote = request.pruningNote
            overallNote = request.overallNote
            updatedAt = LocalDateTime.now()
        }
        return PlantCareGuideResponse.from(guide)
    }
}
