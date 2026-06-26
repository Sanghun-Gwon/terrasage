export const dynamic = "force-dynamic";

import Link from "next/link";
import { notFound } from "next/navigation";
import { getSpeciesDetail } from "@/lib/api";
import SpeciesForm from "@/components/SpeciesForm";

export default async function EditSpeciesPage({
  params,
}: {
  params: Promise<{ id: string }>;
}) {
  const { id } = await params;
  let species;
  try {
    species = await getSpeciesDetail(Number(id));
  } catch {
    notFound();
  }

  return (
    <div>
      <Link href="/species" className="text-sm text-gray-500 hover:text-gray-800 mb-6 inline-block">
        ← 목록으로
      </Link>
      <div className="flex items-center gap-3 mb-8">
        <h1 className="text-2xl font-bold text-gray-900">종 수정</h1>
        <span className="text-gray-400 text-sm">— {species.commonNameKo}</span>
      </div>
      <SpeciesForm initial={species} />
    </div>
  );
}
