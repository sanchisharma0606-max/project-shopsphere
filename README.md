# ShopSphere — Advanced Java E-Commerce Management System

[![Java Version](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)
[![Tomcat](https://img.shields.io/badge/Tomcat-11.0-blue.svg)](https://tomcat.apache.org/)
[![Jakarta EE](https://img.shields.io/badge/Jakarta_EE-10-purple.svg)](https://jakarta.ee/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-teal.svg)](https://www.mysql.com/)
[![Build Tool](https://img.shields.io/badge/Maven-3.9+-red.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-Academic_Evaluation-green.svg)]()

> Complete Final Practical Project engineered according to the **Rajasthan Technical University (RTU) Advanced Java / Java Enterprise System** curriculum.

---

## 1. Project Overview & Multi-Module Architecture

ShopSphere provides an enterprise-style e-commerce platform structured into five coordinated Maven modules:

```
ShopSphere/
├── pom.xml                     # Root Aggregator & Parent Dependency Management POM
├── .env.example                # Environment variables template
├── database/                   # MySQL 8.0 schema & seed data
│   ├── shopsphere.sql          # 12 Relational tables, FK constraints, and indexes
│   ├── seed-data.sql           # Realistic seed dataset (Users, Products, Coupons)
│   └── README.md
├── docs/                       # Comprehensive Architecture & Viva documentation
│   ├── architecture.md         # N-Tier architecture & Mermaid sequence flows
│   ├── database-design.md      # Schema documentation & Mermaid ER diagram
│   ├── testing.md              # JUnit 5 test report & Integration checklist
│   └── viva-questions.md       # RTU syllabus questions & answers
├── shopsphere-core/            # Shared Domain POJOs, JDBC Connection, DAOs, Services
├── shopsphere-web/             # Jakarta Servlet 6.0 / JSP 3.1 Web Application (WAR)
├── shopsphere-desktop/         # Java Swing Desktop Management Suite (Swing MVC)
├── shopsphere-network/         # java.net Socket Server & Client with Serialization
└── shopsphere-rmi/             # Distributed Inventory Service via Java RMI
```

---

## 2. Technology Stack & Prerequisites

| Component | Specification |
|---|---|
| **Programming Language** | Java SE 21 (OpenJDK 21 / JBR 21) |
| **Web Framework** | Jakarta Servlet 6.0, JSP 3.1, JSTL 3.0 (Jakarta EE 10) |
| **Servlet Container** | Apache Tomcat 11.0+ |
| **Database Management** | MySQL Server 8.0+ (InnoDB Engine) |
| **Database Driver** | MySQL Connector/J 8.3+ |
| **Build Management** | Apache Maven 3.9+ (includes `mvn.cmd` wrapper) |
| **Desktop GUI** | Java Swing (Swing MVC with `SwingWorker`) |
| **Distributed Systems** | Java RMI (Registry & Remote Interfaces) |
| **Networking** | `java.net.ServerSocket` and `Socket` (Multi-threaded TCP) |
| **Security** | BCrypt Password Hashing (`jBCrypt`) & Filter Authorization |
| **Unit Testing** | JUnit 5 (`junit-jupiter:5.10.2`) |

---

## 3. Database Setup & Initialization

### Step 3.1: Start MySQL Server
Ensure your local MySQL 8 service is running. On Windows PowerShell:
```powershell
Get-Service -Name *mysql*
```

### Step 3.2: Import Schema & Seed Data
You can use the automated interactive batch script:
```powershell
.\import-database.bat
```

Or manually execute the SQL initialization scripts:
```powershell
# Windows PowerShell
mysql -u root -p -e "source database/shopsphere.sql;"
mysql -u root -p -e "source database/seed-data.sql;"
```

*Or on Linux / macOS:*
```bash
mysql -u root -p < database/shopsphere.sql
mysql -u root -p < database/seed-data.sql
```

### Step 3.3: Configure Database Credentials
Configure database settings via environment variables or a local `db.properties` file:

**Option A (Local `db.properties` in `shopsphere-core/src/main/resources/db.properties`):**
```properties
db.url=jdbc:mysql://localhost:3306/shopsphere?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8
db.user=root
db.password=your_mysql_password
```

**Option B (Environment Variables):**
```powershell
$env:SHOPSPHERE_DB_URL = "jdbc:mysql://localhost:3306/shopsphere?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8"
$env:SHOPSPHERE_DB_USER = "root"
$env:SHOPSPHERE_DB_PASSWORD = "your_mysql_password"
```

---

## 4. Default Demonstration Accounts

All pre-seeded evaluation accounts use standard BCrypt password encryption:
- **Default Password for All Accounts:** `Password@123`
- **Administrator Portal:** `admin@shopsphere.com`
- **Customer Account 1:** `rahul@example.com`
- **Customer Account 2:** `priya@example.com`

---

## 5. Building the Complete Multi-Module Project

Run the Maven build command from the root project directory:

```powershell
# Windows PowerShell (using repository wrapper)
.\mvn.cmd clean package

# Or standard Maven
mvn clean package
```

**Output Artifacts Generated (All Self-Contained):**
- `shopsphere-core/target/shopsphere-core-1.0.0.jar`
- `shopsphere-web/target/shopsphere-web.war`
- `shopsphere-desktop/target/shopsphere-desktop-1.0.0.jar` (Executable Fat JAR)
- `shopsphere-network/target/shopsphere-network-1.0.0.jar` (Self-Contained Fat JAR)
- `shopsphere-rmi/target/shopsphere-rmi-1.0.0.jar` (Self-Contained Fat JAR)

---

## 6. Running Automated Tests

Execute the automated JUnit 5 test suite covering models, validation, and algorithms:

```powershell
.\mvn.cmd test
```

---

## 7. Deploying the Web Application (Apache Tomcat 11)

1. Download and extract **Apache Tomcat 11.0+** (Jakarta EE 10 compliant).
2. Copy the generated `shopsphere-web.war` into Tomcat's `webapps/` directory:
   ```powershell
   Copy-Item ".\shopsphere-web\target\shopsphere-web.war" "C:\apache-tomcat-11.x\webapps\"
   ```
3. Start Apache Tomcat:
   ```powershell
   & "C:\apache-tomcat-11.x\bin\startup.bat"
   ```
4. Access the web application in your browser:
   - **Storefront Homepage:** `http://localhost:8080/shopsphere-web/`
   - **Customer Sign In:** `http://localhost:8080/shopsphere-web/login`
   - **Admin Dashboard:** `http://localhost:8080/shopsphere-web/admin/dashboard`
   - **RTU Syllabus Academic Demos:** `http://localhost:8080/shopsphere-web/academic-demos`

---

## 8. Running the Java Swing Desktop Application

Double-click `run-desktop.bat` or run in terminal:

```powershell
# One-click script
.\run-desktop.bat

# Or direct command (executable fat JAR)
java -jar shopsphere-desktop/target/shopsphere-desktop-1.0.0.jar
```
*Login with Administrator credentials: `admin@shopsphere.com` / `Password@123`.*

---

## 9. Running Java Socket Client-Server Networking

### Start the Inventory Socket Server (Port 9090):
Double-click `run-network-server.bat` or run:
```powershell
.\run-network-server.bat

# Or direct command
java -cp shopsphere-network\target\shopsphere-network-1.0.0.jar com.shopsphere.network.server.InventorySocketServer
```

### In a second terminal, start the Socket Client:
Double-click `run-network-client.bat` or run:
```powershell
.\run-network-client.bat

# Or direct command
java -cp shopsphere-network\target\shopsphere-network-1.0.0.jar com.shopsphere.network.client.InventorySocketClient
```
*Interactive commands: `PING`, `GET_PRODUCT 1`, `GET_STOCK 1`, `SEARCH_PRODUCT Apple`, `UPDATE_STOCK 1 45`, `QUIT`.*

---

## 10. Running Java RMI Remote Inventory Service

### Start the RMI Server (Port 1099):
Double-click `run-rmi-server.bat` or run:
```powershell
.\run-rmi-server.bat

# Or direct command
java -cp shopsphere-rmi\target\shopsphere-rmi-1.0.0.jar com.shopsphere.rmi.server.RmiServer
```

### In a second terminal, run the RMI Client:
Double-click `run-rmi-client.bat` or run:
```powershell
.\run-rmi-client.bat

# Or direct command
java -cp shopsphere-rmi\target\shopsphere-rmi-1.0.0.jar com.shopsphere.rmi.client.RmiClient
```

---

## 11. Running the Object Serialization Demonstration

Verify object stream serialization (`Serializable`, `ObjectOutputStream`, `ObjectInputStream`):

Double-click `run-serialization-demo.bat` or run:
```powershell
.\run-serialization-demo.bat

# Or direct command
java -cp shopsphere-network\target\shopsphere-network-1.0.0.jar com.shopsphere.network.serialization.SerializationDemo
```

---

## 12. Troubleshooting Common Issues

1. **MySQL Access Denied (Error 1045):**
   - Verify username and password in `db.properties` or set environment variable `SHOPSPHERE_DB_PASSWORD`.
2. **Tomcat 11 ClassNotFoundException (`jakarta.servlet.*`):**
   - Ensure you are running **Tomcat 11** (Jakarta EE 10), not Tomcat 9 or older (which used legacy `javax.servlet.*`).
3. **Port 1099 / 9090 already in use:**
   - Stop conflicting background tasks or specify a custom port as an argument: `java -jar ... <port>`.
