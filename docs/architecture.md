# ShopSphere — Architecture & System Design

## 1. Architectural Philosophy
ShopSphere is engineered as an enterprise-grade **N-Tier Distributed System** mapped directly to the **RTU Advanced Java** curriculum. It separates concerns strictly into presentation, request control, business domain logic, and persistence layers.

---

## 2. N-Tier Layered Architecture Diagram

```mermaid
graph TD
    subgraph Presentation_Tier ["Presentation Tier"]
        Browser["Modern Web Browser (HTML5 / Bootstrap 5 / JS)"]
        SwingApp["ShopSphere Swing Desktop Admin (JFrame / JTable)"]
        SocketClient["Inventory Socket Client (java.net.Socket)"]
        RmiClient["Inventory RMI Client (LocateRegistry.lookup)"]
    end

    subgraph Container_Tier ["Web Container Tier (Apache Tomcat 11 / Jakarta EE 10)"]
        Filters["EncodingFilter | AuthenticationFilter | AdminFilter"]
        Listeners["AppContextListener | SessionTrackerListener"]
        Servlets["Controllers (LoginServlet, ProductServlet, CheckoutServlet, etc.)"]
        Views["JSP 3.1 Views + EL + JSTL + Reusable Tag Files"]
    end

    subgraph Distributed_Tier ["Distributed & Networking Tier"]
        SocketServer["InventorySocketServer (ServerSocket :9090)"]
        RmiServer["InventoryServiceImpl (RMI Registry :1099)"]
    end

    subgraph Service_Tier ["Business Logic & Service Layer"]
        UserService["UserService"]
        ProductService["ProductService"]
        CartService["CartService"]
        CheckoutService["CheckoutService (Transaction Coordinator)"]
        OrderService["OrderService"]
        CouponService["CouponService"]
    end

    subgraph Persistence_Tier ["Data Access Object (DAO) Layer"]
        UserDAO["UserDAO"]
        ProductDAO["ProductDAO"]
        OrderDAO["OrderDAO"]
        CartDAO["CartDAO"]
        CouponDAO["CouponDAO"]
    end

    subgraph Database_Tier ["Database Storage Tier"]
        MySQL[("MySQL 8.0 InnoDB Database (shopsphere)")]
    end

    Browser -->|HTTP GET/POST| Filters
    Filters --> Servlets
    Servlets --> Views
    Servlets --> Service_Tier

    SwingApp -->|Direct JVM / SwingWorker| Service_Tier
    SocketClient -->|TCP Stream| SocketServer
    SocketServer --> Service_Tier
    RmiClient -->|RMI / JRMP| RmiServer
    RmiServer --> Service_Tier

    Service_Tier --> Persistence_Tier
    Persistence_Tier -->|PreparedStatement / JDBC| MySQL
```

---

## 3. Sequence Diagrams

### 3.1 Customer Authentication Sequence
```mermaid
sequenceDiagram
    autonumber
    actor Customer as User / Browser
    participant Filter as AuthenticationFilter
    participant Servlet as LoginServlet
    participant Service as UserService
    participant DAO as UserDAO
    participant DB as MySQL Database
    participant Session as HttpSession

    Customer->>Servlet: POST /login (email, password)
    Servlet->>Service: authenticate(email, password)
    Service->>DAO: findByEmail(email)
    DAO->>DB: SELECT * FROM users WHERE LOWER(email) = ?
    DB-->>DAO: User Record with BCrypt Hash
    DAO-->>Service: Optional<User>
    Service->>Service: BCrypt.checkpw(plainPassword, storedHash)
    
    alt Password Valid & Active
        Service-->>Servlet: Optional.of(user)
        Servlet->>Session: session.setAttribute("user", user)
        Servlet->>Session: session.setAttribute("role", user.getRole())
        Servlet-->>Customer: 302 Redirect to /home or preserved URL
    else Invalid Password or Inactive
        Service-->>Servlet: Optional.empty()
        Servlet-->>Customer: Forward to login.jsp with Error Alert
    end
```

### 3.2 Transactional Order Checkout & Inventory Deduction
```mermaid
sequenceDiagram
    autonumber
    actor Customer as Authenticated Customer
    participant Servlet as CheckoutServlet
    participant Service as CheckoutService
    participant DB as MySQL (Connection)
    participant ProductDAO as ProductDAO
    participant OrderDAO as OrderDAO
    participant CartDAO as CartDAO

    Customer->>Servlet: POST /checkout (addressId, paymentMethod, coupon)
    Servlet->>Service: processCheckout(userId, addressId, method, coupon)
    Service->>Service: Validate Address Ownership & Recalculate Totals
    Service->>DB: DBConnection.getConnection()
    Service->>DB: conn.setAutoCommit(false) [BEGIN TRANSACTION]

    critical Transaction Atomic Block
        Service->>OrderDAO: createOrder(conn, order)
        OrderDAO->>DB: INSERT INTO orders ...
        DB-->>OrderDAO: Generated orderId

        loop For Each Cart Line Item
            Service->>ProductDAO: deductStock(conn, productId, quantity)
            ProductDAO->>DB: UPDATE products SET stock = stock - ? WHERE product_id = ? AND stock >= ?
            alt Stock Available
                DB-->>ProductDAO: Rows Affected = 1
            else Stock Sold Out (Race condition)
                DB-->>ProductDAO: Rows Affected = 0
                ProductDAO-->>Service: Return false (Oversell prevented!)
                Service->>DB: conn.rollback()
                Service-->>Servlet: Throw IllegalStateException("Sold Out")
            end
        end

        Service->>CartDAO: clearCart(conn, userId)
        CartDAO->>DB: DELETE FROM cart WHERE user_id = ?
        Service->>DB: conn.commit() [COMMIT TRANSACTION]
    end

    Service-->>Servlet: Placed Order Object
    Servlet-->>Customer: 302 Redirect to /order-success?id=orderId
```

---

## 4. Concurrency & Thread Safety
1. **Jakarta Servlets**: Servlets are multi-threaded singletons. All request-scoped variables are kept inside method local parameters or request attributes to eliminate race conditions.
2. **Java Swing Desktop (EDT)**: The Swing GUI Event Dispatch Thread is protected from freezing by offloading all database queries onto asynchronous `SwingWorker<T, V>` background threads.
3. **Socket Networking**: `InventorySocketServer` uses an `ExecutorService` cached thread pool so that multiple client TCP sockets communicate concurrently.
4. **Database Connections**: Connections are leased on demand via `DBConnection.getConnection()` and closed via try-with-resources. No static global `Connection` instances exist.
