"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { getToken, decodeTokenPayload } from "@/lib/auth";

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

export default function PostActionButtons({
  postId,
  authorEmail,
}: {
  postId: number;
  authorEmail: string;
}) {
  const router = useRouter();
  const [isAuthor, setIsAuthor] = useState(false);
  const [deleting, setDeleting] = useState(false);

  useEffect(() => {
    const token = getToken();
    if (!token) return;
    const payload = decodeTokenPayload(token);
    setIsAuthor(payload.sub === authorEmail);
  }, [authorEmail]);

  if (!isAuthor) return null;

  async function handleDelete() {
    if (!confirm("게시글을 삭제하시겠습니까?")) return;
    const token = getToken();
    setDeleting(true);
    try {
      const res = await fetch(`${API_URL}/api/v1/posts/${postId}`, {
        method: "DELETE",
        headers: { Authorization: `Bearer ${token ?? ""}` },
      });
      if (res.ok) router.push("/community");
    } finally {
      setDeleting(false);
    }
  }

  return (
    <div className="flex gap-2">
      <button
        onClick={() => router.push(`/community/${postId}/edit`)}
        className="px-3 py-1 text-xs text-gray-500 border border-gray-300 rounded-lg hover:border-green-400 hover:text-green-700 transition-colors"
      >
        수정
      </button>
      <button
        onClick={handleDelete}
        disabled={deleting}
        className="px-3 py-1 text-xs text-red-500 border border-red-200 rounded-lg hover:bg-red-50 disabled:opacity-40 transition-colors"
      >
        {deleting ? "삭제 중..." : "삭제"}
      </button>
    </div>
  );
}
