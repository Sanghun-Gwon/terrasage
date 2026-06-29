"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { getToken } from "@/lib/auth";
import type { Animal } from "@/types/care";
import { GENDER_LABEL } from "@/types/care";

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

export default function CarePage() {
  const router = useRouter();
  const [animals, setAnimals] = useState<Animal[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = getToken();
    if (!token) { router.push("/login"); return; }

    fetch(`${API_URL}/api/v1/animals`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((r) => r.json())
      .then((json) => { if (json.success) setAnimals(json.data); })
      .finally(() => setLoading(false));
  }, [router]);

  if (loading) {
    return <div className="py-16 text-center text-sm text-gray-400">불러오는 중...</div>;
  }

  return (
    <div className="max-w-2xl mx-auto">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-xl font-bold text-gray-900">내 개체 관리</h1>
        <Link
          href="/care/new"
          className="px-4 py-2 bg-green-700 text-white text-sm rounded-lg hover:bg-green-800 transition-colors"
        >
          + 개체 등록
        </Link>
      </div>

      {animals.length === 0 ? (
        <div className="bg-white border border-gray-200 rounded-2xl p-12 text-center">
          <p className="text-gray-400 text-sm mb-4">등록된 개체가 없습니다</p>
          <Link href="/care/new" className="text-green-700 text-sm hover:underline">
            첫 개체 등록하기 →
          </Link>
        </div>
      ) : (
        <div className="space-y-3">
          {animals.map((animal) => (
            <Link
              key={animal.id}
              href={`/care/${animal.id}`}
              className="block bg-white border border-gray-200 rounded-2xl p-5 hover:border-green-300 transition-colors"
            >
              <div className="flex items-start justify-between">
                <div>
                  <div className="flex items-center gap-2 mb-1">
                    <span className="font-semibold text-gray-900">{animal.name}</span>
                    {animal.nickname && (
                      <span className="text-sm text-gray-400">({animal.nickname})</span>
                    )}
                  </div>
                  <p className="text-sm text-gray-500">{animal.speciesName}</p>
                </div>
                <div className="text-right text-xs text-gray-400 space-y-1">
                  <div>{GENDER_LABEL[animal.gender]}</div>
                  {animal.birthDate && <div>{animal.birthDate}</div>}
                </div>
              </div>
            </Link>
          ))}
        </div>
      )}
    </div>
  );
}
