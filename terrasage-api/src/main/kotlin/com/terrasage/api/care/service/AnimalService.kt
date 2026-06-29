package com.terrasage.api.care.service

import com.terrasage.api.auth.repository.UserRepository
import com.terrasage.api.care.dto.*
import com.terrasage.api.care.entity.Animal
import com.terrasage.api.care.entity.CareRecord
import com.terrasage.api.care.repository.AnimalRepository
import com.terrasage.api.care.repository.CareRecordRepository
import com.terrasage.api.common.exception.ForbiddenException
import com.terrasage.api.common.exception.NotFoundException
import com.terrasage.api.encyclopedia.repository.SpeciesRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AnimalService(
    private val animalRepository: AnimalRepository,
    private val careRecordRepository: CareRecordRepository,
    private val userRepository: UserRepository,
    private val speciesRepository: SpeciesRepository,
) {

    @Transactional(readOnly = true)
    fun getMyAnimals(email: String): List<AnimalResponse> {
        val user = userRepository.findByEmail(email) ?: throw NotFoundException("User", email)
        return animalRepository.findAllByOwnerIdOrderByCreatedAtDesc(user.id)
            .map { AnimalResponse.from(it) }
    }

    @Transactional(readOnly = true)
    fun getAnimal(id: Long, email: String): AnimalResponse {
        val animal = findAnimalOrThrow(id)
        if (!animal.isPublic && animal.owner.email != email) throw ForbiddenException()
        return AnimalResponse.from(animal)
    }

    @Transactional
    fun createAnimal(request: AnimalCreateRequest, email: String): AnimalResponse {
        val user = userRepository.findByEmail(email) ?: throw NotFoundException("User", email)
        val species = request.speciesId?.let {
            speciesRepository.findById(it).orElseThrow { NotFoundException("Species", it) }
        }
        val animal = Animal(
            owner = user,
            species = species,
            speciesName = if (species == null) request.speciesName else null,
            name = request.name,
            nickname = request.nickname,
            birthDate = request.birthDate,
            gender = request.gender,
            notes = request.notes,
            isPublic = request.isPublic,
        )
        return AnimalResponse.from(animalRepository.save(animal))
    }

    @Transactional
    fun updateAnimal(id: Long, request: AnimalUpdateRequest, email: String): AnimalResponse {
        val animal = findAnimalOrThrow(id)
        checkOwner(animal, email)

        val species = request.speciesId?.let {
            speciesRepository.findById(it).orElseThrow { NotFoundException("Species", it) }
        }
        animal.species = species
        animal.speciesName = if (species == null) request.speciesName else null
        animal.name = request.name
        animal.nickname = request.nickname
        animal.birthDate = request.birthDate
        animal.gender = request.gender
        animal.notes = request.notes
        animal.isPublic = request.isPublic
        animal.updatedAt = java.time.LocalDateTime.now()

        return AnimalResponse.from(animal)
    }

    @Transactional
    fun deleteAnimal(id: Long, email: String) {
        val animal = findAnimalOrThrow(id)
        checkOwner(animal, email)
        animalRepository.delete(animal)
    }

    // ── CareRecord ──────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    fun getRecords(animalId: Long, email: String): List<CareRecordResponse> {
        val animal = findAnimalOrThrow(animalId)
        if (!animal.isPublic && animal.owner.email != email) throw ForbiddenException()
        return careRecordRepository.findTop100ByAnimalIdOrderByRecordedAtDesc(animalId)
            .map { CareRecordResponse.from(it) }
    }

    @Transactional
    fun addRecord(animalId: Long, request: CareRecordCreateRequest, email: String): CareRecordResponse {
        val animal = findAnimalOrThrow(animalId)
        checkOwner(animal, email)
        val record = CareRecord(
            animal = animal,
            recordedAt = request.recordedAt,
            temperature = request.temperature,
            humidity = request.humidity,
            lightHours = request.lightHours,
            weight = request.weight,
            feedType = request.feedType,
            feedAmount = request.feedAmount,
            notes = request.notes,
        )
        return CareRecordResponse.from(careRecordRepository.save(record))
    }

    @Transactional
    fun deleteRecord(animalId: Long, recordId: Long, email: String) {
        val animal = findAnimalOrThrow(animalId)
        checkOwner(animal, email)
        if (!careRecordRepository.existsByIdAndAnimalId(recordId, animalId))
            throw NotFoundException("CareRecord", recordId)
        careRecordRepository.deleteById(recordId)
    }

    // ── private ─────────────────────────────────────────────────────────────

    private fun findAnimalOrThrow(id: Long) =
        animalRepository.findById(id).orElseThrow { NotFoundException("Animal", id) }

    private fun checkOwner(animal: Animal, email: String) {
        if (animal.owner.email != email) throw ForbiddenException()
    }
}
