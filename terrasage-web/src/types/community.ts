export type BoardType = "SHOWCASE" | "TIPS" | "MORPH" | "QNA" | "FREE";

export const BOARD_LABEL: Record<BoardType, string> = {
  SHOWCASE: "개체 자랑",
  TIPS: "사육팁",
  MORPH: "모프/변이",
  QNA: "질문/답변",
  FREE: "자유",
};

export const BOARD_COLOR: Record<BoardType, string> = {
  SHOWCASE: "bg-violet-100 text-violet-700",
  TIPS: "bg-green-100 text-green-700",
  MORPH: "bg-blue-100 text-blue-700",
  QNA: "bg-orange-100 text-orange-700",
  FREE: "bg-gray-100 text-gray-600",
};

export interface PostListItem {
  id: number;
  boardType: BoardType;
  title: string;
  authorName: string;
  thumbnailUrl: string | null;
  likeCount: number;
  commentCount: number;
  createdAt: string;
}

export interface CommentItem {
  id: number;
  content: string;
  authorName: string;
  parentId: number | null;
  createdAt: string;
}

export interface PostDetail {
  id: number;
  boardType: BoardType;
  title: string;
  content: string;
  imageUrls: string[];
  authorName: string;
  authorEmail: string;
  likeCount: number;
  comments: CommentItem[];
  createdAt: string;
  updatedAt: string;
}
