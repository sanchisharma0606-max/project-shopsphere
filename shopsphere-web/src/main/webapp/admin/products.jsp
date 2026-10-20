<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Product Management - ShopSphere Admin" />
<c:set var="activePage" value="products" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<div class="container-fluid py-4 px-lg-5">
    <div class="row">
        <%@ include file="/fragments/admin-sidebar.jsp" %>

        <div class="col-lg-10 col-md-9">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="fw-bold text-dark mb-1">Product Inventory Management</h3>
                    <p class="text-muted small mb-0">Maintain catalogue records, stock thresholds, and promotional discounts</p>
                </div>
                <a href="${pageContext.request.contextPath}/admin/products/add" class="btn btn-primary-custom btn-sm">
                    <i class="bi bi-plus-lg me-1"></i> Add Product
                </a>
            </div>

            <div class="card border-0 shadow-sm rounded-4 bg-white p-4">
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>ID</th>
                                <th>Product Details</th>
                                <th>Category</th>
                                <th>Price (₹)</th>
                                <th>Discount</th>
                                <th>Stock</th>
                                <th>Status</th>
                                <th class="text-end">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="p" items="${products}">
                                <tr>
                                    <td class="fw-bold">#${p.productId}</td>
                                    <td>
                                        <div class="d-flex align-items-center gap-2">
                                            <img src="${not empty p.imageUrl ? p.imageUrl : 'assets/images/default-product.png'}"
                                                 alt="<c:out value='${p.name}' />"
                                                 class="rounded product-img border"
                                                 style="width: 44px; height: 44px; object-fit: contain;">
                                            <div>
                                                <div class="fw-semibold text-dark text-truncate" style="max-width: 250px;"><c:out value="${p.name}" /></div>
                                                <small class="text-muted"><c:out value="${p.brand}" /></small>
                                            </div>
                                        </div>
                                    </td>
                                    <td><span class="badge bg-light text-dark border">${p.categoryName}</span></td>
                                    <td class="fw-semibold">₹<fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${p.discount > 0}"><span class="badge bg-danger">-${p.discount}%</span></c:when>
                                            <c:otherwise><span class="text-muted small">None</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <span class="badge ${p.stock <= 10 ? 'bg-danger' : 'bg-success'}">
                                            ${p.stock} units
                                        </span>
                                    </td>
                                    <td>
                                        <span class="badge ${p.status == 'ACTIVE' ? 'bg-success-subtle text-success' : 'bg-secondary-subtle text-secondary'}">
                                            ${p.status}
                                        </span>
                                    </td>
                                    <td class="text-end">
                                        <a href="${pageContext.request.contextPath}/admin/products/edit?id=${p.productId}" class="btn btn-outline-primary btn-sm me-1" title="Edit">
                                            <i class="bi bi-pencil"></i>
                                        </a>
                                        <form action="${pageContext.request.contextPath}/admin/products/delete" method="POST" class="d-inline" onsubmit="return confirm('Delete this product?');">
                                            <input type="hidden" name="productId" value="${p.productId}">
                                            <button type="submit" class="btn btn-outline-danger btn-sm" title="Delete">
                                                <i class="bi bi-trash"></i>
                                            </button>
                                        </form>
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
