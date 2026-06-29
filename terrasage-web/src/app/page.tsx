import Link from "next/link";

const MENUS = [
  {
    href: "/species",
    icon: "🦎",
    title: "백과사전",
    desc: "파충류·양서류·수생생물·식물 사육 정보",
    available: true,
  },
  {
    href: "/community",
    icon: "💬",
    title: "커뮤니티",
    desc: "사육자들과 정보를 나눠보세요",
    available: true,
  },
  {
    href: "/care",
    icon: "🌡️",
    title: "사육환경 관리",
    desc: "온도·습도·먹이 기록 및 시각화",
    available: true,
  },
  {
    href: "/health",
    icon: "🩺",
    title: "건강 진단",
    desc: "AI 기반 증상 분석 및 스마트 모니터링",
    available: false,
  },
  {
    href: "/marketplace",
    icon: "🛒",
    title: "마켓플레이스",
    desc: "생물 거래·경매 플랫폼",
    available: false,
  },
] as const;

export default function HomePage() {
  return (
    <div className="max-w-2xl mx-auto">
      <div className="mb-10 mt-4">
        <h1 className="text-2xl font-bold text-gray-900">메뉴</h1>
        <p className="text-sm text-gray-500 mt-1">서비스를 선택하세요</p>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
        {MENUS.map((menu) =>
          menu.available ? (
            <Link
              key={menu.href}
              href={menu.href}
              className="group flex items-start gap-4 p-5 bg-white border border-gray-200 rounded-2xl hover:border-green-400 hover:shadow-md transition-all"
            >
              <div className="text-3xl">{menu.icon}</div>
              <div>
                <p className="font-semibold text-gray-900 group-hover:text-green-700 transition-colors">
                  {menu.title}
                </p>
                <p className="text-xs text-gray-500 mt-0.5">{menu.desc}</p>
              </div>
            </Link>
          ) : (
            <div
              key={menu.href}
              className="flex items-start gap-4 p-5 bg-gray-50 border border-gray-100 rounded-2xl opacity-60 cursor-not-allowed"
            >
              <div className="text-3xl grayscale">{menu.icon}</div>
              <div>
                <p className="font-semibold text-gray-500">
                  {menu.title}
                  <span className="ml-2 text-xs font-normal bg-gray-200 text-gray-500 px-1.5 py-0.5 rounded-full">
                    준비중
                  </span>
                </p>
                <p className="text-xs text-gray-400 mt-0.5">{menu.desc}</p>
              </div>
            </div>
          )
        )}
      </div>
    </div>
  );
}
