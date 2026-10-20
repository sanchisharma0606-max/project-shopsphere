<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Create Account - ShopSphere" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-md-7 col-lg-6">
            <div class="card border-0 shadow-lg rounded-4 bg-white p-4 p-md-5">
                <div class="text-center mb-4">
                    <div class="mx-auto bg-primary-subtle text-primary rounded-circle d-flex align-items-center justify-content-center mb-3" style="width: 56px; height: 56px;">
                        <i class="bi bi-person-plus-fill fs-2"></i>
                    </div>
                    <h3 class="fw-bold text-dark">Create an Account</h3>
                    <p class="text-muted small">Join ShopSphere to start shopping today</p>
                </div>

                <form action="${pageContext.request.contextPath}/register" method="POST">
                    <div class="mb-3">
                        <label class="form-label fw-semibold text-dark">Full Name</label>
                        <input type="text" name="name" class="form-control" value="<c:out value='${name}' />" placeholder="e.g. Rahul Sharma" required autofocus>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold text-dark">Email Address</label>
                        <input type="email" name="email" class="form-control" value="<c:out value='${email}' />" placeholder="name@example.com" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold text-dark">Mobile Number (Optional)</label>
                        <input type="tel" name="mobile" class="form-control" value="<c:out value='${mobile}' />" placeholder="10-digit mobile number">
                    </div>

                    <div class="row g-3 mb-4">
                        <div class="col-md-6">
                            <label class="form-label fw-semibold text-dark">Password</label>
                            <input type="password" name="password" class="form-control" placeholder="At least 6 characters" minlength="6" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label fw-semibold text-dark">Confirm Password</label>
                            <input type="password" name="confirmPassword" class="form-control" placeholder="Re-type password" minlength="6" required>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-primary-custom w-100 py-2 fw-semibold">
                        Complete Registration
                    </button>
                </form>

                <div class="text-center mt-4 pt-3 border-top">
                    <p class="text-muted small mb-0">
                        Already have an account? <a href="${pageContext.request.contextPath}/login" class="fw-bold">Sign In</a>
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>
