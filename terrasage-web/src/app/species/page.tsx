export const dynamic = "force-dynamic";

import Link from "next/link";
import { getSpeciesList } from "@/lib/api";
import type { DifficultyLevel, SpeciesListItem } from "@/types/species";

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

// searchParams는 Next.js App Router에서 서버 컴포넌트에 자동 주입됨
export default async function SpeciesListPage({
  searchParams,
}: {
  searchParams: Promise<{ keyword?: string; category?: string; difficultyLevel?: string; page?: string }>;
}) {
  const params = await searchParams;
  const currentPage = Number(params.page ?? 0);

  const data = await getSpeciesList({
    keyword: params.keyword,
    category: params.category,
    difficultyLevel: params.difficultyLevel as DifficultyLevel | undefined,
    page: currentPage,
    size: 12,
  });

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">생물 백과사전</h1>

      {/* 검색/필터 폼 — form action으로 GET 요청, JS 없이도 동작 */}
      <form method="GET" className="flex flex-wrap gap-3 mb-8">
        <input
          name="keyword"
          defaultValue={params.keyword}
          placeholder="학명, 한글명 검색..."
          className="border border-gray-300 rounded-lg px-3 py-2 text-sm w-56 focus:outline-none focus:ring-2 focus:ring-green-500"
        />
        <select
          name="category"
          defaultValue={params.category}
          className="border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
        >
          <option value="">전체 카테고리</option>
          <optgroup label="동물">
            <option value="REPTILE">🦎 파충류</option>
            <option value="AMPHIBIAN">🐸 양서류</option>
            <option value="FISH">🐟 어류</option>
          </optgroup>
          <optgroup label="식물">
            <option value="SUCCULENT">🌵 다육식물</option>
            <option value="CACTUS">🌵 선인장</option>
            <option value="ORCHID">🌸 난류</option>
            <option value="FOLIAGE">🌿 관엽식물</option>
            <option value="CARNIVOROUS_PLANT">🪲 식충식물</option>
            <option value="AQUATIC_PLANT">🌊 수생식물</option>
            <option value="BONSAI">🌳 분재</option>
          </optgroup>
        </select>
        <select
          name="difficultyLevel"
          defaultValue={params.difficultyLevel}
          className="border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
        >
          <option value="">전체 난이도</option>
          <option value="BEGINNER">입문</option>
          <option value="INTERMEDIATE">중급</option>
          <option value="ADVANCED">고급</option>
        </select>
        <button
          type="submit"
          className="bg-green-700 text-white px-4 py-2 rounded-lg text-sm hover:bg-green-800 transition-colors"
        >
          검색
        </button>
        {(params.keyword || params.category || params.difficultyLevel) && (
          <a href="/species" className="px-4 py-2 text-sm text-gray-500 hover:text-gray-800">
            초기화
          </a>
        )}
      </form>

      {/* 결과 수 */}
      <p className="text-sm text-gray-500 mb-4">총 {data.totalElements}종</p>

      {/* 종 목록 */}
      {data.content.length === 0 ? (
        <div className="text-center py-20 text-gray-400">검색 결과가 없습니다.</div>
      ) : (
        <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
          {data.content.map((species: SpeciesListItem) => (
            <Link
              key={species.id}
              href={`/species/${species.id}`}
              className="border border-gray-200 rounded-xl p-4 hover:border-green-400 hover:shadow-sm transition-all"
            >
              <div className="aspect-square bg-gray-100 rounded-lg mb-3 flex items-center justify-center text-3xl">
                🦎
              </div>
              <p className="font-medium text-sm text-gray-900 truncate">{species.commonNameKo}</p>
              <p className="text-xs text-gray-400 italic truncate mb-2">{species.scientificName}</p>
              <div className="flex items-center justify-between">
                <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${DIFFICULTY_COLOR[species.difficultyLevel]}`}>
                  {DIFFICULTY_LABEL[species.difficultyLevel]}
                </span>
                <span className="text-xs text-gray-400">{species.family}</span>
              </div>
            </Link>
          ))}
        </div>
      )}

      {/* 페이지네이션 */}
      {data.totalPages > 1 && (
        <div className="flex justify-center gap-2 mt-10">
          {Array.from({ length: data.totalPages }, (_, i) => (
            <a
              key={i}
              href={`?${new URLSearchParams({ ...params, page: String(i) })}`}
              className={`w-9 h-9 flex items-center justify-center rounded-lg text-sm border transition-colors ${
                i === currentPage
                  ? "bg-green-700 text-white border-green-700"
                  : "border-gray-300 text-gray-600 hover:border-green-400"
              }`}
            >
              {i + 1}
            </a>
          ))}
        </div>
      )}
    </div>
  );
}
