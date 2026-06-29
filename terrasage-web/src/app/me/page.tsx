"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { getToken, removeToken } from "@/lib/auth";

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

type Profile = {
  email: string;
  name: string;
  phoneNumber: string | null;
  role: string;
  createdAt: string;
};

type Section = "info" | "profile" | "password";

export default function MyProfilePage() {
  const router = useRouter();
  const [profile, setProfile] = useState<Profile | null>(null);
  const [loading, setLoading] = useState(true);
  const [section, setSection] = useState<Section>("info");

  useEffect(() => {
    const token = getToken();
    if (!token) { router.push("/login"); return; }

    fetch(`${API_URL}/api/v1/me`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((r) => r.json())
      .then((json) => { if (json.success) setProfile(json.data); })
      .finally(() => setLoading(false));
  }, [router]);

  function handleLogout() {
    removeToken();
    router.push("/login");
  }

  if (loading || !profile) {
    return <div className="py-16 text-center text-sm text-gray-400">불러오는 중...</div>;
  }

  return (
    <div className="max-w-lg mx-auto">
      <h1 className="text-xl font-bold text-gray-900 mb-6">내 프로필</h1>

      {/* 기본 정보 카드 */}
      <div className="bg-white border border-gray-200 rounded-2xl p-6 mb-4">
        <div className="flex items-center justify-between mb-4">
          <div>
            <p className="text-lg font-semibold text-gray-900">{profile.name}</p>
            <p className="text-sm text-gray-500">{profile.email}</p>
          </div>
          <span className={`text-xs px-2.5 py-1 rounded-full font-medium ${
            profile.role === "ADMIN"
              ? "bg-red-100 text-red-700"
              : "bg-green-100 text-green-700"
          }`}>
            {profile.role === "ADMIN" ? "관리자" : "일반"}
          </span>
        </div>
        <div className="border-t border-gray-100 pt-4 space-y-2 text-sm">
          <Row label="전화번호" value={profile.phoneNumber ?? "미등록"} />
          <Row
            label="가입일"
            value={new Date(profile.createdAt).toLocaleDateString("ko-KR", {
              year: "numeric", month: "long", day: "numeric",
            })}
          />
        </div>
      </div>

      {/* 탭 메뉴 */}
      <div className="flex border border-gray-200 rounded-xl overflow-hidden mb-4">
        {(["profile", "password"] as const).map((s) => (
          <button
            key={s}
            onClick={() => setSection((prev) => (prev === s ? "info" : s))}
            className={`flex-1 py-2.5 text-sm font-medium transition-colors ${
              section === s
                ? "bg-green-700 text-white"
                : "bg-white text-gray-600 hover:bg-gray-50"
            }`}
          >
            {s === "profile" ? "정보 수정" : "비밀번호 변경"}
          </button>
        ))}
      </div>

      {/* 정보 수정 폼 */}
      {section === "profile" && (
        <UpdateProfileForm
          current={profile}
          onUpdated={(p) => { setProfile(p); setSection("info"); }}
        />
      )}

      {/* 비밀번호 변경 폼 */}
      {section === "password" && (
        <ChangePasswordForm onDone={() => setSection("info")} />
      )}

      {/* 로그아웃 */}
      <button
        onClick={handleLogout}
        className="w-full mt-6 py-2.5 border border-red-200 text-red-500 text-sm rounded-xl hover:bg-red-50 transition-colors"
      >
        로그아웃
      </button>
    </div>
  );
}

// ── 정보 수정 폼 ──────────────────────────────────────────────────────────────

function UpdateProfileForm({
  current,
  onUpdated,
}: {
  current: Profile;
  onUpdated: (p: Profile) => void;
}) {
  const router = useRouter();
  const [name, setName] = useState(current.name);
  const [phoneNumber, setPhoneNumber] = useState(
    current.phoneNumber ? current.phoneNumber.replace(/\*+/, "").replace(/--$/, "") : ""
  );
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    const token = getToken();
    if (!token) { router.push("/login"); return; }

    setSubmitting(true);
    setError(null);
    try {
      const body: Record<string, string> = { name: name.trim() };
      if (phoneNumber.trim()) body.phoneNumber = phoneNumber.trim();

      const res = await fetch(`${API_URL}/api/v1/me`, {
        method: "PUT",
        headers: { "Content-Type": "application/json", Authorization: `Bearer ${token}` },
        body: JSON.stringify(body),
      });
      const json = await res.json();
      if (!json.success) throw new Error(json.error?.message ?? "수정 실패");
      onUpdated(json.data);
    } catch (e) {
      setError(e instanceof Error ? e.message : "수정 실패");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <form onSubmit={handleSubmit} className="bg-white border border-gray-200 rounded-2xl p-6 space-y-4">
      {error && (
        <div className="bg-red-50 border border-red-200 text-red-700 px-3 py-2 rounded-lg text-sm">{error}</div>
      )}
      <div className="space-y-1">
        <label className="text-sm font-medium text-gray-700">닉네임 <span className="text-red-500">*</span></label>
        <input
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
          required
          maxLength={20}
          className={inputCls}
        />
      </div>
      <div className="space-y-1">
        <label className="text-sm font-medium text-gray-700">
          전화번호
          <span className="text-xs text-gray-400 font-normal ml-1">(거래 시 연락처)</span>
        </label>
        <input
          type="tel"
          value={phoneNumber}
          onChange={(e) => setPhoneNumber(e.target.value)}
          placeholder="010-1234-5678"
          className={inputCls}
        />
        <p className="text-xs text-gray-400">마스킹 표시됩니다. 변경 시 새 번호를 입력하세요.</p>
      </div>
      <button
        type="submit"
        disabled={submitting || !name.trim()}
        className="w-full py-2.5 bg-green-700 text-white text-sm rounded-lg hover:bg-green-800 disabled:opacity-40 transition-colors"
      >
        {submitting ? "저장 중..." : "저장"}
      </button>
    </form>
  );
}

// ── 비밀번호 변경 폼 ──────────────────────────────────────────────────────────

function ChangePasswordForm({ onDone }: { onDone: () => void }) {
  const router = useRouter();
  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    if (newPassword !== confirmPassword) { setError("새 비밀번호가 일치하지 않습니다."); return; }
    const token = getToken();
    if (!token) { router.push("/login"); return; }

    setSubmitting(true);
    setError(null);
    try {
      const res = await fetch(`${API_URL}/api/v1/me/password`, {
        method: "PUT",
        headers: { "Content-Type": "application/json", Authorization: `Bearer ${token}` },
        body: JSON.stringify({ currentPassword, newPassword }),
      });
      if (res.status === 204) { onDone(); return; }
      const json = await res.json();
      throw new Error(json.error?.message ?? "변경 실패");
    } catch (e) {
      setError(e instanceof Error ? e.message : "변경 실패");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <form onSubmit={handleSubmit} className="bg-white border border-gray-200 rounded-2xl p-6 space-y-4">
      {error && (
        <div className="bg-red-50 border border-red-200 text-red-700 px-3 py-2 rounded-lg text-sm">{error}</div>
      )}
      <div className="space-y-1">
        <label className="text-sm font-medium text-gray-700">현재 비밀번호</label>
        <input type="password" value={currentPassword} onChange={(e) => setCurrentPassword(e.target.value)} required className={inputCls} />
      </div>
      <div className="space-y-1">
        <label className="text-sm font-medium text-gray-700">새 비밀번호 <span className="text-xs text-gray-400 font-normal">(8자 이상)</span></label>
        <input type="password" value={newPassword} onChange={(e) => setNewPassword(e.target.value)} required minLength={8} className={inputCls} />
      </div>
      <div className="space-y-1">
        <label className="text-sm font-medium text-gray-700">새 비밀번호 확인</label>
        <input type="password" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} required className={inputCls} />
        {confirmPassword && newPassword !== confirmPassword && (
          <p className="text-xs text-red-500">비밀번호가 일치하지 않습니다.</p>
        )}
      </div>
      <button
        type="submit"
        disabled={submitting || !currentPassword || !newPassword || newPassword !== confirmPassword}
        className="w-full py-2.5 bg-green-700 text-white text-sm rounded-lg hover:bg-green-800 disabled:opacity-40 transition-colors"
      >
        {submitting ? "변경 중..." : "비밀번호 변경"}
      </button>
    </form>
  );
}

// ── 공통 ──────────────────────────────────────────────────────────────────────

const inputCls = "w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-500";

function Row({ label, value }: { label: string; value: string }) {
  return (
    <div className="flex justify-between">
      <span className="text-gray-500">{label}</span>
      <span className="text-gray-800">{value}</span>
    </div>
  );
}
