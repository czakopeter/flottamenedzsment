INSERT INTO Roles (role) VALUES ('USER_MNGR')
INSERT INTO Roles (role) VALUES ('SUBSCRIPTION_MNGR')
INSERT INTO Roles (role) VALUES ('DEVICE_MNGR')
INSERT INTO Roles (role) VALUES ('FINANCE_MNGR')

INSERT INTO Sims (imei, pin, puk, status) VALUES ('8936304419070454006', '0123', '0123456789', 1)
INSERT INTO Sims (imei, pin, puk, status) VALUES ('8936304419070454111', '1234', '1234567890', 0)

INSERT INTO Subscriptions(number, create_date, first_available_date) VALUES ('202563364', to_date('20-01-01', 'RR-MM-DD'), to_date('20-01-10', 'RR-MM-DD'))
INSERT INTO Sub_dev_st (sub_id, dev_id, begin_date) VALUES (1, null, to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO Sub_note (note, sub_id, begin_date) VALUES ('', 1, to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO Sub_sim_st (sub_id, sim_id, begin_date) VALUES (1, 1, to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO User_sub_st (user_id, sub_id, begin_date) VALUES (null, 1, to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO User_sub_st (user_id, sub_id, begin_date) VALUES (1, 1, to_date('20-01-10', 'RR-MM-DD'))

INSERT INTO Device_types (brand, model, name, sim_number, microsd, visible) VALUES ('Samsung', 'J330', 'Samsung J3', 1, true, true)
INSERT INTO Device_types (brand, model, name, sim_number, microsd, visible) VALUES ('Motorola', 'C380', 'Motorola C380', 1, false, false)

INSERT INTO Devices (serial_number, type_id, create_date, first_available_date) VALUES ('R58J900J1W', 1, to_date('20-01-01', 'RR-MM-DD'), to_date('20-01-10', 'RR-MM-DD'))
INSERT INTO Sub_dev_st (sub_id, dev_id, begin_date) VALUES (null, 1, to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO Dev_note (note, dev_id, begin_date) VALUES ('', 1, to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO User_dev_st (user_id, dev_id, begin_date) VALUES (null, 1, to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO User_dev_st (user_id, dev_id, begin_date) VALUES (1, 1, to_date('20-01-10', 'RR-MM-DD'))

INSERT INTO Sub_dev_st (sub_id, dev_id, begin_date) VALUES (1, 1, to_date('20-01-10', 'RR-MM-DD'))

INSERT INTO Categories (name) VALUES ('Monthly')
INSERT INTO Categories (name) VALUES ('Call')
INSERT INTO Categories (name) VALUES ('Internet')

INSERT INTO description_category_coupler(name, available) VALUES ('Test description category coupler', true)

INSERT INTO description_category_map (description_category_coupler_id, description_category_map_id, description_category_map_key) VALUES (1, 1, 'Mobil telefon szolgaltatas')
INSERT INTO description_category_map (description_category_coupler_id, description_category_map_id, description_category_map_key) VALUES (1, 2, 'Telekom mobilhalozaton belul')
INSERT INTO description_category_map (description_category_coupler_id, description_category_map_id, description_category_map_key) VALUES (1, 2, 'Belfoldi mas mobilhalozat / Telenor')
INSERT INTO description_category_map (description_category_coupler_id, description_category_map_id, description_category_map_key) VALUES (1, 2, 'Belfoldi mas mobilhalozat / Vodafone')
INSERT INTO description_category_map (description_category_coupler_id, description_category_map_id, description_category_map_key) VALUES (1, 3, 'Mobil net 1GB')

INSERT INTO Charge_ratio_by_category (name, available) VALUES ('Test category ratio', true)

INSERT INTO Category_ratio_map (CHARGE_RATIO_BY_CATEGORY_ID, CATEGORY_RATIO_MAP, CATEGORY_RATIO_MAP_KEY) VALUES (1, 30, 1)
INSERT INTO Category_ratio_map (CHARGE_RATIO_BY_CATEGORY_ID, CATEGORY_RATIO_MAP, CATEGORY_RATIO_MAP_KEY) VALUES (1, 60, 2)
INSERT INTO Category_ratio_map (CHARGE_RATIO_BY_CATEGORY_ID, CATEGORY_RATIO_MAP, CATEGORY_RATIO_MAP_KEY) VALUES (1, 100, 3)

INSERT INTO Users_pay_devs (user_id, pay_devs_id) VALUES (1, 1)

INSERT INTO Participants (address, name, description_category_coupler_id) VALUES ('Budapest', 'Magyar Telekom Nyrt.', 1)