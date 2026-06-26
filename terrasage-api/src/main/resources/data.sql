-- Species sample data
INSERT INTO species (scientific_name, common_name_ko, common_name_en, kingdom, phylum, taxonomy_class, taxonomy_order, family, genus, origin, habitat, lifespan_captive, lifespan_wild, avg_size_cm, avg_weightg, difficulty_level, cites_level, legal_status_note, status, created_at, updated_at)
VALUES
    ('Eublepharis macularius', '레오파드 게코', 'Leopard Gecko', 'Animalia', 'Chordata', 'Reptilia', 'Squamata', 'Eublepharidae', 'Eublepharis', '파키스탄, 인도, 아프가니스탄', '건조한 암석 사막, 초원', 20, 15, 22.0, 65.0, 'BEGINNER', NULL, NULL, 'PUBLISHED', NOW(), NOW()),
    ('Python regius', '볼 파이썬', 'Ball Python', 'Animalia', 'Chordata', 'Reptilia', 'Squamata', 'Pythonidae', 'Python', '서아프리카, 중앙아프리카', '사바나, 초원, 열대림 가장자리', 30, 10, 150.0, 1800.0, 'BEGINNER', 'APPENDIX_II', NULL, 'PUBLISHED', NOW(), NOW()),
    ('Pogona vitticeps', '비어디 드래곤', 'Bearded Dragon', 'Animalia', 'Chordata', 'Reptilia', 'Squamata', 'Agamidae', 'Pogona', '호주 내륙', '건조한 삼림, 사막, 관목지', 12, 8, 55.0, 450.0, 'BEGINNER', NULL, NULL, 'PUBLISHED', NOW(), NOW()),
    ('Correlophus ciliatus', '크레스티드 게코', 'Crested Gecko', 'Animalia', 'Chordata', 'Reptilia', 'Squamata', 'Diplodactylidae', 'Correlophus', '뉴칼레도니아', '열대 우림', 15, 10, 20.0, 45.0, 'BEGINNER', NULL, NULL, 'PUBLISHED', NOW(), NOW()),
    ('Pantherophis guttatus', '콘 스네이크', 'Corn Snake', 'Animalia', 'Chordata', 'Reptilia', 'Squamata', 'Colubridae', 'Pantherophis', '북미 동부', '낙엽수림, 농경지, 초원', 20, 8, 150.0, 900.0, 'BEGINNER', NULL, NULL, 'PUBLISHED', NOW(), NOW()),
    ('Chamaeleo calyptratus', '베일드 카멜레온', 'Veiled Chameleon', 'Animalia', 'Chordata', 'Reptilia', 'Squamata', 'Chamaeleonidae', 'Chamaeleo', '예멘, 사우디아라비아', '고지대 관목, 계곡', 7, 5, 55.0, 170.0, 'INTERMEDIATE', 'APPENDIX_II', NULL, 'PUBLISHED', NOW(), NOW()),
    ('Morelia spilota', '카펫 파이썬', 'Carpet Python', 'Animalia', 'Chordata', 'Reptilia', 'Squamata', 'Pythonidae', 'Morelia', '호주, 뉴기니', '열대림, 삼림, 도시 근교', 20, 15, 250.0, 3500.0, 'INTERMEDIATE', 'APPENDIX_II', NULL, 'PUBLISHED', NOW(), NOW()),
    ('Varanus acanthurus', '리지테일 모니터', 'Ridge-tailed Monitor', 'Animalia', 'Chordata', 'Reptilia', 'Squamata', 'Varanidae', 'Varanus', '호주 북부', '건조한 암석 지대', 15, 10, 70.0, 300.0, 'INTERMEDIATE', 'APPENDIX_II', NULL, 'PUBLISHED', NOW(), NOW()),
    ('Boa constrictor', '보아 컨스트릭터', 'Boa Constrictor', 'Animalia', 'Chordata', 'Reptilia', 'Squamata', 'Boidae', 'Boa', '중남미', '열대림, 사바나', 30, 20, 300.0, 15000.0, 'ADVANCED', 'APPENDIX_II', NULL, 'PUBLISHED', NOW(), NOW()),
    ('Dendrobates tinctorius', '다이잉 포이즌 프로그', 'Dyeing Poison Dart Frog', 'Animalia', 'Chordata', 'Amphibia', 'Anura', 'Dendrobatidae', 'Dendrobates', '남미 북부', '열대 우림 바닥', 10, 5, 5.0, 4.0, 'ADVANCED', 'APPENDIX_II', NULL, 'PUBLISHED', NOW(), NOW()),
    ('Ambystoma mexicanum', '아홀로틀', 'Axolotl', 'Animalia', 'Chordata', 'Amphibia', 'Urodela', 'Ambystomatidae', 'Ambystoma', '멕시코 소치밀코 호수', '담수 호수, 운하', 15, 10, 25.0, 200.0, 'INTERMEDIATE', 'APPENDIX_II', '멕시코 야생 개체 CITES 부속서 II', 'PUBLISHED', NOW(), NOW()),
    ('Testudo hermanni', '헤르만 육지거북', 'Hermann''s Tortoise', 'Animalia', 'Chordata', 'Reptilia', 'Testudines', 'Testudinidae', 'Testudo', '남유럽 (지중해 연안)', '지중해성 관목림, 초원', 75, 30, 25.0, 3500.0, 'INTERMEDIATE', 'APPENDIX_II', NULL, 'PUBLISHED', NOW(), NOW())
ON CONFLICT (scientific_name) DO NOTHING;

-- Care guide sample data
INSERT INTO care_guide (species_id, enclosure_type, enclosure_size_cm, substrate, temp_hot_zone, temp_cool_zone, temp_night, humidity_min, humidity_max, uvb_required, photoperiod_hours, feed_type, feed_frequency, supplements, handling_level, cohabitation_note, updated_at)
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
WHERE NOT EXISTS (SELECT 1 FROM care_guide cg WHERE cg.species_id = s.id);

-- Morph sample data
INSERT INTO morph (species_id, name, genetic_pattern, description, image_url)
SELECT s.id, v.name, v.genetic_pattern::varchar, v.description, NULL
FROM (VALUES
    ('Eublepharis macularius', '하이 옐로우', 'LINE_BRED', '전체적으로 밝은 노란색이 강조된 모프'),
    ('Eublepharis macularius', '탠저린', 'LINE_BRED', '짙은 오렌지색 체색'),
    ('Eublepharis macularius', '블리자드', 'RECESSIVE', '패턴 없이 전체가 하얀색 또는 연한 노란색'),
    ('Eublepharis macularius', '알비노 (트렘퍼)', 'RECESSIVE', '멜라닌 부족으로 밝은 체색, 붉은 눈'),
    ('Eublepharis macularius', '맥 스노우', 'CO_DOMINANT', '밝은 흰색/회색 바탕에 검은 패턴'),
    ('Python regius', '파스텔', 'CO_DOMINANT', '밝은 갈색/황금색, 기본 모프의 밝은 버전'),
    ('Python regius', '스파이더', 'DOMINANT', '얇은 검은 패턴이 거미줄 형태'),
    ('Python regius', '바나나', 'CO_DOMINANT', '밝은 노란색 바탕에 라벤더 반점'),
    ('Python regius', '파이볼', 'RECESSIVE', '흰색과 정상 패턴이 불규칙하게 섞임'),
    ('Python regius', '클라운', 'RECESSIVE', '등 중앙에 진한 줄무늬, 옆면 깨끗'),
    ('Pogona vitticeps', '하이포', 'RECESSIVE', '검은 색소 감소, 밝은 체색'),
    ('Pogona vitticeps', '트랜스', 'RECESSIVE', '반투명한 피부, 검은 눈'),
    ('Pogona vitticeps', '제로', 'RECESSIVE', '패턴 없이 단일 색상'),
    ('Pogona vitticeps', '시트러스', 'LINE_BRED', '밝은 오렌지/노란색 체색'),
    ('Correlophus ciliatus', '할리퀸', 'LINE_BRED', '측면에 대조적인 밝은 패턴'),
    ('Correlophus ciliatus', '달마시안', 'LINE_BRED', '전체에 검은 점박이 패턴'),
    ('Correlophus ciliatus', '릴리 화이트', 'CO_DOMINANT', '흰색 바탕이 강조된 밝은 모프'),
    ('Pantherophis guttatus', '아멜라니스틱', 'RECESSIVE', '검은 색소 없음, 붉은/오렌지 체색'),
    ('Pantherophis guttatus', '아네리스틱', 'RECESSIVE', '붉은/오렌지 색소 없음, 회색 톤'),
    ('Pantherophis guttatus', '스노우', 'RECESSIVE', '아멜+아네리 콤보, 흰색/핑크색')
) AS v(scientific_name, name, genetic_pattern, description)
JOIN species s ON s.scientific_name = v.scientific_name
WHERE NOT EXISTS (SELECT 1 FROM morph m WHERE m.species_id = s.id AND m.name = v.name);
