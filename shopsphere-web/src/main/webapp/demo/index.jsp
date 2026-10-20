<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="RTU Syllabus Academic Demos - ShopSphere" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<div class="container my-5">
    <div class="text-center mb-5">
        <span class="badge bg-primary-subtle text-primary px-3 py-2 rounded-pill fw-bold mb-2">
            RTU Advanced Java / Java Enterprise Syllabus Reference
        </span>
        <h2 class="display-6 fw-bold text-dark">Academic Syllabus Demonstration Suite</h2>
        <p class="text-muted mx-auto" style="max-width: 700px;">
            This module provides dedicated, clearly labeled educational demonstrations for the theoretical and practical syllabus topics specified by Rajasthan Technical University (RTU).
        </p>
    </div>

    <div class="row g-4">
        <!-- Card 1: Traditional JSP vs Modern EL -->
        <div class="col-md-6 col-lg-4">
            <div class="card h-100 border-0 shadow-sm rounded-4 bg-white p-4">
                <div class="bg-primary-subtle text-primary rounded-3 p-3 mb-3 d-inline-block" style="width: 50px; height: 50px;">
                    <i class="bi bi-code-slash fs-4"></i>
                </div>
                <h5 class="fw-bold text-dark">1. Traditional JSP Elements</h5>
                <p class="text-muted small">
                    Explore declarations (<code>&lt;%! %&gt;</code>), scriptlets (<code>&lt;% %&gt;</code>), expressions (<code>&lt;%= %&gt;</code>), and comments contrasted against modern Expression Language (EL).
                </p>
                <div class="mt-auto">
                    <a href="${pageContext.request.contextPath}/demo/scriptlets" class="btn btn-outline-custom btn-sm w-100">
                        Launch JSP Demo &rarr;
                    </a>
                </div>
            </div>
        </div>

        <!-- Card 2: JSTL XML Processing -->
        <div class="col-md-6 col-lg-4">
            <div class="card h-100 border-0 shadow-sm rounded-4 bg-white p-4">
                <div class="bg-info-subtle text-info rounded-3 p-3 mb-3 d-inline-block" style="width: 50px; height: 50px;">
                    <i class="bi bi-filetype-xml fs-4"></i>
                </div>
                <h5 class="fw-bold text-dark">2. JSTL XML Tag Library</h5>
                <p class="text-muted small">
                    Demonstration of <code>&lt;x:parse&gt;</code>, <code>&lt;x:forEach&gt;</code>, and XPath expressions parsing a structured XML e-commerce catalog feed.
                </p>
                <div class="mt-auto">
                    <a href="${pageContext.request.contextPath}/demo/jstl-xml" class="btn btn-outline-custom btn-sm w-100">
                        Launch XML Demo &rarr;
                    </a>
                </div>
            </div>
        </div>

        <!-- Card 3: JSTL SQL Tags -->
        <div class="col-md-6 col-lg-4">
            <div class="card h-100 border-0 shadow-sm rounded-4 bg-white p-4">
                <div class="bg-warning-subtle text-warning rounded-3 p-3 mb-3 d-inline-block" style="width: 50px; height: 50px;">
                    <i class="bi bi-database fs-4"></i>
                </div>
                <h5 class="fw-bold text-dark">3. JSTL SQL Library</h5>
                <p class="text-muted small">
                    Educational demonstration of <code>&lt;sql:query&gt;</code> and <code>&lt;sql:param&gt;</code> alongside an architectural discussion on why the DAO pattern is superior for enterprise systems.
                </p>
                <div class="mt-auto">
                    <a href="${pageContext.request.contextPath}/demo/jstl-sql" class="btn btn-outline-custom btn-sm w-100">
                        Launch SQL Demo &rarr;
                    </a>
                </div>
            </div>
        </div>

        <!-- Card 4: URL & URLConnection -->
        <div class="col-md-6 col-lg-4">
            <div class="card h-100 border-0 shadow-sm rounded-4 bg-white p-4">
                <div class="bg-success-subtle text-success rounded-3 p-3 mb-3 d-inline-block" style="width: 50px; height: 50px;">
                    <i class="bi bi-globe fs-4"></i>
                </div>
                <h5 class="fw-bold text-dark">4. java.net URLConnection</h5>
                <p class="text-muted small">
                    Direct HTTP resource retrieval using <code>java.net.URL</code>, <code>URLConnection</code>, connection timeouts, and stream reading buffers.
                </p>
                <div class="mt-auto">
                    <a href="${pageContext.request.contextPath}/demo/url-connection" class="btn btn-outline-custom btn-sm w-100">
                        Launch URL Demo &rarr;
                    </a>
                </div>
            </div>
        </div>

        <!-- Card 5: JNDI Architecture -->
        <div class="col-md-6 col-lg-4">
            <div class="card h-100 border-0 shadow-sm rounded-4 bg-white p-4">
                <div class="bg-secondary-subtle text-secondary rounded-3 p-3 mb-3 d-inline-block" style="width: 50px; height: 50px;">
                    <i class="bi bi-hdd-network fs-4"></i>
                </div>
                <h5 class="fw-bold text-dark">5. JNDI Resource Naming</h5>
                <p class="text-muted small">
                    Java Naming and Directory Interface (JNDI) <code>InitialContext</code> lookups, Tomcat <code>context.xml</code> connection pooling, and enterprise decoupling.
                </p>
                <div class="mt-auto">
                    <a href="${pageContext.request.contextPath}/demo/jndi" class="btn btn-outline-custom btn-sm w-100">
                        Launch JNDI Demo &rarr;
                    </a>
                </div>
            </div>
        </div>

        <!-- Card 6: Historical Applets -->
        <div class="col-md-6 col-lg-4">
            <div class="card h-100 border-0 shadow-sm rounded-4 bg-white p-4">
                <div class="bg-danger-subtle text-danger rounded-3 p-3 mb-3 d-inline-block" style="width: 50px; height: 50px;">
                    <i class="bi bi-clock-history fs-4"></i>
                </div>
                <h5 class="fw-bold text-dark">6. Historical Java Applets</h5>
                <p class="text-muted small">
                    Applet lifecycle methods (<code>init</code>, <code>start</code>, <code>stop</code>, <code>destroy</code>), sandbox security model, and historical evolution toward Servlets/JSP.
                </p>
                <div class="mt-auto">
                    <a href="${pageContext.request.contextPath}/demo/applets-history" class="btn btn-outline-custom btn-sm w-100">
                        View Applet History &rarr;
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>
