import type { Metadata } from "next";
import { Geist } from "next/font/google";
import Link from "next/link";
import "./globals.css";

// [Security 연동 시]
// 이 layout에서 세션/토큰 검증 후 미인증 시 /login으로 redirect:
//   import { auth } from "@/lib/auth"
//   const session = await auth()
//   if (!session) redirect("/login")

const geist = Geist({ subsets: ["latin"], variable: "--font-geist" });

export const metadata: Metadata = {
  title: "TerraSage Admin",
  description: "TerraSage 관리자 페이지",
};

export default function AdminLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="ko" className={`${geist.variable} h-full`}>
      <body className="min-h-full bg-gray-50 antialiased">
        <div className="flex min-h-screen">
          {/* 사이드바 */}
          <aside className="w-56 bg-gray-900 text-gray-300 flex flex-col shrink-0">
            <div className="px-4 py-5 border-b border-gray-700">
              <p className="text-white font-bold text-lg">🌿 TerraSage</p>
              <p className="text-xs text-gray-500 mt-0.5">Admin</p>
            </div>
            <nav className="flex-1 px-3 py-4 space-y-1 text-sm">
              <Link
                href="/species"
                className="flex items-center gap-2 px-3 py-2 rounded-lg hover:bg-gray-800 hover:text-white transition-colors"
              >
                🦎 종 관리
              </Link>
            </nav>
            {/* [Security 연동 시] 로그아웃 버튼 추가:
              <button onClick={() => signOut()}>로그아웃</button>
            */}
          </aside>
          {/* 메인 콘텐츠 */}
          <main className="flex-1 p-8 overflow-auto">{children}</main>
        </div>
      </body>
    </html>
  );
}
