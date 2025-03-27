INSERT INTO books (title, description, price, type, stock, added_at, updated_at)
VALUES ('Spring Boot in Action', 'Comprehensive guide to Spring Boot', 39.99, 'NEW_RELEASE', 10, NOW(), NOW()),
       ('Effective Java', 'Best practices for Java programming', 49.99, 'REGULAR', 5, NOW(), NOW()),
       ('Clean Code', 'A Handbook of Agile Software Craftsmanship', 29.99, 'OLD_EDITION', 8, NOW(), NOW()),
       ('Microservices Patterns', 'Designing microservice architectures', 45.50, 'REGULAR', 12, NOW(), NOW()),
       ('Java Concurrency in Practice', 'Deep dive into Java multithreading', 55.00, 'NEW_RELEASE', 6, NOW(), NOW());


INSERT INTO customers (name, loyalty_points)
VALUES ('Alice Johnson', 5),
       ('Bob Smith', 10),
       ('Charlie Brown', 2);


INSERT INTO orders (customer_id, total_price, order_date)
VALUES (1, 79.98, NOW()),
       (2, 50.00, NOW()),
       (3, 90.00, NOW());


INSERT INTO order_items (order_id, book_id, quantity, price)
VALUES (1, 2, 1, 49.99), -- Alice buys "Effective Java"
       (1, 3, 1, 29.99), -- Alice buys "Clean Code"
       (2, 1, 1, 39.99), -- Bob buys "Spring Boot in Action"
       (3, 4, 2, 45.00); -- Charlie buys 2 copies of "Microservices Patterns"