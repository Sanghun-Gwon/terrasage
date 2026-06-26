export type DifficultyLevel = "BEGINNER" | "INTERMEDIATE" | "ADVANCED";
export type CitesLevel = "APPENDIX_I" | "APPENDIX_II" | "APPENDIX_III";
export type SpeciesStatus = "DRAFT" | "PUBLISHED" | "ARCHIVED";
export type HandlingLevel = "EASY" | "MODERATE" | "DIFFICULT" | "EXPERT_ONLY";
export type GeneticPattern = "DOMINANT" | "RECESSIVE" | "CO_DOMINANT" | "LINE_BRED";

export interface SpeciesListItem {
  id: number;
  scientificName: string;
  commonNameKo: string;
  commonNameEn: string | null;
  thumbnailUrl: string | null;
  difficultyLevel: DifficultyLevel;
  family: string;
  status: SpeciesStatus;
}

export interface CareGuide {
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

export interface Morph {
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
  citesLevel: CitesLevel | null;
  legalStatusNote: string | null;
  thumbnailUrl: string | null;
  status: SpeciesStatus;
  careGuide: CareGuide | null;
  morphs: Morph[];
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
