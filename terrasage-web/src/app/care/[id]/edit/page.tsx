"use client";

import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import Link from "next/link";
import { getToken } from "@/lib/auth";
import type { Gender } from "@/types/care";
import { GENDER_LABEL } from "@/types/care";

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";
const GENDERS: Gender[] = ["MALE", "FEMALE", "UNKNOWN"];

export default function EditAnimalPage() {
  const params = useParams<{ id: string }>();
  const router = useRouter();
  const animalId = Number(params.id);

  const [name, setName] = useState("");
  const [nickname, setNickname] = useState("");
  const [speciesName, setSpeciesName] = useState("");
  const [birthDate, setBirthDate] = useState("");
  const [gender, setGender] = useState<Gender>("UNKNOWN");
  const [notes, setNotes] = useState("");
  const [isPublic, setIsPublic] = useState(false);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const token = getToken();
    if (!token) { router.push("/login"); return; }

    fetch(`${API_URL}/api/v1/animals/${animalId}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((r) => r.json())
      .then((json) => {
        if (!json.success) { router.push("/care"); return; }
        const a = json.data;
        setName(a.name);
        setNickname(a.nickname ?? "");
        setSpeciesName(a.speciesId ? "" : (a.speciesName ?? ""));
        setBirthDate(a.birthDate ?? "");
        setGender(a.gender);
        setNotes(a.notes ?? "");
        setIsPublic(a.isPublic);
      })
      .finally(() => setLoading(false));
  }, [animalId, router]);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    const token = getToken();
    if (!token) { router.push("/login"); return; }

    setSubmitting(true);
    setError(null);
    try {
      const body: Record<string, unknown> = { name: name.trim(), gender, isPublic };
      if (nickname.trim()) body.nickname = nickname.trim();
      if (speciesName.trim()) body.speciesName = speciesName.trim();
      if (birthDate) body.birthDate = birthDate;
      if (notes.trim()) body.notes = notes.trim();

      const res = await fetch(`${API_URL}/api/v1/animals/${animalId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json", Authorization: `Bearer ${token}` },
        body: JSON.stringify(body),
      });
      const json = await res.json();
      if (!json.success) throw new Error(json.error?.message ?? "수정 실패");
      router.push(`/care/${animalId}`);
    } catch (e) {
      setError(e instanceof Error ? e.message : "수정 실패");
    } finally {
      setSubmitting(false);
    }
  }

  if (loading) {
    return <div className="py-16 text-center text-sm text-gray-400">불러오는 중...</div>;
  }

  return (
    <div className="max-w-2xl mx-auto">
      <div className="flex items-center gap-3 mb-6">
        <Link href={`/care/${animalId}`} className="text-sm text-gray-400 hover:text-green-700 transition-colors">
          ← 돌아가기
        </Link>
        <h1 className="text-xl font-bold text-gray-900">개체 수정</h1>
      </div>

      <form onSubmit={handleSubmit} className="bg-white border border-gray-200 rounded-2xl p-6 space-y-5">
        {error && (
          <div className="bg-red-50 border border-red-200 text-red-700 px-3 py-2 rounded-lg text-sm">{error}</div>
        )}

        <Field label="관리명" required>
          <input type="text" value={name} onChange={(e) => setName(e.target.value)} required maxLength={50} className={inputCls} />
        </Field>

        <Field label="애칭">
          <input type="text" value={nickname} onChange={(e) => setNickname(e.target.value)} maxLength={50} placeholder="별명 (선택)" className={inputCls} />
        </Field>

        <Field label="종명">
          <input type="text" value={speciesName} onChange={(e) => setSpeciesName(e.target.value)} maxLength={100} className={inputCls} />
        </Field>

        <Field label="성별">
          <div className="flex gap-2">
            {GENDERS.map((g) => (
              <button key={g} type="button" onClick={() => setGender(g)}
                className={`flex-1 py-2 rounded-lg text-sm font-medium border transition-colors ${
                  gender === g ? "bg-green-700 text-white border-green-700" : "bg-white text-gray-600 border-gray-300 hover:border-green-400"
                }`}
              >
                {GENDER_LABEL[g]}
              </button>
            ))}
          </div>
        </Field>

        <Field label="생년월일">
          <input type="date" value={birthDate} onChange={(e) => setBirthDate(e.target.value)} max={new Date().toISOString().split("T")[0]} className={inputCls} />
        </Field>

        <Field label="메모">
          <textarea value={notes} onChange={(e) => setNotes(e.target.value)} rows={3} className={`${inputCls} resize-none`} />
        </Field>

        <label className="flex items-center gap-2 cursor-pointer">
          <input type="checkbox" checked={isPublic} onChange={(e) => setIsPublic(e.target.checked)} className="w-4 h-4 rounded accent-green-700" />
          <span className="text-sm text-gray-700">사육 기록 공개</span>
        </label>

        <div className="flex gap-3 pt-2">
          <Link href={`/care/${animalId}`} className="flex-1 text-center py-2.5 border border-gray-300 rounded-lg text-sm text-gray-600 hover:border-gray-400 transition-colors">
            취소
          </Link>
          <button type="submit" disabled={submitting || !name.trim()}
            className="flex-1 py-2.5 bg-green-700 text-white rounded-lg text-sm font-medium hover:bg-green-800 disabled:opacity-40 transition-colors"
          >
            {submitting ? "수정 중..." : "수정 완료"}
          </button>
        </div>
      </form>
    </div>
  );
}

const inputCls = "w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-500";

function Field({ label, required, children }: { label: string; required?: boolean; children: React.ReactNode }) {
  return (
    <div className="space-y-1">
      <label className="text-sm font-medium text-gray-700">
        {label}{required && <span className="text-red-500 ml-0.5">*</span>}
      </label>
      {children}
    </div>
  );
}
