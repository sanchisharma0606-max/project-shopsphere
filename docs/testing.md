# ShopSphere — Testing Strategy & Verification Report

## 1. Testing Philosophy
ShopSphere incorporates two rigorous tiers of verification:
1. **Automated Unit Tests (JUnit 5)**: Covering core mathematical domain models, coupon algorithms, BCrypt hashing, input validation, and XSS sanitization.
2. **Manual Integration Checklist**: Covering all end-to-end user journeys across the web storefront, admin portal, Swing desktop GUI, Socket networking, and RMI distributed service.

---

## 2. Automated JUnit 5 Test Results

The automated unit tests were executed with Maven Surefire against OpenJDK 21:

```
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running com.shopsphere.model.CartItemTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.045 s
Running com.shopsphere.model.CouponModelTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.021 s
Running com.shopsphere.model.ProductModelTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.010 s
Running com.shopsphere.util.I18nUtilTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.026 s
Running com.shopsphere.util.PasswordUtilTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.298 s
Running com.shopsphere.util.ValidationUtilTest
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.008 s

Results:
Tests run: 16, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

---

## 3. Manual Integration Verification Checklist

| Test ID | Module | Action Description | Expected Result | Verified Status |
|---|---|---|---|---|
| **INT-01** | Web | Customer Registration with unique email | User record created with BCrypt password hash in MySQL | Pass |
| **INT-02** | Web | Registration with duplicate email | Validation error displayed: "An account with this email already exists" | Pass |
| **INT-03** | Web | Login with valid credentials (`rahul@example.com` / `Password@123`) | Authenticated session created, cart count loaded, redirects to home | Pass |
| **INT-04** | Web | Login with incorrect password | Friendly error shown: "Invalid email address or password" | Pass |
| **INT-05** | Web | Unauthenticated access to `/cart` or `/orders` | `AuthenticationFilter` intercepts and redirects to `/login?error=auth_required` with target preserved | Pass |
| **INT-06** | Web | Customer attempts to access `/admin/dashboard` | `AdminFilter` denies access (HTTP 403 Forbidden or redirect) | Pass |
| **INT-07** | Web | Admin login (`admin@shopsphere.com` / `Password@123`) | `AdminFilter` authorizes, redirects to `/admin/dashboard` | Pass |
| **INT-08** | Web | Keyword product search (e.g., "MacBook") | Returns matching products with accurate title, brand, and pricing | Pass |
| **INT-09** | Web | Category and price range filtering | Products filtered by category ID and min/max price bounds | Pass |
| **INT-10** | Web | Add item to cart | Cart table updated in MySQL, badge counter increments | Pass |
| **INT-11** | Web | Increase quantity beyond warehouse stock | Rejected with alert: "Requested quantity exceeds available stock" | Pass |
| **INT-12** | Web | Apply coupon code `WELCOME10` on cart | 10% discount subtracted on server, grand total recalculates | Pass |
| **INT-13** | Web | Transactional checkout submission | Order & order items created in MySQL, stock deducted atomically, cart cleared | Pass |
| **INT-14** | Web | Visual order timeline | Displays current order status badge and step timeline | Pass |
| **INT-15** | Web | Customer cancels order | Order status marked CANCELLED, warehouse stock replenished | Pass |
| **INT-16** | Swing | Launch Desktop Admin GUI (`DesktopAdminApp`) | Native look & feel applied, login frame opens | Pass |
| **INT-17** | Swing | Admin desktop authentication | Validated via `UserService` on background `SwingWorker` thread | Pass |
| **INT-18** | Swing | Product management in `ProductPanel` | Table loads products from MySQL, Add/Edit/Delete dialogs work | Pass |
| **INT-19** | Swing | Inventory stock update in `InventoryPanel` | New stock quantity committed to database | Pass |
| **INT-20** | Socket | Start `InventorySocketServer` on port 9090 | Listens on port 9090, accepts client TCP sockets | Pass |
| **INT-21** | Socket | Run client commands (`PING`, `GET_PRODUCT 1`, `GET_STOCK 1`, `QUIT`) | Server executes commands and returns structured protocol responses | Pass |
| **INT-22** | Serial | Run `SerializationDemo` | `Product` serialized to byte stream and restored with 100% field integrity | Pass |
| **INT-23** | RMI | Start `RmiServer` and run `RmiClient` | Registry bound on port 1099, remote calls (`ping`, `getProduct`, `getStock`) executed | Pass |
| **INT-24** | Academic | Access `/academic-demos` | Demonstrations for Scriptlets vs EL, JSTL XML, JSTL SQL, JNDI, and Applets load | Pass |
