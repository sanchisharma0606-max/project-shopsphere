<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Historical Java Applets - RTU Syllabus" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<div class="container my-5">
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb mb-4">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/academic-demos">RTU Demos</a></li>
            <li class="breadcrumb-item active" aria-current="page">Applets History</li>
        </ol>
    </nav>

    <div class="card border-0 shadow-sm rounded-4 bg-white p-4 p-md-5">
        <div class="d-flex align-items-center gap-3 mb-4">
            <div class="bg-danger-subtle text-danger rounded-3 p-3">
                <i class="bi bi-clock-history fs-2"></i>
            </div>
            <div>
                <h3 class="fw-bold text-dark mb-1">Historical Evolution: Java Applets &amp; Client-Side Runtime</h3>
                <p class="text-muted small mb-0">RTU Syllabus Topic: Applet architecture, lifecycle states, sandbox restrictions, and modern web paradigms</p>
            </div>
        </div>

        <div class="alert alert-light border small mb-4">
            <strong>Curriculum Context:</strong> Traditional Java Applets were small Java programs downloaded from an HTTP web server and executed inside the client browser’s JVM via NPAPI browser plugins. Because modern web browsers and Java (deprecated in Java 9, removed in Java 11+) no longer execute NPAPI browser applets, this project documents the architectural foundations and explains how Servlets/JSP replaced them.
        </div>

        <h5 class="fw-bold text-dark mb-3">1. The Traditional Applet Lifecycle</h5>
        <p class="text-muted small">
            Every Applet inherited from <code>java.applet.Applet</code> or <code>javax.swing.JApplet</code>, and executed through five lifecycle callbacks:
        </p>

        <div class="row g-3 mb-4">
            <div class="col-md">
                <div class="card h-100 p-3 bg-light border-0 rounded-3 text-center">
                    <span class="badge bg-primary mb-2">Stage 1</span>
                    <h6 class="fw-bold text-dark mb-1"><code>init()</code></h6>
                    <small class="text-muted">Invoked once when the browser loads the applet to initialize GUI controls and resources.</small>
                </div>
            </div>
            <div class="col-md">
                <div class="card h-100 p-3 bg-light border-0 rounded-3 text-center">
                    <span class="badge bg-success mb-2">Stage 2</span>
                    <h6 class="fw-bold text-dark mb-1"><code>start()</code></h6>
                    <small class="text-muted">Called each time the browser window becomes active or after <code>init()</code>.</small>
                </div>
            </div>
            <div class="col-md">
                <div class="card h-100 p-3 bg-light border-0 rounded-3 text-center">
                    <span class="badge bg-info mb-2">Stage 3</span>
                    <h6 class="fw-bold text-dark mb-1"><code>paint()</code></h6>
                    <small class="text-muted">Executes when the applet canvas needs redrawing with <code>Graphics g</code>.</small>
                </div>
            </div>
            <div class="col-md">
                <div class="card h-100 p-3 bg-light border-0 rounded-3 text-center">
                    <span class="badge bg-warning text-dark mb-2">Stage 4</span>
                    <h6 class="fw-bold text-dark mb-1"><code>stop()</code></h6>
                    <small class="text-muted">Suspends background threads when the user navigates away from the web page.</small>
                </div>
            </div>
            <div class="col-md">
                <div class="card h-100 p-3 bg-light border-0 rounded-3 text-center">
                    <span class="badge bg-danger mb-2">Stage 5</span>
                    <h6 class="fw-bold text-dark mb-1"><code>destroy()</code></h6>
                    <small class="text-muted">Releases system resources completely prior to unloading from browser memory.</small>
                </div>
            </div>
        </div>

        <h5 class="fw-bold text-dark mb-3">2. Architectural Paradigm Shift: Applets vs Servlets</h5>
        <div class="table-responsive mb-4">
            <table class="table table-bordered small">
                <thead class="table-light">
                    <tr>
                        <th>Architectural Attribute</th>
                        <th>Java Applets (Client-Side)</th>
                        <th>Jakarta Servlets / JSP (Server-Side)</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><strong>Execution Location</strong></td>
                        <td>Client browser's local JVM</td>
                        <td>Server-side web container (Apache Tomcat)</td>
                    </tr>
                    <tr>
                        <td><strong>Client Requirements</strong></td>
                        <td>Heavy JRE plugin installed on every client machine</td>
                        <td>Zero plugins; any modern web browser via HTML5/CSS</td>
                    </tr>
                    <tr>
                        <td><strong>Database Security</strong></td>
                        <td>High risk: direct JDBC exposed client credentials</td>
                        <td>Secure: DB connections isolated in private server tier</td>
                    </tr>
                    <tr>
                        <td><strong>Security Model</strong></td>
                        <td>Restricted in a local browser sandbox</td>
                        <td>Full server OS capabilities, firewalls, and filters</td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div class="d-flex justify-content-end">
            <a href="${pageContext.request.contextPath}/academic-demos" class="btn btn-outline-custom btn-sm">
                &larr; Back to RTU Syllabus Suite
            </a>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>
