INSERT INTO "location" ("location_id", "city") VALUES (1, 'Ha Noi');
INSERT INTO "location" ("location_id", "city") VALUES (2, 'Ho Chi Minh');

INSERT INTO "category" ("category_id", "name") VALUES (1, 'Monitor');
INSERT INTO "category"  ("category_id", "name") VALUES (2, 'Laptop');
INSERT INTO "category" ("category_id", "name") VALUES (3, 'PC');

INSERT INTO "user" ("staff_code", "username", "password", "first_name", "last_name", "birthdate", "gender", "email", "created_at", "created_by", "active", "location_id", "role", "first_login") VALUES ( 'SD0001','vangdv', '$2a$10$ZSlYKBxG/G9TAxc4UK5Irezg6y0QaIMpEkJa1AyxTBs6SBhJ1NBeu', 'Vang', 'Do Van', CURRENT_DATE(), 'MALE', 'vangdo@gmail.com', CURRENT_TIMESTAMP(), 'root', 1, 1, 'ADMIN', 1);
INSERT INTO "user" ("staff_code", "username", "password", "first_name", "last_name", "birthdate", "gender", "email", "created_at", "created_by", "active", "location_id", "role", "first_login") VALUES ( 'SD0002','vietvh', '$2a$10$ZSlYKBxG/G9TAxc4UK5Irezg6y0QaIMpEkJa1AyxTBs6SBhJ1NBeu ', 'Viet', 'Vu Hoang', CURRENT_DATE(), 'MALE', 'vietvuhoang@gmail.com', CURRENT_TIMESTAMP(), 'root', 1, 2, 'ADMIN', 0);
INSERT INTO "user" ("staff_code", "username", "password", "first_name", "last_name", "birthdate", "gender", "email", "created_at", "created_by", "active", "location_id", "role", "first_login") VALUES ( 'SD0003','tuandv', '$2a$10$ZSlYKBxG/G9TAxc4UK5Irezg6y0QaIMpEkJa1AyxTBs6SBhJ1NBeu', 'Tuan', 'Do Van', CURRENT_DATE(), 'MALE', 'tuando@gmail.com', CURRENT_TIMESTAMP(), 'SD0001', 1, 1, 'STAFF', 1);
INSERT INTO "user" ("staff_code", "username", "password", "first_name", "last_name", "birthdate", "gender", "email", "created_at", "created_by", "active", "location_id", "role", "first_login") VALUES ( 'SD0004','quynhct', '$2a$10$ZSlYKBxG/G9TAxc4UK5Irezg6y0QaIMpEkJa1AyxTBs6SBhJ1NBeuW', 'Quynh', 'Chu Thuy', CURRENT_DATE(), 'FEMALE', 'quynhchu@gmail.com', CURRENT_TIMESTAMP(), 'SD0002', 1, 2, 'STAFF', 0);

INSERT INTO "asset" ("asset_id", "name", "specification", "category", "installed_date", "location_id", "state") VALUES ('LA001', 'laptop Dell HP Probook 450 G4', 'RAM 4GB, HDD 500GB, Intel Core i5 gen 5', 2, CURRENT_DATE(),  1, 'AVAILABLE');
INSERT INTO "asset" ("asset_id", "name", "specification", "category", "installed_date", "location_id", "state") VALUES ('LA002', 'laptop Dell HP Probook 450 G4', 'RAM 8GB, HDD 250GB, Intel Core i5 gen 5', 2,  CURRENT_DATE(), 2, 'AVAILABLE');
INSERT INTO "asset" ("asset_id", "name", "specification", "category", "installed_date", "location_id", "state") VALUES ('LA003', 'laptpp Dell HP Pavilion', 'RAM 16GB, SSD 256GB, Intel Core i5 gen 5', 2, CURRENT_DATE(), 1, 'AVAILABLE');

INSERT INTO "assignment" ("assignment_id", "assigner", "assignee", "asset_id", "assign_date", "assign_note", "state" ) VALUES (1, 'SD0001', 'SD0003', 'LA003', CURRENT_DATE(), 'nothing', 'ACCEPTED');
INSERT INTO "assignment" ("assignment_id", "assigner", "assignee", "asset_id", "assign_date", "assign_note", "state" ) VALUES (2, 'SD0001', 'SD0003', 'LA001', CURRENT_DATE(), 'nothing in heere', 'WAITING_FOR_ACCEPTANCE');

INSERT INTO "return_request" ("request_id", "assignment_id", "request_note", "created_at", "created_by", "state") VALUES (1, 1, 'I want to return this asset', CURRENT_TIMESTAMP(), 'SD0004', 'WAITING_FOR_RETURNING');

