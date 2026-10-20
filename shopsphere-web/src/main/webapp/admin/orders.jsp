<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Order Processing - ShopSphere Admin" />
<c:set var="activePage" value="orders" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<div class="container-fluid py-4 px-lg-5">
    <div class="row">
        <%@ include file="/fragments/admin-sidebar.jsp" %>

        <div class="col-lg-10 col-md-9">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="fw-bold text-dark mb-1">Customer Order Management</h3>
                    <p class="text-muted small mb-0">Track order lifecycle stages, review payments, and dispatch shipments</p>
                </div>
            </div>

            <div class="card border-0 shadow-sm rounded-4 bg-white p-4">
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>Order ID</th>
                                <th>Customer</th>
                                <th>Items</th>
                                <th>Amount</th>
                                <th>Payment</th>
                                <th>Order Status</th>
                                <th>Update Status</th>
                                <th class="text-end">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="ord" items="${orders}">
                                <tr>
                                    <td class="fw-bold">#${ord.orderId}</td>
                                    <td>
                                        <div class="fw-semibold text-dark"><c:out value="${ord.userName}" /></div>
                                        <small class="text-muted"><c:out value="${ord.userEmail}" /></small>
                                    </td>
                                    <td>${ord.items.size()} item(s)</td>
                                    <td class="fw-bold">₹<fmt:formatNumber value="${ord.netPayable}" pattern="#,##0.00"/></td>
                                    <td>
                                        <div class="small fw-semibold">${ord.paymentMethod}</div>
                                        <span class="badge ${ord.paymentStatus == 'PAID' ? 'bg-success' : 'bg-warning text-dark'} small">
                                            ${ord.paymentStatus}
                                        </span>
                                    </td>
                                    <td>
                                        <span class="badge ${ord.orderStatus == 'DELIVERED' ? 'bg-success' : (ord.orderStatus == 'CANCELLED' ? 'bg-danger' : 'bg-primary')} px-2 py-1">
                                            ${ord.orderStatus}
                                        </span>
                                    </td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/admin/orders/status" method="POST" class="d-flex gap-1 align-items-center">
                                            <input type="hidden" name="orderId" value="${ord.orderId}">
                                            <select name="orderStatus" class="form-select form-select-sm" style="width: 130px;">
                                                <option value="PLACED" ${ord.orderStatus == 'PLACED' ? 'selected' : ''}>PLACED</option>
                                                <option value="CONFIRMED" ${ord.orderStatus == 'CONFIRMED' ? 'selected' : ''}>CONFIRMED</option>
                                                <option value="SHIPPED" ${ord.orderStatus == 'SHIPPED' ? 'selected' : ''}>SHIPPED</option>
                                                <option value="DELIVERED" ${ord.orderStatus == 'DELIVERED' ? 'selected' : ''}>DELIVERED</option>
                                                <option value="CANCELLED" ${ord.orderStatus == 'CANCELLED' ? 'selected' : ''}>CANCELLED</option>
                                            </select>
                                            <button type="submit" class="btn btn-primary-custom btn-sm py-1 px-2">Update</button>
                                        </form>
                                    </td>
                                    <td class="text-end">
                                        <a href="${pageContext.request.contextPath}/order-details?id=${ord.orderId}" class="btn btn-outline-custom btn-sm" title="View details" target="_blank">
                                            <i class="bi bi-eye"></i>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>
