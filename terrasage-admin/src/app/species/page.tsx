export const dynamic = "force-dynamic";

import Link from "next/link";
import { getSpeciesList } from "@/lib/api";
import { DeleteButton } from "./DeleteButton";
import type { DifficultyLevel, SpeciesStatus } from "@/types/species";

const DIFFICULTY_LABEL: Record<DifficultyLevel, string> = {
  BEGINNER: "입문",
  INTERMEDIATE: "중급",
  ADVANCED: "고급",
};

const STATUS_STYLE: Record<SpeciesStatus, string> = {
  DRAFT: "bg-gray-100 text-gray-600",
  PUBLISHED: "bg-green-100 text-green-700",
  ARCHIVED: "bg-red-100 text-red-600",
};

const STATUS_LABEL: Record<SpeciesStatus, string> = {
  DRAFT: "초안",
  PUBLISHED: "공개",
  ARCHIVED: "보관",
};

export default async function AdminSpeciesPage() {
  const data = await getSpeciesList(0, 100);

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">종 관리</h1>
          <p className="text-sm text-gray-500 mt-1">총 {data.totalElements}종</p>
        </div>
        <Link
          href="/species/new"
          className="bg-green-700 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-green-800 transition-colors"
        >
          + 새 종 등록
        </Link>
      </div>

      <div className="bg-white rounded-xl border border-gray-200 overflow-hidden">
        <table className="w-full text-sm">
          <thead className="bg-gray-50 border-b border-gray-200">
            <tr>
              <th className="text-left px-4 py-3 text-gray-500 font-medium w-8">ID</th>
              <th className="text-left px-4 py-3 text-gray-500 font-medium">한글명</th>
              <th className="text-left px-4 py-3 text-gray-500 font-medium hidden md:table-cell">학명</th>
              <th className="text-left px-4 py-3 text-gray-500 font-medium hidden md:table-cell">과</th>
              <th className="text-left px-4 py-3 text-gray-500 font-medium">난이도</th>
              <th className="text-left px-4 py-3 text-gray-500 font-medium">상태</th>
              <th className="text-right px-4 py-3 text-gray-500 font-medium">관리</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {data.content.map((species) => (
              <tr key={species.id} className="hover:bg-gray-50 transition-colors">
                <td className="px-4 py-3 text-gray-400">{species.id}</td>
                <td className="px-4 py-3 font-medium text-gray-900">{species.commonNameKo}</td>
                <td className="px-4 py-3 text-gray-500 italic hidden md:table-cell">{species.scientificName}</td>
                <td className="px-4 py-3 text-gray-500 hidden md:table-cell">{species.family}</td>
                <td className="px-4 py-3">
                  <span className="text-xs px-2 py-0.5 rounded-full bg-gray-100 text-gray-600">
                    {DIFFICULTY_LABEL[species.difficultyLevel]}
                  </span>
                </td>
                <td className="px-4 py-3">
                  <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${STATUS_STYLE[species.status]}`}>
                    {STATUS_LABEL[species.status]}
                  </span>
                </td>
                <td className="px-4 py-3 text-right">
                  <div className="flex items-center justify-end gap-2">
                    <Link
                      href={`/species/${species.id}/edit`}
                      className="text-xs text-blue-600 hover:underline"
                    >
                      수정
                    </Link>
                    <DeleteButton id={species.id} name={species.commonNameKo} />
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
