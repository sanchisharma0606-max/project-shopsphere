<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sql" uri="jakarta.tags.sql" %>

<c:set var="pageTitle" value="JSTL SQL Demonstration - RTU Syllabus" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<div class="container my-5">
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb mb-4">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/academic-demos">RTU Demos</a></li>
            <li class="breadcrumb-item active" aria-current="page">JSTL SQL Demonstration</li>
        </ol>
    </nav>

    <div class="card border-0 shadow-sm rounded-4 bg-white p-4 p-md-5 mb-4">
        <div class="d-flex align-items-center gap-3 mb-4">
            <div class="bg-warning-subtle text-warning rounded-3 p-3">
                <i class="bi bi-database fs-2"></i>
            </div>
            <div>
                <h3 class="fw-bold text-dark mb-1">JSTL SQL Tag Library &amp; Architectural Analysis</h3>
                <p class="text-muted small mb-0">RTU Syllabus Topic: <code>&lt;sql:query&gt;</code>, <code>&lt;sql:param&gt;</code>, and N-Tier separation</p>
            </div>
        </div>

        <div class="alert alert-warning border-0 shadow-sm mb-4">
            <h6 class="fw-bold"><i class="bi bi-exclamation-triangle-fill me-1"></i> Critical Software Engineering Distinction</h6>
            <p class="small mb-0">
                While the RTU syllabus includes JSTL SQL tags as an educational concept, placing raw SQL queries inside JSP view pages directly violates the <strong>Model-View-Controller (MVC)</strong> and <strong>N-Tier Layered Architecture</strong> paradigms.
                The primary ShopSphere application deliberately routes all persistence through <code>DAO classes &rarr; PreparedStatement &rarr; JDBC &rarr; MySQL</code>.
            </p>
        </div>

        <h5 class="fw-bold text-dark mb-3">Syntax Demonstration</h5>
        <div class="bg-dark text-light p-4 rounded-3 font-monospace small mb-4">
&lt;%-- Example JSTL SQL Query Syntax --%&gt;<br>
&lt;sql:setDataSource var="ds"<br>
&nbsp;&nbsp;&nbsp;&nbsp;driver="com.mysql.cj.jdbc.Driver"<br>
&nbsp;&nbsp;&nbsp;&nbsp;url="jdbc:mysql://localhost:3306/shopsphere"<br>
&nbsp;&nbsp;&nbsp;&nbsp;user="root" password="" /&gt;<br><br>
&lt;sql:query dataSource="\${ds}" var="categoriesList"&gt;<br>
&nbsp;&nbsp;&nbsp;&nbsp;SELECT category_id, name, status FROM categories WHERE status = ?<br>
&nbsp;&nbsp;&nbsp;&nbsp;&lt;sql:param value="ACTIVE" /&gt;<br>
&lt;/sql:query&gt;<br><br>
&lt;c:forEach var="row" items="\${categoriesList.rows}"&gt;<br>
&nbsp;&nbsp;&nbsp;&nbsp;&lt;p&gt;\${row.name} (\${row.status})&lt;/p&gt;<br>
&lt;/c:forEach&gt;
        </div>

        <h5 class="fw-bold text-dark mb-3">Comparison: JSTL SQL vs DAO / JDBC Architecture</h5>
        <div class="table-responsive">
            <table class="table table-bordered small">
                <thead class="table-light">
                    <tr>
                        <th>Criteria</th>
                        <th>JSTL SQL in JSP</th>
                        <th>ShopSphere DAO / Service Layer (Production standard)</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><strong>Separation of Concerns</strong></td>
                        <td class="text-danger">Poor: Mixes presentation with SQL</td>
                        <td class="text-success">Excellent: Strict MVC separation</td>
                    </tr>
                    <tr>
                        <td><strong>Security &amp; Secret Isolation</strong></td>
                        <td class="text-danger">Exposes DB credentials in view files</td>
                        <td class="text-success">Secure: Secrets kept in isolated config</td>
                    </tr>
                    <tr>
                        <td><strong>Transaction Control</strong></td>
                        <td class="text-danger">Very difficult to handle rollback</td>
                        <td class="text-success">Supported: Atomicity via JDBC Connection</td>
                    </tr>
                    <tr>
                        <td><strong>Testability</strong></td>
                        <td class="text-danger">Requires full web container</td>
                        <td class="text-success">Easily unit tested with JUnit &amp; Mocks</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>
