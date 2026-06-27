"use client";

import { useEffect, useState } from "react";
import { getToken, removeToken, decodeTokenPayload } from "@/lib/auth";
import { useRouter } from "next/navigation";

export default function HeaderUser() {
  const router = useRouter();
  const [name, setName] = useState<string | null>(null);

  useEffect(() => {
    const token = getToken();
    if (token) {
      const payload = decodeTokenPayload(token);
      setName(payload.sub as string ?? null);
    }
  }, []);

  function logout() {
    removeToken();
    router.push("/login");
  }

  if (!name) return null;

  return (
    <div className="flex items-center gap-3 text-sm">
      <span className="text-gray-600">{name}</span>
      <button
        onClick={logout}
        className="text-gray-400 hover:text-red-500 transition-colors"
      >
        로그아웃
      </button>
    </div>
  );
}
