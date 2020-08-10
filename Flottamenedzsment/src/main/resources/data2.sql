INSERT INTO Roles (role) VALUES ('ADMIN')
INSERT INTO Roles (role) VALUES ('USER')
INSERT INTO Roles (role) VALUES ('MOBILE')
INSERT INTO Roles (role) VALUES ('FINANCIAL')
INSERT INTO Roles (role) VALUES ('F')

INSERT INTO Users (email, password, full_name, enabled) VALUES ('admin', 'admin', 'Admin', true)
INSERT INTO Users (email, password, full_name, enabled) VALUES ('u', 'u', 'User', true)
INSERT INTO Users (email, password, full_name, enabled) VALUES ('m', 'm', 'Mobile', true)
INSERT INTO Users (email, password, full_name, enabled) VALUES ('f', 'f', 'Financial', true)

INSERT INTO Users_roles (user_id, role_id) VALUES (1, 1)
INSERT INTO Users_roles (user_id, role_id) VALUES (2, 2)
INSERT INTO Users_roles (user_id, role_id) VALUES (3, 3)
INSERT INTO Users_roles (user_id, role_id) VALUES (4, 5)

INSERT INTO Subscriptions(number, create_date, first_available_date) VALUES ('201234567', to_date('20-01-01', 'RR-MM-DD'), to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO Subscriptions(number, create_date, first_available_date) VALUES ('207654321', to_date('20-01-02', 'RR-MM-DD'), to_date('20-01-02', 'RR-MM-DD'))

INSERT INTO Sims (imei, pin, puk) VALUES ('imei1', '1111', '1111111111')
INSERT INTO Sims (imei, pin, puk) VALUES ('imei2', '2222', '2222222222')
INSERT INTO Sims (imei, pin, puk) VALUES ('imei3', '3333', '3333333333')

INSERT INTO Sub_note (note, sub_id, date) VALUES ('', 1, to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO Sub_note (note, sub_id, date) VALUES ('', 2, to_date('20-01-02', 'RR-MM-DD'))

INSERT INTO Sim_status (sim_id, connect, status) VALUES (1, to_date('20-01-01', 'RR-MM-DD'), 1)
INSERT INTO Sim_status (sim_id, connect, status) VALUES (2, to_date('20-01-02', 'RR-MM-DD'), 1)
INSERT INTO Sim_status (sim_id, connect, status) VALUES (3, to_date('20-01-03', 'RR-MM-DD'), 0)

INSERT INTO Device_types (brand, model, name, sim_number, microsd, visible) VALUES ('Samsung', 'S10', 'Samsung S10', 2, true, true)
INSERT INTO Device_types (brand, model, name, sim_number, microsd, visible) VALUES ('Samsung', 'A71', 'Samsung A71', 2, true, true)
INSERT INTO Device_types (brand, model, name, sim_number, microsd, visible) VALUES ('Huawie', 'Mate 30', 'Huawie Mate 30', 1, true, true)

INSERT INTO Devices (serial_number, type_id) VALUES ('sn1', 1)
INSERT INTO Devices (serial_number, type_id) VALUES ('sn2', 2)
INSERT INTO Devices (serial_number, type_id) VALUES ('sn3', 3)

INSERT INTO User_sub_st (user_id, sub_id, begin_date) VALUES (1, 1, to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO User_sub_st (user_id, sub_id, begin_date) VALUES (2, 2, to_date('20-01-02', 'RR-MM-DD'))

INSERT INTO Sub_sim_st (sub_id, sim_id, begin_date) VALUES (1, 1, to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO Sub_sim_st (sub_id, sim_id, begin_date) VALUES (2, 2, to_date('20-01-02', 'RR-MM-DD'))

INSERT INTO User_dev_st (user_id, dev_id, connect) VALUES (1, 1, to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO User_dev_st (user_id, dev_id, connect) VALUES (2, 2, to_date('20-01-02', 'RR-MM-DD'))
INSERT INTO User_dev_st (user_id, dev_id, connect) VALUES (3, 3, to_date('20-01-03', 'RR-MM-DD'))

INSERT INTO Sub_dev_st (sub_id, dev_id, connect) VALUES (1, 1, to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO Sub_dev_st (sub_id, dev_id, connect) VALUES (2, 2, to_date('20-01-02', 'RR-MM-DD'))
INSERT INTO Sub_dev_st (sub_id, dev_id, connect) VALUES (null, 3, to_date('20-01-03', 'RR-MM-DD'))

INSERT INTO Categories (name) VALUES ('Monthly')
INSERT INTO Categories (name) VALUES ('Call')
INSERT INTO Categories (name) VALUES ('Internet')

INSERT INTO Pay_devision (available, name) VALUES (true, 'basic')

INSERT INTO Pay_devision_category_ratio (pay_devision_id, category_ratio_key, category_ratio) VALUES (1, 'Call' , 50)
INSERT INTO Pay_devision_category_ratio (pay_devision_id, category_ratio_key, category_ratio) VALUES (1, 'Monthly' , 0)
INSERT INTO Pay_devision_category_ratio (pay_devision_id, category_ratio_key, category_ratio) VALUES (1, 'Internet' , 30)

INSERT INTO Users_pay_devs (user_id, pay_devs_id) VALUES (1, 1)
