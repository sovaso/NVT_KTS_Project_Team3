INSERT INTO user (dtype, id, email, enabled, last_password_reset_date, name, password, surname, username,version) VALUES ('RegularUser', 1, 'a@a', 1, '2019-08-08 00:00', 'user1', '$2a$10$xMipTNv6mB4FdLt52YK4KuzVVFx891Pr0cnWySeko67UbjbZcIAK2', 'user1', 'user1',0) ON DUPLICATE KEY UPDATE id = 1;

INSERT INTO user (dtype, id, email, enabled, last_password_reset_date, name, password, surname, username,version) VALUES ('Administrator', 2, 'b@b', 1, '2019-09-08 00:00', 'user2', '$2a$10$xMipTNv6mB4FdLt52YK4KuzVVFx891Pr0cnWySeko67UbjbZcIAK2', 'user2', 'user2',0) ON DUPLICATE KEY UPDATE id = 2;

INSERT INTO user (dtype, id, email, enabled, last_password_reset_date, name, password, surname, username,version) VALUES ('Administrator', 3, 'c@c', 1, '2019-09-08 00:00', 'user3', '$2a$10$xMipTNv6mB4FdLt52YK4KuzVVFx891Pr0cnWySeko67UbjbZcIAK2', 'user3', 'user3',0) ON DUPLICATE KEY UPDATE id = 3;

INSERT INTO user (dtype, id, email, enabled, last_password_reset_date, name, password, surname, username,version) VALUES ('RegularUser', 4, 'd@d', 1, '2019-08-08 00:00', 'user4', '$2a$10$xMipTNv6mB4FdLt52YK4KuzVVFx891Pr0cnWySeko67UbjbZcIAK2', 'user4', 'user4',0) ON DUPLICATE KEY UPDATE id = 4;

INSERT INTO user (dtype, id, email, enabled, last_password_reset_date, name, password, surname, username,version) VALUES ('RegularUser', 5, 'e@e', 0, '2019-08-08 00:00', 'user5', '$2a$10$xMipTNv6mB4FdLt52YK4KuzVVFx891Pr0cnWySeko67UbjbZcIAK2', 'user5', 'user5',0) ON DUPLICATE KEY UPDATE id = 5;

INSERT INTO user (dtype, id, email, enabled, last_password_reset_date, name, password, surname, username,version) VALUES ('RegularUser', 6, 'f@f', 1, '2019-08-08 00:00', 'user6', '$2a$10$xMipTNv6mB4FdLt52YK4KuzVVFx891Pr0cnWySeko67UbjbZcIAK2', 'user6', 'user6',0) ON DUPLICATE KEY UPDATE id = 6;


INSERT INTO location (id, address, description, name, status, version) VALUES (1, 'Address1', 'Description1', 'Name1', 1, 1) ON DUPLICATE KEY UPDATE id = 1;
INSERT INTO location (id, address, description, name, status, version) VALUES (2, 'Address2', 'Description2', 'Name2', 1, 1) ON DUPLICATE KEY UPDATE id = 2;
INSERT INTO location (id, address, description, name, status,version) VALUES (3, 'Address3', 'Description3', 'Name3', 0,0) ON DUPLICATE KEY UPDATE id = 3;
INSERT INTO location (id, address, description, name, status,version) VALUES (4, 'Address4', 'Description4', 'Name4', 1,0) ON DUPLICATE KEY UPDATE id = 4;
INSERT INTO location (id, address, description, name, status,version) VALUES (5, 'Address5', 'Description5', 'Name5', 1,0) ON DUPLICATE KEY UPDATE id = 5;
INSERT INTO location (id, address, description, name, status,version) VALUES (6, 'Address6', 'Description6', 'Name6', 1,0) ON DUPLICATE KEY UPDATE id = 6;
INSERT INTO location (id, address, description, name, status,version) VALUES (7, 'Address7', 'Description7', 'Name7', 0,0) ON DUPLICATE KEY UPDATE id = 7;


INSERT INTO event (id, name, status,version, type, location_info_id) VALUES (1, 'Event1', 1, 0,'SPORTS', 1) ON DUPLICATE KEY UPDATE id = 1;
INSERT INTO event (id, name, status,version, type, location_info_id) VALUES (2, 'Event1', 1, 0,'CULTURAL', 2) ON DUPLICATE KEY UPDATE id = 2;
INSERT INTO event (id, name, status,version, type, location_info_id) VALUES (3, 'Event2', 1, 0,'CULTURAL', 1) ON DUPLICATE KEY UPDATE id = 3;
INSERT INTO event (id, name, status,version, type, location_info_id) VALUES (4, 'Event3', 0, 0,'ENTERTAINMENT', 1) ON DUPLICATE KEY UPDATE id = 4;
INSERT INTO event (id, name, status,version, type, location_info_id) VALUES (5, 'Event4', 1, 0,'SPORTS', 2) ON DUPLICATE KEY UPDATE id = 5;
INSERT INTO event (id, name, status,version, type, location_info_id) VALUES (6, 'Event5', 1, 0,'CULTURAL', 4) ON DUPLICATE KEY UPDATE id = 6;
INSERT INTO event (id, name, status,version, type, location_info_id) VALUES (7, 'Event6', 0, 0,'SPORTS', 5) ON DUPLICATE KEY UPDATE id = 7;
INSERT INTO event (id, name, status,version, type, location_info_id) VALUES (8, 'Event8', 1, 0,'ENTERTAINMENT', 1) ON DUPLICATE KEY UPDATE id = 8;
INSERT INTO event (id, name, status,version, type, location_info_id) VALUES (9, 'Event9', 0, 0,'SPORTS', 6) ON DUPLICATE KEY UPDATE id = 9;

INSERT INTO maintenance (id, maintenance_date, maintenance_end_time, reservation_expiry, version, event_id) VALUES (1, '2021-01-15 00:00:00', '2021-01-20 00:00:00', '2021-01-17 00:00:00',0, 1) ON DUPLICATE KEY UPDATE id = 1;
INSERT INTO maintenance (id, maintenance_date, maintenance_end_time, reservation_expiry, version, event_id)VALUES (2, '2021-01-25 00:00:00', '2021-01-30 00:00:00', '2021-01-27 00:00:00',0, 3) ON DUPLICATE KEY UPDATE id = 2;
INSERT INTO maintenance (id, maintenance_date, maintenance_end_time, reservation_expiry, version, event_id) VALUES (3, '2019-12-01 00:00:00', '2019-12-05 00:00:00', '2019-12-03 00:00:00',0, 2) ON DUPLICATE KEY UPDATE id = 3;
INSERT INTO maintenance (id, maintenance_date, maintenance_end_time, reservation_expiry, version, event_id)VALUES (4, '2019-10-01 00:00:00', '2019-10-06 00:00:00', '2019-10-04 00:00:00',0, 5) ON DUPLICATE KEY UPDATE id = 4;
INSERT INTO maintenance (id, maintenance_date, maintenance_end_time, reservation_expiry, version, event_id) VALUES (5, '2019-02-10 00:00:00', '2019-02-15 00:00:00', '2019-02-12 00:00:00',0, 6) ON DUPLICATE KEY UPDATE id = 5;
INSERT INTO maintenance (id, maintenance_date, maintenance_end_time, reservation_expiry, version, event_id)VALUES (6, '2019-09-25 00:00:00', '2019-09-30 00:00:00', '2019-09-28 00:00:00', 0,4) ON DUPLICATE KEY UPDATE id = 6;
INSERT INTO maintenance (id, maintenance_date, maintenance_end_time, reservation_expiry, version, event_id)VALUES (7, '2019-08-07 00:00:00', '2019-08-14 00:00:00', '2019-09-12 00:00:00',0, 7) ON DUPLICATE KEY UPDATE id = 7;
INSERT INTO maintenance (id, maintenance_date, maintenance_end_time, reservation_expiry, version, event_id)VALUES (8, '2019-12-20 00:00:00', '2019-12-28 00:00:00', '2019-12-25 00:00:00',0, 1) ON DUPLICATE KEY UPDATE id = 8;
INSERT INTO maintenance (id, maintenance_date, maintenance_end_time, reservation_expiry, version, event_id) VALUES (9, '2019-05-05 00:00:00', '2019-05-15 00:00:00', '2019-05-13 00:00:00',0, 8) ON DUPLICATE KEY UPDATE id = 9;
INSERT INTO maintenance (id, maintenance_date, maintenance_end_time, reservation_expiry, version, event_id)VALUES (10, '2019-01-01 00:00:00', '2019-01-20 00:00:00', '2019-01-17 00:00:00',0, 9) ON DUPLICATE KEY UPDATE id = 10;
INSERT INTO maintenance (id, maintenance_date, maintenance_end_time, reservation_expiry, version, event_id)VALUES (11, '2019-02-02 00:00:00', '2019-02-12 00:00:00', '2019-02-10 00:00:00',0, 4) ON DUPLICATE KEY UPDATE id = 11;
INSERT INTO maintenance (id, maintenance_date, maintenance_end_time, reservation_expiry,version, event_id) VALUES (12, '2018-01-01 00:00:00', '2018-01-15 00:00:00', '2019-01-13 00:00:00',0, 8) ON DUPLICATE KEY UPDATE id = 12;
INSERT INTO maintenance (id, maintenance_date, maintenance_end_time, reservation_expiry, version,event_id) VALUES (13, '2018-01-01 00:00:00', '2018-01-10 00:00:00', '2019-01-08 00:00:00', 0,7) ON DUPLICATE KEY UPDATE id = 13;


INSERT INTO location_zone (id, capacity, number_col, matrix, name, number_row,version, location_id) VALUES (1, 200, 20, 1, 'Name1', 10,0, 1) ON DUPLICATE KEY UPDATE id = 1;
INSERT INTO location_zone (id, capacity, number_col, matrix, name, number_row,version, location_id) VALUES (2, 300, 30, 1, 'Name2', 10,0, 3) ON DUPLICATE KEY UPDATE id = 2;

INSERT INTO location_zone (id, capacity, number_col, matrix, name, number_row,version,location_id) VALUES (3, 300, 30, 1, 'Name3', 10,0, 6) ON DUPLICATE KEY UPDATE id = 3;

INSERT INTO location_zone (id, capacity, number_col, matrix, name, number_row,version, location_id) VALUES (4, 400, 40, 1, 'Name4', 10,0, 6) ON DUPLICATE KEY UPDATE id = 4;
INSERT INTO location_zone (id, capacity, number_col, matrix, name, number_row,version, location_id) VALUES (5, 200, 20, 1, 'Name5', 10,0, 2) ON DUPLICATE KEY UPDATE id = 5;
INSERT INTO location_zone (id, capacity, number_col, matrix, name, number_row,version, location_id) VALUES (6, 200, 20, 1, 'Name6', 10,0, 5) ON DUPLICATE KEY UPDATE id = 6;

INSERT INTO leased_zone (id, seat_price, maintenance_id,version, location_zone_id) VALUES (1, 200, 1, 0,1) ON DUPLICATE KEY UPDATE id = 1; 

INSERT INTO leased_zone (id, seat_price, maintenance_id,version, location_zone_id) VALUES (2, 200, 10, 0,4) ON DUPLICATE KEY UPDATE id = 2;

INSERT INTO leased_zone (id, seat_price, maintenance_id,version, location_zone_id) VALUES (3, 200, 11, 0,1) ON DUPLICATE KEY UPDATE id = 3; 

INSERT INTO leased_zone (id, seat_price, maintenance_id,version, location_zone_id) VALUES (4, 200, 3, 0,5) ON DUPLICATE KEY UPDATE id = 4; 

INSERT INTO leased_zone (id, seat_price, maintenance_id,version, location_zone_id) VALUES (5, 200, 13, 0,6) ON DUPLICATE KEY UPDATE id = 5; 

INSERT INTO reservation (id, date_of_reservation, paid, total_price,version, event_id, user_id)  VALUES (1, '2019-11-09 00:00', 0, 200,0, 2, 1) ON DUPLICATE KEY UPDATE id = 1;

INSERT INTO reservation (id, date_of_reservation, paid, total_price,version, event_id, user_id) VALUES (2, '2019-11-11 00:00', 1, 200,0, 2, 1) ON DUPLICATE KEY UPDATE id = 2;

INSERT INTO reservation (id, date_of_reservation, paid, total_price,version, event_id, user_id) VALUES (3, '2019-11-11 00:00', 0, 2600,0, 3, 1) ON DUPLICATE KEY UPDATE id = 3;

INSERT INTO reservation (id, date_of_reservation, paid, total_price,version, event_id, user_id) VALUES (4, '2019-11-11 00:00', 0, 200,0, 3, 6) ON DUPLICATE KEY UPDATE id = 4;

INSERT INTO reservation (id, date_of_reservation, paid, total_price,version, event_id, user_id) VALUES (5, '2019-11-11 00:00', 1, 200,0, 3, 6) ON DUPLICATE KEY UPDATE id = 5;

INSERT INTO reservation (id, date_of_reservation, paid, total_price,version, event_id, user_id) VALUES (6, '2018-11-11 00:00', 0, 200,0, 2, 6) ON DUPLICATE KEY UPDATE id = 6;

INSERT INTO reservation (id, date_of_reservation, paid, total_price,version, event_id, user_id) VALUES (7, '2018-11-11 00:00', 0, 200,0, 2, 5) ON DUPLICATE KEY UPDATE id = 7;

INSERT INTO reservation (id, date_of_reservation, paid, total_price,version, event_id, user_id) VALUES (8, '2018-11-11 00:00', 0, 200,0, 7, 4) ON DUPLICATE KEY UPDATE id = 8;


INSERT INTO ticket (id, number_col, price, reserved, number_row,version, leased_zone_id) VALUES (1, 1, 200, 0, 1,0, 1) ON DUPLICATE KEY UPDATE id = 1;
INSERT INTO ticket (id, number_col, price, reserved, number_row, version,reservation_id,leased_zone_id) VALUES (2, 1, 200, 1, 2,0, 1, 4) ON DUPLICATE KEY UPDATE id = 2;
INSERT INTO ticket (id, number_col, price, reserved, number_row,version,reservation_id, leased_zone_id) VALUES (3, 1, 200, 1, 3,0, 2, 4) ON DUPLICATE KEY UPDATE id = 3;
INSERT INTO ticket (id, number_col, price, reserved, number_row, version, leased_zone_id) VALUES (4, 1, 200, 0, 4, 0,4) ON DUPLICATE KEY UPDATE id = 4;
INSERT INTO ticket (id, number_col, price, reserved, number_row, version,leased_zone_id) VALUES (5, 1, 200, 0, 5,0, 4) ON DUPLICATE KEY UPDATE id = 5;
INSERT INTO ticket (id, number_col, price, reserved, number_row, version,reservation_id,leased_zone_id) VALUES (6, 1, 200, 1, 6,0,3, 1) ON DUPLICATE KEY UPDATE id = 6;
INSERT INTO ticket (id, number_col, price, reserved, number_row, version,reservation_id,leased_zone_id) VALUES (7, 1, 200, 1, 7,0,3, 1) ON DUPLICATE KEY UPDATE id = 7;
INSERT INTO ticket (id, number_col, price, reserved, number_row, version,reservation_id,leased_zone_id) VALUES (8, 1, 200, 1, 8,0,3, 1) ON DUPLICATE KEY UPDATE id = 8;
INSERT INTO ticket (id, number_col, price, reserved, number_row, version,reservation_id,leased_zone_id) VALUES (9, 1, 200, 1, 9,0,3, 1) ON DUPLICATE KEY UPDATE id = 9;
INSERT INTO ticket (id, number_col, price, reserved, number_row, version,reservation_id,leased_zone_id) VALUES (10, 1, 200, 1, 10,0,3, 1) ON DUPLICATE KEY UPDATE id = 10;
INSERT INTO ticket (id, number_col, price, reserved, number_row, version,reservation_id,leased_zone_id) VALUES (11, 1, 200, 1, 11,0,3, 1) ON DUPLICATE KEY UPDATE id = 11;
INSERT INTO ticket (id, number_col, price, reserved, number_row, version,reservation_id,leased_zone_id) VALUES (12, 1, 200, 1, 12,0,3, 1) ON DUPLICATE KEY UPDATE id = 12;
INSERT INTO ticket (id, number_col, price, reserved, number_row, version,reservation_id,leased_zone_id) VALUES (13, 1, 200, 1, 13,0,3, 1) ON DUPLICATE KEY UPDATE id = 13;
INSERT INTO ticket (id, number_col, price, reserved, number_row, version,reservation_id,leased_zone_id) VALUES (14, 1, 200, 1, 14,0,3, 1) ON DUPLICATE KEY UPDATE id = 14;
INSERT INTO ticket (id, number_col, price, reserved, number_row, version,reservation_id,leased_zone_id) VALUES (15, 1, 200, 1, 15,0,3, 1) ON DUPLICATE KEY UPDATE id = 15;
INSERT INTO ticket (id, number_col, price, reserved, number_row, version,reservation_id,leased_zone_id) VALUES (16, 1, 200, 1, 16,0,3, 1) ON DUPLICATE KEY UPDATE id = 16;
INSERT INTO ticket (id, number_col, price, reserved, number_row, version,reservation_id,leased_zone_id) VALUES (17, 1, 200, 1, 17,0,3,1) ON DUPLICATE KEY UPDATE id = 17;
INSERT INTO ticket (id, number_col, price, reserved, number_row, version,reservation_id,leased_zone_id) VALUES (18, 1, 200, 1, 18,0,3,1) ON DUPLICATE KEY UPDATE id = 18;

INSERT INTO ticket (id, number_col, price, reserved, number_row, version,reservation_id,leased_zone_id) VALUES (19, 1, 200, 1, 19,0,4,1) ON DUPLICATE KEY UPDATE id = 19;

INSERT INTO ticket (id, number_col, price, reserved, number_row, version,reservation_id,leased_zone_id) VALUES (20, 1, 200, 1, 20,0,5,1) ON DUPLICATE KEY UPDATE id = 20;

INSERT INTO ticket (id, number_col, price, reserved, number_row, version,reservation_id,leased_zone_id) VALUES (21, 1, 200, 1, 16,0,6,4) ON DUPLICATE KEY UPDATE id = 21;


INSERT INTO ticket (id, number_col, price, reserved, number_row, version,reservation_id,leased_zone_id) VALUES (22, 1, 200, 1, 1,0,7,4) ON DUPLICATE KEY UPDATE id = 22;

INSERT INTO ticket (id, number_col, price, reserved, number_row, version,reservation_id,leased_zone_id) VALUES (23, 1, 200, 1, 1,0,8,5) ON DUPLICATE KEY UPDATE id = 23;

INSERT INTO ticket (id, number_col, price, reserved, number_row, version,leased_zone_id) VALUES (24, 1, 200, 0, 21,0, 1) ON DUPLICATE KEY UPDATE id = 24;

INSERT INTO ticket (id, number_col, price, reserved, number_row, version,leased_zone_id) VALUES (25, 1, 200, 0, 22,0, 1) ON DUPLICATE KEY UPDATE id = 25;

INSERT INTO authority (id, name) VALUES (1,'ROLE_USER') ON DUPLICATE KEY UPDATE id = 1;
INSERT INTO authority (id, name) VALUES (2,'ROLE_ADMIN') ON DUPLICATE KEY UPDATE id = 2;
INSERT INTO authority (id, name) VALUES (3,'ROLE_ADMIN') ON DUPLICATE KEY UPDATE id = 3;
INSERT INTO authority (id, name) VALUES (4,'ROLE_USER') ON DUPLICATE KEY UPDATE id = 4;
INSERT INTO authority (id, name) VALUES (5,'ROLE_USER') ON DUPLICATE KEY UPDATE id = 5;
INSERT INTO authority (id, name) VALUES (6,'ROLE_USER') ON DUPLICATE KEY UPDATE id = 6;


INSERT INTO user_authority (user_id, authority_id) VALUES(1, 1);
INSERT INTO user_authority (user_id, authority_id) VALUES(2, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES(3, 3);
INSERT INTO user_authority (user_id, authority_id) VALUES(4, 4);
INSERT INTO user_authority (user_id, authority_id) VALUES(5, 5);
INSERT INTO user_authority (user_id, authority_id) VALUES(6, 6);