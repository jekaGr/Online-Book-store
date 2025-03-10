INSERT INTO users (id, email, password, first_name, last_name, shipping_address)
VALUES (3, 'user@i.ua', 'qwerty123', 'John', 'Smith', 'Ukraine');

INSERT INTO users_roles(user_id, role_id)
VALUES (3, 2);