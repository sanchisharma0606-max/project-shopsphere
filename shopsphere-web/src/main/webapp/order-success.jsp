<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Order Confirmation - ShopSphere" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<div class="container my-5 text-center">
    <div class="card border-0 shadow-sm rounded-4 bg-white p-5 mx-auto" style="max-width: 650px;">
        <div class="mx-auto bg-success-subtle text-success rounded-circle d-flex align-items-center justify-content-center mb-4" style="width: 80px; height: 80px;">
            <i class="bi bi-check2-circle display-4"></i>
        </div>

        <h2 class="fw-bold text-dark mb-2">Order Confirmed!</h2>
        <p class="text-muted mb-4">
            Thank you for shopping with us. Your order has been placed successfully and is being prepared for shipment.
        </p>

        <div class="bg-light p-4 rounded-3 text-start mb-4">
            <div class="row g-2 small">
                <div class="col-6 text-muted">Order ID:</div>
                <div class="col-6 text-end fw-bold text-dark">#${order.orderId}</div>

                <div class="col-6 text-muted">Order Date:</div>
                <div class="col-6 text-end"><c:out value="${order.createdAt}" /></div>

                <div class="col-6 text-muted">Payment Method:</div>
                <div class="col-6 text-end fw-semibold">${order.paymentMethod}</div>

                <div class="col-6 text-muted">Payment Status:</div>
                <div class="col-6 text-end">
                    <span class="badge ${order.paymentStatus == 'PAID' ? 'bg-success' : 'bg-warning text-dark'}">
                        ${order.paymentStatus}
                    </span>
                </div>

                <div class="col-6 text-muted">Total Amount:</div>
                <div class="col-6 text-end fw-bold text-primary fs-6">
                    ₹<fmt:formatNumber value="${order.netPayable}" pattern="#,##0.00"/>
                </div>
            </div>
        </div>

        <div class="d-flex justify-content-center gap-3">
            <a href="${pageContext.request.contextPath}/order-details?id=${order.orderId}" class="btn btn-primary-custom px-4">
                <i class="bi bi-geo-alt me-1"></i> Track Order
            </a>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-custom px-4">
                Continue Shopping
            </a>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>
