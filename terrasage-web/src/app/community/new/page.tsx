"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import { getToken } from "@/lib/auth";
import ImageUploadField from "@/app/_components/ImageUploadField";
import type { BoardType } from "@/types/community";
import { BOARD_LABEL } from "@/types/community";

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

const BOARDS: BoardType[] = ["SHOWCASE", "TIPS", "MORPH", "QNA", "FREE"];

export default function NewPostPage() {
  const router = useRouter();
  const [boardType, setBoardType] = useState<BoardType>("FREE");
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [imageUrls, setImageUrls] = useState<string[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    const token = getToken();
    if (!token) { router.push("/login"); return; }
    if (!title.trim() || !content.trim()) return;

    setSubmitting(true);
    setError(null);
    try {
      const res = await fetch(`${API_URL}/api/v1/posts`, {
        method: "POST",
        headers: { "Content-Type": "application/json", Authorization: `Bearer ${token}` },
        body: JSON.stringify({
          boardType,
          title: title.trim(),
          content: content.trim(),
          imageUrls: imageUrls.length > 0 ? imageUrls : null,
        }),
      });
      const json = await res.json();
      if (!json.success) throw new Error(json.error?.message ?? "등록 실패");
      router.push(`/community/${json.data.id}`);
    } catch (e) {
      setError(e instanceof Error ? e.message : "등록 실패");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="max-w-2xl mx-auto">
      <div className="flex items-center gap-3 mb-6">
        <Link href="/community" className="text-sm text-gray-400 hover:text-green-700 transition-colors">
          ← 커뮤니티로
        </Link>
        <h1 className="text-xl font-bold text-gray-900">글쓰기</h1>
      </div>

      <form onSubmit={handleSubmit} className="bg-white border border-gray-200 rounded-2xl p-6 space-y-5">
        {error && (
          <div className="bg-red-50 border border-red-200 text-red-700 px-3 py-2 rounded-lg text-sm">{error}</div>
        )}

        {/* 게시판 선택 */}
        <div>
          <label className="text-sm font-medium text-gray-700 mb-2 block">게시판</label>
          <div className="flex flex-wrap gap-2">
            {BOARDS.map((b) => (
              <button
                key={b}
                type="button"
                onClick={() => setBoardType(b)}
                className={`px-3 py-1.5 rounded-full text-sm font-medium transition-colors border ${
                  boardType === b
                    ? "bg-green-700 text-white border-green-700"
                    : "bg-white text-gray-600 border-gray-300 hover:border-green-400"
                }`}
              >
                {BOARD_LABEL[b]}
              </button>
            ))}
          </div>
        </div>

        {/* 제목 */}
        <div>
          <label className="text-sm font-medium text-gray-700 mb-1 block">제목</label>
          <input
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
            placeholder="제목을 입력하세요"
            className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
          />
        </div>

        {/* 본문 */}
        <div>
          <label className="text-sm font-medium text-gray-700 mb-1 block">내용</label>
          <textarea
            value={content}
            onChange={(e) => setContent(e.target.value)}
            required
            placeholder="내용을 입력하세요"
            rows={10}
            className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm resize-none focus:outline-none focus:ring-2 focus:ring-green-500"
          />
        </div>

        {/* 이미지 업로드 (SHOWCASE에서 강조) */}
        <ImageUploadField
          imageUrls={imageUrls}
          onChange={setImageUrls}
          hint={boardType === "SHOWCASE" ? "개체 자랑 게시글에 이미지를 추가해보세요" : undefined}
        />

        {/* 제출 */}
        <div className="flex gap-3 pt-2">
          <Link
            href="/community"
            className="flex-1 text-center py-2.5 border border-gray-300 rounded-lg text-sm text-gray-600 hover:border-gray-400 transition-colors"
          >
            취소
          </Link>
          <button
            type="submit"
            disabled={submitting || !title.trim() || !content.trim()}
            className="flex-1 py-2.5 bg-green-700 text-white rounded-lg text-sm font-medium hover:bg-green-800 disabled:opacity-40 transition-colors"
          >
            {submitting ? "등록 중..." : "게시글 등록"}
          </button>
        </div>
      </form>
    </div>
  );
}
