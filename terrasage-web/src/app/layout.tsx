import type { Metadata } from "next";
import { Geist } from "next/font/google";
import "./globals.css";

const geist = Geist({ subsets: ["latin"], variable: "--font-geist" });

export const metadata: Metadata = {
  title: "TerraSage — 생물 백과사전",
  description: "파충류·양서류 사육 정보 플랫폼",
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="ko" className={`${geist.variable} h-full`}>
      <body className="min-h-full bg-white text-gray-900 antialiased">
        <header className="border-b border-gray-200">
          <div className="mx-auto max-w-6xl px-4 py-4 flex items-center gap-6">
            <a href="/" className="text-xl font-bold text-green-700">
              🌿 TerraSage
            </a>
            <nav className="flex gap-4 text-sm text-gray-600">
              <a href="/species" className="hover:text-green-700">백과사전</a>
            </nav>
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
