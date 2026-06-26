export const dynamic = "force-dynamic";

import { notFound } from "next/navigation";
import Link from "next/link";
import { getSpeciesDetail } from "@/lib/api";
import type { DifficultyLevel, CitesLevel, GeneticPattern } from "@/types/species";

const DIFFICULTY_LABEL: Record<DifficultyLevel, string> = {
  BEGINNER: "입문",
  INTERMEDIATE: "중급",
  ADVANCED: "고급",
};

const CITES_LABEL: Record<CitesLevel, string> = {
  APPENDIX_I: "부속서 I (상업적 거래 금지)",
  APPENDIX_II: "부속서 II (허가 필요)",
  APPENDIX_III: "부속서 III (원산국 규제)",
};

const GENETIC_LABEL: Record<GeneticPattern, string> = {
  DOMINANT: "우성",
  RECESSIVE: "열성",
  CO_DOMINANT: "공우성",
  LINE_BRED: "라인브리드",
};

export default async function SpeciesDetailPage({
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

  const cg = species.careGuide;

  return (
    <div className="max-w-4xl mx-auto">
      <Link href="/species" className="text-sm text-green-700 hover:underline mb-6 inline-block">
        ← 목록으로
      </Link>

      {/* 헤더 */}
      <div className="flex gap-6 mb-10">
        <div className="w-40 h-40 bg-gray-100 rounded-2xl flex items-center justify-center text-6xl shrink-0">
          🦎
        </div>
        <div>
          <h1 className="text-3xl font-bold text-gray-900">{species.commonNameKo}</h1>
          {species.commonNameEn && (
            <p className="text-gray-500">{species.commonNameEn}</p>
          )}
          <p className="text-lg italic text-gray-400 mb-3">{species.scientificName}</p>
          <div className="flex gap-2 flex-wrap">
            <span className="text-xs bg-green-100 text-green-700 px-2 py-1 rounded-full font-medium">
              {DIFFICULTY_LABEL[species.difficultyLevel]}
            </span>
            {species.citesLevel && (
              <span className="text-xs bg-orange-100 text-orange-700 px-2 py-1 rounded-full font-medium">
                CITES {species.citesLevel.replace("APPENDIX_", "")}
              </span>
            )}
          </div>
        </div>
      </div>

      {/* 분류 정보 */}
      <section className="mb-8">
        <h2 className="text-lg font-semibold mb-3 border-b pb-2">분류</h2>
        <div className="grid grid-cols-3 md:grid-cols-6 gap-3 text-sm">
          {[
            ["계", species.kingdom],
            ["문", species.phylum],
            ["강", species.taxonomyClass],
            ["목", species.taxonomyOrder],
            ["과", species.family],
            ["속", species.genus],
          ].map(([label, value]) => (
            <div key={label} className="bg-gray-50 rounded-lg p-2 text-center">
              <p className="text-xs text-gray-400">{label}</p>
              <p className="font-medium text-gray-800">{value}</p>
            </div>
          ))}
        </div>
      </section>

      {/* 기본 정보 */}
      <section className="mb-8">
        <h2 className="text-lg font-semibold mb-3 border-b pb-2">기본 정보</h2>
        <dl className="grid grid-cols-2 md:grid-cols-3 gap-4 text-sm">
          {species.origin && <InfoItem label="원산지" value={species.origin} />}
          {species.habitat && <InfoItem label="서식지" value={species.habitat} />}
          {species.lifespanCaptive && <InfoItem label="수명(사육)" value={`${species.lifespanCaptive}년`} />}
          {species.lifespanWild && <InfoItem label="수명(야생)" value={`${species.lifespanWild}년`} />}
          {species.avgSizeCm && <InfoItem label="평균 크기" value={`${species.avgSizeCm}cm`} />}
          {species.avgWeightG && <InfoItem label="평균 무게" value={`${species.avgWeightG}g`} />}
        </dl>
        {species.citesLevel && (
          <div className="mt-4 p-3 bg-orange-50 rounded-lg text-sm text-orange-800">
            ⚠️ CITES {CITES_LABEL[species.citesLevel]}
            {species.legalStatusNote && <p className="mt-1 text-orange-600">{species.legalStatusNote}</p>}
          </div>
        )}
      </section>

      {/* 사육 가이드 */}
      {cg && (
        <section className="mb-8">
          <h2 className="text-lg font-semibold mb-3 border-b pb-2">사육 가이드</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="bg-gray-50 rounded-xl p-4">
              <h3 className="font-medium mb-3 text-gray-700">🏠 사육장</h3>
              <dl className="space-y-2 text-sm">
                {cg.enclosureType && <InfoItem label="형태" value={cg.enclosureType} />}
                {cg.enclosureSizeCm && <InfoItem label="크기" value={`${cg.enclosureSizeCm}cm`} />}
                {cg.substrate && <InfoItem label="바닥재" value={cg.substrate} />}
              </dl>
            </div>
            <div className="bg-gray-50 rounded-xl p-4">
              <h3 className="font-medium mb-3 text-gray-700">🌡️ 환경</h3>
              <dl className="space-y-2 text-sm">
                {cg.tempHotZone && <InfoItem label="핫존" value={`${cg.tempHotZone}°C`} />}
                {cg.tempCoolZone && <InfoItem label="쿨존" value={`${cg.tempCoolZone}°C`} />}
                {cg.tempNight && <InfoItem label="야간" value={`${cg.tempNight}°C`} />}
                {(cg.humidityMin || cg.humidityMax) && (
                  <InfoItem label="습도" value={`${cg.humidityMin ?? "?"}~${cg.humidityMax ?? "?"}%`} />
                )}
                <InfoItem label="UVB" value={cg.uvbRequired ? "필요" : "불필요"} />
                {cg.photoperiodHours && <InfoItem label="광주기" value={`${cg.photoperiodHours}시간`} />}
              </dl>
            </div>
            <div className="bg-gray-50 rounded-xl p-4">
              <h3 className="font-medium mb-3 text-gray-700">🍖 먹이</h3>
              <dl className="space-y-2 text-sm">
                {cg.feedType && <InfoItem label="종류" value={cg.feedType} />}
                {cg.feedFrequency && <InfoItem label="급여 주기" value={cg.feedFrequency} />}
                {cg.supplements && <InfoItem label="보충제" value={cg.supplements} />}
              </dl>
            </div>
            <div className="bg-gray-50 rounded-xl p-4">
              <h3 className="font-medium mb-3 text-gray-700">🤝 핸들링/합사</h3>
              <dl className="space-y-2 text-sm">
                {cg.handlingLevel && <InfoItem label="핸들링" value={cg.handlingLevel} />}
                {cg.cohabitationNote && <InfoItem label="합사" value={cg.cohabitationNote} />}
              </dl>
            </div>
          </div>
        </section>
      )}

      {/* 모프 */}
      {species.morphs.length > 0 && (
        <section>
          <h2 className="text-lg font-semibold mb-3 border-b pb-2">
            모프 ({species.morphs.length}종)
          </h2>
          <div className="grid grid-cols-2 md:grid-cols-3 gap-3">
            {species.morphs.map((morph) => (
              <div key={morph.id} className="border border-gray-200 rounded-xl p-3">
                <p className="font-medium text-sm">{morph.name}</p>
                <p className="text-xs text-gray-400 mb-1">{GENETIC_LABEL[morph.geneticPattern]}</p>
                {morph.description && (
                  <p className="text-xs text-gray-500">{morph.description}</p>
                )}
              </div>
            ))}
          </div>
        </section>
      )}
    </div>
  );
}

function InfoItem({ label, value }: { label: string; value: string }) {
  return (
    <div className="flex gap-2">
      <dt className="text-gray-400 shrink-0 w-20">{label}</dt>
      <dd className="text-gray-800">{value}</dd>
    </div>
  );
}
