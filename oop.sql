CREATE TABLE users (
    ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    First_Name VARCHAR(40) NOT NULL,
    Last_name VARCHAR(40) NOT NULL,
    roles CHAR(1) NOT NULL,
    userName VARCHAR(30) UNIQUE NOT NULL,
    age DATE NOT NULL,
    shipping_info CLOB NOT NULL, -- Changed to CLOB for long text
    password CLOB NOT NULL -- Changed to CLOB for long text, added password column
);

-- Added constraint for roles
ALTER TABLE users
ADD CONSTRAINT rolescons CHECK(roles = 's' OR roles = 'c');

CREATE TABLE cart (
    ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    total DECIMAL(10, 2) NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(ID)
);

-- Added constraint for total
ALTER TABLE cart
ADD CONSTRAINT totalcons CHECK(total > 0.00);

CREATE TABLE orders (
    ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    price DECIMAL(10, 2) NOT NULL,
    odate DATE DEFAULT CURRENT_TIMESTAMP,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(ID)
);

-- Added constraint for price
ALTER TABLE orders
ADD CONSTRAINT opricecons CHECK(price > 0.00);

CREATE TABLE product (
    ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, -- Changed position of GENERATED ALWAYS
    p_name VARCHAR(70) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    Manufacturer VARCHAR(50), -- Corrected spelling of 'Manufacturer'
    seller INT NOT NULL,
    FOREIGN KEY (seller) REFERENCES users(ID)
);

-- Added constraint for price
ALTER TABLE product
ADD CONSTRAINT ppricecons CHECK(price > 0.00);

CREATE TABLE cart_products (
    ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    cart INT,
    product INT,
    FOREIGN KEY (cart) REFERENCES cart(ID),
    FOREIGN KEY (product) REFERENCES product(ID)
);

CREATE TABLE Order_Product (
    order_id INT NOT NULL,
    product INT NOT NULL,
    FOREIGN KEY (product) REFERENCES product(ID),
    FOREIGN KEY (order_id) REFERENCES orders(ID)
);
