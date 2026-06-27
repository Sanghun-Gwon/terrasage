import type {
  ApiResponse,
  PageResponse,
  SpeciesDetail,
  SpeciesListItem,
} from "@/types/species";

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

async function adminHeaders(): Promise<Record<string, string>> {
  const headers: Record<string, string> = { "Content-Type": "application/json" };
  let token: string | undefined;

  if (typeof window === "undefined") {
    // 서버 컴포넌트 — next/headers 쿠키에서 읽기
    const { cookies } = await import("next/headers");
    const store = await cookies();
    token = store.get("terrasage_token")?.value;
  } else {
    // 클라이언트 컴포넌트 — 브라우저 쿠키에서 읽기
    const { getToken } = await import("@/lib/auth");
    token = getToken() ?? undefined;
  }

  if (token) headers["Authorization"] = `Bearer ${token}`;
  return headers;
}

async function handleResponse<T>(res: Response): Promise<T> {
  if (res.status === 401) {
    if (typeof window === "undefined") {
      const { redirect } = await import("next/navigation");
      redirect("/login");
    } else {
      window.location.href = "/login";
      throw new Error("로그인이 필요합니다");
    }
  }
  const json: ApiResponse<T> = await res.json();
  if (!json.success || !json.data) throw new Error(json.error?.message ?? "API error");
  return json.data as T;
}

export async function getSpeciesList(page = 0, size = 20): Promise<PageResponse<SpeciesListItem>> {
  const res = await fetch(`${API_URL}/api/v1/admin/species?page=${page}&size=${size}`, {
    headers: await adminHeaders(),
    cache: "no-store",
  });
  return handleResponse(res);
}

export async function getSpeciesDetail(id: number): Promise<SpeciesDetail> {
  const res = await fetch(`${API_URL}/api/v1/species/${id}`, {
    headers: await adminHeaders(),
    cache: "no-store",
  });
  return handleResponse(res);
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
    headers: await adminHeaders(),
    body: JSON.stringify(data),
  });
  return handleResponse(res);
}

export async function updateSpecies(id: number, data: SpeciesFormData & { status: string }): Promise<SpeciesDetail> {
  const res = await fetch(`${API_URL}/api/v1/admin/species/${id}`, {
    method: "PUT",
    headers: await adminHeaders(),
    body: JSON.stringify(data),
  });
  return handleResponse(res);
}

export async function deleteSpecies(id: number): Promise<void> {
  const res = await fetch(`${API_URL}/api/v1/admin/species/${id}`, {
    method: "DELETE",
    headers: await adminHeaders(),
  });
  if (!res.ok) throw new Error("삭제 실패");
}
