"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

type FormState = {
  email: string;
  password: string;
  passwordConfirm: string;
  name: string;
  phoneNumber: string;
  agreeTerms: boolean;
  agreePrivacy: boolean;
  agreeMarketing: boolean;
};

const INITIAL: FormState = {
  email: "",
  password: "",
  passwordConfirm: "",
  name: "",
  phoneNumber: "",
  agreeTerms: false,
  agreePrivacy: false,
  agreeMarketing: false,
};

export default function SignupPage() {
  const router = useRouter();
  const [form, setForm] = useState<FormState>(INITIAL);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  function set<K extends keyof FormState>(key: K, value: FormState[K]) {
    setForm((prev) => ({ ...prev, [key]: value }));
  }

  function validate(): string | null {
    if (!form.email) return "이메일을 입력해주세요.";
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) return "올바른 이메일 형식이 아닙니다.";
    if (form.password.length < 8) return "비밀번호는 8자 이상이어야 합니다.";
    if (form.password !== form.passwordConfirm) return "비밀번호가 일치하지 않습니다.";
    if (!form.name.trim()) return "닉네임을 입력해주세요.";
    if (form.name.length > 20) return "닉네임은 20자 이하여야 합니다.";
    if (form.phoneNumber && !/^01[016789][- ]?(\d{3,4})[- ]?(\d{4})$/.test(form.phoneNumber)) {
      return "올바른 전화번호 형식이 아닙니다. (예: 010-1234-5678)";
    }
    if (!form.agreeTerms) return "이용약관에 동의해주세요.";
    if (!form.agreePrivacy) return "개인정보 처리방침에 동의해주세요.";
    return null;
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    const err = validate();
    if (err) { setError(err); return; }

    setLoading(true);
    setError(null);
    try {
      const body: Record<string, string> = {
        email: form.email,
        password: form.password,
        name: form.name.trim(),
      };
      if (form.phoneNumber) body.phoneNumber = form.phoneNumber.replace(/[- ]/g, "");

      const res = await fetch(`${API_URL}/api/v1/auth/signup`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body),
      });
      const json = await res.json();
      if (!json.success) throw new Error(json.error?.message ?? "회원가입 실패");

      router.push("/login?registered=1");
    } catch (e) {
      setError(e instanceof Error ? e.message : "회원가입 중 오류가 발생했습니다.");
    } finally {
      setLoading(false);
    }
  }

  const allRequired = form.agreeTerms && form.agreePrivacy;

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 py-12">
      <div className="w-full max-w-md">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-green-700">TerraSage</h1>
          <p className="text-gray-500 mt-2 text-sm">회원가입</p>
        </div>

        <div className="bg-white rounded-2xl shadow p-8">
          <form onSubmit={handleSubmit} className="space-y-5">
            {error && (
              <div className="bg-red-50 border border-red-200 text-red-700 px-3 py-2 rounded-lg text-sm">
                {error}
              </div>
            )}

            {/* 이메일 */}
            <Field label="이메일" required>
              <input
                type="email"
                value={form.email}
                onChange={(e) => set("email", e.target.value)}
                placeholder="이메일을 입력하세요"
                className={inputCls}
              />
            </Field>

            {/* 비밀번호 */}
            <Field label="비밀번호" required hint="8자 이상">
              <input
                type="password"
                value={form.password}
                onChange={(e) => set("password", e.target.value)}
                placeholder="비밀번호를 입력하세요"
                className={inputCls}
              />
            </Field>

            {/* 비밀번호 확인 */}
            <Field label="비밀번호 확인" required>
              <input
                type="password"
                value={form.passwordConfirm}
                onChange={(e) => set("passwordConfirm", e.target.value)}
                placeholder="비밀번호를 다시 입력하세요"
                className={inputCls}
              />
              {form.passwordConfirm && form.password !== form.passwordConfirm && (
                <p className="text-xs text-red-500 mt-1">비밀번호가 일치하지 않습니다.</p>
              )}
            </Field>

            {/* 닉네임 */}
            <Field label="닉네임" required hint="20자 이하, 커뮤니티에 표시됩니다">
              <input
                type="text"
                value={form.name}
                onChange={(e) => set("name", e.target.value)}
                placeholder="닉네임을 입력하세요"
                maxLength={20}
                className={inputCls}
              />
            </Field>

            {/* 전화번호 */}
            <Field label="전화번호" hint="선택 · 거래 시 연락처로 사용됩니다">
              <input
                type="tel"
                value={form.phoneNumber}
                onChange={(e) => set("phoneNumber", e.target.value)}
                placeholder="010-1234-5678"
                className={inputCls}
              />
            </Field>

            {/* 구분선 */}
            <hr className="border-gray-100" />

            {/* 약관 전체 동의 */}
            <div className="space-y-3">
              <label className="flex items-center gap-2 cursor-pointer">
                <input
                  type="checkbox"
                  checked={allRequired && form.agreeMarketing}
                  onChange={(e) =>
                    setForm((prev) => ({
                      ...prev,
                      agreeTerms: e.target.checked,
                      agreePrivacy: e.target.checked,
                      agreeMarketing: e.target.checked,
                    }))
                  }
                  className="w-4 h-4 rounded accent-green-700"
                />
                <span className="text-sm font-medium text-gray-800">전체 동의</span>
              </label>

              <div className="ml-6 space-y-2 border-l-2 border-gray-100 pl-4">
                <AgreeRow
                  label="이용약관 동의"
                  required
                  checked={form.agreeTerms}
                  onChange={(v) => set("agreeTerms", v)}
                  href="/terms"
                />
                <AgreeRow
                  label="개인정보 처리방침 동의"
                  required
                  checked={form.agreePrivacy}
                  onChange={(v) => set("agreePrivacy", v)}
                  href="/privacy"
                />
                <AgreeRow
                  label="마케팅 정보 수신 동의"
                  checked={form.agreeMarketing}
                  onChange={(v) => set("agreeMarketing", v)}
                />
              </div>
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full bg-green-700 text-white py-2.5 rounded-lg text-sm font-medium hover:bg-green-800 disabled:opacity-50 transition-colors mt-2"
            >
              {loading ? "처리 중..." : "회원가입"}
            </button>
          </form>

          <p className="text-center text-xs text-gray-400 mt-6">
            이미 계정이 있으신가요?{" "}
            <a href="/login" className="text-green-700 hover:underline">
              로그인
            </a>
          </p>
        </div>
      </div>
    </div>
  );
}

// ── 공통 스타일 ────────────────────────────────────────────────────────────

const inputCls =
  "w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-500";

function Field({
  label,
  required,
  hint,
  children,
}: {
  label: string;
  required?: boolean;
  hint?: string;
  children: React.ReactNode;
}) {
  return (
    <div className="space-y-1">
      <label className="text-sm text-gray-600">
        {label}
        {required && <span className="text-red-500 ml-0.5">*</span>}
        {hint && <span className="text-xs text-gray-400 ml-1">({hint})</span>}
      </label>
      {children}
    </div>
  );
}

function AgreeRow({
  label,
  required,
  checked,
  onChange,
  href,
}: {
  label: string;
  required?: boolean;
  checked: boolean;
  onChange: (v: boolean) => void;
  href?: string;
}) {
  return (
    <div className="flex items-center justify-between">
      <label className="flex items-center gap-2 cursor-pointer">
        <input
          type="checkbox"
          checked={checked}
          onChange={(e) => onChange(e.target.checked)}
          className="w-4 h-4 rounded accent-green-700"
        />
        <span className="text-sm text-gray-700">
          {required && <span className="text-red-500 mr-1">[필수]</span>}
          {!required && <span className="text-gray-400 mr-1">[선택]</span>}
          {label}
        </span>
      </label>
      {href && (
        <a
          href={href}
          target="_blank"
          className="text-xs text-gray-400 hover:text-gray-600 underline shrink-0"
        >
          보기
        </a>
      )}
    </div>
  );
}
