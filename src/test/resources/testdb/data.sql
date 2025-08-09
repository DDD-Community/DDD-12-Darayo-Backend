SET foreign_key_checks = 0;

-- 1. 데이터 삭제(외래키 역순)
-- DELETE FROM timetable_artist;
-- DELETE FROM timetable;
-- DELETE FROM reservation_info;
-- DELETE FROM performanceurl;
-- DELETE FROM performance;
-- DELETE FROM performance_hall;
-- DELETE FROM performance_place;
-- DELETE FROM artist_alias;
-- DELETE FROM artist;
-- DELETE FROM user_performance_alarm;
-- DELETE FROM user_alarm_token;
-- DELETE FROM user;

-- 2. 공연 장소 (performance_place)
INSERT INTO performance_place (id, address, name) VALUES
  (1, '서울특별시 송파구 올림픽로 424', '올림픽공원'),
  (2, '부산광역시 사상구 삼락동 123', '삼락생태공원'),
  (3, '서울특별시 마포구 상암동 495', '난지한강공원');

-- 3. 공연장 (performance_hall)
INSERT INTO performance_hall (id, place_id, name) VALUES
  (1, 1, '88잔디마당'),
  (2, 2, '메인 스테이지'),
  (3, 3, '메인 스테이지');

-- 4. 아티스트 (artist)
INSERT INTO artist (id, description, display_name) VALUES
  (1, '재즈 피아니스트, 20년 경력의 베테랑', '김재즈'),
  (2, '록 밴드 ''스톰''의 보컬리스트', '이록'),
  (3, '인디 밴드 ''새벽''의 기타리스트', '박인디'),
  (4, '클래식 피아니스트, 쇼팽 콩쿠르 우승자', '최클래식'),
  (5, 'K-POP 그룹 ''스타라이트''의 메인 보컬', '정팝'),
  (6, '힙합 아티스트, ''비트마스터'' 크루 리더', '강힙합'),
  (7, '포크 가수, 10년 경력의 싱어송라이터', '윤포크'),
  (8, '일렉트로닉 음악 프로듀서, ''사운드랩'' 대표', '장일렉트로닉');

-- 5. 아티스트 별명 (artist_alias)
INSERT INTO artist_alias (id, artist_id, name) VALUES
  (1, 1, '재즈의 제왕'),
  (2, 2, '록의 전설'),
  (3, 3, '인디의 신동'),
  (4, 4, '피아노의 마에스트로'),
  (5, 5, 'K-POP의 퀸'),
  (6, 6, '비트의 지배자'),
  (7, 7, '포크의 전설'),
  (8, 8, '사운드의 마법사');

-- 6. 공연 (performance)
INSERT INTO performance (id, start_date, end_date, place_id, poster_url, name, ban_goods, remark, transportation_info) VALUES
  (1, '2024-05-24', '2024-05-26', 1, 'https://example.com/seoul-jazz-2024.jpg', '2024 서울 재즈 페스티벌', '음식물, 음료, 촬영장비', '우천시 실내공연장으로 변경될 수 있습니다.', '5호선 올림픽공원역 3번 출구'),
  (2, '2024-07-15', '2024-07-17', 2, 'https://example.com/busan-rock-2024.jpg', '부산 록 페스티벌', '유리병, 캔, 촬영장비', '주차장이 제한적이니 대중교통 이용을 권장합니다.', '2호선 사상역 1번 출구'),
  (3, '2024-09-20', '2024-09-22', 3, 'https://example.com/indie-music-2024.jpg', '인디뮤직 페스티벌', '음식물, 음료, 촬영장비', '야외 공연장이므로 우의를 준비하세요.', '6호선 월드컵경기장역 1번 출구'),
  (4, '2024-08-15', '2024-08-17', 1, 'https://example.com/summer-fest-2024.jpg', '썸머 페스티벌', '음식물, 음료, 촬영장비', '우천시에도 진행됩니다.', '5호선 올림픽공원역 3번 출구'),
  (5, '2024-08-20', '2024-08-22', 2, 'https://example.com/hiphop-fest-2024.jpg', '힙합 페스티벌', '음식물, 음료, 촬영장비', '스탠딩석 운영', '2호선 사상역 1번 출구'),
  (6, '2024-08-25', '2024-08-27', 3, 'https://example.com/edm-fest-2024.jpg', 'EDM 페스티벌', '음식물, 음료, 촬영장비', '우천시에도 진행됩니다.', '6호선 월드컵경기장역 1번 출구');

-- 7. 공연 URL (performanceurl)
INSERT INTO performanceurl (id, performance_id, type, url) VALUES
  (1, 1, 0, 'https://info.example.com/seoul-jazz-2024'),
  (2, 2, 0, 'https://info.example.com/busan-rock-2024'),
  (3, 3, 0, 'https://info.example.com/indie-music-2024');


-- 8. 예매 정보 (reservation_info)
INSERT INTO reservation_info (id, performance_id, open_date_time, close_date_time, ticketurl, remark, type, open_time_modified_at) VALUES
  -- 19:00 이전 (오전/오후/경계)
  (1, 1, '2024-04-01 10:00:00.000000', '2024-05-23 23:59:59.000000', 'https://ticket.example.com/seoul-jazz-2024', '3일권 티켓 구매 가능', 'GENERAL', '2024-04-01 05:30:00.000000'),
  (2, 2, '2024-06-01 10:00:00.000000', '2024-07-14 23:59:59.000000', 'https://ticket.example.com/busan-rock-2024', '1일권, 3일권 티켓 구매 가능', 'GENERAL', '2024-06-01 07:00:00.000000'),
  (3, 3, '2024-08-01 10:00:00.000000', '2024-09-19 23:59:59.000000', 'https://ticket.example.com/indie-music-2024', '1일권, 3일권 티켓 구매 가능', 'GENERAL', '2024-08-01 11:30:00.000000'),
  (4, 4, '2024-06-02 10:00:00.000000', '2024-08-14 23:59:59.000000', 'https://ticket.example.com/summer-fest-2024/early', '얼리버드 티켓 (팬클럽)', 'EARLY_BIRD', '2024-06-02 12:00:00.000000'),
  (5, 5, '2024-06-02 10:00:00.000000', '2024-08-19 23:59:59.000000', 'https://ticket.example.com/hiphop-fest-2024/early', '얼리버드 티켓 (팬클럽)', 'EARLY_BIRD', '2024-06-02 15:45:00.000000'),
  (6, 6, '2024-06-02 10:00:00.000000', '2024-08-24 23:59:59.000000', 'https://ticket.example.com/edm-fest-2024/early', '얼리버드 티켓 (팬클럽)', 'EARLY_BIRD', '2024-06-02 18:30:00.000000'),
  (7, 4, '2024-06-04 10:00:00.000000', '2024-08-14 23:59:59.000000', 'https://ticket.example.com/summer-fest-2024/early2', '얼리버드 2차 (일반)', 'EARLY_BIRD', '2024-06-04 18:59:59.000000'),

  -- 19:00:00 (정확히)
  (8, 5, '2024-06-04 10:00:00.000000', '2024-08-19 23:59:59.000000', 'https://ticket.example.com/hiphop-fest-2024/early2', '얼리버드 2차 (일반)', 'EARLY_BIRD', '2024-06-04 19:00:00.000000'),

  -- 19:00 이후 (저녁/밤/자정/경계)
  (9, 4, '2024-06-08 10:00:00.000000', '2024-08-14 23:59:59.000000', 'https://ticket.example.com/summer-fest-2024', '일반 예매', 'GENERAL', '2024-06-08 19:00:01.000000'),
  (10, 5, '2024-06-08 10:00:00.000000', '2024-08-19 23:59:59.000000', 'https://ticket.example.com/hiphop-fest-2024', '일반 예매', 'GENERAL', '2024-06-08 20:15:00.000000'),
  (11, 6, '2024-06-08 10:00:00.000000', '2024-08-24 23:59:59.000000', 'https://ticket.example.com/edm-fest-2024', '일반 예매', 'GENERAL', '2024-06-08 22:45:00.000000'),
  (12, 4, '2024-06-15 10:00:00.000000', '2024-08-14 23:59:59.000000', 'https://ticket.example.com/summer-fest-2024/onsite', '현장 예매', 'GENERAL', '2024-06-15 23:59:00.000000'),
  (13, 5, '2024-06-15 10:00:00.000000', '2024-08-19 23:59:59.000000', 'https://ticket.example.com/hiphop-fest-2024/onsite', '현장 예매', 'GENERAL', '2024-06-15 00:15:00.000000'),
  (14, 6, '2024-06-15 10:00:00.000000', '2024-08-24 23:59:59.000000', 'https://ticket.example.com/edm-fest-2024/onsite', '현장 예매', 'GENERAL', '2024-06-15 01:00:00.000000');

-- 9. 타임테이블 (timetable)
INSERT INTO timetable (id, performance_id, hall_id, performance_date, start_time, end_time) VALUES
  (1, 1, 1, '2024-05-24', '18:00:00', '22:00:00'),
  (2, 2, 2, '2024-07-15', '14:00:00', '23:00:00'),
  (3, 3, 3, '2024-09-20', '15:00:00', '22:00:00');

-- 10. 타임테이블-아티스트 매핑 (timetable_artist)
INSERT INTO timetable_artist (id, timetable_id, artist_id, participation_type) VALUES
  (1, 1, 1, 'MAIN'),
  (2, 1, 2, 'SUB'),
  (3, 2, 3, 'MAIN'),
  (4, 2, 4, 'SUB'),
  (5, 2, 5, 'SUB'),
  (6, 3, 6, 'MAIN'),
  (7, 3, 7, 'SUB'),
  (8, 3, 8, 'SUB');

-- 11. 사용자 (users)
INSERT INTO user (id, is_alarm_allowed, last_login_at, provider, provider_user_id) VALUES
  (1, b'1', '2024-05-01 10:00:00.000000', 'DEVICE', 'user1'),
  (2, b'1', '2024-05-02 11:00:00.000000', 'DEVICE', 'user2'),
  (3, b'0', '2024-05-02 11:00:00.000000', 'DEVICE', 'user2'),
  (4, b'1', '2024-05-02 11:00:00.000000', 'DEVICE', 'user2'),
  (5, b'1', '2024-05-02 11:00:00.000000', 'DEVICE', 'user5');


-- 12. 사용자 알람 토큰 (user_alarm_token)
INSERT INTO user_alarm_token (id, alarm_token, expired_at, is_valid, user_id, updated_at) VALUES
  (null, 'token-1-1', '2024-12-31 23:59:59.000000', b'1', 1, '2024-12-31 23:59:59.000000'),
  (null, 'token-2-1', '2024-12-31 23:59:59.000000', b'1', 2, '2024-12-31 23:59:59.000000'),
  (null, 'token-3-1', '2024-12-31 23:59:59.000000', b'1', 3, '2024-12-31 23:59:59.000000'),
  (null, 'token-4-1', '2024-12-31 23:59:59.000000', b'0', 4, '2024-12-31 23:59:59.000000'),
  (null, 'token-1-2', '2024-12-31 23:59:59.000000', b'0', 1, '2024-12-31 23:59:59.000000');

-- 13. 사용자 공연 알람 (user_performance_alarm)
INSERT INTO user_performance_alarm (id, target_id, type, user_id) VALUES
  (1, 1, 0, 1),
  (2, 2, 0, 2),
  (3, 3, 0, 2),
  (4, 1,0,2),
  (5, 2, 0, 1),
  (6,3, 0, 3),
  (7, 4, 0,4)
;

SET foreign_key_checks = 1;