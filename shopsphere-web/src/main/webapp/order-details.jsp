<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Order #${order.orderId} Details - ShopSphere" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<div class="container my-4">
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb mb-4">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">Home</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/orders">Orders</a></li>
            <li class="breadcrumb-item active" aria-current="page">Order #${order.orderId}</li>
        </ol>
    </nav>

    <!-- Header bar -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 class="fw-bold mb-1">Order #${order.orderId}</h2>
            <span class="text-muted small">Placed on <c:out value="${order.createdAt}" /></span>
        </div>
        <div>
            <span class="badge ${order.orderStatus == 'DELIVERED' ? 'bg-success' : (order.orderStatus == 'CANCELLED' ? 'bg-danger' : 'bg-primary')} fs-6 px-3 py-2 rounded-pill">
                Status: ${order.orderStatus}
            </span>
        </div>
    </div>

    <!-- Visual Order Tracking Timeline (if not cancelled) -->
    <c:choose>
        <c:when test="${order.orderStatus != 'CANCELLED'}">
            <div class="card border-0 shadow-sm rounded-4 bg-white p-4 mb-4">
                <h6 class="fw-bold text-dark mb-4">Live Tracking Timeline</h6>
                <div class="order-timeline">
                    <!-- Step 1: Placed -->
                    <div class="timeline-step completed">
                        <div class="step-icon"><i class="bi bi-check-lg"></i></div>
                        <div class="step-title">Placed</div>
                    </div>

                    <!-- Step 2: Confirmed -->
                    <div class="timeline-step ${order.orderStatus == 'CONFIRMED' || order.orderStatus == 'SHIPPED' || order.orderStatus == 'DELIVERED' ? 'completed' : ''}">
                        <div class="step-icon"><i class="bi bi-box-seam"></i></div>
                        <div class="step-title">Confirmed</div>
                    </div>

                    <!-- Step 3: Shipped -->
                    <div class="timeline-step ${order.orderStatus == 'SHIPPED' || order.orderStatus == 'DELIVERED' ? 'completed' : ''}">
                        <div class="step-icon"><i class="bi bi-truck"></i></div>
                        <div class="step-title">Shipped</div>
                    </div>

                    <!-- Step 4: Delivered -->
                    <div class="timeline-step ${order.orderStatus == 'DELIVERED' ? 'completed' : ''}">
                        <div class="step-icon"><i class="bi bi-house-check"></i></div>
                        <div class="step-title">Delivered</div>
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-danger rounded-4 p-4 mb-4 d-flex align-items-center gap-3">
                <i class="bi bi-x-octagon-fill fs-2"></i>
                <div>
                    <h5 class="fw-bold mb-1">This order was cancelled</h5>
                    <p class="mb-0 small">All items have been returned to warehouse stock and payments reversed.</p>
                </div>
            </div>
        </c:otherwise>
    </c:choose>

    <div class="row g-4">
        <!-- Purchased Items -->
        <div class="col-lg-8">
            <div class="card border-0 shadow-sm rounded-4 bg-white p-4">
                <h5 class="fw-bold text-dark mb-3">Purchased Items (${order.items.size()})</h5>
                <div class="table-responsive">
                    <table class="table align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>Item</th>
                                <th>Price</th>
                                <th>Qty</th>
                                <th>Subtotal</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${order.items}">
                                <tr>
                                    <td>
                                        <div class="d-flex align-items-center gap-3">
                                            <img src="${not empty item.product.imageUrl ? item.product.imageUrl : 'assets/images/default-product.png'}"
                                                 alt="<c:out value='${item.product.name}' />"
                                                 class="rounded-3 border product-img"
                                                 style="width: 50px; height: 50px; object-fit: contain;">
                                            <div>
                                                <a href="${pageContext.request.contextPath}/product-details?id=${item.productId}" class="fw-semibold text-dark text-decoration-none">
                                                    <c:out value="${item.product.name}" />
                                                </a>
                                                <div class="small text-muted"><c:out value="${item.product.brand}" /></div>
                                            </div>
                                        </div>
                                    </td>
                                    <td>₹<fmt:formatNumber value="${item.price}" pattern="#,##0.00"/></td>
                                    <td>${item.quantity}</td>
                                    <td class="fw-bold">₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- Cancel Order Action (if PLACED or CONFIRMED) -->
                <c:if test="${order.orderStatus == 'PLACED' || order.orderStatus == 'CONFIRMED'}">
                    <div class="mt-4 pt-3 border-top">
                        <form action="${pageContext.request.contextPath}/order/cancel" method="POST" onsubmit="return confirm('Are you sure you want to cancel this order? Stock will be replenished.');">
                            <input type="hidden" name="orderId" value="${order.orderId}">
                            <button type="submit" class="btn btn-outline-danger btn-sm">
                                <i class="bi bi-x-circle me-1"></i> Cancel Order &amp; Restore Inventory
                            </button>
                        </form>
                    </div>
                </c:if>
            </div>
        </div>

        <!-- Sidebar: Shipping & Payment Summary -->
        <div class="col-lg-4">
            <!-- Shipping Address -->
            <div class="card border-0 shadow-sm rounded-4 bg-white p-4 mb-4">
                <h6 class="fw-bold text-dark mb-2"><i class="bi bi-geo-alt me-1 text-primary"></i> Shipping Address</h6>
                <c:choose>
                    <c:when test="${not empty order.address}">
                        <div class="text-dark fw-semibold mt-1"><c:out value="${order.address.addressLine}" /></div>
                        <div class="text-muted small"><c:out value="${order.address.city}" />, <c:out value="${order.address.state}" /> - <c:out value="${order.address.pincode}" /></div>
                        <div class="badge bg-light text-secondary border mt-2">${order.address.addressType}</div>
                    </c:when>
                    <c:otherwise>
                        <span class="text-muted">Standard Delivery Address</span>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Financial Breakdown -->
            <div class="card border-0 shadow-sm rounded-4 bg-white p-4">
                <h6 class="fw-bold text-dark mb-3"><i class="bi bi-receipt me-1 text-primary"></i> Payment Details</h6>
                <div class="d-flex justify-content-between mb-2">
                    <span class="text-muted">Subtotal</span>
                    <span>₹<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></span>
                </div>
                <c:if test="${order.discount > 0}">
                    <div class="d-flex justify-content-between mb-2 text-success">
                        <span>Discount</span>
                        <span>-₹<fmt:formatNumber value="${order.discount}" pattern="#,##0.00"/></span>
                    </div>
                </c:if>
                <div class="d-flex justify-content-between mb-2">
                    <span class="text-muted">Payment Method</span>
                    <span class="fw-semibold">${order.paymentMethod}</span>
                </div>
                <div class="d-flex justify-content-between mb-3">
                    <span class="text-muted">Payment Status</span>
                    <span class="badge ${order.paymentStatus == 'PAID' ? 'bg-success' : 'bg-warning text-dark'}">${order.paymentStatus}</span>
                </div>

                <hr class="my-3">

                <div class="d-flex justify-content-between">
                    <span class="fw-bold text-dark">Grand Total</span>
                    <span class="fw-bold text-primary fs-5">₹<fmt:formatNumber value="${order.netPayable}" pattern="#,##0.00"/></span>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>
