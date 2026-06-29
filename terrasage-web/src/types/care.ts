export type Gender = "MALE" | "FEMALE" | "UNKNOWN";

export const GENDER_LABEL: Record<Gender, string> = {
  MALE: "수컷",
  FEMALE: "암컷",
  UNKNOWN: "미확인",
};

export interface Animal {
  id: number;
  speciesId: number | null;
  speciesName: string;
  name: string;
  nickname: string | null;
  birthDate: string | null;
  gender: Gender;
  notes: string | null;
  isPublic: boolean;
  createdAt: string;
}

export interface CareRecord {
  id: number;
  recordedAt: string;
  temperature: number | null;
  humidity: number | null;
  lightHours: number | null;
  weight: number | null;
  feedType: string | null;
  feedAmount: string | null;
  notes: string | null;
  createdAt: string;
}
