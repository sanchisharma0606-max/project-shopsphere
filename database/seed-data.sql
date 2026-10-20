-- =====================================================================
-- ShopSphere Sample Seed Data
-- Project: RTU Advanced Java E-Commerce Project (ShopSphere)
-- Note: Password for all demo accounts: 'Password@123'
-- BCrypt Hash: '$2a$12$1/E81o7d1l0pYI1W.g5d1.jXjQkPjUeQ5O2Xz6o5u2wP5qK0J9GWW' (standard BCrypt string)
-- We also support a BCrypt utility generator during setup.
-- =====================================================================

USE shopsphere;

-- ---------------------------------------------------------------------
-- 1. Seed Categories
-- ---------------------------------------------------------------------
INSERT INTO categories (category_id, name, description, status) VALUES
(1, 'Electronics', 'Smartphones, Laptops, Audio gear, and Smart gadgets', 'ACTIVE'),
(2, 'Fashion & Apparel', 'Men and Women clothing, Footwear, and Accessories', 'ACTIVE'),
(3, 'Home & Kitchen', 'Home appliances, cookware, and interior decor', 'ACTIVE'),
(4, 'Books & Stationary', 'Academic textbooks, novels, and office stationery', 'ACTIVE'),
(5, 'Fitness & Sports', 'Gym equipment, yoga mats, sports wear, and outdoor gear', 'ACTIVE');

-- ---------------------------------------------------------------------
-- 2. Seed Users
-- Passwords: 'Password@123'
-- Hashed using standard BCrypt algorithm
-- ---------------------------------------------------------------------
INSERT INTO users (user_id, name, email, password_hash, mobile, role, status) VALUES
(1, 'ShopSphere Administrator', 'admin@shopsphere.com', '$2a$10$p5exMtzCmRU6ydGlNfckw..aS0TqSWDwiflCcEW5cjihaKgVMyNAm', '9876543210', 'ADMIN', 'ACTIVE'),
(2, 'Rahul Sharma', 'rahul@example.com', '$2a$10$p5exMtzCmRU6ydGlNfckw..aS0TqSWDwiflCcEW5cjihaKgVMyNAm', '9812345678', 'CUSTOMER', 'ACTIVE'),
(3, 'Priya Patel', 'priya@example.com', '$2a$10$p5exMtzCmRU6ydGlNfckw..aS0TqSWDwiflCcEW5cjihaKgVMyNAm', '9823456789', 'CUSTOMER', 'ACTIVE');

-- ---------------------------------------------------------------------
-- 3. Seed Addresses
-- ---------------------------------------------------------------------
INSERT INTO addresses (address_id, user_id, address_line, city, state, pincode, address_type) VALUES
(1, 2, 'Flat 402, Sunshine Apartments, Civil Lines', 'Jaipur', 'Rajasthan', '302006', 'HOME'),
(2, 2, 'Tech Park, Tower B, Sector 62', 'Noida', 'Uttar Pradesh', '201309', 'WORK'),
(3, 3, '12-A, Lakeview Colony, Malviya Nagar', 'Jaipur', 'Rajasthan', '302017', 'HOME');

-- ---------------------------------------------------------------------
-- 4. Seed Products
-- ---------------------------------------------------------------------
INSERT INTO products (product_id, category_id, name, brand, description, price, discount, stock, image_url, status) VALUES
(1, 1, 'Apple MacBook Air M3 (16GB, 512GB SSD)', 'Apple', '13.6-inch Liquid Retina Display, 18 hours battery life, 8-core CPU and 10-core GPU in sleek Midnight finish.', 114900.00, 7.50, 25, 'assets/images/products/macbook-air.jpg', 'ACTIVE'),
(2, 1, 'Sony WH-1000XM5 Wireless Noise Cancelling Headphones', 'Sony', 'Industry-leading noise canceling with two processors and 8 microphones, Ultra-comfortable design, 30-hour battery life.', 29990.00, 15.00, 40, 'assets/images/products/sony-headphones.jpg', 'ACTIVE'),
(3, 1, 'Samsung Galaxy S24 Ultra 5G (Titanium Gray)', 'Samsung', '200MP Camera with Galaxy AI, Snapdragon 8 Gen 3, S-Pen included, Flat 6.8-inch Dynamic AMOLED 2X display.', 129999.00, 10.00, 15, 'assets/images/products/samsung-s24.jpg', 'ACTIVE'),
(4, 1, 'Logitech MX Master 3S Wireless Performance Mouse', 'Logitech', 'Quiet clicks, 8K DPI any-surface tracking, MagSpeed electromagnetic scrolling, ergonomic silhouette.', 8995.00, 12.00, 60, 'assets/images/products/logitech-mouse.jpg', 'ACTIVE'),
(5, 2, 'Men Slim Fit Premium Oxford Cotton Shirt', 'Arrow', '100% breathable combed cotton, button-down collar, perfect for business meetings or casual evenings.', 2199.00, 20.00, 80, 'assets/images/products/oxford-shirt.jpg', 'ACTIVE'),
(6, 2, 'Women Pure Silk Handloom Banarasi Saree', 'FabIndia', 'Intricate zari floral weave, woven matching blouse piece, traditional artisanal craftsmanship.', 8499.00, 25.00, 30, 'assets/images/products/banarasi-saree.jpg', 'ACTIVE'),
(7, 2, 'Levi\'s Men 511 Slim Fit Stretch Jeans', 'Levi\'s', 'Modern slim fit with room to move, stretch denim engineered for all-day comfort and durability.', 3599.00, 30.00, 50, 'assets/images/products/levis-jeans.jpg', 'ACTIVE'),
(8, 3, 'Philips Air Fryer HD9252/90 (4.1L Capacity)', 'Philips', 'Rapid Air technology for 90% less fat, digital touch screen with 7 presets, keep-warm function.', 7999.00, 18.00, 35, 'assets/images/products/philips-airfryer.jpg', 'ACTIVE'),
(9, 3, 'Prestige Iris 750W Mixer Grinder with 4 Jars', 'Prestige', 'Heavy duty 750 Watt motor, stainless steel multipurpose grinding jars, 3-speed control with pulse.', 3499.00, 22.00, 45, 'assets/images/products/prestige-mixer.jpg', 'ACTIVE'),
(10, 4, 'Core Java: Fundamentals (Volume I, 12th Edition)', 'Cay S. Horstmann', 'Definitive guide to robust Java programming, updated for modern Java features, OOP, Generics, and Collections.', 1250.00, 15.00, 100, 'assets/images/products/core-java-book.jpg', 'ACTIVE'),
(11, 4, 'Atomic Habits by James Clear', 'James Clear', 'An easy and proven way to build good habits and break bad ones. International bestseller with practical frameworks.', 599.00, 25.00, 150, 'assets/images/products/atomic-habits.jpg', 'ACTIVE'),
(12, 5, 'Strauss Anti-Skid TPE Yoga Mat (6mm Thickness)', 'Strauss', 'High-density dual-tone eco-friendly TPE foam, non-slip textured grip, alignment lines, carrying strap included.', 1499.00, 30.00, 70, 'assets/images/products/yoga-mat.jpg', 'ACTIVE');

-- ---------------------------------------------------------------------
-- 5. Seed Coupons
-- ---------------------------------------------------------------------
INSERT INTO coupons (coupon_id, code, discount_type, discount_value, minimum_order, maximum_discount, expiry_date, status) VALUES
(1, 'WELCOME10', 'PERCENTAGE', 10.00, 1000.00, 500.00, '2027-12-31', 'ACTIVE'),
(2, 'FLAT500', 'FIXED', 500.00, 2999.00, 500.00, '2027-12-31', 'ACTIVE'),
(3, 'FESTIVE20', 'PERCENTAGE', 20.00, 5000.00, 2000.00, '2027-12-31', 'ACTIVE');

-- ---------------------------------------------------------------------
-- 6. Seed Cart Items (for Customer Rahul)
-- ---------------------------------------------------------------------
INSERT INTO cart (cart_id, user_id, product_id, quantity) VALUES
(1, 2, 4, 1),
(2, 2, 10, 2);

-- ---------------------------------------------------------------------
-- 7. Seed Wishlist Items
-- ---------------------------------------------------------------------
INSERT INTO wishlist (wishlist_id, user_id, product_id) VALUES
(1, 2, 1),
(2, 2, 2),
(3, 3, 6);

-- ---------------------------------------------------------------------
-- 8. Seed Orders & Items
-- ---------------------------------------------------------------------
INSERT INTO orders (order_id, user_id, address_id, total_amount, discount, payment_method, payment_status, order_status, created_at) VALUES
(1, 2, 1, 3058.00, 500.00, 'UPI', 'PAID', 'DELIVERED', '2026-03-10 11:30:00'),
(2, 2, 1, 25491.50, 0.00, 'CREDIT_CARD', 'PAID', 'CONFIRMED', '2026-04-01 16:45:00');

INSERT INTO order_items (order_item_id, order_id, product_id, quantity, price) VALUES
(1, 1, 10, 1, 1062.50),
(2, 1, 5, 1, 1759.20),
(3, 1, 11, 1, 449.25),
(4, 2, 2, 1, 25491.50);

INSERT INTO payments (payment_id, order_id, payment_method, transaction_reference, amount, payment_status) VALUES
(1, 1, 'UPI', 'TXN_UPI_20260310_884920', 3058.00, 'COMPLETED'),
(2, 2, 'CREDIT_CARD', 'TXN_CC_20260401_991823', 25491.50, 'COMPLETED');

-- ---------------------------------------------------------------------
-- 9. Seed Reviews
-- ---------------------------------------------------------------------
INSERT INTO reviews (review_id, user_id, product_id, rating, review_text) VALUES
(1, 2, 2, 5, 'Best noise cancellation on the market. Extremely lightweight and battery lasts for days!'),
(2, 3, 2, 4, 'Amazing sound quality, clear calls. Fast charging is a life saver.'),
(3, 2, 10, 5, 'Must-read book for every Advanced Java student. Concepts are explained thoroughly.'),
(4, 3, 6, 5, 'Authentic Banarasi silk quality. Stunning luster and finish for wedding occasions.');
