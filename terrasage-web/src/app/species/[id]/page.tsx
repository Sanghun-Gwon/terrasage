export const dynamic = "force-dynamic";

import { notFound } from "next/navigation";
import Link from "next/link";
import { getSpeciesDetail } from "@/lib/api";
import type { AnimalCareGuide, PlantCareGuide, DifficultyLevel, CitesLevel, GeneticPattern } from "@/types/species";

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
  CULTIVAR: "품종",
  VARIEGATED: "바리에가타",
  HYBRID: "교잡종",
  SPORT: "지변",
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

  const acg = species.animalCareGuide;
  const pcg = species.plantCareGuide;

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

      {/* 동물 사육 가이드 */}
      {acg && <AnimalCareGuideSection guide={acg} />}

      {/* 식물 재배 가이드 */}
      {pcg && <PlantCareGuideSection guide={pcg} />}

      {/* 변이/품종 */}
      {species.variants.length > 0 && (
        <section>
          <h2 className="text-lg font-semibold mb-3 border-b pb-2">
            변이/품종 ({species.variants.length}종)
          </h2>
          <div className="grid grid-cols-2 md:grid-cols-3 gap-3">
            {species.variants.map((variant) => (
              <div key={variant.id} className="border border-gray-200 rounded-xl p-3">
                <p className="font-medium text-sm">{variant.name}</p>
                <p className="text-xs text-gray-400 mb-1">{GENETIC_LABEL[variant.geneticPattern]}</p>
                {variant.description && (
                  <p className="text-xs text-gray-500">{variant.description}</p>
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

function GuideCard({ title, children }: { title: string; children: React.ReactNode }) {
  return (
    <div className="bg-gray-50 rounded-xl p-4">
      <h3 className="font-medium mb-3 text-gray-700">{title}</h3>
      <dl className="space-y-2 text-sm">{children}</dl>
    </div>
  );
}

function AnimalCareGuideSection({ guide: g }: { guide: AnimalCareGuide }) {
  return (
    <section className="mb-8">
      <h2 className="text-lg font-semibold mb-3 border-b pb-2">사육 가이드</h2>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <GuideCard title="🏠 사육장">
          {g.enclosureType && <InfoItem label="형태" value={g.enclosureType} />}
          {g.enclosureSizeCm && <InfoItem label="크기" value={g.enclosureSizeCm} />}
          {g.substrate && <InfoItem label="바닥재" value={g.substrate} />}
        </GuideCard>
        <GuideCard title="🌡️ 환경">
          {g.tempHotZone != null && <InfoItem label="핫존" value={`${g.tempHotZone}°C`} />}
          {g.tempCoolZone != null && <InfoItem label="쿨존" value={`${g.tempCoolZone}°C`} />}
          {g.tempNight != null && <InfoItem label="야간" value={`${g.tempNight}°C`} />}
          {(g.humidityMin != null || g.humidityMax != null) && (
            <InfoItem label="습도" value={`${g.humidityMin ?? "?"}~${g.humidityMax ?? "?"}%`} />
          )}
          <InfoItem label="UVB" value={g.uvbRequired ? "필요" : "불필요"} />
          {g.photoperiodHours != null && <InfoItem label="광주기" value={`${g.photoperiodHours}시간`} />}
        </GuideCard>
        <GuideCard title="🍖 먹이">
          {g.feedType && <InfoItem label="종류" value={g.feedType} />}
          {g.feedFrequency && <InfoItem label="급여 주기" value={g.feedFrequency} />}
          {g.supplements && <InfoItem label="보충제" value={g.supplements} />}
        </GuideCard>
        <GuideCard title="🤝 핸들링 / 합사">
          {g.handlingLevel && <InfoItem label="핸들링" value={g.handlingLevel} />}
          {g.cohabitationNote && <InfoItem label="합사" value={g.cohabitationNote} />}
        </GuideCard>
      </div>
    </section>
  );
}

function PlantCareGuideSection({ guide: g }: { guide: PlantCareGuide }) {
  return (
    <section className="mb-8">
      <h2 className="text-lg font-semibold mb-3 border-b pb-2">재배 가이드</h2>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <GuideCard title="🪴 재배 환경">
          {g.potType && <InfoItem label="화분" value={g.potType} />}
          {g.growingMedium && <InfoItem label="배양토" value={g.growingMedium} />}
        </GuideCard>
        <GuideCard title="☀️ 빛 / 온도">
          {g.lightRequirement && <InfoItem label="광량" value={g.lightRequirement} />}
          {g.lightHoursPerDay != null && <InfoItem label="일조 시간" value={`${g.lightHoursPerDay}시간/일`} />}
          {(g.tempMin != null || g.tempMax != null) && (
            <InfoItem label="생육 온도" value={`${g.tempMin ?? "?"}°C ~ ${g.tempMax ?? "?"}°C`} />
          )}
          {(g.humidityMin != null || g.humidityMax != null) && (
            <InfoItem label="습도" value={`${g.humidityMin ?? "?"}~${g.humidityMax ?? "?"}%`} />
          )}
        </GuideCard>
        <GuideCard title="💧 물주기">
          {g.wateringFrequency && <InfoItem label="주기" value={g.wateringFrequency} />}
          {g.wateringMethod && <InfoItem label="방법" value={g.wateringMethod} />}
        </GuideCard>
        <GuideCard title="🌱 비료 / 관리">
          {g.fertilizerType && <InfoItem label="비료" value={g.fertilizerType} />}
          {g.fertilizerFrequency && <InfoItem label="시비 주기" value={g.fertilizerFrequency} />}
          {g.repottingNote && <InfoItem label="분갈이" value={g.repottingNote} />}
          {g.pruningNote && <InfoItem label="전정" value={g.pruningNote} />}
        </GuideCard>
      </div>
      {g.overallNote && (
        <div className="mt-4 p-3 bg-green-50 rounded-lg text-sm text-green-800">
          💡 {g.overallNote}
        </div>
      )}
    </section>
  );
}
