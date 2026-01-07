-- Create the database
CREATE DATABASE IF NOT EXISTS ecommerce_db;
USE ecommerce_db;

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    isAdmin BOOLEAN DEFAULT FALSE
);

-- Products Table
CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL,
    category VARCHAR(100),
    rating DECIMAL(2, 1) DEFAULT 0.0,
    addedBy VARCHAR(100) DEFAULT 'Admin'
);

-- Orders Table
CREATE TABLE IF NOT EXISTS orders (
    orderId INT AUTO_INCREMENT PRIMARY KEY,
    userId INT NOT NULL,
    totalPrice DECIMAL(10, 2) NOT NULL,
    orderDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (userId) REFERENCES users(id)
);

-- Order Items Table (to link orders and products)
CREATE TABLE IF NOT EXISTS order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    orderId INT NOT NULL,
    productId INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (orderId) REFERENCES orders(orderId),
    FOREIGN KEY (productId) REFERENCES products(id)
);

-- Insert Default Admin User
-- Password is 'admin123'
INSERT INTO users (name, email, password, isAdmin) VALUES ('Admin User', 'admin', 'admin123', TRUE);

-- Insert some sample products
INSERT INTO products (name, price, quantity, category, rating) VALUES 
('Smartphone', 15000.00, 10, 'Electronics', 4.5),
('Laptop', 45000.00, 5, 'Electronics', 4.8),
('Headphones', 2500.00, 20, 'Accessories', 4.2),
('T-Shirt', 500.00, 50, 'Fashion', 4.0);
