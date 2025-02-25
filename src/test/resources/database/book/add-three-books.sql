INSERT INTO books (id, title, author, price, is_deleted, description, isbn)
values (1, 'Test book', 'Test author', 40.99, false,
        'Test description', '123456789');
INSERT INTO books (id, title, author, price, is_deleted, description, isbn)
values (2, 'Test book1', 'Test author1', 30.99, false,
        'Test description1', '123456798');
INSERT INTO books (id, title, author, price, is_deleted, description, isbn)
values (3, 'Test book2', 'Test author2', 10.99, false,
        'Test description2', '123456987');
insert into categories (name, description, is_deleted)
VALUES ('category1', 'category desc1', false),
       ('category2', 'category desc2', false),
       ('category3', 'category desc3', false);
INSERT INTO books_categories (book_id, category_id)
VALUES (1, 1),
       (2, 2),
       (3, 3);