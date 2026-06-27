export type DifficultyLevel = "BEGINNER" | "INTERMEDIATE" | "ADVANCED";
export type CitesLevel = "APPENDIX_I" | "APPENDIX_II" | "APPENDIX_III";
export type SpeciesStatus = "DRAFT" | "PUBLISHED" | "ARCHIVED";
export type HandlingLevel = "EASY" | "MODERATE" | "DIFFICULT" | "EXPERT_ONLY";
export type GeneticPattern =
  | "DOMINANT" | "RECESSIVE" | "CO_DOMINANT" | "LINE_BRED"
  | "CULTIVAR" | "VARIEGATED" | "HYBRID" | "SPORT";
export type SpeciesCategory =
  | "REPTILE" | "AMPHIBIAN" | "FISH" | "INVERTEBRATE" | "MAMMAL" | "BIRD"
  | "SUCCULENT" | "CACTUS" | "ORCHID" | "FOLIAGE" | "CARNIVOROUS_PLANT" | "AQUATIC_PLANT" | "BONSAI"
  | "OTHER";

export interface SpeciesListItem {
  id: number;
  scientificName: string;
  commonNameKo: string;
  commonNameEn: string | null;
  thumbnailUrl: string | null;
  category: SpeciesCategory | null;
  difficultyLevel: DifficultyLevel;
  family: string;
  status: SpeciesStatus;
}

export interface AnimalCareGuide {
  enclosureType: string | null;
  enclosureSizeCm: string | null;
  substrate: string | null;
  tempHotZone: number | null;
  tempCoolZone: number | null;
  tempNight: number | null;
  humidityMin: number | null;
  humidityMax: number | null;
  uvbRequired: boolean;
  photoperiodHours: number | null;
  feedType: string | null;
  feedFrequency: string | null;
  supplements: string | null;
  handlingLevel: HandlingLevel | null;
  cohabitationNote: string | null;
}

export interface PlantCareGuide {
  potType: string | null;
  growingMedium: string | null;
  lightRequirement: string | null;
  lightHoursPerDay: number | null;
  tempMin: number | null;
  tempMax: number | null;
  humidityMin: number | null;
  humidityMax: number | null;
  wateringFrequency: string | null;
  wateringMethod: string | null;
  fertilizerType: string | null;
  fertilizerFrequency: string | null;
  repottingNote: string | null;
  pruningNote: string | null;
  overallNote: string | null;
}

export interface Variant {
  id: number;
  name: string;
  geneticPattern: GeneticPattern;
  description: string | null;
  imageUrl: string | null;
}

export interface SpeciesDetail {
  id: number;
  scientificName: string;
  commonNameKo: string;
  commonNameEn: string | null;
  kingdom: string;
  phylum: string;
  taxonomyClass: string;
  taxonomyOrder: string;
  family: string;
  genus: string;
  origin: string | null;
  habitat: string | null;
  lifespanCaptive: number | null;
  lifespanWild: number | null;
  avgSizeCm: number | null;
  avgWeightG: number | null;
  difficultyLevel: DifficultyLevel;
  category: SpeciesCategory | null;
  citesLevel: CitesLevel | null;
  legalStatusNote: string | null;
  thumbnailUrl: string | null;
  status: SpeciesStatus;
  animalCareGuide: AnimalCareGuide | null;
  plantCareGuide: PlantCareGuide | null;
  variants: Variant[];
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

export interface ApiResponse<T> {
  success: boolean;
  data: T | null;
  error: { code: string; message: string } | null;
}
