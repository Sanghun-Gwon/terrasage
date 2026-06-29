"use client";

import { useEffect, useState } from "react";
import { getToken, decodeTokenPayload } from "@/lib/auth";
import Link from "next/link";

export default function HeaderUser() {
  const [name, setName] = useState<string | null>(null);

  useEffect(() => {
    const token = getToken();
    if (token) {
      const payload = decodeTokenPayload(token);
      setName(payload.sub as string ?? null);
    }
  }, []);

  if (!name) return null;

  return (
    <div className="flex items-center gap-3 text-sm">
      <Link href="/me" className="text-gray-600 hover:text-green-700 transition-colors">
        {name}
      </Link>
    </div>
  );
}
