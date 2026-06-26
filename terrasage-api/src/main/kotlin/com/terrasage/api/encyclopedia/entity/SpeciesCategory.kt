package com.terrasage.api.encyclopedia.entity

enum class SpeciesCategory {
    // 동물
    REPTILE,            // 파충류
    AMPHIBIAN,          // 양서류
    FISH,               // 어류 (관상어 중심)
    INVERTEBRATE,       // 무척추동물
    MAMMAL,             // 포유류
    BIRD,               // 조류

    // 식물 (취미 원예계 표준 분류)
    SUCCULENT,          // 다육식물 (선인장 제외)
    CACTUS,             // 선인장
    ORCHID,             // 난류
    FOLIAGE,            // 관엽식물
    CARNIVOROUS_PLANT,  // 식충식물
    AQUATIC_PLANT,      // 수생식물 (수초 포함)
    BONSAI,             // 분재

    OTHER,
}
