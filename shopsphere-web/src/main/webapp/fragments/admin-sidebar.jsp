<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="col-lg-2 col-md-3 mb-4">
    <div class="card shadow-sm border-0 rounded-3 sticky-top" style="top: 80px;">
        <div class="card-body p-3">
            <div class="d-flex align-items-center gap-2 mb-3 pb-3 border-bottom">
                <i class="bi bi-shield-lock-fill text-danger fs-4"></i>
                <div>
                    <div class="fw-bold text-dark">Admin Portal</div>
                    <small class="text-muted">ShopSphere</small>
                </div>
            </div>

            <nav class="nav flex-column nav-pills gap-1">
                <a class="nav-link ${activePage == 'dashboard' ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/dashboard">
                    <i class="bi bi-speedometer2 me-2"></i> Dashboard
                </a>
                <a class="nav-link ${activePage == 'products' ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/products">
                    <i class="bi bi-box me-2"></i> Products
                </a>
                <a class="nav-link ${activePage == 'categories' ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/categories">
                    <i class="bi bi-tags me-2"></i> Categories
                </a>
                <a class="nav-link ${activePage == 'orders' ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/orders">
                    <i class="bi bi-receipt me-2"></i> Orders
                </a>
                <a class="nav-link ${activePage == 'users' ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/users">
                    <i class="bi bi-people me-2"></i> Customers
                </a>
                <a class="nav-link ${activePage == 'coupons' ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/coupons">
                    <i class="bi bi-ticket-perforated me-2"></i> Coupons
                </a>
                
                <hr class="my-2">
                <a class="nav-link text-primary" href="${pageContext.request.contextPath}/home" target="_blank">
                    <i class="bi bi-arrow-up-right-square me-2"></i> View Storefront
                </a>
            </nav>
        </div>
    </div>
</div>
