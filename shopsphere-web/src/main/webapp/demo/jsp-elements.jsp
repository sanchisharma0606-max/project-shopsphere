<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%!
    // 1. Traditional JSP Declaration (<%! ... %>)
    // Declares member variables and methods in the generated Servlet class
    private int pageAccessCounter = 0;

    public String formatWelcomeMessage(String name) {
        return "Hello " + name + ", welcome to the RTU Advanced Java Syllabus Demonstration!";
    }
%>

<%
    // 2. Traditional JSP Scriptlet (<% ... %>)
    // Code placed inside the generated _jspService() method
    pageAccessCounter++;
    String candidateName = (session.getAttribute("user") != null)
            ? ((com.shopsphere.model.User) session.getAttribute("user")).getName()
            : "University Student / External Examiner";
    java.util.Date renderedAt = new java.util.Date();
%>

<c:set var="pageTitle" value="JSP Elements Demonstration - RTU Syllabus" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<div class="container my-5">
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb mb-4">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/academic-demos">RTU Demos</a></li>
            <li class="breadcrumb-item active" aria-current="page">JSP Elements</li>
        </ol>
    </nav>

    <div class="card border-0 shadow-sm rounded-4 bg-white p-4 p-md-5 mb-4">
        <div class="d-flex align-items-center gap-3 mb-4">
            <div class="bg-primary-subtle text-primary rounded-3 p-3">
                <i class="bi bi-code-square fs-2"></i>
            </div>
            <div>
                <h3 class="fw-bold text-dark mb-1">Traditional JSP Elements vs Modern EL / JSTL</h3>
                <p class="text-muted small mb-0">RTU Syllabus Topic: Declarations, Scriptlets, Expressions, Comments, and Expression Language</p>
            </div>
        </div>

        <div class="row g-4">
            <!-- Left: Live Execution Output -->
            <div class="col-lg-6">
                <h5 class="fw-bold text-dark mb-3">Live Execution Results</h5>

                <div class="list-group shadow-sm border-0 mb-4">
                    <div class="list-group-item p-3">
                        <small class="text-muted d-block">1. Declaration Method Execution:</small>
                        <strong class="text-primary"><%= formatWelcomeMessage(candidateName) %></strong>
                    </div>

                    <div class="list-group-item p-3">
                        <small class="text-muted d-block">2. Scriptlet Variable &amp; Expression Output (<code>&lt;%= candidateName %&gt;</code>):</small>
                        <strong class="text-dark"><%= candidateName %></strong>
                    </div>

                    <div class="list-group-item p-3">
                        <small class="text-muted d-block">3. Declaration Counter (<code>&lt;%= pageAccessCounter %&gt;</code>):</small>
                        <span class="badge bg-success fs-6"><%= pageAccessCounter %> Hits since server start</span>
                    </div>

                    <div class="list-group-item p-3">
                        <small class="text-muted d-block">4. Scriptlet Timestamp Expression:</small>
                        <span class="text-secondary"><%= renderedAt.toString() %></span>
                    </div>

                    <div class="list-group-item p-3">
                        <small class="text-muted d-block">5. Modern Expression Language (EL) Counterpart (<code>\${sessionScope.user.name}</code>):</small>
                        <strong class="text-indigo">${not empty sessionScope.user.name ? sessionScope.user.name : "Guest Session"}</strong>
                    </div>
                </div>

                <%-- 4. Traditional JSP Server-Side Comment:
                     This comment is processed exclusively by the JSP container
                     and is never transmitted to the browser DOM. --%>
                <div class="alert alert-light border small">
                    <i class="bi bi-shield-check text-success me-1"></i>
                    <strong>JSP Server Comment Verified:</strong>
                    Server-side comments (<code>&lt;%-- ... --%&gt;</code>) are stripped out during servlet translation and never appear in HTML source code!
                </div>
            </div>

            <!-- Right: Code Syntax Comparison Table -->
            <div class="col-lg-6">
                <h5 class="fw-bold text-dark mb-3">Syntax Comparison Table</h5>
                <div class="table-responsive">
                    <table class="table table-bordered small">
                        <thead class="table-light">
                            <tr>
                                <th>JSP Construct</th>
                                <th>Syntax</th>
                                <th>Role in Translation</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td><strong>Declaration</strong></td>
                                <td><code>&lt;%! int x = 0; %&gt;</code></td>
                                <td>Generates class member fields / methods outside <code>_jspService()</code></td>
                            </tr>
                            <tr>
                                <td><strong>Scriptlet</strong></td>
                                <td><code>&lt;% int y = 1; %&gt;</code></td>
                                <td>Inserts raw Java logic directly inside <code>_jspService()</code></td>
                            </tr>
                            <tr>
                                <td><strong>Expression</strong></td>
                                <td><code>&lt;%= y %&gt;</code></td>
                                <td>Evaluates expression and writes to <code>out.print()</code></td>
                            </tr>
                            <tr>
                                <td><strong>EL (Modern)</strong></td>
                                <td><code>\${user.name}</code></td>
                                <td>Resolves scoped attributes cleanly with null-safety</td>
                            </tr>
                            <tr>
                                <td><strong>JSTL (Modern)</strong></td>
                                <td><code>&lt;c:forEach&gt;</code></td>
                                <td>Eliminates spaghetti Java scriptlets in presentation layer</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>
