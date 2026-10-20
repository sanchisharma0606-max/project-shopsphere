<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="JNDI & URLConnection Demonstrations - RTU Syllabus" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<div class="container my-5">
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb mb-4">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/academic-demos">RTU Demos</a></li>
            <li class="breadcrumb-item active" aria-current="page">JNDI &amp; URLConnection</li>
        </ol>
    </nav>

    <!-- Part 1: java.net URLConnection Live Demonstration -->
    <div class="card border-0 shadow-sm rounded-4 bg-white p-4 p-md-5 mb-5">
        <div class="d-flex align-items-center gap-3 mb-4">
            <div class="bg-success-subtle text-success rounded-3 p-3">
                <i class="bi bi-globe fs-2"></i>
            </div>
            <div>
                <h3 class="fw-bold text-dark mb-1">1. java.net URL &amp; URLConnection Live Demonstration</h3>
                <p class="text-muted small mb-0">RTU Syllabus Topic: Connecting to HTTP resources, timeout configurations, and stream buffers</p>
            </div>
        </div>

        <p class="text-muted small">
            Submit a test endpoint URL below. The server will execute <code>new URL(url).openConnection()</code>, configure read/connect timeouts, read the headers, and stream content into a buffer.
        </p>

        <form action="${pageContext.request.contextPath}/demo/url-connection" method="GET" class="mb-4">
            <div class="input-group">
                <input type="url" name="url" class="form-control" placeholder="https://example.com" value="${not empty targetUrl ? targetUrl : ''}">
                <button type="submit" class="btn btn-primary-custom">Execute URLConnection</button>
            </div>
        </form>

        <c:if test="${not empty snippetContent}">
            <div class="card bg-light border-0 rounded-3 p-3">
                <h6 class="fw-bold text-dark mb-2">Live Response Telemetry</h6>
                <div class="small mb-2">
                    <span class="text-muted">Target Endpoint:</span> <code>${targetUrl}</code><br>
                    <span class="text-muted">Content-Type Header:</span> <code>${contentType}</code>
                </div>
                <label class="small text-muted fw-semibold">Buffered Stream Content (First 30 Lines):</label>
                <pre class="bg-dark text-light p-3 rounded-2 small mb-0" style="max-height: 250px; overflow-y: auto;"><code><c:out value="${snippetContent}" /></code></pre>
            </div>
        </c:if>
    </div>

    <!-- Part 2: JNDI (Java Naming and Directory Interface) Demonstration -->
    <div class="card border-0 shadow-sm rounded-4 bg-white p-4 p-md-5">
        <div class="d-flex align-items-center gap-3 mb-4">
            <div class="bg-secondary-subtle text-secondary rounded-3 p-3">
                <i class="bi bi-hdd-network fs-2"></i>
            </div>
            <div>
                <h3 class="fw-bold text-dark mb-1">2. JNDI Resource Naming &amp; DataSource Architecture</h3>
                <p class="text-muted small mb-0">RTU Syllabus Topic: <code>javax.naming.InitialContext</code> and J2EE Enterprise Resource Decoupling</p>
            </div>
        </div>

        <p class="text-muted">
            The Java Naming and Directory Interface (JNDI) enables Java enterprise applications to bind and look up resources (such as database connection pools, mail sessions, or JMS queues) by logical name without hardcoding environment specifics.
        </p>

        <h6 class="fw-bold text-dark mt-4 mb-2">How JNDI DataSource Pooling Operates in Tomcat 11:</h6>
        <div class="bg-dark text-light p-4 rounded-3 font-monospace small mb-4">
// 1. Container Configuration in META-INF/context.xml<br>
&lt;Resource name="jdbc/ShopSphereDS"<br>
&nbsp;&nbsp;&nbsp;&nbsp;auth="Container"<br>
&nbsp;&nbsp;&nbsp;&nbsp;type="javax.sql.DataSource"<br>
&nbsp;&nbsp;&nbsp;&nbsp;maxTotal="20" maxIdle="10"<br>
&nbsp;&nbsp;&nbsp;&nbsp;driverClassName="com.mysql.cj.jdbc.Driver"<br>
&nbsp;&nbsp;&nbsp;&nbsp;url="jdbc:mysql://localhost:3306/shopsphere"<br>
&nbsp;&nbsp;&nbsp;&nbsp;username="root" password="" /&gt;<br><br>
// 2. Application-side JNDI Lookup Code<br>
Context initContext = new InitialContext();<br>
Context envContext  = (Context) initContext.lookup("java:/comp/env");<br>
DataSource ds = (DataSource) envContext.lookup("jdbc/ShopSphereDS");<br>
try (Connection conn = ds.getConnection()) {<br>
&nbsp;&nbsp;&nbsp;&nbsp;// Pooled Connection successfully leased from Tomcat<br>
}
        </div>

        <div class="alert alert-light border small">
            <strong>Key Examiner Viva Point:</strong> Direct JDBC connections (used by ShopSphere's core) create a new TCP socket per request, while JNDI DataSource connection pooling pre-allocates reusable sockets to achieve massive concurrency.
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>
