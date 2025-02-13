INSERT INTO users (user_id, city, country, email, first_name, last_name, personal_id_number, phone_number, sex,
                   user_type, address_line, password, zip_code, date_of_birth, is_non_expired, is_non_locked,
                   is_credentials_non_expired, is_enabled, language)
VALUES (NEXTVAL('user_id_seq'), 'Test city', 'Test country', 'test@mail.com', 'Test', 'Test', '123456789', '123456789', true,
        'DOCTOR', 'Test address line', 'password', '12345', '1990-01-01', true, true, true, true, 'en');

INSERT INTO users (user_id, city, country, email, first_name, last_name, personal_id_number, phone_number, sex,
                   user_type, address_line, password, zip_code, date_of_birth, is_non_expired, is_non_locked,
                   is_credentials_non_expired, is_enabled, language)
VALUES (NEXTVAL('user_id_seq'), 'Test city', 'Test country', 'test2@mail.com', 'Test2', 'Test2', '123456789', '123456789',
        false, 'PATIENT', 'Test address line', 'password', '12345', '1990-01-01', true, true, true, true, 'en');

INSERT INTO users(user_id, city, country, email, first_name, last_name, personal_id_number, phone_number, sex,
                  user_type, address_line, password, zip_code, date_of_birth, is_non_expired, is_non_locked,
                  is_credentials_non_expired, is_enabled, language)
VALUES (NEXTVAL('user_id_seq'), 'Test city', 'Test country', 'test3@mail.com', 'Test3', 'Test3', '123456789', '123456789',
        true, 'DOCTOR', 'Test address line', 'password', '12345', '1990-01-01', true, true, true, true, 'pl');

INSERT INTO treatments (treatment_id, treatment_description, treatment_name, treatment_price)
VALUES (NEXTVAL('treatment_id_seq'), 'Test description', 'Test name', 100.0);

INSERT INTO treatments (treatment_id, treatment_description, treatment_name, treatment_price)
VALUES (NEXTVAL('treatment_id_seq'), 'Test description2', 'Test name2', 150.0);

INSERT INTO treatments (treatment_id, treatment_description, treatment_name, treatment_price)
VALUES (NEXTVAL('treatment_id_seq'), 'Test description3', 'Test name3', 300.0);

INSERT INTO supplies (supply_id, link, name, quantity)
VALUES (NEXTVAL('supply_id_seq'), 'http://test.com', 'Test supply', 10);

INSERT INTO supplies (supply_id, link, name, quantity)
VALUES (NEXTVAL('supply_id_seq'), 'http://test2.com', 'Test supply2', 5);

INSERT INTO supplies (supply_id, link, name, quantity)
VALUES (NEXTVAL('supply_id_seq'), 'http://test3.com', 'Test supply3', 8);