"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import { getToken } from "@/lib/auth";
import type { Gender } from "@/types/care";
import { GENDER_LABEL } from "@/types/care";

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";
const GENDERS: Gender[] = ["MALE", "FEMALE", "UNKNOWN"];

export default function NewAnimalPage() {
  const router = useRouter();
  const [name, setName] = useState("");
  const [nickname, setNickname] = useState("");
  const [speciesName, setSpeciesName] = useState("");
  const [birthDate, setBirthDate] = useState("");
  const [gender, setGender] = useState<Gender>("UNKNOWN");
  const [notes, setNotes] = useState("");
  const [isPublic, setIsPublic] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    const token = getToken();
    if (!token) { router.push("/login"); return; }

    setSubmitting(true);
    setError(null);
    try {
      const body: Record<string, unknown> = {
        name: name.trim(),
        gender,
        isPublic,
      };
      if (nickname.trim()) body.nickname = nickname.trim();
      if (speciesName.trim()) body.speciesName = speciesName.trim();
      if (birthDate) body.birthDate = birthDate;
      if (notes.trim()) body.notes = notes.trim();

      const res = await fetch(`${API_URL}/api/v1/animals`, {
        method: "POST",
        headers: { "Content-Type": "application/json", Authorization: `Bearer ${token}` },
        body: JSON.stringify(body),
      });
      const json = await res.json();
      if (!json.success) throw new Error(json.error?.message ?? "등록 실패");
      router.push(`/care/${json.data.id}`);
    } catch (e) {
      setError(e instanceof Error ? e.message : "등록 실패");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="max-w-2xl mx-auto">
      <div className="flex items-center gap-3 mb-6">
        <Link href="/care" className="text-sm text-gray-400 hover:text-green-700 transition-colors">
          ← 개체 목록
        </Link>
        <h1 className="text-xl font-bold text-gray-900">개체 등록</h1>
      </div>

      <form onSubmit={handleSubmit} className="bg-white border border-gray-200 rounded-2xl p-6 space-y-5">
        {error && (
          <div className="bg-red-50 border border-red-200 text-red-700 px-3 py-2 rounded-lg text-sm">{error}</div>
        )}

        <Field label="관리명" required hint="예: 레오게코 #1">
          <input
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
            maxLength={50}
            placeholder="개체를 식별할 이름"
            className={inputCls}
          />
        </Field>

        <Field label="애칭" hint="예: 콩이, 레오">
          <input
            type="text"
            value={nickname}
            onChange={(e) => setNickname(e.target.value)}
            maxLength={50}
            placeholder="별명 (선택)"
            className={inputCls}
          />
        </Field>

        <Field label="종명" hint="백과사전 미등록 종 직접 입력">
          <input
            type="text"
            value={speciesName}
            onChange={(e) => setSpeciesName(e.target.value)}
            maxLength={100}
            placeholder="예: 레오파드 게코, 크레스티드 게코"
            className={inputCls}
          />
        </Field>

        <Field label="성별">
          <div className="flex gap-2">
            {GENDERS.map((g) => (
              <button
                key={g}
                type="button"
                onClick={() => setGender(g)}
                className={`flex-1 py-2 rounded-lg text-sm font-medium border transition-colors ${
                  gender === g
                    ? "bg-green-700 text-white border-green-700"
                    : "bg-white text-gray-600 border-gray-300 hover:border-green-400"
                }`}
              >
                {GENDER_LABEL[g]}
              </button>
            ))}
          </div>
        </Field>

        <Field label="생년월일">
          <input
            type="date"
            value={birthDate}
            onChange={(e) => setBirthDate(e.target.value)}
            max={new Date().toISOString().split("T")[0]}
            className={inputCls}
          />
        </Field>

        <Field label="메모">
          <textarea
            value={notes}
            onChange={(e) => setNotes(e.target.value)}
            rows={3}
            placeholder="특이사항, 구입처 등 자유롭게 기록"
            className={`${inputCls} resize-none`}
          />
        </Field>

        <label className="flex items-center gap-2 cursor-pointer">
          <input
            type="checkbox"
            checked={isPublic}
            onChange={(e) => setIsPublic(e.target.checked)}
            className="w-4 h-4 rounded accent-green-700"
          />
          <span className="text-sm text-gray-700">사육 기록 공개 (커뮤니티 공유)</span>
        </label>

        <div className="flex gap-3 pt-2">
          <Link
            href="/care"
            className="flex-1 text-center py-2.5 border border-gray-300 rounded-lg text-sm text-gray-600 hover:border-gray-400 transition-colors"
          >
            취소
          </Link>
          <button
            type="submit"
            disabled={submitting || !name.trim()}
            className="flex-1 py-2.5 bg-green-700 text-white rounded-lg text-sm font-medium hover:bg-green-800 disabled:opacity-40 transition-colors"
          >
            {submitting ? "등록 중..." : "개체 등록"}
          </button>
        </div>
      </form>
    </div>
  );
}

const inputCls = "w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-500";

function Field({ label, required, hint, children }: {
  label: string; required?: boolean; hint?: string; children: React.ReactNode;
}) {
  return (
    <div className="space-y-1">
      <label className="text-sm font-medium text-gray-700">
        {label}
        {required && <span className="text-red-500 ml-0.5">*</span>}
        {hint && <span className="text-xs text-gray-400 font-normal ml-1">({hint})</span>}
      </label>
      {children}
    </div>
  );
}
