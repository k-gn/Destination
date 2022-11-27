-- insert user
INSERT INTO t_user(user_username, user_password, user_name, user_role) VALUES ('gyunam', '$2a$10$LCdnGJ4ssH0o1.h47gSsleUBitnRFjz2ecbuYzEwMRmU..xqdXQmq', '규남', 'ROLE_USER');
INSERT INTO t_user(user_username, user_password, user_name, user_role) VALUES ('minsoo', '$2a$10$LCdnGJ4ssH0o1.h47gSsleUBitnRFjz2ecbuYzEwMRmU..xqdXQmq', '민수', 'ROLE_USER');
INSERT INTO t_user(user_username, user_password, user_name, user_role) VALUES ('gildong', '$2a$10$LCdnGJ4ssH0o1.h47gSsleUBitnRFjz2ecbuYzEwMRmU..xqdXQmq', '길동', 'ROLE_USER');

-- insert town
INSERT INTO t_town(town_country, town_name, town_code) VALUES ('대한민국', '부산', 1);
INSERT INTO t_town(town_country, town_name, town_code) VALUES ('대한민국', '서울', 2);
INSERT INTO t_town(town_country, town_name, town_code) VALUES ('대한민국', '대구', 3);
INSERT INTO t_town(town_country, town_name, town_code) VALUES ('대한민국', '대전', 4);
INSERT INTO t_town(town_country, town_name, town_code) VALUES ('대한민국', '제주', 5);
INSERT INTO t_town(town_country, town_name, town_code) VALUES ('대한민국', '수원', 6);
INSERT INTO t_town(town_country, town_name, town_code) VALUES ('대한민국', '광주', 7);
INSERT INTO t_town(town_country, town_name, town_code) VALUES ('대한민국', '전북', 8);
INSERT INTO t_town(town_country, town_name, town_code) VALUES ('대한민국', '충북', 9);
INSERT INTO t_town(town_country, town_name, town_code) VALUES ('대한민국', '충남', 10);
INSERT INTO t_town(town_country, town_name, town_code) VALUES ('대한민국', '전남', 11);
INSERT INTO t_town(town_country, town_name, town_code) VALUES ('대한민국', '포항', 12);

-- insert trip
INSERT INTO t_trip(trip_start_date, trip_end_date, town_id, user_id) VALUES (DATEADD(day, -10, CURRENT_TIMESTAMP), DATEADD(day, -5, CURRENT_TIMESTAMP), 1, 1);
INSERT INTO t_trip(trip_start_date, trip_end_date, town_id, user_id) VALUES (DATEADD(day, -1, CURRENT_TIMESTAMP), DATEADD(day, 5, CURRENT_TIMESTAMP), 3, 1);
INSERT INTO t_trip(trip_start_date, trip_end_date, town_id, user_id) VALUES (DATEADD(day, 10, CURRENT_TIMESTAMP), DATEADD(day, 15, CURRENT_TIMESTAMP), 5, 1);

INSERT INTO t_trip(trip_start_date, trip_end_date, town_id, user_id) VALUES (DATEADD(day, 5, CURRENT_TIMESTAMP), DATEADD(day, 10, CURRENT_TIMESTAMP), 1, 2);
INSERT INTO t_trip(trip_start_date, trip_end_date, town_id, user_id) VALUES (DATEADD(day, 15, CURRENT_TIMESTAMP), DATEADD(day, 20, CURRENT_TIMESTAMP), 2, 2);
INSERT INTO t_trip(trip_start_date, trip_end_date, town_id, user_id) VALUES (DATEADD(day, 25, CURRENT_TIMESTAMP), DATEADD(day, 30, CURRENT_TIMESTAMP), 3, 2);

INSERT INTO t_trip(trip_start_date, trip_end_date, town_id, user_id) VALUES (DATEADD(day, 10, CURRENT_TIMESTAMP), DATEADD(day, 15, CURRENT_TIMESTAMP), 1, 3);
INSERT INTO t_trip(trip_start_date, trip_end_date, town_id, user_id) VALUES (DATEADD(day, 20, CURRENT_TIMESTAMP), DATEADD(day, 30, CURRENT_TIMESTAMP), 3, 3);

-- insert search
INSERT INTO t_user_search(user_id, town_id) VALUES (1, 7);
INSERT INTO t_user_search(user_id, town_id) VALUES (1, 3);
INSERT INTO t_user_search(user_id, town_id) VALUES (1, 9);

INSERT INTO t_user_search(user_id, town_id) VALUES (2, 6);
INSERT INTO t_user_search(user_id, town_id) VALUES (2, 4);

INSERT INTO t_user_search(user_id, town_id) VALUES (3, 5);
INSERT INTO t_user_search(user_id, town_id) VALUES (3, 1);
INSERT INTO t_user_search(user_id, town_id) VALUES (3, 4);
INSERT INTO t_user_search(user_id, town_id) VALUES (3, 2);
