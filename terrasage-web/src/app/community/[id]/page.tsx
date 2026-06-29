export const dynamic = "force-dynamic";

import Link from "next/link";
import { notFound } from "next/navigation";
import { getPostDetail } from "@/lib/api";
import { BOARD_LABEL, BOARD_COLOR } from "@/types/community";
import LikeButton from "./_components/LikeButton";
import CommentSection from "./_components/CommentSection";
import PostActionButtons from "./_components/PostActionButtons";

function relativeTime(dateStr: string) {
  const diff = Date.now() - new Date(dateStr).getTime();
  const m = Math.floor(diff / 60000);
  if (m < 60) return `${m}분 전`;
  const h = Math.floor(m / 60);
  if (h < 24) return `${h}시간 전`;
  const d = Math.floor(h / 24);
  return d < 7
    ? `${d}일 전`
    : new Date(dateStr).toLocaleDateString("ko-KR", { year: "numeric", month: "short", day: "numeric" });
}

export default async function PostDetailPage({ params }: { params: Promise<{ id: string }> }) {
  const { id } = await params;
  let post;
  try {
    post = await getPostDetail(Number(id));
  } catch {
    notFound();
  }

  return (
    <div className="max-w-2xl mx-auto">
      {/* 뒤로가기 */}
      <Link href="/community" className="text-sm text-gray-400 hover:text-green-700 transition-colors mb-4 inline-block">
        ← 커뮤니티로
      </Link>

      {/* 게시글 카드 */}
      <article className="bg-white border border-gray-200 rounded-2xl p-6 mb-4">
        {/* 게시판 배지 + 날짜 */}
        <div className="flex items-center justify-between mb-3">
          <span className={`text-xs font-medium px-2.5 py-1 rounded-full ${BOARD_COLOR[post.boardType]}`}>
            {BOARD_LABEL[post.boardType]}
          </span>
          <span className="text-xs text-gray-400">{relativeTime(post.createdAt)}</span>
        </div>

        {/* 제목 */}
        <h1 className="text-xl font-bold text-gray-900 mb-2">{post.title}</h1>

        {/* 작성자 + 수정/삭제 버튼 */}
        <div className="flex items-center justify-between mb-5">
          <p className="text-sm text-gray-500">{post.authorName}</p>
          <PostActionButtons postId={post.id} authorEmail={post.authorEmail} />
        </div>

        {/* 이미지 (있을 때만) */}
        {post.imageUrls.length > 0 && (
          <div className={`grid gap-2 mb-5 ${post.imageUrls.length === 1 ? "grid-cols-1" : "grid-cols-2"}`}>
            {post.imageUrls.map((url, i) => (
              // eslint-disable-next-line @next/next/no-img-element
              <img
                key={i}
                src={url}
                alt=""
                className="w-full rounded-xl object-cover max-h-80"
              />
            ))}
          </div>
        )}

        {/* 본문 */}
        <div className="text-sm text-gray-800 leading-relaxed whitespace-pre-line border-t border-gray-100 pt-4">
          {post.content}
        </div>

        {/* 좋아요 */}
        <div className="mt-6 pt-4 border-t border-gray-100 flex">
          <LikeButton postId={post.id} initialCount={post.likeCount} />
        </div>
      </article>

      {/* 댓글 */}
      <div className="bg-white border border-gray-200 rounded-2xl p-6">
        <CommentSection postId={post.id} initialComments={post.comments} />
      </div>
    </div>
  );
}
