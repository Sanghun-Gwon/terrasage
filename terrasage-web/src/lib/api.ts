import type {
  ApiResponse,
  PageResponse,
  SpeciesDetail,
  SpeciesListItem,
  DifficultyLevel,
  SpeciesStatus,
} from "@/types/species";

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

export interface SpeciesSearchParams {
  keyword?: string;
  taxonomyClass?: string;
  family?: string;
  difficultyLevel?: DifficultyLevel;
  status?: SpeciesStatus;
  page?: number;
  size?: number;
}

export async function getSpeciesList(
  params: SpeciesSearchParams = {}
): Promise<PageResponse<SpeciesListItem>> {
  const query = new URLSearchParams();
  if (params.keyword) query.set("keyword", params.keyword);
  if (params.taxonomyClass) query.set("taxonomyClass", params.taxonomyClass);
  if (params.family) query.set("family", params.family);
  if (params.difficultyLevel) query.set("difficultyLevel", params.difficultyLevel);
  if (params.status) query.set("status", params.status);
  query.set("page", String(params.page ?? 0));
  query.set("size", String(params.size ?? 12));

  const res = await fetch(`${API_URL}/api/v1/species?${query}`);
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
