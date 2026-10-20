<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="My Orders - ShopSphere" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<div class="container my-4">
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb mb-4">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">Home</a></li>
            <li class="breadcrumb-item active" aria-current="page">My Orders</li>
        </ol>
    </nav>

    <h2 class="fw-bold mb-4">Order History</h2>

    <c:choose>
        <c:when test="${not empty orders}">
            <div class="d-flex flex-column gap-3">
                <c:forEach var="ord" items="${orders}">
                    <div class="card border-0 shadow-sm rounded-4 bg-white p-4">
                        <div class="row align-items-center g-3">
                            <div class="col-md-3">
                                <span class="fw-bold text-dark fs-5">Order #${ord.orderId}</span>
                                <div class="small text-muted"><c:out value="${ord.createdAt}" /></div>
                            </div>
                            <div class="col-md-2">
                                <span class="badge ${ord.orderStatus == 'DELIVERED' ? 'bg-success' : (ord.orderStatus == 'CANCELLED' ? 'bg-danger' : 'bg-primary')} px-3 py-2 rounded-pill">
                                    ${ord.orderStatus}
                                </span>
                            </div>
                            <div class="col-md-3">
                                <div class="small text-muted">${ord.items.size()} item(s)</div>
                                <div class="fw-bold text-dark fs-6">₹<fmt:formatNumber value="${ord.netPayable}" pattern="#,##0.00"/></div>
                            </div>
                            <div class="col-md-4 text-md-end">
                                <a href="${pageContext.request.contextPath}/order-details?id=${ord.orderId}" class="btn btn-outline-custom btn-sm px-3">
                                    <i class="bi bi-eye me-1"></i> View Order Details
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="card border-0 shadow-sm rounded-4 bg-white p-5 text-center my-4">
                <i class="bi bi-box2 text-muted display-1 mb-3"></i>
                <h4 class="fw-bold">No Orders Placed Yet</h4>
                <p class="text-muted">You haven't placed any orders with ShopSphere yet.</p>
                <div class="mt-3">
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary-custom px-4">Browse Products</a>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/fragments/footer.jsp" %>
