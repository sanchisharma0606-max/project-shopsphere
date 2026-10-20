<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Sign In - ShopSphere" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-md-6 col-lg-5">
            <div class="card border-0 shadow-lg rounded-4 bg-white p-4 p-md-5">
                <div class="text-center mb-4">
                    <div class="mx-auto bg-primary-subtle text-primary rounded-circle d-flex align-items-center justify-content-center mb-3" style="width: 56px; height: 56px;">
                        <i class="bi bi-person-fill fs-2"></i>
                    </div>
                    <h3 class="fw-bold text-dark">Welcome Back</h3>
                    <p class="text-muted small">Sign in to your ShopSphere account</p>
                </div>

                <!-- Evaluator Demo Credentials Quick-Fill Alert -->
                <div class="alert alert-light border small mb-4">
                    <strong class="d-block text-dark mb-1"><i class="bi bi-info-circle me-1 text-primary"></i> Evaluation Accounts:</strong>
                    <div><strong>Admin:</strong> <code>admin@shopsphere.com</code> / <code>Password@123</code></div>
                    <div><strong>Customer:</strong> <code>rahul@example.com</code> / <code>Password@123</code></div>
                </div>

                <form action="${pageContext.request.contextPath}/login" method="POST">
                    <div class="mb-3">
                        <label class="form-label fw-semibold text-dark">Email Address</label>
                        <div class="input-group">
                            <span class="input-group-text bg-light border-end-0"><i class="bi bi-envelope"></i></span>
                            <input type="email" name="email" class="form-control" value="<c:out value='${email}' />" placeholder="name@example.com" required autofocus>
                        </div>
                    </div>

                    <div class="mb-4">
                        <label class="form-label fw-semibold text-dark">Password</label>
                        <div class="input-group">
                            <span class="input-group-text bg-light border-end-0"><i class="bi bi-lock"></i></span>
                            <input type="password" name="password" class="form-control" placeholder="••••••••" required>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-primary-custom w-100 py-2 fw-semibold">
                        Sign In
                    </button>
                </form>

                <div class="text-center mt-4 pt-3 border-top">
                    <p class="text-muted small mb-0">
                        Don't have an account? <a href="${pageContext.request.contextPath}/register" class="fw-bold">Sign Up</a>
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>
