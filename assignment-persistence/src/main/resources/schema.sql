CREATE TABLE IF NOT EXISTS books
(
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(255)   NOT NULL,
    description TEXT, -- Added description column
    price       DECIMAL(10, 2) NOT NULL,
    type        VARCHAR(50) CHECK (type IN ('NEW_RELEASE', 'REGULAR', 'OLD_EDITION')),
    stock       INT            NOT NULL,
    added_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS customers
(
    id             SERIAL PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    loyalty_points INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS orders
(
    id          SERIAL PRIMARY KEY,
    customer_id INT REFERENCES customers (id),
    total_price DECIMAL(10, 2) NOT NULL,
    order_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_items
(
    id       SERIAL PRIMARY KEY,
    order_id INT REFERENCES orders (id),
    book_id  INT REFERENCES books (id),
    quantity INT            NOT NULL,
    price    DECIMAL(10, 2) NOT NULL
);