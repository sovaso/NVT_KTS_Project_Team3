INSERT INTO user (dtype, id, email, enabled, last_password_reset_date, name, password, surname, username,version) VALUES ('RegularUser', 1, 'a@a', 1, '2019-08-08 00:00', 'user1', '$2a$10$xMipTNv6mB4FdLt52YK4KuzVVFx891Pr0cnWySeko67UbjbZcIAK2', 'user1', 'user1',0) ON DUPLICATE KEY UPDATE id = 1;

INSERT INTO user (dtype, id, email, enabled, last_password_reset_date, name, password, surname, username,version) VALUES ('Admin', 2, 'b@b', 1, '2019-09-08 00:00', 'user2', '$2a$10$.0EvoW1g2cAX.fcXuvrgzO2e6iOpeWUhAdLJDJHv8xSFZOcrR8uUa', 'user2', 'user2',0) ON DUPLICATE KEY UPDATE id = 2;

INSERT INTO user (dtype, id, email, enabled, last_password_reset_date, name, password, surname, username,version) VALUES ('Admin', 3, 'c@c', 1, '2019-10-08 00:00', 'user3', '$2a$10$.0EvoW1g2cAX.fcXuvrgzO2e6iOpeWUhAdLJDJHv8xSFZOcrR8uUa', 'user3', 'user3',0) ON DUPLICATE KEY UPDATE id = 3;

INSERT INTO user (dtype, id, email, enabled, last_password_reset_date, name, password, surname, username,version) VALUES ('RegularUser', 4, 'd@d', 1, '2019-08-08 00:00', 'user4', '$2a$10$.0EvoW1g2cAX.fcXuvrgzO2e6iOpeWUhAdLJDJHv8xSFZOcrR8uUa', 'user4', 'user4',0) ON DUPLICATE KEY UPDATE id = 4;

INSERT INTO user (dtype, id, email, enabled, last_password_reset_date, name, password, surname, username,version) VALUES ('RegularUser', 5, 'e@e', 1, '2019-08-08 00:00', 'user5', '$2a$10$.0EvoW1g2cAX.fcXuvrgzO2e6iOpeWUhAdLJDJHv8xSFZOcrR8uUa', 'user5', 'user5',0) ON DUPLICATE KEY UPDATE id = 5;

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


INSERT INTO maintenance (id, maintenance_date, maintenance_end_time, reservation_expiry, version, event_id) VALUES (1, '2020-01-15 00:00:00', '2020-01-20 00:00:00', '2020-01-17 00:00:00',0, 1) ON DUPLICATE KEY UPDATE id = 1;
INSERT INTO maintenance (id, maintenance_date, maintenance_end_time, reservation_expiry, version, event_id)VALUES (2, '2020-01-25 00:00:00', '2020-01-30 00:00:00', '2020-01-27 00:00:00',0, 3) ON DUPLICATE KEY UPDATE id = 2;
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

INSERT INTO location_zone (id, capacity, number_col, matrix, name, number_row, location_id) VALUES (1, 200, 20, 1, 'Name1', 10, 1) ON DUPLICATE KEY UPDATE id = 1;

INSERT INTO leased_zone (id, seat_price, maintenance_id,version, location_zone_id) VALUES (1, 200, 1, 0,1) ON DUPLICATE KEY UPDATE id = 1;

INSERT INTO reservation (id, date_of_reservation, paid, total_price,version, event_id, user_id)  VALUES (1, '2019-11-09 00:00', 0, 200,0, 2, 1) ON DUPLICATE KEY UPDATE id = 1;

INSERT INTO reservation (id, date_of_reservation, paid, total_price,version, event_id, user_id) VALUES (2, '2019-11-11', 1, 200,0, 2, 1) ON DUPLICATE KEY UPDATE id = 2;

INSERT INTO ticket (id, number_col, price, reserved, number_row,version, leased_zone_id) VALUES (1, 1, 200, 0, 1,0, 1) ON DUPLICATE KEY UPDATE id = 1;
INSERT INTO ticket (id, number_col, price, reserved, number_row, version,reservation_id,leased_zone_id) VALUES (2, 1, 200, 1, 2,0, 1, 1) ON DUPLICATE KEY UPDATE id = 2;
INSERT INTO ticket (id, number_col, price, reserved, number_row,version,reservation_id, leased_zone_id) VALUES (3, 1, 200, 1, 3,0, 2, 1) ON DUPLICATE KEY UPDATE id = 3;
INSERT INTO ticket (id, number_col, price, reserved, number_row, version, leased_zone_id) VALUES (4, 1, 200, 0, 4, 0,1) ON DUPLICATE KEY UPDATE id = 4;
INSERT INTO ticket (id, number_col, price, reserved, number_row, version,leased_zone_id) VALUES (5, 1, 200, 0, 5,0, 1) ON DUPLICATE KEY UPDATE id = 5;