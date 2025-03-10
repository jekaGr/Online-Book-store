-- Add test books
INSERT INTO books (id, title, author, isbn, price, description, cover_image)
VALUES (4, 'Sample Book 1', 'Author A', '978-1-23-456789-7', 19.99, 'This is a sample book description.', 'http://example.com/cover1.jpg');
-- Add test shopping carts
INSERT INTO shopping_carts (id ,user_id) VALUES (3,3);
INSERT INTO cart_items (shopping_cart_id, book_id, quantity)
VALUES (3, 4, 6);