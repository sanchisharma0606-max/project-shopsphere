<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="${not empty coupon ? 'Edit Coupon' : 'Add Coupon'} - ShopSphere Admin" />
<c:set var="activePage" value="coupons" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<div class="container-fluid py-4 px-lg-5">
    <div class="row">
        <%@ include file="/fragments/admin-sidebar.jsp" %>

        <div class="col-lg-10 col-md-9">
            <div class="card border-0 shadow-sm rounded-4 bg-white p-4 mx-auto" style="max-width: 650px;">
                <h4 class="fw-bold text-dark mb-4">${not empty coupon ? 'Edit Coupon' : 'Create New Coupon'}</h4>

                <form action="${pageContext.request.contextPath}/admin/coupons" method="POST">
                    <input type="hidden" name="couponId" value="${coupon.couponId != null ? coupon.couponId : 0}">

                    <div class="row g-3">
                        <div class="col-md-6">
                            <label class="form-label fw-semibold">Coupon Code</label>
                            <input type="text" name="code" class="form-control text-uppercase" value="<c:out value='${coupon.code}' />" required placeholder="e.g. FESTIVE25">
                        </div>

                        <div class="col-md-6">
                            <label class="form-label fw-semibold">Discount Type</label>
                            <select name="discountType" class="form-select">
                                <option value="PERCENTAGE" ${coupon.discountType == 'PERCENTAGE' ? 'selected' : ''}>PERCENTAGE (%)</option>
                                <option value="FIXED" ${coupon.discountType == 'FIXED' ? 'selected' : ''}>FIXED AMOUNT (₹)</option>
                            </select>
                        </div>

                        <div class="col-md-4">
                            <label class="form-label fw-semibold">Discount Value</label>
                            <input type="number" step="0.01" name="discountValue" class="form-control" value="${coupon.discountValue}" required min="0.01">
                        </div>

                        <div class="col-md-4">
                            <label class="form-label fw-semibold">Minimum Order (₹)</label>
                            <input type="number" step="0.01" name="minimumOrder" class="form-control" value="${coupon.minimumOrder != null ? coupon.minimumOrder : 0}" min="0">
                        </div>

                        <div class="col-md-4">
                            <label class="form-label fw-semibold">Max Cap (₹)</label>
                            <input type="number" step="0.01" name="maximumDiscount" class="form-control" value="${coupon.maximumDiscount}" placeholder="Optional">
                        </div>

                        <div class="col-md-6">
                            <label class="form-label fw-semibold">Expiry Date</label>
                            <input type="date" name="expiryDate" class="form-control" value="${coupon.expiryDate}" required>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label fw-semibold">Status</label>
                            <select name="status" class="form-select">
                                <option value="ACTIVE" ${coupon.status == 'ACTIVE' ? 'selected' : ''}>ACTIVE</option>
                                <option value="INACTIVE" ${coupon.status == 'INACTIVE' ? 'selected' : ''}>INACTIVE</option>
                            </select>
                        </div>
                    </div>

                    <div class="d-flex justify-content-end gap-2 mt-4 pt-3 border-top">
                        <a href="${pageContext.request.contextPath}/admin/coupons" class="btn btn-outline-custom">Cancel</a>
                        <button type="submit" class="btn btn-primary-custom px-4">Save Coupon</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>
