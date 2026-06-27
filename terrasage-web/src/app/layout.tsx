import type { Metadata } from "next";
import { Geist } from "next/font/google";
import "./globals.css";
import HeaderUser from "./_components/HeaderUser";

const geist = Geist({ subsets: ["latin"], variable: "--font-geist" });

export const metadata: Metadata = {
  title: "TerraSage",
  description: "생물 사육 관리 플랫폼",
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="ko" className={`${geist.variable} h-full`}>
      <body className="min-h-full bg-gray-50 text-gray-900 antialiased">
        <header className="bg-white border-b border-gray-200">
          <div className="mx-auto max-w-6xl px-4 py-4 flex items-center justify-between">
            <div className="flex items-center gap-6">
              <a href="/" className="text-xl font-bold text-green-700">
                TerraSage
              </a>
              <nav className="hidden sm:flex gap-4 text-sm text-gray-600">
                <a href="/species" className="hover:text-green-700 transition-colors">백과사전</a>
                <a href="/community" className="hover:text-green-700 transition-colors">커뮤니티</a>
              </nav>
            </div>
            <HeaderUser />
          </div>
        </header>
        <main className="mx-auto max-w-6xl px-4 py-8">{children}</main>
        <footer className="border-t border-gray-200 mt-16 py-8 text-center text-sm text-gray-400">
          © 2026 TerraSage
        </footer>
      </body>
    </html>
  );
}
