INSERT INTO users (user_id, city, country, email, first_name, last_name, personal_id_number, phone_number, sex,
                   user_type, address_line, password, zip_code, date_of_birth, is_non_expired, is_non_locked,
                   is_credentials_non_expired, is_enabled)
VALUES (NEXTVAL('user_id_seq'), 'Test city', 'Test country', 'test@mail.com', 'Test', 'Test', '123456789', '123456789', true,
        'DOCTOR', 'Test address line', 'password', '12345', '1990-01-01', true, true, true, true);

INSERT INTO users (user_id, city, country, email, first_name, last_name, personal_id_number, phone_number, sex,
                   user_type, address_line, password, zip_code, date_of_birth, is_non_expired, is_non_locked,
                   is_credentials_non_expired, is_enabled)
VALUES (NEXTVAL('user_id_seq'), 'Test city', 'Test country', 'test2@mail.com', 'Test2', 'Test2', '123456789', '123456789',
        false, 'PATIENT', 'Test address line', 'password', '12345', '1990-01-01', true, true, true, true);

INSERT INTO users(user_id, city, country, email, first_name, last_name, personal_id_number, phone_number, sex,
                  user_type, address_line, password, zip_code, date_of_birth, is_non_expired, is_non_locked,
                  is_credentials_non_expired, is_enabled)
VALUES (NEXTVAL('user_id_seq'), 'Test city', 'Test country', 'test3@mail.com', 'Test3', 'Test3', '123456789', '123456789',
        true, 'ASSISTANT', 'Test address line', 'password', '12345', '1990-01-01', true, true, true, true);
