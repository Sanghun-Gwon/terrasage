// API 서버에서 실시간 데이터를 가져오므로 빌드 시 prerender 하지 않음
export const dynamic = "force-dynamic";

import Link from "next/link";
import { getSpeciesList } from "@/lib/api";
import type { DifficultyLevel } from "@/types/species";

const DIFFICULTY_LABEL: Record<DifficultyLevel, string> = {
  BEGINNER: "입문",
  INTERMEDIATE: "중급",
  ADVANCED: "고급",
};

const DIFFICULTY_COLOR: Record<DifficultyLevel, string> = {
  BEGINNER: "bg-green-100 text-green-700",
  INTERMEDIATE: "bg-yellow-100 text-yellow-700",
  ADVANCED: "bg-red-100 text-red-700",
};

export default async function HomePage() {
  const page = await getSpeciesList({ size: 6 });

  return (
    <div>
      {/* 히어로 */}
      <section className="text-center py-16">
        <h1 className="text-4xl font-bold text-gray-900 mb-4">생물 백과사전</h1>
        <p className="text-lg text-gray-500 mb-8">파충류·양서류 사육 정보를 한곳에서</p>
        <Link
          href="/species"
          className="inline-block bg-green-700 text-white px-6 py-3 rounded-lg font-medium hover:bg-green-800 transition-colors"
        >
          전체 종 보기
        </Link>
      </section>

      {/* 최신 등록 종 */}
      <section>
        <h2 className="text-xl font-semibold mb-6">등록된 종</h2>
        <div className="grid grid-cols-2 md:grid-cols-3 gap-4">
          {page.content.map((species) => (
            <Link
              key={species.id}
              href={`/species/${species.id}`}
              className="border border-gray-200 rounded-xl p-4 hover:border-green-400 hover:shadow-sm transition-all"
            >
              <div className="aspect-square bg-gray-100 rounded-lg mb-3 flex items-center justify-center text-4xl">
                🦎
              </div>
              <p className="font-medium text-gray-900 truncate">{species.commonNameKo}</p>
              <p className="text-xs text-gray-400 italic truncate mb-2">{species.scientificName}</p>
              <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${DIFFICULTY_COLOR[species.difficultyLevel]}`}>
                {DIFFICULTY_LABEL[species.difficultyLevel]}
              </span>
            </Link>
          ))}
        </div>
        <div className="text-center mt-8">
          <Link href="/species" className="text-sm text-green-700 hover:underline">
            전체 {page.totalElements}종 보기 →
          </Link>
        </div>
      </section>
    </div>
  );
}
