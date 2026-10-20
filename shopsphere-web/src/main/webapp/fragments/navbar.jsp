<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<nav class="navbar navbar-expand-lg navbar-custom">
    <div class="container">
        <!-- Brand Logo -->
        <a class="navbar-brand" href="${pageContext.request.contextPath}/home">
            <i class="bi bi-bag-check-fill text-primary"></i>
            <span>ShopSphere</span>
            <span class="brand-badge">Pro</span>
        </a>

        <!-- Mobile Toggle Button -->
        <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#navbarMain" aria-controls="navbarMain" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarMain">
            <!-- Search Bar Form -->
            <form class="d-flex mx-lg-4 my-2 my-lg-0 flex-grow-1" role="search" action="${pageContext.request.contextPath}/products" method="GET">
                <div class="input-group">
                    <input class="form-control border-end-0 bg-light" type="search" name="q" placeholder="Search laptops, gadgets, apparel..." value="<c:out value='${param.q}' />" aria-label="Search">
                    <button class="btn btn-light border-start-0 border text-muted" type="submit">
                        <i class="bi bi-search"></i>
                    </button>
                </div>
            </form>

            <!-- Navigation Links -->
            <ul class="navbar-nav ms-auto align-items-center gap-1">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/home">
                        <i class="bi bi-house me-1"></i> Home
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/products">
                        <i class="bi bi-grid me-1"></i> Products
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/academic-demos">
                        <i class="bi bi-mortarboard-fill text-warning me-1"></i> RTU Demos
                    </a>
                </li>

                <!-- Language Selector -->
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="bi bi-translate me-1"></i>
                        <c:choose>
                            <c:when test="${sessionScope.langCode == 'hi'}">हिन्दी</c:when>
                            <c:otherwise>EN</c:otherwise>
                        </c:choose>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0">
                        <li><a class="dropdown-item ${sessionScope.langCode != 'hi' ? 'active' : ''}" href="${pageContext.request.contextPath}/language?lang=en">English</a></li>
                        <li><a class="dropdown-item ${sessionScope.langCode == 'hi' ? 'active' : ''}" href="${pageContext.request.contextPath}/language?lang=hi">हिन्दी (Hindi)</a></li>
                    </ul>
                </li>

                <!-- Wishlist (Logged in) -->
                <c:if test="${not empty sessionScope.user}">
                    <li class="nav-item">
                        <a class="nav-link position-relative" href="${pageContext.request.contextPath}/wishlist" title="My Wishlist">
                            <i class="bi bi-heart fs-5"></i>
                        </a>
                    </li>
                </c:if>

                <!-- Shopping Cart Icon with Badge -->
                <li class="nav-item">
                    <a class="nav-link position-relative" href="${pageContext.request.contextPath}/cart" title="Shopping Cart">
                        <i class="bi bi-cart3 fs-5"></i>
                        <c:if test="${sessionScope.cartCount > 0}">
                            <span class="badge bg-danger badge-counter">${sessionScope.cartCount}</span>
                        </c:if>
                    </a>
                </li>

                <!-- User Account / Login State -->
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <li class="nav-item dropdown ms-2">
                            <a class="nav-link dropdown-toggle d-flex align-items-center gap-2" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                <div class="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center" style="width: 32px; height: 32px; font-size: 0.85rem; font-weight: 700;">
                                    ${fn:toUpperCase(fn:substring(sessionScope.user.name, 0, 1))}
                                </div>
                                <span class="d-none d-lg-inline fw-semibold"><c:out value="${sessionScope.user.name}" /></span>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0">
                                <li class="dropdown-header">
                                    <small class="text-muted"><c:out value="${sessionScope.user.email}" /></small>
                                    <c:if test="${sessionScope.user.admin}">
                                        <div class="badge bg-danger mt-1">ADMINISTRATOR</div>
                                    </c:if>
                                </li>
                                <li><hr class="dropdown-divider"></li>
                                <c:if test="${sessionScope.user.admin}">
                                    <li>
                                        <a class="dropdown-item text-danger fw-semibold" href="${pageContext.request.contextPath}/admin/dashboard">
                                            <i class="bi bi-speedometer2 me-2"></i> Admin Dashboard
                                        </a>
                                    </li>
                                    <li><hr class="dropdown-divider"></li>
                                </c:if>
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/profile">
                                        <i class="bi bi-person me-2"></i> My Profile
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/orders">
                                        <i class="bi bi-box-seam me-2"></i> Order History
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/wishlist">
                                        <i class="bi bi-heart me-2"></i> Wishlist
                                    </a>
                                </li>
                                <li><hr class="dropdown-divider"></li>
                                <li>
                                    <a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">
                                        <i class="bi bi-box-arrow-right me-2"></i> Sign Out
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item ms-2">
                            <a class="btn btn-outline-custom btn-sm px-3" href="${pageContext.request.contextPath}/login">Sign In</a>
                        </li>
                        <li class="nav-item">
                            <a class="btn btn-primary-custom btn-sm px-3" href="${pageContext.request.contextPath}/register">Sign Up</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>

<!-- Global Flash Alert Messages -->
<%@ include file="/fragments/alerts.jsp" %>
