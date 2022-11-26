-- insert user
INSERT INTO t_user(user_username, user_password, user_name) VALUES ('gyunam', '$2a$10$LCdnGJ4ssH0o1.h47gSsleUBitnRFjz2ecbuYzEwMRmU..xqdXQmq', '규남');
INSERT INTO t_user(user_username, user_password, user_name) VALUES ('minsoo', '$2a$10$LCdnGJ4ssH0o1.h47gSsleUBitnRFjz2ecbuYzEwMRmU..xqdXQmq', '민수');
INSERT INTO t_user(user_username, user_password, user_name) VALUES ('gildong', '$2a$10$LCdnGJ4ssH0o1.h47gSsleUBitnRFjz2ecbuYzEwMRmU..xqdXQmq', '길동');

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
INSERT INTO t_trip(trip_start_date, trip_end_date, town_id, user_id) VALUES ('2022-11-20', '2022-11-22', 1, 1);
INSERT INTO t_trip(trip_start_date, trip_end_date, town_id, user_id) VALUES ('2022-11-23', '2022-11-24', 6, 1);
INSERT INTO t_trip(trip_start_date, trip_end_date, town_id, user_id) VALUES ('2022-11-25', '2022-11-26', 7, 1);
INSERT INTO t_trip(trip_start_date, trip_end_date, town_id, user_id) VALUES ('2022-11-26', '2022-11-29', 3, 1);
INSERT INTO t_trip(trip_start_date, trip_end_date, town_id, user_id) VALUES ('2022-11-30', '2022-12-03', 2, 1);

INSERT INTO t_trip(trip_start_date, trip_end_date, town_id, user_id) VALUES ('2023-01-01', '2023-01-20', 3, 1);

INSERT INTO t_trip(trip_start_date, trip_end_date, town_id, user_id) VALUES ('2022-12-20', '2022-12-28', 1, 2);
INSERT INTO t_trip(trip_start_date, trip_end_date, town_id, user_id) VALUES ('2022-11-30', '2022-12-03', 2, 2);
INSERT INTO t_trip(trip_start_date, trip_end_date, town_id, user_id) VALUES ('2023-02-01', '2023-02-20', 3, 2);

INSERT INTO t_trip(trip_start_date, trip_end_date, town_id, user_id) VALUES ('2023-03-20', '2022-04-28', 1, 3);
INSERT INTO t_trip(trip_start_date, trip_end_date, town_id, user_id) VALUES ('2023-01-01', '2023-01-20', 3, 3);

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
