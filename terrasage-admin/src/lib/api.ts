import type {
  ApiResponse,
  PageResponse,
  SpeciesDetail,
  SpeciesListItem,
} from "@/types/species";

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

// [Security 연동 시]
// 모든 admin API 요청에 Authorization 헤더 추가 필요:
//   headers: { Authorization: `Bearer ${getAccessToken()}` }
// getAccessToken()은 쿠키 또는 메모리에서 JWT를 읽어오는 함수로 구현

export async function getSpeciesList(page = 0, size = 20): Promise<PageResponse<SpeciesListItem>> {
  // 관리자 전용 엔드포인트 — 모든 상태(DRAFT/PUBLISHED/ARCHIVED) 조회
  const res = await fetch(`${API_URL}/api/v1/admin/species?page=${page}&size=${size}`);
  const json: ApiResponse<PageResponse<SpeciesListItem>> = await res.json();
  if (!json.success || !json.data) throw new Error(json.error?.message ?? "API error");
  return json.data;
}

export async function getSpeciesDetail(id: number): Promise<SpeciesDetail> {
  const res = await fetch(`${API_URL}/api/v1/species/${id}`);
  const json: ApiResponse<SpeciesDetail> = await res.json();
  if (!json.success || !json.data) throw new Error(json.error?.message ?? "API error");
  return json.data;
}

export type SpeciesFormData = {
  scientificName: string;
  commonNameKo: string;
  commonNameEn?: string;
  kingdom: string;
  phylum: string;
  taxonomyClass: string;
  taxonomyOrder: string;
  family: string;
  genus: string;
  origin?: string;
  habitat?: string;
  lifespanCaptive?: number;
  lifespanWild?: number;
  avgSizeCm?: number;
  avgWeightG?: number;
  difficultyLevel: string;
  category?: string;
  citesLevel?: string;
  legalStatusNote?: string;
  thumbnailUrl?: string;
  status?: string;
};

export async function createSpecies(data: SpeciesFormData): Promise<SpeciesDetail> {
  const res = await fetch(`${API_URL}/api/v1/admin/species`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  const json: ApiResponse<SpeciesDetail> = await res.json();
  if (!json.success || !json.data) throw new Error(json.error?.message ?? "등록 실패");
  return json.data;
}

export async function updateSpecies(id: number, data: SpeciesFormData & { status: string }): Promise<SpeciesDetail> {
  const res = await fetch(`${API_URL}/api/v1/admin/species/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  const json: ApiResponse<SpeciesDetail> = await res.json();
  if (!json.success || !json.data) throw new Error(json.error?.message ?? "수정 실패");
  return json.data;
}

export async function deleteSpecies(id: number): Promise<void> {
  const res = await fetch(`${API_URL}/api/v1/admin/species/${id}`, {
    method: "DELETE",
  });
  if (!res.ok) throw new Error("삭제 실패");
}
