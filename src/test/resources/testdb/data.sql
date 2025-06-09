SET foreign_key_checks = 0;

insert into timetable (end_time, performance_date, start_time, id, performance_id, performance_hall)
values  ('22:00:00', '2024-05-24', '18:00:00', 1, 1, '88잔디마당'),
        ('23:00:00', '2024-07-15', '14:00:00', 2, 2, '메인 스테이지'),
        ('22:00:00', '2024-09-20', '15:00:00', 3, 3, '메인 스테이지');

insert into artist_alias (artist_id, id, name)
values  (1, 1, '재즈의 제왕'),
        (2, 2, '록의 전설'),
        (3, 3, '인디의 신동'),
        (4, 4, '피아노의 마에스트로'),
        (5, 5, 'K-POP의 퀸'),
        (6, 6, '비트의 지배자'),
        (7, 7, '포크의 전설'),
        (8, 8, '사운드의 마법사');

insert into artist (id, description, display_name)
values  (1, '재즈 피아니스트, 20년 경력의 베테랑', '김재즈'),
        (2, '록 밴드 ''스톰''의 보컬리스트', '이록'),
        (3, '인디 밴드 ''새벽''의 기타리스트', '박인디'),
        (4, '클래식 피아니스트, 쇼팽 콩쿠르 우승자', '최클래식'),
        (5, 'K-POP 그룹 ''스타라이트''의 메인 보컬', '정팝'),
        (6, '힙합 아티스트, ''비트마스터'' 크루 리더', '강힙합'),
        (7, '포크 가수, 10년 경력의 싱어송라이터', '윤포크'),
        (8, '일렉트로닉 음악 프로듀서, ''사운드랩'' 대표', '장일렉트로닉');

insert into performance_artist (performance_date, artist_id, id, performance_id)
values  ('2024-05-24', 1, 1, 1),
        ('2024-05-24', 2, 2, 1),
        ('2024-07-15', 3, 3, 2),
        ('2024-07-15', 4, 4, 2),
        ('2024-07-15', 5, 5, 2),
        ('2024-09-20', 6, 6, 3),
        ('2024-09-20', 7, 7, 3),
        ('2024-09-20', 8, 8, 3);

insert into performance (end_date, start_date, id, place_address, poster_url, name, place_name, ban_goods, remark, transportation_info)
values  ('2024-05-26', '2024-05-24', 1, '서울특별시 송파구 올림픽로 424', 'https://example.com/seoul-jazz-2024.jpg', '2024 서울 재즈 페스티벌', '올림픽공원', '음식물, 음료, 촬영장비', '우천시 실내공연장으로 변경될 수 있습니다.', '5호선 올림픽공원역 3번 출구'),
        ('2024-07-17', '2024-07-15', 2, '부산광역시 사상구 삼락동 123', 'https://example.com/busan-rock-2024.jpg', '부산 록 페스티벌', '삼락생태공원', '유리병, 캔, 촬영장비', '주차장이 제한적이니 대중교통 이용을 권장합니다.', '2호선 사상역 1번 출구'),
        ('2024-09-22', '2024-09-20', 3, '서울특별시 마포구 상암동 495', 'https://example.com/indie-music-2024.jpg', '인디뮤직 페스티벌', '난지한강공원', '음식물, 음료, 촬영장비', '야외 공연장이므로 우의를 준비하세요.', '6호선 월드컵경기장역 1번 출구');

insert into reservation_info (close_date_time, id, open_date_time, performance_id, ticketurl, remark, type)
values  ('2024-05-23 23:59:59.000000', 1, '2024-04-01 10:00:00.000000', 1, 'https://ticket.example.com/seoul-jazz-2024', '3일권 티켓 구매 가능', 'GENERAL'),
        ('2024-07-14 23:59:59.000000', 2, '2024-06-01 10:00:00.000000', 2, 'https://ticket.example.com/busan-rock-2024', '1일권, 3일권 티켓 구매 가능', 'GENERAL'),
        ('2024-09-19 23:59:59.000000', 3, '2024-08-01 10:00:00.000000', 3, 'https://ticket.example.com/indie-music-2024', '1일권, 3일권 티켓 구매 가능', 'GENERAL');

insert into timetable_artist (artist_id, id, timetable_id, participation_type)
values  (1, 1, 1, 'MAIN'),
        (2, 2, 1, 'SUB'),
        (3, 3, 2, 'MAIN'),
        (4, 4, 2, 'SUB'),
        (5, 5, 2, 'SUB'),
        (6, 6, 3, 'MAIN'),
        (7, 7, 3, 'SUB'),
        (8, 8, 3, 'SUB');

SET foreign_key_checks = 1;