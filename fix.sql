CREATE TABLE orders (
    ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    price DECIMAL(10, 2) NOT NULL,
    odate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Changed data type to TIMESTAMP
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES USERS(ID)
);
CREATE TABLE Order_Product (
    order_id INT NOT NULL,
    product INT NOT NULL,
    FOREIGN KEY (product) REFERENCES product(ID),
    FOREIGN KEY (order_id) REFERENCES orders(ID)
);