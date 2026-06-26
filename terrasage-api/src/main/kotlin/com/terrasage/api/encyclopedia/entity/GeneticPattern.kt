package com.terrasage.api.encyclopedia.entity

enum class GeneticPattern {
    // 파충류 모프 유전 패턴
    DOMINANT,       // 우성 — 한 쌍 중 1개만 있어도 발현
    RECESSIVE,      // 열성 — 두 쌍 모두 있어야 발현 (het 캐리어 존재)
    CO_DOMINANT,    // 공우성 — 1개: 표현형 A, 2개: 슈퍼 표현형
    LINE_BRED,      // 라인브리드 — 유전자가 아닌 선발 육종으로 고정

    // 식물 변이 타입 (cultivar/variegata)
    CULTIVAR,       // 품종 — 인위 선발·명명된 원예 품종 (예: Echeveria 'Lola')
    VARIEGATED,     // 무늬종(바리에가타) — 색소 결핍으로 흰/노랑 무늬 발생
    HYBRID,         // 교잡종 — 이종 간 교배 (속간 교잡 포함)
    SPORT,          // 지변 — 자연 돌연변이 (가지 변이 등)
}
