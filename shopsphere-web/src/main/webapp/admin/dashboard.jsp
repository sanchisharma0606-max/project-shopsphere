<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Admin Dashboard - ShopSphere" />
<c:set var="activePage" value="dashboard" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<div class="container-fluid py-4 px-lg-5">
    <div class="row">
        <!-- Sidebar Navigation -->
        <%@ include file="/fragments/admin-sidebar.jsp" %>

        <!-- Main Dashboard View -->
        <div class="col-lg-10 col-md-9">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="fw-bold text-dark mb-1">Administrative Dashboard</h3>
                    <p class="text-muted small mb-0">Real-time e-commerce operational intelligence &amp; warehouse telemetry</p>
                </div>
                <div>
                    <span class="badge bg-success-subtle text-success px-3 py-2 border border-success-subtle">
                        <i class="bi bi-circle-fill me-1 small"></i> Live Database Connected
                    </span>
                </div>
            </div>

            <!-- Analytics KPI Cards -->
            <div class="row g-3 mb-4">
                <div class="col-sm-6 col-xl-3">
                    <div class="stat-card">
                        <div class="stat-icon bg-primary-subtle text-primary">
                            <i class="bi bi-currency-rupee"></i>
                        </div>
                        <div>
                            <div class="stat-value">₹<fmt:formatNumber value="${stats.totalSales}" pattern="#,##0"/></div>
                            <div class="stat-label">Total Realized Revenue</div>
                        </div>
                    </div>
                </div>

                <div class="col-sm-6 col-xl-3">
                    <div class="stat-card">
                        <div class="stat-icon bg-info-subtle text-info">
                            <i class="bi bi-cart-check"></i>
                        </div>
                        <div>
                            <div class="stat-value">${stats.totalOrders}</div>
                            <div class="stat-label">Total Lifetime Orders</div>
                        </div>
                    </div>
                </div>

                <div class="col-sm-6 col-xl-3">
                    <div class="stat-card">
                        <div class="stat-icon bg-warning-subtle text-warning">
                            <i class="bi bi-hourglass-split"></i>
                        </div>
                        <div>
                            <div class="stat-value">${stats.pendingOrders}</div>
                            <div class="stat-label">Pending Processing</div>
                        </div>
                    </div>
                </div>

                <div class="col-sm-6 col-xl-3">
                    <div class="stat-card">
                        <div class="stat-icon bg-danger-subtle text-danger">
                            <i class="bi bi-exclamation-triangle"></i>
                        </div>
                        <div>
                            <div class="stat-value">${stats.lowStockCount}</div>
                            <div class="stat-label">Low Stock Alerts (&le;10)</div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row g-4">
                <!-- Recent Orders Table -->
                <div class="col-lg-8">
                    <div class="card border-0 shadow-sm rounded-4 bg-white p-4">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <h5 class="fw-bold text-dark mb-0">Recent Placed Orders</h5>
                            <a href="${pageContext.request.contextPath}/admin/orders" class="small text-primary fw-semibold">View All Orders &rarr;</a>
                        </div>

                        <div class="table-responsive">
                            <table class="table table-hover align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <th>Order ID</th>
                                        <th>Customer</th>
                                        <th>Amount</th>
                                        <th>Status</th>
                                        <th>Date</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="ord" items="${stats.recentOrders}">
                                        <tr>
                                            <td class="fw-bold">#${ord.orderId}</td>
                                            <td>
                                                <div class="fw-semibold text-dark"><c:out value="${ord.userName}" /></div>
                                                <small class="text-muted"><c:out value="${ord.userEmail}" /></small>
                                            </td>
                                            <td class="fw-bold text-dark">₹<fmt:formatNumber value="${ord.netPayable}" pattern="#,##0.00"/></td>
                                            <td>
                                                <span class="badge ${ord.orderStatus == 'DELIVERED' ? 'bg-success' : (ord.orderStatus == 'CANCELLED' ? 'bg-danger' : 'bg-primary')}">
                                                    ${ord.orderStatus}
                                                </span>
                                            </td>
                                            <td class="small text-muted"><c:out value="${ord.createdAt}" /></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <!-- Quick Overview & Warehouse Status -->
                <div class="col-lg-4">
                    <div class="card border-0 shadow-sm rounded-4 bg-white p-4 mb-4">
                        <h6 class="fw-bold text-dark mb-3">Warehouse Inventory Overview</h6>
                        <div class="d-flex justify-content-between align-items-center py-2 border-bottom">
                            <span class="text-muted">Total Catalog Products</span>
                            <span class="fw-bold text-dark">${stats.totalProducts}</span>
                        </div>
                        <div class="d-flex justify-content-between align-items-center py-2 border-bottom">
                            <span class="text-muted">Registered Customers</span>
                            <span class="fw-bold text-dark">${stats.totalCustomers}</span>
                        </div>
                        <div class="d-flex justify-content-between align-items-center py-2">
                            <span class="text-muted">Active Sessions</span>
                            <span class="badge bg-info-subtle text-info">${applicationScope.activeSessions != null ? applicationScope.activeSessions : 1}</span>
                        </div>

                        <div class="mt-4 pt-2">
                            <a href="${pageContext.request.contextPath}/admin/products/add" class="btn btn-primary-custom btn-sm w-100 mb-2">
                                <i class="bi bi-plus-circle me-1"></i> Add New Product
                            </a>
                            <a href="${pageContext.request.contextPath}/admin/categories/add" class="btn btn-outline-custom btn-sm w-100">
                                <i class="bi bi-tag me-1"></i> Add New Category
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>
