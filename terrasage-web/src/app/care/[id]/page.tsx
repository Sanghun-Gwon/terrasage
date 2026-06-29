"use client";

import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import Link from "next/link";
import { getToken } from "@/lib/auth";
import type { Animal, CareRecord } from "@/types/care";
import { GENDER_LABEL } from "@/types/care";

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

// ── SVG 라인 차트 ──────────────────────────────────────────────────────────────

function LineChart({
  data,
  label,
  unit,
  color = "#15803d",
}: {
  data: { x: string; y: number }[];
  label: string;
  unit: string;
  color?: string;
}) {
  const W = 480;
  const H = 120;
  const PAD = { top: 12, right: 16, bottom: 28, left: 40 };

  if (data.length < 2) {
    return (
      <div className="flex items-center justify-center h-[120px] text-xs text-gray-400">
        데이터가 2개 이상이어야 차트가 표시됩니다
      </div>
    );
  }

  const ys = data.map((d) => d.y);
  const minY = Math.min(...ys);
  const maxY = Math.max(...ys);
  const rangeY = maxY === minY ? 1 : maxY - minY;

  const toX = (i: number) =>
    PAD.left + (i / (data.length - 1)) * (W - PAD.left - PAD.right);
  const toY = (y: number) =>
    PAD.top + (1 - (y - minY) / rangeY) * (H - PAD.top - PAD.bottom);

  const points = data.map((d, i) => `${toX(i)},${toY(d.y)}`).join(" ");
  const pathD = `M ${data.map((d, i) => `${toX(i)} ${toY(d.y)}`).join(" L ")}`;

  // y축 눈금 3개
  const ticks = [minY, (minY + maxY) / 2, maxY].map((v) => Math.round(v * 10) / 10);

  return (
    <div>
      <p className="text-xs font-medium text-gray-600 mb-1">{label}</p>
      <svg viewBox={`0 0 ${W} ${H}`} className="w-full" style={{ height: H }}>
        {/* y축 눈금 */}
        {ticks.map((t) => (
          <g key={t}>
            <line
              x1={PAD.left} y1={toY(t)}
              x2={W - PAD.right} y2={toY(t)}
              stroke="#e5e7eb" strokeWidth={1}
            />
            <text x={PAD.left - 4} y={toY(t) + 4} textAnchor="end" fontSize={9} fill="#9ca3af">
              {t}
            </text>
          </g>
        ))}

        {/* 라인 */}
        <path d={pathD} fill="none" stroke={color} strokeWidth={2} strokeLinejoin="round" />

        {/* 도트 + 툴팁 */}
        {data.map((d, i) => (
          <g key={i}>
            <circle cx={toX(i)} cy={toY(d.y)} r={3} fill={color} />
            <title>{`${d.x}: ${d.y}${unit}`}</title>
          </g>
        ))}

        {/* x축 — 첫/마지막 날짜만 */}
        {[0, data.length - 1].map((i) => (
          <text
            key={i}
            x={toX(i)}
            y={H - 4}
            textAnchor={i === 0 ? "start" : "end"}
            fontSize={9}
            fill="#9ca3af"
          >
            {data[i].x.slice(5, 10)}
          </text>
        ))}
      </svg>
      <p className="text-xs text-gray-400 text-right">단위: {unit}</p>
    </div>
  );
}

// ── 기록 입력 폼 ──────────────────────────────────────────────────────────────

function RecordForm({
  animalId,
  onAdded,
}: {
  animalId: number;
  onAdded: (record: CareRecord) => void;
}) {
  const router = useRouter();
  const [open, setOpen] = useState(false);
  const [temperature, setTemperature] = useState("");
  const [humidity, setHumidity] = useState("");
  const [lightHours, setLightHours] = useState("");
  const [weight, setWeight] = useState("");
  const [feedType, setFeedType] = useState("");
  const [feedAmount, setFeedAmount] = useState("");
  const [notes, setNotes] = useState("");
  const [submitting, setSubmitting] = useState(false);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    const token = getToken();
    if (!token) { router.push("/login"); return; }

    setSubmitting(true);
    try {
      const body: Record<string, unknown> = {};
      if (temperature) body.temperature = parseFloat(temperature);
      if (humidity) body.humidity = parseFloat(humidity);
      if (lightHours) body.lightHours = parseFloat(lightHours);
      if (weight) body.weight = parseFloat(weight);
      if (feedType.trim()) body.feedType = feedType.trim();
      if (feedAmount.trim()) body.feedAmount = feedAmount.trim();
      if (notes.trim()) body.notes = notes.trim();

      const res = await fetch(`${API_URL}/api/v1/animals/${animalId}/records`, {
        method: "POST",
        headers: { "Content-Type": "application/json", Authorization: `Bearer ${token}` },
        body: JSON.stringify(body),
      });
      const json = await res.json();
      if (json.success) {
        onAdded(json.data);
        setTemperature(""); setHumidity(""); setLightHours(""); setWeight("");
        setFeedType(""); setFeedAmount(""); setNotes("");
        setOpen(false);
      }
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="bg-white border border-gray-200 rounded-2xl overflow-hidden mb-4">
      <button
        onClick={() => setOpen((v) => !v)}
        className="w-full flex items-center justify-between px-5 py-4 text-sm font-medium text-gray-700 hover:bg-gray-50 transition-colors"
      >
        <span>+ 기록 입력</span>
        <span className="text-gray-400">{open ? "▲" : "▼"}</span>
      </button>

      {open && (
        <form onSubmit={handleSubmit} className="border-t border-gray-100 px-5 py-4 space-y-4">
          <div className="grid grid-cols-2 gap-3">
            <NumField label="온도 (°C)" value={temperature} onChange={setTemperature} step="0.1" />
            <NumField label="습도 (%)" value={humidity} onChange={setHumidity} step="0.1" />
            <NumField label="광량 (시간)" value={lightHours} onChange={setLightHours} step="0.5" />
            <NumField label="체중 (g)" value={weight} onChange={setWeight} step="0.1" />
          </div>
          <div className="grid grid-cols-2 gap-3">
            <div className="space-y-1">
              <label className="text-xs text-gray-500">먹이 종류</label>
              <input
                type="text"
                value={feedType}
                onChange={(e) => setFeedType(e.target.value)}
                placeholder="예: 귀뚜라미"
                className={inputCls}
              />
            </div>
            <div className="space-y-1">
              <label className="text-xs text-gray-500">먹이 양</label>
              <input
                type="text"
                value={feedAmount}
                onChange={(e) => setFeedAmount(e.target.value)}
                placeholder="예: 3마리"
                className={inputCls}
              />
            </div>
          </div>
          <div className="space-y-1">
            <label className="text-xs text-gray-500">메모</label>
            <textarea
              value={notes}
              onChange={(e) => setNotes(e.target.value)}
              rows={2}
              placeholder="특이사항 기록"
              className={`${inputCls} resize-none`}
            />
          </div>
          <div className="flex justify-end">
            <button
              type="submit"
              disabled={submitting}
              className="px-5 py-2 bg-green-700 text-white text-sm rounded-lg hover:bg-green-800 disabled:opacity-40 transition-colors"
            >
              {submitting ? "저장 중..." : "기록 저장"}
            </button>
          </div>
        </form>
      )}
    </div>
  );
}

function NumField({ label, value, onChange, step }: {
  label: string; value: string; onChange: (v: string) => void; step?: string;
}) {
  return (
    <div className="space-y-1">
      <label className="text-xs text-gray-500">{label}</label>
      <input
        type="number"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        step={step}
        min={0}
        className={inputCls}
      />
    </div>
  );
}

const inputCls = "w-full border border-gray-300 rounded-lg px-3 py-1.5 text-sm focus:outline-none focus:ring-2 focus:ring-green-500";

// ── 메인 페이지 ───────────────────────────────────────────────────────────────

export default function AnimalDetailPage() {
  const params = useParams<{ id: string }>();
  const router = useRouter();
  const animalId = Number(params.id);

  const [animal, setAnimal] = useState<Animal | null>(null);
  const [records, setRecords] = useState<CareRecord[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = getToken();
    if (!token) { router.push("/login"); return; }

    Promise.all([
      fetch(`${API_URL}/api/v1/animals/${animalId}`, {
        headers: { Authorization: `Bearer ${token}` },
      }).then((r) => r.json()),
      fetch(`${API_URL}/api/v1/animals/${animalId}/records`, {
        headers: { Authorization: `Bearer ${token}` },
      }).then((r) => r.json()),
    ])
      .then(([animalJson, recordsJson]) => {
        if (animalJson.success) setAnimal(animalJson.data);
        else router.push("/care");
        if (recordsJson.success) setRecords(recordsJson.data);
      })
      .finally(() => setLoading(false));
  }, [animalId, router]);

  if (loading || !animal) {
    return <div className="py-16 text-center text-sm text-gray-400">불러오는 중...</div>;
  }

  // 차트용 데이터 — 오래된 순으로 정렬
  const sorted = [...records].sort(
    (a, b) => new Date(a.recordedAt).getTime() - new Date(b.recordedAt).getTime()
  );
  const toChartData = (key: keyof CareRecord) =>
    sorted
      .filter((r) => r[key] != null)
      .map((r) => ({ x: r.recordedAt, y: r[key] as number }));

  async function deleteRecord(recordId: number) {
    if (!confirm("기록을 삭제하시겠습니까?")) return;
    const token = getToken();
    const res = await fetch(`${API_URL}/api/v1/animals/${animalId}/records/${recordId}`, {
      method: "DELETE",
      headers: { Authorization: `Bearer ${token ?? ""}` },
    });
    if (res.ok) setRecords((prev) => prev.filter((r) => r.id !== recordId));
  }

  const tempData = toChartData("temperature");
  const humidData = toChartData("humidity");
  const weightData = toChartData("weight");
  const hasChart = tempData.length >= 2 || humidData.length >= 2 || weightData.length >= 2;

  return (
    <div className="max-w-2xl mx-auto">
      <Link href="/care" className="text-sm text-gray-400 hover:text-green-700 transition-colors mb-4 inline-block">
        ← 개체 목록
      </Link>

      {/* 개체 정보 */}
      <div className="bg-white border border-gray-200 rounded-2xl p-6 mb-4">
        <div className="flex items-start justify-between mb-1">
          <div>
            <div className="flex items-center gap-2">
              <h1 className="text-xl font-bold text-gray-900">{animal.name}</h1>
              {animal.nickname && (
                <span className="text-base text-gray-400">({animal.nickname})</span>
              )}
            </div>
            <p className="text-sm text-gray-500 mt-0.5">{animal.speciesName}</p>
          </div>
          <Link
            href={`/care/${animalId}/edit`}
            className="text-xs text-gray-400 border border-gray-300 px-3 py-1 rounded-lg hover:border-green-400 hover:text-green-700 transition-colors"
          >
            수정
          </Link>
        </div>
        <div className="flex gap-4 mt-3 text-xs text-gray-500">
          <span>{GENDER_LABEL[animal.gender]}</span>
          {animal.birthDate && <span>생일 {animal.birthDate}</span>}
          {animal.isPublic && <span className="text-green-600">공개</span>}
        </div>
        {animal.notes && (
          <p className="mt-3 text-sm text-gray-600 bg-gray-50 rounded-lg px-3 py-2">{animal.notes}</p>
        )}
      </div>

      {/* 기록 입력 */}
      <RecordForm animalId={animalId} onAdded={(r) => setRecords((prev) => [r, ...prev])} />

      {/* 차트 */}
      {hasChart && (
        <div className="bg-white border border-gray-200 rounded-2xl p-6 mb-4 space-y-6">
          <h2 className="text-sm font-semibold text-gray-900">추이 차트</h2>
          {tempData.length >= 2 && (
            <LineChart data={tempData} label="온도" unit="°C" color="#ef4444" />
          )}
          {humidData.length >= 2 && (
            <LineChart data={humidData} label="습도" unit="%" color="#3b82f6" />
          )}
          {weightData.length >= 2 && (
            <LineChart data={weightData} label="체중" unit="g" color="#8b5cf6" />
          )}
        </div>
      )}

      {/* 기록 목록 */}
      <div className="bg-white border border-gray-200 rounded-2xl p-6">
        <h2 className="text-sm font-semibold text-gray-900 mb-4">
          기록 <span className="text-gray-400 font-normal">{records.length}</span>
        </h2>
        {records.length === 0 ? (
          <p className="text-sm text-gray-400 text-center py-6">첫 기록을 입력해보세요</p>
        ) : (
          <div className="space-y-3">
            {records.map((r) => (
              <div key={r.id} className="border border-gray-100 rounded-xl p-3">
                <div className="flex items-center justify-between mb-2">
                  <span className="text-xs text-gray-500">
                    {new Date(r.recordedAt).toLocaleString("ko-KR", {
                      month: "short", day: "numeric", hour: "2-digit", minute: "2-digit",
                    })}
                  </span>
                  <button
                    onClick={() => deleteRecord(r.id)}
                    className="text-xs text-gray-300 hover:text-red-400 transition-colors"
                  >
                    삭제
                  </button>
                </div>
                <div className="flex flex-wrap gap-x-4 gap-y-1 text-xs text-gray-600">
                  {r.temperature != null && <span>🌡 {r.temperature}°C</span>}
                  {r.humidity != null && <span>💧 {r.humidity}%</span>}
                  {r.lightHours != null && <span>☀ {r.lightHours}h</span>}
                  {r.weight != null && <span>⚖ {r.weight}g</span>}
                  {r.feedType && <span>🍖 {r.feedType}{r.feedAmount ? ` ${r.feedAmount}` : ""}</span>}
                </div>
                {r.notes && <p className="mt-1.5 text-xs text-gray-500">{r.notes}</p>}
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
