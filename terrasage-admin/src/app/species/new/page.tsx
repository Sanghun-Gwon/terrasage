import Link from "next/link";
import SpeciesForm from "@/components/SpeciesForm";

export default function NewSpeciesPage() {
  return (
    <div>
      <Link href="/species" className="text-sm text-gray-500 hover:text-gray-800 mb-6 inline-block">
        ← 목록으로
      </Link>
      <h1 className="text-2xl font-bold text-gray-900 mb-8">새 종 등록</h1>
      <SpeciesForm />
    </div>
  );
}
