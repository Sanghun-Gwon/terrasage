-- ──────────────────────────────────────────────────────
-- 테스트 계정 시드
--   admin@terrasage.com / admin1234  (ADMIN)
--   user@terrasage.com  / user1234   (USER)
-- ──────────────────────────────────────────────────────
INSERT INTO users (email, password, name, role, created_at)
VALUES
    ('admin@terrasage.com', '$2a$10$dq3Ls6zk0QICWj45zkHmXey5DIIrOYrTm/5Y.P9bq4gUO/b1IiTQq', '관리자', 'ADMIN', NOW()),
    ('user@terrasage.com',  '$2a$10$vZkIRIHMSpzeJ9GMpaKB7OJ/yEznIVHD01Z.Vqn7dFjsLkba/oSi6', '레오게코덕후', 'USER', NOW())
ON CONFLICT (email) DO NOTHING;

-- ──────────────────────────────────────────────────────
-- Species sample data
-- ON CONFLICT DO UPDATE: category 컬럼 추가 시 기존 데이터 갱신용
-- ──────────────────────────────────────────────────────
INSERT INTO species (scientific_name, common_name_ko, common_name_en, kingdom, phylum, taxonomy_class, taxonomy_order, family, genus, origin, habitat, lifespan_captive, lifespan_wild, avg_size_cm, avg_weightg, difficulty_level, category, cites_level, legal_status_note, status, created_at, updated_at)
VALUES
    -- 파충류 (REPTILE)
    ('Eublepharis macularius', '레오파드 게코', 'Leopard Gecko', 'Animalia', 'Chordata', 'Reptilia', 'Squamata', 'Eublepharidae', 'Eublepharis', '파키스탄, 인도, 아프가니스탄', '건조한 암석 사막, 초원', 20, 15, 22.0, 65.0, 'BEGINNER', 'REPTILE', NULL, NULL, 'PUBLISHED', NOW(), NOW()),
    ('Python regius', '볼 파이썬', 'Ball Python', 'Animalia', 'Chordata', 'Reptilia', 'Squamata', 'Pythonidae', 'Python', '서아프리카, 중앙아프리카', '사바나, 초원, 열대림 가장자리', 30, 10, 150.0, 1800.0, 'BEGINNER', 'REPTILE', 'APPENDIX_II', NULL, 'PUBLISHED', NOW(), NOW()),
    ('Pogona vitticeps', '비어디 드래곤', 'Bearded Dragon', 'Animalia', 'Chordata', 'Reptilia', 'Squamata', 'Agamidae', 'Pogona', '호주 내륙', '건조한 삼림, 사막, 관목지', 12, 8, 55.0, 450.0, 'BEGINNER', 'REPTILE', NULL, NULL, 'PUBLISHED', NOW(), NOW()),
    ('Correlophus ciliatus', '크레스티드 게코', 'Crested Gecko', 'Animalia', 'Chordata', 'Reptilia', 'Squamata', 'Diplodactylidae', 'Correlophus', '뉴칼레도니아', '열대 우림', 15, 10, 20.0, 45.0, 'BEGINNER', 'REPTILE', NULL, NULL, 'PUBLISHED', NOW(), NOW()),
    ('Pantherophis guttatus', '콘 스네이크', 'Corn Snake', 'Animalia', 'Chordata', 'Reptilia', 'Squamata', 'Colubridae', 'Pantherophis', '북미 동부', '낙엽수림, 농경지, 초원', 20, 8, 150.0, 900.0, 'BEGINNER', 'REPTILE', NULL, NULL, 'PUBLISHED', NOW(), NOW()),
    ('Chamaeleo calyptratus', '베일드 카멜레온', 'Veiled Chameleon', 'Animalia', 'Chordata', 'Reptilia', 'Squamata', 'Chamaeleonidae', 'Chamaeleo', '예멘, 사우디아라비아', '고지대 관목, 계곡', 7, 5, 55.0, 170.0, 'INTERMEDIATE', 'REPTILE', 'APPENDIX_II', NULL, 'PUBLISHED', NOW(), NOW()),
    ('Morelia spilota', '카펫 파이썬', 'Carpet Python', 'Animalia', 'Chordata', 'Reptilia', 'Squamata', 'Pythonidae', 'Morelia', '호주, 뉴기니', '열대림, 삼림, 도시 근교', 20, 15, 250.0, 3500.0, 'INTERMEDIATE', 'REPTILE', 'APPENDIX_II', NULL, 'PUBLISHED', NOW(), NOW()),
    ('Varanus acanthurus', '리지테일 모니터', 'Ridge-tailed Monitor', 'Animalia', 'Chordata', 'Reptilia', 'Squamata', 'Varanidae', 'Varanus', '호주 북부', '건조한 암석 지대', 15, 10, 70.0, 300.0, 'INTERMEDIATE', 'REPTILE', 'APPENDIX_II', NULL, 'PUBLISHED', NOW(), NOW()),
    ('Boa constrictor', '보아 컨스트릭터', 'Boa Constrictor', 'Animalia', 'Chordata', 'Reptilia', 'Squamata', 'Boidae', 'Boa', '중남미', '열대림, 사바나', 30, 20, 300.0, 15000.0, 'ADVANCED', 'REPTILE', 'APPENDIX_II', NULL, 'PUBLISHED', NOW(), NOW()),
    -- 양서류 (AMPHIBIAN)
    ('Dendrobates tinctorius', '다이잉 포이즌 프로그', 'Dyeing Poison Dart Frog', 'Animalia', 'Chordata', 'Amphibia', 'Anura', 'Dendrobatidae', 'Dendrobates', '남미 북부', '열대 우림 바닥', 10, 5, 5.0, 4.0, 'ADVANCED', 'AMPHIBIAN', 'APPENDIX_II', NULL, 'PUBLISHED', NOW(), NOW()),
    ('Ambystoma mexicanum', '아홀로틀', 'Axolotl', 'Animalia', 'Chordata', 'Amphibia', 'Urodela', 'Ambystomatidae', 'Ambystoma', '멕시코 소치밀코 호수', '담수 호수, 운하', 15, 10, 25.0, 200.0, 'INTERMEDIATE', 'AMPHIBIAN', 'APPENDIX_II', '멕시코 야생 개체 CITES 부속서 II', 'PUBLISHED', NOW(), NOW()),
    ('Testudo hermanni', '헤르만 육지거북', 'Hermann''s Tortoise', 'Animalia', 'Chordata', 'Reptilia', 'Testudines', 'Testudinidae', 'Testudo', '남유럽 (지중해 연안)', '지중해성 관목림, 초원', 75, 30, 25.0, 3500.0, 'INTERMEDIATE', 'REPTILE', 'APPENDIX_II', NULL, 'PUBLISHED', NOW(), NOW()),
    -- 다육식물 (SUCCULENT)
    ('Echeveria laui', '에케베리아 라우이', 'Lau''s Echeveria', 'Plantae', 'Tracheophyta', 'Magnoliopsida', 'Saxifragales', 'Crassulaceae', 'Echeveria', '멕시코 오아하카', '고지대 암석 지대', NULL, NULL, 10.0, NULL, 'INTERMEDIATE', 'SUCCULENT', NULL, NULL, 'PUBLISHED', NOW(), NOW()),
    ('Haworthiopsis attenuata', '줄무늬 하워시아', 'Zebra Plant', 'Plantae', 'Tracheophyta', 'Magnoliopsida', 'Asparagales', 'Asphodelaceae', 'Haworthiopsis', '남아프리카', '반건조 암석지, 덤불 그늘', NULL, NULL, 12.0, NULL, 'BEGINNER', 'SUCCULENT', NULL, NULL, 'PUBLISHED', NOW(), NOW()),
    ('Aloe vera', '알로에 베라', 'Aloe Vera', 'Plantae', 'Tracheophyta', 'Magnoliopsida', 'Asparagales', 'Asphodelaceae', 'Aloe', '아라비아 반도', '건조한 해안, 암석지', NULL, NULL, 60.0, NULL, 'BEGINNER', 'SUCCULENT', NULL, NULL, 'PUBLISHED', NOW(), NOW()),
    -- 선인장 (CACTUS)
    ('Echinocactus grusonii', '금호선인장', 'Golden Barrel Cactus', 'Plantae', 'Tracheophyta', 'Magnoliopsida', 'Caryophyllales', 'Cactaceae', 'Echinocactus', '멕시코 중부', '건조 협곡, 암석 사면', NULL, NULL, 80.0, NULL, 'BEGINNER', 'CACTUS', NULL, NULL, 'PUBLISHED', NOW(), NOW()),
    ('Gymnocalycium mihanovichii', '달걀선인장 (히보탄)', 'Moon Cactus', 'Plantae', 'Tracheophyta', 'Magnoliopsida', 'Caryophyllales', 'Cactaceae', 'Gymnocalycium', '파라과이, 아르헨티나', '건조 초원', NULL, NULL, 5.0, NULL, 'BEGINNER', 'CACTUS', NULL, NULL, 'PUBLISHED', NOW(), NOW()),
    -- 관엽식물 (FOLIAGE)
    ('Monstera deliciosa', '몬스테라', 'Swiss Cheese Plant', 'Plantae', 'Tracheophyta', 'Magnoliopsida', 'Alismatales', 'Araceae', 'Monstera', '중앙아메리카, 멕시코', '열대 우림 하층', NULL, NULL, 300.0, NULL, 'BEGINNER', 'FOLIAGE', NULL, NULL, 'PUBLISHED', NOW(), NOW()),
    ('Epipremnum aureum', '스킨답서스', 'Pothos', 'Plantae', 'Tracheophyta', 'Magnoliopsida', 'Alismatales', 'Araceae', 'Epipremnum', '솔로몬 제도', '열대 우림, 절벽면', NULL, NULL, 1000.0, NULL, 'BEGINNER', 'FOLIAGE', NULL, NULL, 'PUBLISHED', NOW(), NOW()),
    -- 난류 (ORCHID)
    ('Phalaenopsis amabilis', '호접란', 'Moth Orchid', 'Plantae', 'Tracheophyta', 'Magnoliopsida', 'Asparagales', 'Orchidaceae', 'Phalaenopsis', '동남아시아, 호주', '열대 우림 수목 착생', NULL, NULL, 60.0, NULL, 'INTERMEDIATE', 'ORCHID', NULL, NULL, 'PUBLISHED', NOW(), NOW()),
    -- 식충식물 (CARNIVOROUS_PLANT)
    ('Dionaea muscipula', '파리지옥', 'Venus Flytrap', 'Plantae', 'Tracheophyta', 'Magnoliopsida', 'Caryophyllales', 'Droseraceae', 'Dionaea', '미국 노스캐롤라이나', '습한 사바나, 이탄지', NULL, NULL, 15.0, NULL, 'INTERMEDIATE', 'CARNIVOROUS_PLANT', NULL, NULL, 'PUBLISHED', NOW(), NOW()),
    ('Nepenthes alata', '네펜데스 알라타', 'Winged Pitcher Plant', 'Plantae', 'Tracheophyta', 'Magnoliopsida', 'Caryophyllales', 'Nepenthaceae', 'Nepenthes', '필리핀', '열대 산악 초원', NULL, NULL, 200.0, NULL, 'ADVANCED', 'CARNIVOROUS_PLANT', NULL, NULL, 'PUBLISHED', NOW(), NOW()),
    -- 수생식물 (AQUATIC_PLANT)
    ('Nymphaea tetragona', '수련', 'White Water Lily', 'Plantae', 'Tracheophyta', 'Magnoliopsida', 'Nymphaeales', 'Nymphaeaceae', 'Nymphaea', '아시아 (한국, 일본, 중국)', '연못, 저수지, 습지', NULL, NULL, 20.0, NULL, 'BEGINNER', 'AQUATIC_PLANT', NULL, NULL, 'PUBLISHED', NOW(), NOW()),
    ('Echinodorus grisebachii', '아마존 소드', 'Amazon Sword', 'Plantae', 'Tracheophyta', 'Magnoliopsida', 'Alismatales', 'Alismataceae', 'Echinodorus', '남미 (아마존)', '담수 하천, 수족관 수초', NULL, NULL, 50.0, NULL, 'BEGINNER', 'AQUATIC_PLANT', NULL, NULL, 'PUBLISHED', NOW(), NOW()),
    ('Microsorum pteropus', '자바 펀', 'Java Fern', 'Plantae', 'Tracheophyta', 'Polypodiopsida', 'Polypodiales', 'Polypodiaceae', 'Microsorum', '동남아시아', '담수 하천, 바위 착생', NULL, NULL, 35.0, NULL, 'BEGINNER', 'AQUATIC_PLANT', NULL, NULL, 'PUBLISHED', NOW(), NOW()),
    -- 분재 (BONSAI)
    ('Juniperus chinensis', '향나무 분재', 'Chinese Juniper Bonsai', 'Plantae', 'Tracheophyta', 'Pinopsida', 'Cupressales', 'Cupressaceae', 'Juniperus', '동아시아 (한국, 중국, 일본)', '산지 암석지, 분재 수형', NULL, NULL, 100.0, NULL, 'ADVANCED', 'BONSAI', NULL, NULL, 'PUBLISHED', NOW(), NOW()),
    ('Acer palmatum', '단풍 분재', 'Japanese Maple Bonsai', 'Plantae', 'Tracheophyta', 'Magnoliopsida', 'Sapindales', 'Sapindaceae', 'Acer', '동아시아 (한국, 일본)', '산지, 계곡부', NULL, NULL, 150.0, NULL, 'INTERMEDIATE', 'BONSAI', NULL, NULL, 'PUBLISHED', NOW(), NOW())
ON CONFLICT (scientific_name) DO UPDATE SET category = EXCLUDED.category;

-- ──────────────────────────────────────────────────────
-- Animal care guide sample data
-- ──────────────────────────────────────────────────────
INSERT INTO animal_care_guide (species_id, enclosure_type, enclosure_size_cm, substrate, temp_hot_zone, temp_cool_zone, temp_night, humidity_min, humidity_max, uvb_required, photoperiod_hours, feed_type, feed_frequency, supplements, handling_level, cohabitation_note, updated_at)
SELECT s.id, v.enclosure_type, v.enclosure_size_cm, v.substrate, v.temp_hot_zone, v.temp_cool_zone, v.temp_night, v.humidity_min, v.humidity_max, v.uvb_required, v.photoperiod_hours, v.feed_type, v.feed_frequency, v.supplements, v.handling_level::varchar, v.cohabitation_note, NOW()
FROM (VALUES
    ('Eublepharis macularius', '지상형 유리 테라리움', '60x45x30', '키친타월, 타일, 굴착용 흙', 32.0, 25.0, 22.0, 30, 40, false, 12, '곤충 (귀뚜라미, 밀웜, 두비아)', '성체 주 2-3회, 유체 매일', '칼슘 매 급여, 비타민D3 주 1회', 'EASY', '단독 사육 권장. 수컷끼리 절대 합사 금지'),
    ('Python regius', '지상형 플라스틱 터브/테라리움', '120x60x45', '코코넛 칩, 사이프러스 멀치', 32.0, 26.0, 24.0, 50, 60, false, 12, '설치류 (마우스→래트)', '유체 주 1회, 성체 2주 1회', '별도 보충 불필요', 'EASY', '단독 사육 필수'),
    ('Pogona vitticeps', '반건조 유리 테라리움', '120x60x60', '타일, 모래+흙 혼합', 40.0, 28.0, 22.0, 30, 40, true, 12, '곤충 + 채소 (혼합식)', '유체: 곤충 70% 매일, 성체: 채소 70% 매일', '칼슘+D3 주 3회', 'EASY', '단독 사육 권장. 암수 페어링은 번식 시에만'),
    ('Correlophus ciliatus', '수직형 유리 테라리움', '45x45x60', '코코넛 섬유, 이끼', 26.0, 22.0, 20.0, 60, 80, false, 12, 'CGD(크레스티드 게코 다이어트) + 곤충', 'CGD 격일, 곤충 주 1회', 'CGD에 포함', 'EASY', '암컷끼리 합사 가능. 수컷은 단독'),
    ('Pantherophis guttatus', '지상형 플라스틱 터브/테라리움', '90x45x45', '아스펜 베딩, 코코넛 칩', 30.0, 24.0, 22.0, 40, 60, false, 12, '설치류 (핑키→마우스)', '유체 주 1회, 성체 7-10일 1회', '별도 보충 불필요', 'EASY', '단독 사육 권장'),
    ('Chamaeleo calyptratus', '풀 메쉬 수직형 케이지', '60x60x120', '없음 (바닥재 불필요)', 35.0, 24.0, 18.0, 40, 60, true, 12, '곤충 (귀뚜라미, 두비아, 실크웜)', '성체 격일, 유체 매일', '칼슘 매 급여, D3 월 2회', 'MODERATE', '단독 사육 필수. 시야에 다른 개체 안 보이게'),
    ('Dendrobates tinctorius', '바이오액티브 비바리움', '45x45x45', '열대 흙+이끼+낙엽층', 26.0, 22.0, 20.0, 80, 100, true, 12, '초소형 곤충 (초파리, 스프링테일)', '매일 소량 급여', '칼슘+비타민 더스팅 매 급여', 'MODERATE', '동종 2-3마리 합사 가능. 수컷 간 영역 다툼 주의'),
    ('Ambystoma mexicanum', '수조 (완전 수생)', '60x30 이상 (수심 30cm)', '없음 또는 대형 자갈 (삼킴 방지)', 18.0, 16.0, 14.0, NULL, NULL, false, 12, '냉동 장구벌레, 새우, 지렁이', '성체 2-3일 1회', '별도 보충 불필요', 'EASY', '같은 크기끼리 합사 가능. 크기 차이 시 카니발리즘 주의')
) AS v(scientific_name, enclosure_type, enclosure_size_cm, substrate, temp_hot_zone, temp_cool_zone, temp_night, humidity_min, humidity_max, uvb_required, photoperiod_hours, feed_type, feed_frequency, supplements, handling_level, cohabitation_note)
JOIN species s ON s.scientific_name = v.scientific_name
WHERE NOT EXISTS (SELECT 1 FROM animal_care_guide acg WHERE acg.species_id = s.id);

-- ──────────────────────────────────────────────────────
-- Plant care guide sample data
-- ──────────────────────────────────────────────────────
INSERT INTO plant_care_guide (species_id, pot_type, growing_medium, light_requirement, light_hours_per_day, temp_min, temp_max, humidity_min, humidity_max, watering_frequency, watering_method, fertilizer_type, fertilizer_frequency, repotting_note, pruning_note, overall_note, updated_at)
SELECT s.id, v.pot_type, v.growing_medium, v.light_requirement, v.light_hours_per_day, v.temp_min, v.temp_max, v.humidity_min, v.humidity_max, v.watering_frequency, v.watering_method, v.fertilizer_type, v.fertilizer_frequency, v.repotting_note, v.pruning_note, v.overall_note, NOW()
FROM (VALUES
    -- 다육식물
    ('Echeveria laui',          '소형 토분/테라코타 화분', '다육 전용 배합토 (펄라이트 50% 혼합)', '직사광선 필요', 6::int, 5.0::double precision, 25.0, 20::int, 40::int, '봄-가을 주 1회, 겨울 월 1회', '표면관수 (잎에 닿지 않게)', '다육 전용 액비 (질소 낮은 것)', '봄-가을 월 1회', '2-3년마다 봄에 분갈이 권장', NULL, '과습 주의. 잎에 물 닿으면 반점 생김'),
    ('Haworthiopsis attenuata', '소형 토분', '배수성 좋은 다육 배합토', '반음지 (직사광선 불필요)', 4, 5.0, 22.0, 30, 50, '봄-가을 주 1회, 겨울 월 1회', NULL, '다육 전용 액비', '봄-가을 월 1회', NULL, NULL, '반음지 적응력 우수. 직사광선 피함'),
    ('Aloe vera',               '중형 화분 (안정적인 것)', '선인장/다육 배합토', '직사광선 선호', 6, 5.0, 28.0, 20, 40, '봄-가을 주 1-2회, 겨울 월 1회', '토양 완전 건조 후 급수', '다목적 액비', '봄-가을 2개월 1회', '2-3년마다 봄에', NULL, '뿌리 과습 주의. 통풍 잘 되는 곳'),
    -- 선인장
    ('Echinocactus grusonii',   '토분/토기 화분', '선인장 전용 배합토 (굵은 모래 혼합)', '직사광선 필수', 8, 0.0, 30.0, 10, 30, '봄-가을 주 1-2회, 겨울 완전 단수', '토양 완전 건조 후 충분히', '선인장 전용 비료', '여름 월 1회, 겨울 단수', '3-4년마다 봄에', NULL, '겨울 완전 단수 (5도 이하). 가시 조심'),
    ('Gymnocalycium mihanovichii', '소형 토분', '선인장 배합토', '간접광 (직사광선 화상 주의)', 4, 10.0, 25.0, 20, 40, '봄-여름 주 1회, 겨울 월 1회', NULL, '선인장 전용 비료', '봄-여름 월 1회', NULL, '접목 개체는 대목 상태 정기 확인', '엽록소 없는 변이종 — 접목 개체만 생존 가능. 직사광선 화상 주의'),
    -- 관엽식물
    ('Monstera deliciosa',      '중-대형 화분', '일반 원예용 상토 (배수성 보완)', '밝은 간접광', 6, 15.0, 28.0, 50, 70, '봄-여름 주 1-2회, 겨울 주 1회', '표면 2-3cm 건조 후 급수', '관엽식물 전용 액비 (질소 높은 것)', '봄-가을 격주 1회', '1-2년마다 봄에 한 치수 큰 화분으로', '지지대 필요. 에어루트 그대로 두기 권장', NULL),
    ('Epipremnum aureum',       '소-중형 화분 또는 수경 재배', '일반 상토 또는 수경 재배', '음지 적응 가능 (다양한 환경)', 4, 15.0, 26.0, 40, 60, '주 1-2회 (과습 주의)', '표면 건조 후 급수. 수경 재배 가능', '관엽식물 액비', '봄-가을 월 1-2회', '1-2년마다', NULL, '음지 적응력 최상. 고양이에게 독성 있음'),
    -- 난류
    ('Phalaenopsis amabilis',   '투명 슬릿 화분 (착생란 전용)', '수태 또는 난석 (바크 혼합)', '밝은 간접광 (직사광선 화상)', 10, 18.0, 25.0, 60, 80, '주 1회 (뿌리 건조 확인 후)', '저면관수 또는 하이드로, 잎에 물 안 닿게', '난 전용 액비 (5000배 희석)', '꽃 없는 시기 격주 1회', '2-3년마다 꽃 진 직후', '꽃대 기부에서 자르면 재개화 유도', '뿌리 관찰 용이한 투명 화분 권장. 과습 주의'),
    -- 식충식물
    ('Dionaea muscipula',       '반투명 밀폐 용기 또는 수조', '이탄토 + 펄라이트 1:1 (무비료 필수)', '직사광선 필수', 6, 5.0, 28.0, 70, 90, '저면관수 (항상 물 고여있게)', '증류수/빗물 전용. 저면관수 방식', '비료 절대 금지 (포충엽이 곤충 소화)', '시비 불필요', '2년마다 봄에', '죽은 잎 및 포충엽 제거', '수돗물 미네랄 치명적 — 증류수/빗물만 사용. 겨울 휴면 필요 (5도 이하)'),
    ('Nepenthes alata',         '중형 슬릿 화분 (통풍 중요)', '수태 100% 또는 바크+펄라이트', '밝은 간접광 (직사광선 주의)', 10, 18.0, 26.0, 70, 90, '표면 약간 건조 시 급수', '증류수/빗물 사용', '비료 불필요 (포충낭이 소화)', '시비 불필요', '2년마다', '고사한 포충낭 제거', '고온다습 유지 핵심. 포충낭 내 소화액 건조 주의'),
    -- 수생식물
    ('Nymphaea tetragona',      '수조 또는 연못 (30cm+ 수심)', '수생 식물용 점토 토양', '직사광선 필수', 10, 10.0, 28.0, 60, 80, '수위 유지 (5-10cm 이상)', '수위 유지, 수돗물 사용 가능', '수생 전용 고형 비료 (뿌리 부근 매립)', '봄-가을 2개월 1회', '2-3년마다', '황엽 제거, 꽃 진 후 화경 제거', '직사광선 6시간 이상 필수. 수온 15도 이하 시 휴면'),
    ('Echinodorus grisebachii', '수족관 (수초 어항)', '수초 전용 저면 소일', '중간 밝기 (수중 LED)', 8, 18.0, 26.0, 50, 70, '수위 유지 (주 1회 환수)', '수족관 수위 유지', 'CO2 첨가 + 수초 전용 액비', '주 1-2회 소량', NULL, '황엽 제거, 러너 정리', 'CO2 첨가 시 성장 빠름. 약산성(pH 6.5-7.0) 유지 권장'),
    ('Microsorum pteropus',     '수족관 (착생 수초)', '유목/돌에 실로 고정 (소일 불필요)', '저광량 ~ 중간 밝기', 6, 18.0, 26.0, 50, 70, '수위 유지', '수족관 수위 유지', '액비 소량 (과비료 시 이끼 번짐)', '주 1회 미량', '유목/돌에 새로 묶기 (성장에 따라)', NULL, 'CO2 없이도 잘 자람. 초보 수초 입문 최적. 그늘에 강함'),
    -- 분재
    ('Juniperus chinensis',     '분재 화분 (배수공 필수)', '배수성 최우선 — 적옥토+경석+퇴비 혼합', '직사광선 필수 (야외)', 6, -10.0, 28.0, 40, 60, '흙 표면 건조 시마다', '위에서 충분히, 배수 확인', '분재 전용 유기질 비료', '봄-가을 월 1-2회', '2-3년마다 봄에 수형 정리 동시 진행', '봄 새순 솎아내기, 가을 전정 (수형 유지)', '옥외 월동 필수 (실내 과동 시 쇠약). 병충해 정기 관리'),
    ('Acer palmatum',           '분재 화분 (깊은 화분 권장)', '산성 배합토 (적옥토+부엽토)', '봄-가을 직사광선, 여름 차광 50%', 8, -15.0, 25.0, 50, 70, '봄-여름 하루 1-2회, 가을-겨울 줄임', '위에서 충분히, 배수 확인', '질소 적은 유기질 비료', '봄 꽃눈 전후, 가을 단풍 후', '매년 봄 이식 권장', '여름 강전정 금지. 단풍 후 가지치기', '여름 직사광선 차광 필요. 단풍 위해 일교차 필수')
) AS v(scientific_name, pot_type, growing_medium, light_requirement, light_hours_per_day, temp_min, temp_max, humidity_min, humidity_max, watering_frequency, watering_method, fertilizer_type, fertilizer_frequency, repotting_note, pruning_note, overall_note)
JOIN species s ON s.scientific_name = v.scientific_name
WHERE NOT EXISTS (SELECT 1 FROM plant_care_guide pcg WHERE pcg.species_id = s.id);

-- ──────────────────────────────────────────────────────
-- Variant sample data
-- 파충류: 유전 패턴 변이(모프), 식물: 품종/무늬종(바리에가타 등)
-- ──────────────────────────────────────────────────────
INSERT INTO variant (species_id, name, genetic_pattern, description, image_url)
SELECT s.id, v.name, v.genetic_pattern::varchar, v.description, NULL
FROM (VALUES
    -- 레오파드 게코 모프
    ('Eublepharis macularius', '하이 옐로우', 'LINE_BRED', '전체적으로 밝은 노란색이 강조된 모프'),
    ('Eublepharis macularius', '탠저린', 'LINE_BRED', '짙은 오렌지색 체색'),
    ('Eublepharis macularius', '블리자드', 'RECESSIVE', '패턴 없이 전체가 하얀색 또는 연한 노란색'),
    ('Eublepharis macularius', '알비노 (트렘퍼)', 'RECESSIVE', '멜라닌 부족으로 밝은 체색, 붉은 눈'),
    ('Eublepharis macularius', '맥 스노우', 'CO_DOMINANT', '밝은 흰색/회색 바탕에 검은 패턴'),
    -- 볼 파이썬 모프
    ('Python regius', '파스텔', 'CO_DOMINANT', '밝은 갈색/황금색, 기본 모프의 밝은 버전'),
    ('Python regius', '스파이더', 'DOMINANT', '얇은 검은 패턴이 거미줄 형태'),
    ('Python regius', '바나나', 'CO_DOMINANT', '밝은 노란색 바탕에 라벤더 반점'),
    ('Python regius', '파이볼', 'RECESSIVE', '흰색과 정상 패턴이 불규칙하게 섞임'),
    ('Python regius', '클라운', 'RECESSIVE', '등 중앙에 진한 줄무늬, 옆면 깨끗'),
    -- 비어디 드래곤 모프
    ('Pogona vitticeps', '하이포', 'RECESSIVE', '검은 색소 감소, 밝은 체색'),
    ('Pogona vitticeps', '트랜스', 'RECESSIVE', '반투명한 피부, 검은 눈'),
    ('Pogona vitticeps', '제로', 'RECESSIVE', '패턴 없이 단일 색상'),
    ('Pogona vitticeps', '시트러스', 'LINE_BRED', '밝은 오렌지/노란색 체색'),
    -- 크레스티드 게코 모프
    ('Correlophus ciliatus', '할리퀸', 'LINE_BRED', '측면에 대조적인 밝은 패턴'),
    ('Correlophus ciliatus', '달마시안', 'LINE_BRED', '전체에 검은 점박이 패턴'),
    ('Correlophus ciliatus', '릴리 화이트', 'CO_DOMINANT', '흰색 바탕이 강조된 밝은 모프'),
    -- 콘 스네이크 모프
    ('Pantherophis guttatus', '아멜라니스틱', 'RECESSIVE', '검은 색소 없음, 붉은/오렌지 체색'),
    ('Pantherophis guttatus', '아네리스틱', 'RECESSIVE', '붉은/오렌지 색소 없음, 회색 톤'),
    ('Pantherophis guttatus', '스노우', 'RECESSIVE', '아멜+아네리 콤보, 흰색/핑크색'),
    -- 몬스테라 품종/무늬종 (바리에가타)
    ('Monstera deliciosa', '알보 바리에가타', 'VARIEGATED', '흰색 무늬종. 엽록소 결핍으로 잎에 흰 반점/섹터 발생. 희소성 높음'),
    ('Monstera deliciosa', '타이 컨스텔레이션', 'CULTIVAR', '조직 배양으로 생산된 안정적 무늬종. 별자리처럼 흩뿌려진 크림색 무늬'),
    -- 에케베리아 품종
    ('Echeveria laui', '에케베리아 ''롤라''', 'HYBRID', 'E. lilacina × E. subsessilis 교잡종. 보라빛 도는 핑크-흰색 로제트'),
    -- 파리지옥 품종
    ('Dionaea muscipula', '비너스 레드 드래곤', 'CULTIVAR', '포충엽 내부까지 진적색을 띠는 선발 품종'),
    ('Dionaea muscipula', '퓨전 모어헤드', 'CULTIVAR', '흰색 포충엽 줄무늬 선발 품종. 희소종')
) AS v(scientific_name, name, genetic_pattern, description)
JOIN species s ON s.scientific_name = v.scientific_name
WHERE NOT EXISTS (SELECT 1 FROM variant vt WHERE vt.species_id = s.id AND vt.name = v.name);

-- ──────────────────────────────────────────────────────
-- 커뮤니티 샘플 게시글
-- ──────────────────────────────────────────────────────
INSERT INTO post (author_id, board_type, title, content, image_urls, created_at, updated_at)
SELECT u.id, v.board_type, v.title, v.content, v.image_urls, NOW() - (v.offset_hours || ' hours')::interval, NOW() - (v.offset_hours || ' hours')::interval
FROM (VALUES
    ('user@terrasage.com',  'SHOWCASE',
     '우리집 블리자드 레오게코 자랑합니다 🦎',
     '6개월 전에 분양받은 블리자드 모프 레오게코입니다. 흰 몸에 노란 꼬리가 정말 예쁘더라고요. 지금은 꼬리 굵기도 적당하고 먹이 반응도 아주 좋아요!',
     'https://upload.wikimedia.org/wikipedia/commons/3/33/Leopard_gecko_at_19_days_old.jpg',
     '5'),
    ('admin@terrasage.com', 'TIPS',
     '레오게코 사육 환경 셋업 가이드 (초보자용)',
     E'처음 레오게코를 키우는 분들을 위한 기본 셋업 가이드입니다.\n\n**온도**\n- 핫존: 32-34°C\n- 쿨존: 24-26°C\n- 야간: 20-22°C\n\n**습도**\n- 평소: 30-40%\n- 탈피 시: 70% (웻박스 필수)\n\n**먹이**\n- 귀뚜라미 또는 밀웜 주 2-3회\n- 성체는 주 1-2회도 충분\n- 칼슘제 더스팅 필수',
     NULL,
     '12'),
    ('user@terrasage.com',  'MORPH',
     '레오게코 모프 정리 — ENIGMA부터 RAPTOR까지',
     E'레오게코는 모프 종류가 정말 다양합니다. 주요 모프를 정리해봤어요.\n\n**단일 유전자 모프**\n- TREMPER ALBINO: 눈이 붉고 몸 패턴이 연해짐\n- BLIZZARD: 몸 전체가 흰색/회색, 패턴 없음\n- MACK SNOW: 노란색이 줄고 흰색 증가\n\n**복합 모프**\n- RAPTOR = RUBY EYE + ALBINO + TREMPER ORANGE\n- ENIGMA: 불규칙 패턴, 단 신경계 문제(ES) 주의\n\n**주의할 점**: W&Y(White & Yellow) 계열은 신경계 합사 주의!',
     NULL,
     '24'),
    ('admin@terrasage.com', 'QNA',
     '볼파이썬이 탈피 전에 먹이 거부하는 게 정상인가요?',
     E'볼파이썬 키운 지 3달 됐는데요, 평소엔 냉동쥐를 잘 먹다가 갑자기 2주째 거부합니다.\n눈이 약간 뿌옇게 보이기도 하고... 탈피 전 증상인 건지 걱정되네요.',
     NULL,
     '48'),
    ('user@terrasage.com',  'TIPS',
     '사막 이구아나 사육장 식물 배치 공유합니다',
     E'사막 이구아나 사육장을 꾸밀 때 고민이 많았는데요.\n\n저는 이렇게 구성했어요:\n- 바닥재: 사막 모래 + 파충류 전용 토양 혼합 (60:40)\n- 은신처: 코르크 바크 터널 2개 (핫존/쿨존 각각)\n- 식물: 조화 선인장 (실제 선인장은 찔릴 위험)\n- UVB: 12% 사막 램프 12시간\n\n핫존 온도가 45°C까지 올라가야 해서 할로겐 스팟 필수입니다!',
     NULL,
     '72')
) AS v(email, board_type, title, content, image_urls, offset_hours)
JOIN users u ON u.email = v.email
WHERE NOT EXISTS (SELECT 1 FROM post p WHERE p.title = v.title);

-- 샘플 댓글
INSERT INTO comment (post_id, author_id, parent_id, content, created_at)
SELECT p.id, u.id, NULL, v.content, NOW() - (v.offset_hours || ' hours')::interval
FROM (VALUES
    ('레오게코 사육 환경 셋업 가이드 (초보자용)', 'user@terrasage.com',  '가이드 감사합니다! 웻박스는 어떤 용기 쓰시나요?', '10'),
    ('레오게코 사육 환경 셋업 가이드 (초보자용)', 'admin@terrasage.com', '저는 락앤락 작은 용기에 구멍 뚫어서 씁니다. 안에 코코피트 촉촉하게 넣어두면 좋아요!', '9'),
    ('볼파이썬이 탈피 전에 먹이 거부하는 게 정상인가요?', 'user@terrasage.com', '눈이 뿌예지면 거의 탈피 직전이에요. 1-2주 후면 탈피할 거예요. 먹이 거부는 탈피 전 완전히 정상입니다!', '46'),
    ('볼파이썬이 탈피 전에 먹이 거부하는 게 정상인가요?', 'admin@terrasage.com', '탈피 끝나고 나서 먹이 줘보세요. 탈피 직후에 굉장히 잘 먹어요 ㅎㅎ', '44'),
    ('레오게코 모프 정리 — ENIGMA부터 RAPTOR까지', 'admin@terrasage.com', 'ENIGMA는 예쁜데 ES 확률이 있어서 저도 처음엔 고민했어요. 구매 전 부모 개체 ES 이력 확인 필수!', '22')
) AS v(post_title, email, content, offset_hours)
JOIN post p ON p.title = v.post_title
JOIN users u ON u.email = v.email
WHERE NOT EXISTS (SELECT 1 FROM comment c WHERE c.post_id = p.id AND c.author_id = u.id AND c.content = v.content);

-- ──────────────────────────────────────────────────────
-- 사육환경 관리 샘플 데이터 (카테고리별 개체 + 기록)
--   user@terrasage.com 계정 기준
--   카테고리: REPTILE, AMPHIBIAN, SUCCULENT(식물), AQUATIC_PLANT(수생식물)
-- ──────────────────────────────────────────────────────

-- 개체 등록
INSERT INTO animals (owner_id, species_id, species_name, name, nickname, birth_date, gender, notes, is_public, created_at, updated_at)
SELECT u.id, s.id, NULL, v.name, v.nickname, v.birth_date::date, v.gender, v.notes, true,
       NOW() - (v.days_ago || ' days')::interval, NOW()
FROM (VALUES
    ('user@terrasage.com', 'Eublepharis macularius', '레오 #1',     '콩이', '2023-03-15', 'FEMALE',  '하이 옐로우 모프. 식욕 왕성함.', '30'),
    ('user@terrasage.com', 'Ambystoma mexicanum',    '아홀로 #1',   '무지', '2024-01-20', 'MALE',    '루시스틱. 수질 관리 중요.', '20'),
    ('user@terrasage.com', 'Echeveria laui',          '에케베리아 #1', NULL,  '2023-06-01', 'UNKNOWN', '분갈이 후 뿌리 안착 중.', '10'),
    ('user@terrasage.com', 'Microsorum pteropus',     '자바펀 #1',   NULL,  NULL,         'UNKNOWN', '수조 후경에 고정. CO2 주입 중.', '5')
) AS v(email, scientific_name, name, nickname, birth_date, gender, notes, days_ago)
JOIN users u ON u.email = v.email
JOIN species s ON s.scientific_name = v.scientific_name
WHERE NOT EXISTS (
    SELECT 1 FROM animals a WHERE a.owner_id = u.id AND a.name = v.name
);

-- 사육 기록 (개체별 3~5개, 차트 표시용)
INSERT INTO care_records (animal_id, recorded_at, temperature, humidity, light_hours, weight, feed_type, feed_amount, notes, created_at)
SELECT a.id,
       NOW() - (v.days_ago || ' days')::interval,
       v.temperature, v.humidity, v.light_hours, v.weight, v.feed_type, v.feed_amount, v.notes,
       NOW()
FROM (VALUES
    -- 레오 #1 (파충류 — 온도/습도/체중 추이 확인)
    ('레오 #1', '12', 31.5, 34.0, 12.0,  61.0, '귀뚜라미', '5마리', NULL),
    ('레오 #1', '9',  32.0, 35.0, 12.0,  62.5, '두비아',   '4마리', '식욕 좋음'),
    ('레오 #1', '6',  32.5, 36.0, 12.0,  63.0, '귀뚜라미', '5마리', '탈피 준비 중'),
    ('레오 #1', '3',  31.0, 33.0, 12.0,  64.5, '두비아',   '4마리', '탈피 완료'),
    ('레오 #1', '0',  32.0, 35.0, 12.0,  65.0, '귀뚜라미', '5마리', NULL),
    -- 아홀로 #1 (양서류 — 수온/체중)
    ('아홀로 #1', '10', 18.5, NULL, NULL, 182.0, '냉동 장구벌레', '5ml', '수질 정상'),
    ('아홀로 #1', '7',  18.0, NULL, NULL, 185.0, '지렁이',        '1마리', NULL),
    ('아홀로 #1', '4',  17.5, NULL, NULL, 187.0, '냉동 장구벌레', '5ml', '수질 양호'),
    ('아홀로 #1', '1',  18.0, NULL, NULL, 190.0, '지렁이',        '1마리', NULL),
    -- 에케베리아 #1 (식물 — 온도/습도/광량)
    ('에케베리아 #1', '14', 24.0, 45.0, 8.0, NULL, '물주기', '저면관수', NULL),
    ('에케베리아 #1', '10', 25.0, 42.0, 8.0, NULL, NULL,     NULL,     '새 잎 확인'),
    ('에케베리아 #1', '6',  23.0, 50.0, 8.0, NULL, '물주기', '저면관수', '겉흙 건조 후 급수'),
    ('에케베리아 #1', '2',  24.5, 44.0, 8.0, NULL, NULL,     NULL,     '성장 양호'),
    -- 자바펀 #1 (수생식물 — 수온/광량)
    ('자바펀 #1', '8', 25.0, NULL, 10.0, NULL, NULL, NULL, 'CO2 주입 정상'),
    ('자바펀 #1', '5', 25.5, NULL, 10.0, NULL, NULL, NULL, '수초 비료 소량 첨가'),
    ('자바펀 #1', '2', 24.5, NULL, 10.0, NULL, NULL, NULL, '성장 양호, 새 잎 전개')
) AS v(animal_name, days_ago, temperature, humidity, light_hours, weight, feed_type, feed_amount, notes)
JOIN animals a ON a.name = v.animal_name
WHERE NOT EXISTS (
    SELECT 1 FROM care_records cr WHERE cr.animal_id = a.id
);
