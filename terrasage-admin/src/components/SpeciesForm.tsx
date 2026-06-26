"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { createSpecies, updateSpecies, type SpeciesFormData } from "@/lib/api";
import type { SpeciesDetail } from "@/types/species";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Textarea } from "@/components/ui/textarea";
import { Badge } from "@/components/ui/badge";

interface Props {
  initial?: SpeciesDetail;
}

export default function SpeciesForm({ initial }: Props) {
  const router = useRouter();
  const isEdit = !!initial;

  const [form, setForm] = useState<SpeciesFormData>({
    scientificName: initial?.scientificName ?? "",
    commonNameKo: initial?.commonNameKo ?? "",
    commonNameEn: initial?.commonNameEn ?? "",
    kingdom: initial?.kingdom ?? "Animalia",
    phylum: initial?.phylum ?? "Chordata",
    taxonomyClass: initial?.taxonomyClass ?? "",
    taxonomyOrder: initial?.taxonomyOrder ?? "",
    family: initial?.family ?? "",
    genus: initial?.genus ?? "",
    origin: initial?.origin ?? "",
    habitat: initial?.habitat ?? "",
    lifespanCaptive: initial?.lifespanCaptive ?? undefined,
    lifespanWild: initial?.lifespanWild ?? undefined,
    avgSizeCm: initial?.avgSizeCm ?? undefined,
    avgWeightG: initial?.avgWeightG ?? undefined,
    difficultyLevel: initial?.difficultyLevel ?? "BEGINNER",
    citesLevel: initial?.citesLevel ?? "",
    legalStatusNote: initial?.legalStatusNote ?? "",
    thumbnailUrl: initial?.thumbnailUrl ?? "",
    status: initial?.status ?? "DRAFT",
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const set = (key: keyof SpeciesFormData, value: string | number | null | undefined) =>
    setForm((prev) => ({ ...prev, [key]: value ?? undefined }));

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const payload = {
        ...form,
        status: form.status ?? "DRAFT",
        // 빈 문자열은 null로 변환
        commonNameEn: form.commonNameEn || undefined,
        origin: form.origin || undefined,
        habitat: form.habitat || undefined,
        citesLevel: form.citesLevel || undefined,
        legalStatusNote: form.legalStatusNote || undefined,
        thumbnailUrl: form.thumbnailUrl || undefined,
      };
      if (isEdit) {
        await updateSpecies(initial!.id, { ...payload, status: payload.status! });
      } else {
        await createSpecies(payload);
      }
      router.push("/species");
      router.refresh();
    } catch (e) {
      setError(e instanceof Error ? e.message : "저장 실패");
    } finally {
      setLoading(false);
    }
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-8 max-w-3xl">
      {error && (
        <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg text-sm">
          {error}
        </div>
      )}

      {/* 기본 정보 */}
      <section>
        <h2 className="text-base font-semibold text-gray-700 mb-4 border-b pb-2">기본 정보</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <FormField label="학명 *">
            <Input value={form.scientificName} onChange={(e) => set("scientificName", e.target.value)} required placeholder="Eublepharis macularius" />
          </FormField>
          <FormField label="한글명 *">
            <Input value={form.commonNameKo} onChange={(e) => set("commonNameKo", e.target.value)} required placeholder="레오파드 게코" />
          </FormField>
          <FormField label="영문명">
            <Input value={form.commonNameEn ?? ""} onChange={(e) => set("commonNameEn", e.target.value)} placeholder="Leopard Gecko" />
          </FormField>
          <FormField label="난이도 *">
            <Select value={form.difficultyLevel} onValueChange={(v) => set("difficultyLevel", v)}>
              <SelectTrigger><SelectValue /></SelectTrigger>
              <SelectContent>
                <SelectItem value="BEGINNER">입문</SelectItem>
                <SelectItem value="INTERMEDIATE">중급</SelectItem>
                <SelectItem value="ADVANCED">고급</SelectItem>
              </SelectContent>
            </Select>
          </FormField>
        </div>
      </section>

      {/* 분류 */}
      <section>
        <h2 className="text-base font-semibold text-gray-700 mb-4 border-b pb-2">분류 정보</h2>
        <div className="grid grid-cols-2 md:grid-cols-3 gap-4">
          {(["kingdom", "phylum", "taxonomyClass", "taxonomyOrder", "family", "genus"] as const).map((key) => (
            <FormField key={key} label={`${TAXONOMY_LABEL[key]} *`}>
              <Input value={form[key] as string} onChange={(e) => set(key, e.target.value)} required placeholder={TAXONOMY_PLACEHOLDER[key]} />
            </FormField>
          ))}
        </div>
      </section>

      {/* 서식 정보 */}
      <section>
        <h2 className="text-base font-semibold text-gray-700 mb-4 border-b pb-2">서식 정보</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <FormField label="원산지">
            <Input value={form.origin ?? ""} onChange={(e) => set("origin", e.target.value)} />
          </FormField>
          <FormField label="서식지">
            <Input value={form.habitat ?? ""} onChange={(e) => set("habitat", e.target.value)} />
          </FormField>
          <FormField label="수명 (사육, 년)">
            <Input type="number" value={form.lifespanCaptive ?? ""} onChange={(e) => set("lifespanCaptive", e.target.value ? Number(e.target.value) : undefined)} />
          </FormField>
          <FormField label="수명 (야생, 년)">
            <Input type="number" value={form.lifespanWild ?? ""} onChange={(e) => set("lifespanWild", e.target.value ? Number(e.target.value) : undefined)} />
          </FormField>
          <FormField label="평균 크기 (cm)">
            <Input type="number" step="0.1" value={form.avgSizeCm ?? ""} onChange={(e) => set("avgSizeCm", e.target.value ? Number(e.target.value) : undefined)} />
          </FormField>
          <FormField label="평균 무게 (g)">
            <Input type="number" step="0.1" value={form.avgWeightG ?? ""} onChange={(e) => set("avgWeightG", e.target.value ? Number(e.target.value) : undefined)} />
          </FormField>
        </div>
      </section>

      {/* 법적 정보 */}
      <section>
        <h2 className="text-base font-semibold text-gray-700 mb-4 border-b pb-2">법적 정보</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <FormField label="CITES 등급">
            <Select value={form.citesLevel ?? ""} onValueChange={(v) => set("citesLevel", v === "none" ? "" : v)}>
              <SelectTrigger><SelectValue placeholder="해당 없음" /></SelectTrigger>
              <SelectContent>
                <SelectItem value="none">해당 없음</SelectItem>
                <SelectItem value="APPENDIX_I">부속서 I</SelectItem>
                <SelectItem value="APPENDIX_II">부속서 II</SelectItem>
                <SelectItem value="APPENDIX_III">부속서 III</SelectItem>
              </SelectContent>
            </Select>
          </FormField>
          <FormField label="법적 비고">
            <Input value={form.legalStatusNote ?? ""} onChange={(e) => set("legalStatusNote", e.target.value)} />
          </FormField>
        </div>
      </section>

      {/* 상태 (수정 시만) */}
      {isEdit && (
        <section>
          <h2 className="text-base font-semibold text-gray-700 mb-4 border-b pb-2">공개 상태</h2>
          <Select value={form.status ?? "DRAFT"} onValueChange={(v) => set("status", v)}>
            <SelectTrigger className="w-40"><SelectValue /></SelectTrigger>
            <SelectContent>
              <SelectItem value="DRAFT">초안</SelectItem>
              <SelectItem value="PUBLISHED">공개</SelectItem>
              <SelectItem value="ARCHIVED">보관</SelectItem>
            </SelectContent>
          </Select>
        </section>
      )}

      <div className="flex gap-3 pt-2">
        <Button type="submit" disabled={loading}>
          {loading ? "저장 중..." : isEdit ? "수정 완료" : "등록"}
        </Button>
        <Button type="button" variant="outline" onClick={() => router.back()}>
          취소
        </Button>
      </div>
    </form>
  );
}

function FormField({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div className="space-y-1.5">
      <Label className="text-sm text-gray-600">{label}</Label>
      {children}
    </div>
  );
}

const TAXONOMY_LABEL: Record<string, string> = {
  kingdom: "계",
  phylum: "문",
  taxonomyClass: "강",
  taxonomyOrder: "목",
  family: "과",
  genus: "속",
};

const TAXONOMY_PLACEHOLDER: Record<string, string> = {
  kingdom: "Animalia",
  phylum: "Chordata",
  taxonomyClass: "Reptilia",
  taxonomyOrder: "Squamata",
  family: "Eublepharidae",
  genus: "Eublepharis",
};
