create table checkuser_info
(
    id              INTEGER      not null
        primary key autoincrement,
    user_id         INTEGER      not null,
    score           INTEGER       default 0 not null,
    lastCheckInTime varchar(255) not null,
    flow            INTEGER       default 0,
    days            decimal(4, 2) default 0,
    big_luck        Integer       default 0
);

create unique index checkuser_info_user_id_uindex
    on checkuser_info (user_id);

INSERT INTO checkuser_info (id, user_id, score, lastCheckInTime, flow, days, big_luck) VALUES (1, 1388874184, 0, '2021-09-07', 0, 0.00, 0);
INSERT INTO checkuser_info (id, user_id, score, lastCheckInTime, flow, days, big_luck) VALUES (2, 2018426990, 0, '2021-09-07', 0, 0.00, 0);
INSERT INTO checkuser_info (id, user_id, score, lastCheckInTime, flow, days, big_luck) VALUES (3, 1172143423, 10, '2021-10-07', 0, 0.00, 0);
INSERT INTO checkuser_info (id, user_id, score, lastCheckInTime, flow, days, big_luck) VALUES (4, 1078423812, 94, '2021-10-07', 0, 0.00, 0);
INSERT INTO checkuser_info (id, user_id, score, lastCheckInTime, flow, days, big_luck) VALUES (5, 1276159605, 10, '2021-10-03', 0, 0.00, 0);
INSERT INTO checkuser_info (id, user_id, score, lastCheckInTime, flow, days, big_luck) VALUES (6, 1811643422, 0, '2021-09-07', 0, 0.00, 0);
INSERT INTO checkuser_info (id, user_id, score, lastCheckInTime, flow, days, big_luck) VALUES (7, 1865415112, 10453, '2021-10-07', 0, 0.00, 0);
INSERT INTO checkuser_info (id, user_id, score, lastCheckInTime, flow, days, big_luck) VALUES (8, 1767403536, 9, '2021-10-07', 0, 0.00, 0);
INSERT INTO checkuser_info (id, user_id, score, lastCheckInTime, flow, days, big_luck) VALUES (9, 1920209693, 6048, '2021-10-07', 0, 0.00, 0);
INSERT INTO checkuser_info (id, user_id, score, lastCheckInTime, flow, days, big_luck) VALUES (10, 1415179571, 2711, '2021-10-07', 0, 0.00, 0);
INSERT INTO checkuser_info (id, user_id, score, lastCheckInTime, flow, days, big_luck) VALUES (11, 1989449209, 0, '2021-10-07', 0, 0.00, 0);
INSERT INTO checkuser_info (id, user_id, score, lastCheckInTime, flow, days, big_luck) VALUES (12, 1188795787, 20, '2021-10-05', 0, 0.00, 0);
INSERT INTO checkuser_info (id, user_id, score, lastCheckInTime, flow, days, big_luck) VALUES (13, 1903789771, 535, '2021-10-07', 0, 0.00, 0);
INSERT INTO checkuser_info (id, user_id, score, lastCheckInTime, flow, days, big_luck) VALUES (14, 1930966154, 10, '2021-10-05', 0, 0.00, 0);
INSERT INTO checkuser_info (id, user_id, score, lastCheckInTime, flow, days, big_luck) VALUES (15, 1850530463, 39, '2021-10-07', 0, 0.00, 0);
INSERT INTO checkuser_info (id, user_id, score, lastCheckInTime, flow, days, big_luck) VALUES (16, 1810288498, 0, '2021-09-07', 0, 0.00, 0);
INSERT INTO checkuser_info (id, user_id, score, lastCheckInTime, flow, days, big_luck) VALUES (17, 1149286453, 5952, '2021-10-08', 0, 0.00, 0);
INSERT INTO checkuser_info (id, user_id, score, lastCheckInTime, flow, days, big_luck) VALUES (18, 1874829256, 1346, '2021-10-07', 0, 0.00, 0);
INSERT INTO checkuser_info (id, user_id, score, lastCheckInTime, flow, days, big_luck) VALUES (19, 1420287836, 10, '2021-10-05', 0, 0.00, 0);