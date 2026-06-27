"use client";

import { useState } from "react";
import { getToken } from "@/lib/auth";
import { useRouter } from "next/navigation";

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

export default function LikeButton({ postId, initialCount }: { postId: number; initialCount: number }) {
  const router = useRouter();
  const [count, setCount] = useState(initialCount);
  const [liked, setLiked] = useState(false);
  const [loading, setLoading] = useState(false);

  async function toggle() {
    const token = getToken();
    if (!token) { router.push("/login"); return; }
    setLoading(true);
    try {
      const res = await fetch(`${API_URL}/api/v1/posts/${postId}/likes`, {
        method: "POST",
        headers: { Authorization: `Bearer ${token}` },
      });
      const json = await res.json();
      const nowLiked: boolean = json.data.liked;
      setLiked(nowLiked);
      setCount((c) => (nowLiked ? c + 1 : c - 1));
    } finally {
      setLoading(false);
    }
  }

  return (
    <button
      onClick={toggle}
      disabled={loading}
      className={`flex items-center gap-1.5 px-4 py-2 rounded-full text-sm font-medium border transition-colors ${
        liked
          ? "bg-red-50 border-red-300 text-red-600"
          : "bg-white border-gray-300 text-gray-600 hover:border-red-300 hover:text-red-500"
      }`}
    >
      <span>{liked ? "❤️" : "🤍"}</span>
      <span>{count}</span>
    </button>
  );
}
