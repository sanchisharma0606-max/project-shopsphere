# ShopSphere Database Setup & Management

This directory contains the complete database scripts for the ShopSphere Advanced Java E-Commerce Project.

## Files
1. `shopsphere.sql`: Complete DDL schema script (12 relational tables, foreign key constraints, indexes, cascade rules, and check constraints).
2. `seed-data.sql`: Realistic seed dataset for categories, products, coupons, customer accounts, orders, reviews, and admin account.

---

## Quick Database Setup via MySQL CLI

Run the following commands in PowerShell or Command Prompt:

```powershell
# 1. Execute Schema Script (Creates database 'shopsphere' and tables)
mysql -u root -p < database/shopsphere.sql

# 2. Execute Sample Seed Data Script
mysql -u root -p < database/seed-data.sql
```

Alternatively, from within MySQL shell:
```sql
SOURCE b:/Swing_Java/ShopSphere/database/shopsphere.sql;
SOURCE b:/Swing_Java/ShopSphere/database/seed-data.sql;
```

---

## Seed Credentials

All seed accounts come pre-configured with the default password:
- **Default Password:** `Password@123`
- **Admin Account:** `admin@shopsphere.com`
- **Customer 1:** `rahul@example.com`
- **Customer 2:** `priya@example.com`

> Passwords are encrypted using the standard BCrypt hashing algorithm (`$2a$10$...`) with salt rounds = 10. Plaintext passwords are never stored in the database.
