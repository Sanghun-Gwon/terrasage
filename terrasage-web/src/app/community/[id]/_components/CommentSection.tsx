"use client";

import { useState } from "react";
import { getToken } from "@/lib/auth";
import { useRouter } from "next/navigation";
import type { CommentItem } from "@/types/community";

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

function relativeTime(dateStr: string) {
  const diff = Date.now() - new Date(dateStr).getTime();
  const m = Math.floor(diff / 60000);
  if (m < 60) return `${m}분 전`;
  const h = Math.floor(m / 60);
  if (h < 24) return `${h}시간 전`;
  const d = Math.floor(h / 24);
  return d < 7 ? `${d}일 전` : new Date(dateStr).toLocaleDateString("ko-KR", { month: "short", day: "numeric" });
}

export default function CommentSection({
  postId,
  initialComments,
}: {
  postId: number;
  initialComments: CommentItem[];
}) {
  const router = useRouter();
  const [comments, setComments] = useState(initialComments);
  const [content, setContent] = useState("");
  const [replyTo, setReplyTo] = useState<{ id: number; authorName: string } | null>(null);
  const [submitting, setSubmitting] = useState(false);

  async function submit(e: React.FormEvent) {
    e.preventDefault();
    const token = getToken();
    if (!token) { router.push("/login"); return; }
    if (!content.trim()) return;

    setSubmitting(true);
    try {
      const res = await fetch(`${API_URL}/api/v1/posts/${postId}/comments`, {
        method: "POST",
        headers: { "Content-Type": "application/json", Authorization: `Bearer ${token}` },
        body: JSON.stringify({ content: content.trim(), parentId: replyTo?.id ?? null }),
      });
      const json = await res.json();
      if (json.success) {
        setComments((prev) => [...prev, json.data]);
        setContent("");
        setReplyTo(null);
        router.refresh();
      }
    } finally {
      setSubmitting(false);
    }
  }

  // 루트 댓글 + 자식 댓글 그룹핑
  const roots = comments.filter((c) => c.parentId === null);
  const childrenOf = (id: number) => comments.filter((c) => c.parentId === id);

  return (
    <div>
      <h2 className="text-base font-semibold text-gray-900 mb-4">
        댓글 <span className="text-gray-400 font-normal">{comments.length}</span>
      </h2>

      {/* 댓글 목록 */}
      <div className="space-y-4 mb-6">
        {roots.length === 0 && (
          <p className="text-sm text-gray-400 py-4 text-center">첫 댓글을 남겨보세요</p>
        )}
        {roots.map((comment) => (
          <div key={comment.id}>
            <CommentCard
              comment={comment}
              onReply={() => setReplyTo({ id: comment.id, authorName: comment.authorName })}
            />
            {/* 대댓글 */}
            {childrenOf(comment.id).map((child) => (
              <div key={child.id} className="ml-8 mt-2">
                <CommentCard comment={child} onReply={null} />
              </div>
            ))}
          </div>
        ))}
      </div>

      {/* 댓글 입력 */}
      <form onSubmit={submit} className="bg-gray-50 rounded-xl p-4 border border-gray-200">
        {replyTo && (
          <div className="flex items-center justify-between mb-2 text-xs text-gray-500">
            <span>↩ <strong>{replyTo.authorName}</strong>님에게 답글</span>
            <button type="button" onClick={() => setReplyTo(null)} className="hover:text-red-500">✕</button>
          </div>
        )}
        <textarea
          value={content}
          onChange={(e) => setContent(e.target.value)}
          placeholder="댓글을 입력하세요..."
          rows={3}
          className="w-full bg-white border border-gray-300 rounded-lg px-3 py-2 text-sm resize-none focus:outline-none focus:ring-2 focus:ring-green-500"
        />
        <div className="flex justify-end mt-2">
          <button
            type="submit"
            disabled={submitting || !content.trim()}
            className="px-4 py-1.5 bg-green-700 text-white text-sm rounded-lg hover:bg-green-800 disabled:opacity-40 transition-colors"
          >
            {submitting ? "등록 중..." : "댓글 등록"}
          </button>
        </div>
      </form>
    </div>
  );
}

function CommentCard({
  comment,
  onReply,
}: {
  comment: CommentItem;
  onReply: (() => void) | null;
}) {
  return (
    <div className="bg-white border border-gray-100 rounded-xl p-3">
      <div className="flex items-center justify-between mb-1.5">
        <span className="text-sm font-medium text-gray-800">{comment.authorName}</span>
        <span className="text-xs text-gray-400">{relativeTime(comment.createdAt)}</span>
      </div>
      <p className="text-sm text-gray-700 whitespace-pre-line">{comment.content}</p>
      {onReply && (
        <button
          onClick={onReply}
          className="mt-1.5 text-xs text-gray-400 hover:text-green-700 transition-colors"
        >
          답글달기
        </button>
      )}
    </div>
  );
}
