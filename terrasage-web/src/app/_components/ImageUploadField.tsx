"use client";

import { useRef, useState } from "react";
import { uploadImage } from "@/lib/upload";

interface Props {
  imageUrls: string[];
  onChange: (urls: string[]) => void;
  hint?: string;
}

// 파일 선택 → GCS 직접 업로드(Signed URL) + 외부 이미지 URL 직접 입력 겸용 필드
export default function ImageUploadField({ imageUrls, onChange, hint }: Props) {
  const fileInputRef = useRef<HTMLInputElement>(null);
  const [urlInput, setUrlInput] = useState("");
  const [uploading, setUploading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  function addUrl() {
    const url = urlInput.trim();
    if (url && !imageUrls.includes(url)) {
      onChange([...imageUrls, url]);
      setUrlInput("");
    }
  }

  async function handleFiles(e: React.ChangeEvent<HTMLInputElement>) {
    const files = Array.from(e.target.files ?? []);
    if (files.length === 0) return;

    setUploading(true);
    setError(null);
    try {
      const uploaded: string[] = [];
      for (const file of files) {
        uploaded.push(await uploadImage(file));
      }
      onChange([...imageUrls, ...uploaded]);
    } catch (err) {
      setError(err instanceof Error ? err.message : "이미지 업로드 실패");
    } finally {
      setUploading(false);
      if (fileInputRef.current) fileInputRef.current.value = "";
    }
  }

  return (
    <div>
      <label className="text-sm font-medium text-gray-700 mb-1 block">
        이미지
        {hint && <span className="ml-2 text-xs font-normal text-violet-600">{hint}</span>}
      </label>

      {error && (
        <div className="mb-2 bg-red-50 border border-red-200 text-red-700 px-3 py-2 rounded-lg text-xs">{error}</div>
      )}

      <div className="flex gap-2">
        <input
          ref={fileInputRef}
          type="file"
          accept="image/jpeg,image/png,image/webp,image/gif"
          multiple
          onChange={handleFiles}
          className="hidden"
        />
        <button
          type="button"
          onClick={() => fileInputRef.current?.click()}
          disabled={uploading}
          className="px-3 py-2 bg-green-50 border border-green-300 text-green-800 rounded-lg text-sm hover:bg-green-100 disabled:opacity-40 transition-colors"
        >
          {uploading ? "업로드 중..." : "사진 업로드"}
        </button>
        <input
          type="url"
          value={urlInput}
          onChange={(e) => setUrlInput(e.target.value)}
          onKeyDown={(e) => e.key === "Enter" && (e.preventDefault(), addUrl())}
          placeholder="또는 이미지 URL 직접 입력 (https://...)"
          className="flex-1 border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
        />
        <button
          type="button"
          onClick={addUrl}
          className="px-3 py-2 border border-gray-300 rounded-lg text-sm hover:border-green-400 transition-colors"
        >
          추가
        </button>
      </div>

      {imageUrls.length > 0 && (
        <div className="mt-2 flex flex-wrap gap-2">
          {imageUrls.map((url, i) => (
            <div
              key={i}
              className="flex items-center gap-2 bg-gray-50 border border-gray-200 rounded-lg px-2 py-1 text-xs text-gray-600"
            >
              {/* eslint-disable-next-line @next/next/no-img-element */}
              <img src={url} alt="" className="w-8 h-8 object-cover rounded" />
              <span className="truncate max-w-[160px]">{url}</span>
              <button
                type="button"
                onClick={() => onChange(imageUrls.filter((_, j) => j !== i))}
                className="text-gray-400 hover:text-red-500"
              >
                ✕
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
