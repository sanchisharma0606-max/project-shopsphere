<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Secure Checkout - ShopSphere" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<div class="container my-4">
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb mb-4">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">Home</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/cart">Cart</a></li>
            <li class="breadcrumb-item active" aria-current="page">Checkout</li>
        </ol>
    </nav>

    <!-- Educational Banner -->
    <div class="alert alert-info border-0 shadow-sm rounded-3 d-flex align-items-center gap-3 mb-4">
        <i class="bi bi-info-circle-fill fs-3 text-info"></i>
        <div>
            <strong>Educational Demonstration Notice:</strong>
            <p class="mb-0 small">This application simulates transactional e-commerce checkout. No real financial credentials or payment cards are used or collected.</p>
        </div>
    </div>

    <form action="${pageContext.request.contextPath}/checkout" method="POST">
        <div class="row g-4">
            <!-- Left: Address & Payment Selection -->
            <div class="col-lg-8">
                <!-- Section 1: Delivery Address -->
                <div class="card border-0 shadow-sm rounded-4 bg-white p-4 mb-4">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h5 class="fw-bold text-dark mb-0"><i class="bi bi-geo-alt-fill text-primary me-2"></i>1. Shipping Address</h5>
                        <button class="btn btn-outline-custom btn-sm" type="button" data-bs-toggle="collapse" data-bs-target="#newAddressForm" aria-expanded="false">
                            + Add New Address
                        </button>
                    </div>

                    <!-- Collapsible Add Address Form -->
                    <div class="collapse mb-4" id="newAddressForm">
                        <div class="card card-body bg-light border-0 rounded-3">
                            <h6 class="fw-bold mb-3">Add a New Delivery Address</h6>
                            <div class="row g-3">
                                <div class="col-12">
                                    <label class="form-label small fw-semibold">Street Address / Flat / Building</label>
                                    <input type="text" id="newAddressLine" class="form-control form-control-sm" placeholder="e.g. Flat 101, Palm Grove">
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label small fw-semibold">City</label>
                                    <input type="text" id="newCity" class="form-control form-control-sm" placeholder="e.g. Jaipur">
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label small fw-semibold">State</label>
                                    <input type="text" id="newState" class="form-control form-control-sm" placeholder="e.g. Rajasthan">
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label small fw-semibold">Pincode</label>
                                    <input type="text" id="newPincode" class="form-control form-control-sm" placeholder="e.g. 302001">
                                </div>
                            </div>
                            <small class="text-muted mt-2 d-block">* You can also manage addresses permanently from your Profile page.</small>
                        </div>
                    </div>

                    <!-- Saved Addresses List -->
                    <c:choose>
                        <c:when test="${not empty addresses}">
                            <div class="d-flex flex-column gap-3">
                                <c:forEach var="addr" items="${addresses}" varStatus="status">
                                    <label class="card p-3 border rounded-3 cursor-pointer d-flex flex-row align-items-start gap-3">
                                        <input class="form-check-input mt-1" type="radio" name="addressId" value="${addr.addressId}" ${status.first ? 'checked' : ''} required>
                                        <div class="flex-grow-1">
                                            <div class="d-flex align-items-center gap-2">
                                                <span class="badge bg-secondary-subtle text-secondary small">${addr.addressType}</span>
                                            </div>
                                            <div class="fw-semibold text-dark mt-1"><c:out value="${addr.addressLine}" /></div>
                                            <div class="text-muted small"><c:out value="${addr.city}" />, <c:out value="${addr.state}" /> - <c:out value="${addr.pincode}" /></div>
                                        </div>
                                    </label>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-warning">
                                No saved addresses found. Please add an address above or visit your <a href="${pageContext.request.contextPath}/profile">Profile</a> to add one.
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Section 2: Payment Method -->
                <div class="card border-0 shadow-sm rounded-4 bg-white p-4">
                    <h5 class="fw-bold text-dark mb-3"><i class="bi bi-credit-card-fill text-primary me-2"></i>2. Payment Method</h5>
                    
                    <div class="d-flex flex-column gap-3">
                        <label class="card p-3 border rounded-3 cursor-pointer d-flex flex-row align-items-center gap-3">
                            <input class="form-check-input" type="radio" name="paymentMethod" value="COD" checked>
                            <i class="bi bi-cash-stack fs-3 text-success"></i>
                            <div>
                                <div class="fw-semibold text-dark">Cash on Delivery (COD)</div>
                                <div class="text-muted small">Pay securely in cash or UPI when your shipment arrives</div>
                            </div>
                        </label>

                        <label class="card p-3 border rounded-3 cursor-pointer d-flex flex-row align-items-center gap-3">
                            <input class="form-check-input" type="radio" name="paymentMethod" value="UPI">
                            <i class="bi bi-qr-code-scan fs-3 text-primary"></i>
                            <div>
                                <div class="fw-semibold text-dark">Instant UPI Payment (Mock)</div>
                                <div class="text-muted small">Google Pay, PhonePe, Paytm simulation</div>
                            </div>
                        </label>

                        <label class="card p-3 border rounded-3 cursor-pointer d-flex flex-row align-items-center gap-3">
                            <input class="form-check-input" type="radio" name="paymentMethod" value="CREDIT_CARD">
                            <i class="bi bi-credit-card fs-3 text-info"></i>
                            <div>
                                <div class="fw-semibold text-dark">Credit / Debit Card (Mock)</div>
                                <div class="text-muted small">Visa, Mastercard, RuPay simulation</div>
                            </div>
                        </label>
                    </div>
                </div>
            </div>

            <!-- Right: Order Review & Submit -->
            <div class="col-lg-4">
                <div class="card border-0 shadow-sm rounded-4 bg-white p-4 sticky-top" style="top: 80px;">
                    <h5 class="fw-bold text-dark mb-3">Order Items (${cartItems.size()})</h5>
                    
                    <div class="mb-3" style="max-height: 240px; overflow-y: auto;">
                        <c:forEach var="item" items="${cartItems}">
                            <div class="d-flex justify-content-between align-items-center py-2 border-bottom">
                                <div class="d-flex align-items-center gap-2">
                                    <span class="badge bg-light text-dark border">${item.quantity}x</span>
                                    <div class="text-truncate" style="max-width: 170px;" title="<c:out value='${item.product.name}' />">
                                        <c:out value="${item.product.name}" />
                                    </div>
                                </div>
                                <span class="fw-semibold small">₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></span>
                            </div>
                        </c:forEach>
                    </div>

                    <div class="d-flex justify-content-between mb-2">
                        <span class="text-muted">Subtotal</span>
                        <span>₹<fmt:formatNumber value="${subtotal}" pattern="#,##0.00"/></span>
                    </div>

                    <c:if test="${discount > 0}">
                        <div class="d-flex justify-content-between mb-2 text-success">
                            <span>Coupon (${coupon.code})</span>
                            <span>-₹<fmt:formatNumber value="${discount}" pattern="#,##0.00"/></span>
                        </div>
                    </c:if>

                    <div class="d-flex justify-content-between mb-3">
                        <span class="text-muted">Delivery</span>
                        <span class="text-success fw-bold">FREE</span>
                    </div>

                    <hr class="my-3">

                    <div class="d-flex justify-content-between mb-4">
                        <span class="fs-5 fw-bold text-dark">Total Amount</span>
                        <span class="fs-5 fw-bold text-primary">₹<fmt:formatNumber value="${grandTotal}" pattern="#,##0.00"/></span>
                    </div>

                    <button type="submit" class="btn btn-primary-custom w-100 py-3 fw-bold d-flex align-items-center justify-content-center gap-2" ${empty addresses ? 'disabled' : ''}>
                        <i class="bi bi-lock-fill"></i> Place Order Now
                    </button>
                    
                    <small class="text-muted text-center d-block mt-3">
                        <i class="bi bi-shield-check text-success"></i> Transaction protected by database atomicity
                    </small>
                </div>
            </div>
        </div>
    </form>
</div>

<%@ include file="/fragments/footer.jsp" %>
