import { getToken } from "@/lib/auth";

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

interface UploadUrlResponse {
  uploadUrl: string;
  publicUrl: string;
}

// Signed URL 방식 업로드:
// 1) 서버에서 서명된 업로드 URL 발급 → 2) 브라우저가 GCS에 직접 PUT → 3) 영구 공개 URL 반환
export async function uploadImage(file: File): Promise<string> {
  const token = getToken();
  if (!token) throw new Error("로그인이 필요합니다");

  let res: Response;
  try {
    res = await fetch(`${API_URL}/api/v1/images/upload-url`, {
      method: "POST",
      headers: { "Content-Type": "application/json", Authorization: `Bearer ${token}` },
      body: JSON.stringify({ contentType: file.type }),
    });
  } catch {
    throw new Error("[1/2] 업로드 URL 발급 요청이 차단됨 — API 서버 연결 실패");
  }
  const json = await res.json();
  if (!json.success) throw new Error(json.error?.message ?? "업로드 URL 발급 실패");

  const { uploadUrl, publicUrl } = json.data as UploadUrlResponse;

  // Content-Type은 서명에 포함되어 있어 발급 요청과 동일한 값이어야 함
  let put: Response;
  try {
    put = await fetch(uploadUrl, {
      method: "PUT",
      headers: { "Content-Type": file.type },
      body: file,
    });
  } catch {
    throw new Error("[2/2] GCS 전송이 차단됨 — 네트워크/CORS 문제");
  }
  if (!put.ok) throw new Error(`이미지 업로드 실패 (HTTP ${put.status})`);

  return publicUrl;
}
