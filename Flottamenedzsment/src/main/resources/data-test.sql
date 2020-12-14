INSERT INTO Roles (role) VALUES ('ADMIN')
INSERT INTO Roles (role) VALUES ('USER_MNGR')
INSERT INTO Roles (role) VALUES ('SUBSCRIPTION_MNGR')
INSERT INTO Roles (role) VALUES ('DEVICE_MNGR')
INSERT INTO Roles (role) VALUES ('FINANCE_MNGR')

INSERT INTO users_roles (user_id, role_id) VALUES (1,1)
INSERT INTO users_roles (user_id, role_id) VALUES (2,3)

INSERT INTO Sims (imei, pin, puk, status) VALUES ('8936304419070454006', '0123', '0123456789', 1)
INSERT INTO Sims (imei, pin, puk, status) VALUES ('8936304419070454111', '1234', '1234567890', 0)

INSERT INTO Subscriptions (number, create_date) VALUES ('201234567', to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO Subscriptions (number, create_date) VALUES ('207654321', to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO sub_sim_st (sub_id, sim_id, begin_date) VALUES (1, 1, to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO sub_sim_st (sub_id, sim_id, begin_date) VALUES (2, 2, to_date('20-01-01', 'RR-MM-DD'))

INSERT INTO Device_types (brand, model, name, availability) VALUES ('Samsung', 'J330', 'Samsung J3', 0)
INSERT INTO Device_types (brand, model, name, availability) VALUES ('Samsung', 'A515F', 'Samsung A51', 0)
INSERT INTO Device_types (brand, model, name, availability) VALUES ('Motorola', 'C380', 'Motorola C380', 1)

INSERT INTO Devices (serial_number, type_id, create_date) VALUES ('R58J900J1W', 1, to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO Devices (serial_number, type_id, create_date) VALUES ('R73D472C2T', 2, to_date('20-01-01', 'RR-MM-DD'))

INSERT INTO Categories (name) VALUES ('Monthly')
INSERT INTO Categories (name) VALUES ('Call')
INSERT INTO Categories (name) VALUES ('Internet')

INSERT INTO description_category_coupler(name, availability) VALUES ('Test description category coupler', 0)

INSERT INTO description_category_map (description_category_coupler_id, description_category_map_id, description_category_map_key) VALUES (1, 1, 'Mobil telefon szolgaltatas')
INSERT INTO description_category_map (description_category_coupler_id, description_category_map_id, description_category_map_key) VALUES (1, 2, 'Telekom mobilhalozaton belul')
INSERT INTO description_category_map (description_category_coupler_id, description_category_map_id, description_category_map_key) VALUES (1, 3, 'Mobil net 1GB')

INSERT INTO Charge_ratio_by_category (name, availability) VALUES ('Test charge ratio', 0)

INSERT INTO Category_ratio_map (CHARGE_RATIO_BY_CATEGORY_ID, CATEGORY_RATIO_MAP, CATEGORY_RATIO_MAP_KEY) VALUES (1, 0, 1)
INSERT INTO Category_ratio_map (CHARGE_RATIO_BY_CATEGORY_ID, CATEGORY_RATIO_MAP, CATEGORY_RATIO_MAP_KEY) VALUES (1, 50, 2)
INSERT INTO Category_ratio_map (CHARGE_RATIO_BY_CATEGORY_ID, CATEGORY_RATIO_MAP, CATEGORY_RATIO_MAP_KEY) VALUES (1, 100, 3)

INSERT INTO Participants (address, name, description_category_coupler_id) VALUES ('Budapest', 'Test participant', 1)