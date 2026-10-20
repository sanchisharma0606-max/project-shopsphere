# ShopSphere — RTU Advanced Java Comprehensive Viva Preparation Guide

This reference provides question-and-answer preparation for practical exams, external vivas, and project defense under the Rajasthan Technical University (RTU) Advanced Java syllabus.

---

### 1. Java Swing & Desktop GUI
**Q1: What is the Swing MVC architecture and how is it used in ShopSphere?**  
**Answer:** Swing follows a Model-View-Controller architecture. In ShopSphere:
- **Model**: Domain classes like `Product`, `Category`, `Order`, and Swing's `DefaultTableModel`.
- **View**: GUI components like `JFrame`, `JPanel`, `JTable`, `JTextField`.
- **Controller**: Event listeners and `SwingWorker` tasks that handle user actions and interact with the service layer.

**Q2: What is the Event Dispatch Thread (EDT) and why must we use `SwingUtilities.invokeLater()`?**  
**Answer:** Swing components are not thread-safe. All GUI rendering and event handling must execute on a single thread called the Event Dispatch Thread (EDT). `SwingUtilities.invokeLater()` queues GUI creation on the EDT to prevent concurrency anomalies.

**Q3: How do you prevent long database operations from freezing the Swing GUI?**  
**Answer:** By using `javax.swing.SwingWorker<T, V>`. The heavy database queries execute asynchronously in the `doInBackground()` method (on a worker thread), and the results are applied to the GUI in the `done()` method on the EDT.

---

### 2. JDBC & Data Access (java.sql)
**Q4: Why should we always use `PreparedStatement` instead of `Statement`?**  
**Answer:** 
1. **Security**: `PreparedStatement` pre-compiles the SQL query and binds user input as typed parameters (`?`), eliminating SQL Injection vulnerabilities.
2. **Performance**: Pre-compiled queries are cached by the database engine for repeated executions.

**Q5: How do database transactions ensure atomicity during checkout?**  
**Answer:** In `CheckoutService`, we invoke `conn.setAutoCommit(false)` before executing multiple operations (inserting the order, deducting stock, recording payment, clearing cart). If all statements succeed, `conn.commit()` is called. If any failure or insufficient stock occurs, `conn.rollback()` restores the database to its previous consistent state.

---

### 3. Servlet Architecture & Web Container
**Q6: What is the lifecycle of a Jakarta Servlet?**  
**Answer:**
1. **Loading & Instantiation**: The container (Tomcat 11) creates a single servlet instance.
2. **Initialization**: The container calls `init(ServletConfig)` once.
3. **Request Handling**: For each HTTP request, the container allocates a worker thread and calls `service()`, which delegates to `doGet()` or `doPost()`.
4. **Destruction**: When the container shuts down or unloads the servlet, it calls `destroy()` to release resources.

**Q7: What is the difference between `ServletConfig` and `ServletContext`?**  
**Answer:**
- `ServletConfig`: Unique to a specific servlet, providing initialization parameters defined in `web.xml` or annotations.
- `ServletContext`: Shared across the entire web application, providing global context attributes (like `appName`, active sessions) accessible by all servlets and JSPs.

---

### 4. JSP, Expression Language (EL), and JSTL
**Q8: Explain the translation and compilation lifecycle of a JSP.**  
**Answer:** When a JSP is first requested, the web container translates the `.jsp` file into a Java Servlet source file (inheriting `HttpJspBase`), compiles it into a `.class` bytecode file, and invokes `_jspService(HttpServletRequest, HttpServletResponse)`. Subsequent requests reuse the compiled servlet.

**Q9: What is the difference between JSP Scriptlets and Expression Language (EL)?**  
**Answer:**
- **Scriptlets (`<% ... %>`)**: Embed raw Java code inside HTML, which can become hard to maintain and violates MVC.
- **Expression Language (`${...}`)**: Provides clean, null-safe syntax to read scoped attributes (`pageScope`, `requestScope`, `sessionScope`, `applicationScope`) and javabean properties without throwing `NullPointerException`.

**Q10: What are JSP Tag Files and how do they differ from JSP Fragments?**  
**Answer:**
- **JSP Fragments (`<%@ include file="..." %>`)**: Statically include reusable HTML/JSP chunks at translation time (e.g., `header.jsp`, `navbar.jsp`).
- **Tag Files (`.tag` in `/WEB-INF/tags/`)**: Reusable components that accept attributes (like custom UI widgets `productCard.tag`, `rating.tag`) without requiring Java classes or TLD registration.

---

### 5. Sessions, Cookies, Filters, and Listeners
**Q11: How does `HttpSession` manage user state across HTTP requests?**  
**Answer:** HTTP is stateless. When `request.getSession(true)` is called, the server creates an `HttpSession` object and transmits a unique session identifier cookie (usually `JSESSIONID`) to the browser. The browser returns this cookie in subsequent requests so Tomcat can resolve the user's session.

**Q12: What is the purpose of `AuthenticationFilter` and `AdminFilter`?**  
**Answer:** Filters intercept HTTP requests before they reach the servlet controllers.
- `AuthenticationFilter` verifies that customer requests have an active session, redirecting unauthenticated users to `/login`.
- `AdminFilter` verifies that the logged-in user possesses the `ADMIN` role before granting access to `/admin/*`.

---

### 6. Java Networking & Distributed Computing (Sockets & RMI)
**Q13: How does client-server communication work in `java.net`?**  
**Answer:** The server creates a `ServerSocket` bound to a TCP port and waits with `serverSocket.accept()`. When a client connects using `new Socket(host, port)`, a full-duplex TCP stream is established. Input and output streams (`BufferedReader`, `PrintWriter`) allow structured text exchange.

**Q14: What is Java RMI and what role does the RMI Registry play?**  
**Answer:** Remote Method Invocation (RMI) enables a Java program on one JVM to invoke methods on an object residing in another JVM.
- The **RMI Registry** acts as a naming lookup directory where the server binds a remote object (`registry.rebind("InventoryService", service)`), and the client locates the remote reference using `registry.lookup("InventoryService")`.

**Q15: Why must objects transferred over RMI or Object Streams implement `Serializable`?**  
**Answer:** `java.io.Serializable` is a marker interface that authorizes the JVM to flatten the object's memory state into a binary byte sequence via `ObjectOutputStream` for network transmission or disk storage. The `serialVersionUID` guarantees version consistency between sender and receiver.

---

### 7. Historical Concepts & Internationalization
**Q16: Why are Java Applets obsolete in modern web development?**  
**Answer:** Java Applets required NPAPI browser plugins and a heavy client-side JRE. Modern browsers removed NPAPI due to major security vulnerabilities. Servlets and modern frontend standards (HTML5/CSS/JavaScript) moved execution to the server side and standards-compliant browser engines.

**Q17: How does ShopSphere implement Internationalization (I18n)?**  
**Answer:** Using `java.util.Locale` and `java.util.ResourceBundle`. Property files (`messages_en.properties`, `messages_hi.properties`) store localized strings. When a user switches languages, the session updates its `Locale`, formatting UI labels, currency, and timestamps accordingly.
