export const dynamic = "force-dynamic";

import Link from "next/link";
import { getPostList } from "@/lib/api";
import type { BoardType } from "@/types/community";
import { BOARD_LABEL, BOARD_COLOR } from "@/types/community";

const ALL_BOARDS: { value: BoardType | "ALL"; label: string }[] = [
  { value: "ALL", label: "전체" },
  { value: "SHOWCASE", label: "개체 자랑" },
  { value: "TIPS", label: "사육팁" },
  { value: "MORPH", label: "모프/변이" },
  { value: "QNA", label: "질문/답변" },
  { value: "FREE", label: "자유" },
];

function relativeTime(dateStr: string) {
  const diff = Date.now() - new Date(dateStr).getTime();
  const m = Math.floor(diff / 60000);
  if (m < 60) return `${m}분 전`;
  const h = Math.floor(m / 60);
  if (h < 24) return `${h}시간 전`;
  const d = Math.floor(h / 24);
  if (d < 7) return `${d}일 전`;
  return new Date(dateStr).toLocaleDateString("ko-KR", { month: "short", day: "numeric" });
}

export default async function CommunityPage({
  searchParams,
}: {
  searchParams: Promise<{ board?: string }>;
}) {
  const { board } = await searchParams;
  const boardType = (board && board !== "ALL" ? board : undefined) as BoardType | undefined;
  const data = await getPostList(boardType, 0, 30);

  return (
    <div>
      {/* 헤더 */}
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">커뮤니티</h1>
          <p className="text-sm text-gray-500 mt-0.5">사육자들과 정보를 나눠보세요</p>
        </div>
        <Link
          href="/community/new"
          className="bg-green-700 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-green-800 transition-colors"
        >
          글쓰기
        </Link>
      </div>

      {/* 게시판 탭 */}
      <div className="flex gap-1 mb-6 overflow-x-auto pb-1">
        {ALL_BOARDS.map((b) => {
          const isActive = (board ?? "ALL") === b.value;
          return (
            <Link
              key={b.value}
              href={`/community?board=${b.value}`}
              className={`px-3 py-1.5 rounded-full text-sm font-medium whitespace-nowrap transition-colors ${
                isActive
                  ? "bg-green-700 text-white"
                  : "bg-gray-100 text-gray-600 hover:bg-gray-200"
              }`}
            >
              {b.label}
            </Link>
          );
        })}
      </div>

      {/* 게시글 목록 */}
      {data.content.length === 0 ? (
        <div className="text-center py-20 text-gray-400">
          <p className="text-lg mb-2">아직 게시글이 없습니다</p>
          <p className="text-sm">첫 번째 글을 작성해보세요!</p>
        </div>
      ) : (
        <div className="space-y-2">
          {data.content.map((post) => (
            <Link
              key={post.id}
              href={`/community/${post.id}`}
              className="flex items-start gap-4 bg-white border border-gray-200 rounded-xl p-4 hover:border-green-400 hover:shadow-sm transition-all group"
            >
              {/* 썸네일 (SHOWCASE만) */}
              {post.thumbnailUrl && (
                // eslint-disable-next-line @next/next/no-img-element
                <img
                  src={post.thumbnailUrl}
                  alt=""
                  className="w-16 h-16 rounded-lg object-cover flex-shrink-0 bg-gray-100"
                />
              )}

              <div className="flex-1 min-w-0">
                <div className="flex items-center gap-2 mb-1">
                  <span className={`text-xs font-medium px-2 py-0.5 rounded-full ${BOARD_COLOR[post.boardType]}`}>
                    {BOARD_LABEL[post.boardType]}
                  </span>
                </div>
                <p className="font-medium text-gray-900 group-hover:text-green-700 transition-colors truncate">
                  {post.title}
                </p>
                <div className="flex items-center gap-3 mt-1.5 text-xs text-gray-400">
                  <span>{post.authorName}</span>
                  <span>·</span>
                  <span>{relativeTime(post.createdAt)}</span>
                  <span>·</span>
                  <span>❤️ {post.likeCount}</span>
                  <span>·</span>
                  <span>💬 {post.commentCount}</span>
                </div>
              </div>
            </Link>
          ))}
        </div>
      )}
    </div>
  );
}
