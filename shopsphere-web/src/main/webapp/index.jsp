<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:set var="pageTitle" value="ShopSphere - Next-Gen E-Commerce" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<div class="container my-4">
    <!-- Hero Banner -->
    <div class="hero-banner">
        <div class="row align-items-center">
            <div class="col-lg-7">
                <span class="badge bg-warning text-dark px-3 py-2 rounded-pill fw-bold mb-3">
                    <i class="bi bi-stars me-1"></i> Spring Mega Sale Live
                </span>
                <h1 class="hero-title">Experience Smarter E-Commerce with ShopSphere</h1>
                <p class="hero-subtitle">
                    Explore high-grade electronics, designer fashion, and everyday essentials engineered on an Advanced Java &amp; Jakarta EE 10 stack.
                </p>
                <div class="d-flex flex-wrap gap-3">
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-warning text-dark fw-bold px-4 py-2 rounded-pill shadow-sm">
                        <i class="bi bi-bag me-1"></i> Shop Now
                    </a>
                    <a href="${pageContext.request.contextPath}/academic-demos" class="btn btn-outline-light px-4 py-2 rounded-pill">
                        <i class="bi bi-mortarboard me-1"></i> View RTU Syllabus Demos
                    </a>
                </div>
            </div>
        </div>
    </div>

    <!-- Category Highlights Section -->
    <div class="mb-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h2 class="fs-4 mb-1">Browse Popular Categories</h2>
                <p class="text-muted small mb-0">Handpicked selections across our digital aisles</p>
            </div>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-sm btn-outline-custom">View All <i class="bi bi-arrow-right"></i></a>
        </div>

        <div class="row g-3">
            <c:forEach var="cat" items="${categories}">
                <div class="col-6 col-md-4 col-lg">
                    <a href="${pageContext.request.contextPath}/products?category=${cat.categoryId}" class="text-decoration-none">
                        <div class="card h-100 text-center p-3 border-0 shadow-sm rounded-3 bg-white hover-shadow transition">
                            <div class="mx-auto bg-light rounded-circle p-3 mb-2 text-primary fs-3" style="width: 64px; height: 64px; display: flex; align-items: center; justify-content: center;">
                                <c:choose>
                                    <c:when test="${cat.categoryId == 1}"><i class="bi bi-laptop"></i></c:when>
                                    <c:when test="${cat.categoryId == 2}"><i class="bi bi-handbag"></i></c:when>
                                    <c:when test="${cat.categoryId == 3}"><i class="bi bi-house-door"></i></c:when>
                                    <c:when test="${cat.categoryId == 4}"><i class="bi bi-book"></i></c:when>
                                    <c:otherwise><i class="bi bi-heart-pulse"></i></c:otherwise>
                                </c:choose>
                            </div>
                            <h6 class="text-dark fw-semibold mb-1"><c:out value="${cat.name}" /></h6>
                            <small class="text-muted"><c:out value="${cat.description}" /></small>
                        </div>
                    </a>
                </div>
            </c:forEach>
        </div>
    </div>

    <!-- Featured Products Grid -->
    <div class="mb-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h2 class="fs-4 mb-1">Featured Deals &amp; Top Picks</h2>
                <p class="text-muted small mb-0">Hand-selected best sellers backed by real MySQL persistence</p>
            </div>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-sm btn-outline-custom">Explore Full Catalogue <i class="bi bi-arrow-right"></i></a>
        </div>

        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-4">
            <c:forEach var="product" items="${featuredProducts}">
                <div class="col">
                    <tags:productCard product="${product}" />
                </div>
            </c:forEach>
        </div>
    </div>

    <!-- Special Value Propositions -->
    <div class="row g-4 my-4 py-4 bg-white rounded-4 shadow-sm border">
        <div class="col-md-3 text-center">
            <i class="bi bi-truck text-primary fs-2 mb-2 d-block"></i>
            <h6 class="fw-bold text-dark">Fast Delivery</h6>
            <small class="text-muted">Standard 2-day priority dispatch across regions</small>
        </div>
        <div class="col-md-3 text-center">
            <i class="bi bi-shield-check text-success fs-2 mb-2 d-block"></i>
            <h6 class="fw-bold text-dark">Secure Mock Payments</h6>
            <small class="text-muted">Transaction-safe checkout simulation with rollback</small>
        </div>
        <div class="col-md-3 text-center">
            <i class="bi bi-arrow-repeat text-info fs-2 mb-2 d-block"></i>
            <h6 class="fw-bold text-dark">Easy Returns</h6>
            <small class="text-muted">Instant order cancellations with inventory restoration</small>
        </div>
        <div class="col-md-3 text-center">
            <i class="bi bi-headset text-warning fs-2 mb-2 d-block"></i>
            <h6 class="fw-bold text-dark">Dedicated Support</h6>
            <small class="text-muted">Comprehensive viva documentation &amp; test suite</small>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>
