-- Add test books
INSERT INTO books (id, title, author, isbn, price, description, cover_image)
VALUES (4, 'Sample Book 1', 'Author A', '978-1-23-456789-7', 19.99, 'This is a sample book description.', 'http://example.com/cover1.jpg');

-- Add test users
INSERT INTO users (id, email, password, first_name, last_name, shipping_address)
VALUES (3, 'user@i.ua', 'qwerty123', 'John', 'Smith', 'Ukraine');

INSERT INTO users_roles(user_id, role_id)
VALUES (3, 2);
-- Add test shopping carts
INSERT INTO shopping_carts (id ,user_id) VALUES (3,3);

-- Add test cart item
INSERT INTO cart_items (shopping_cart_id, book_id, quantity)
VALUES (3, 4, 6);
