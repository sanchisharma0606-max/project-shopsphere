<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Coupon Management - ShopSphere Admin" />
<c:set var="activePage" value="coupons" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<div class="container-fluid py-4 px-lg-5">
    <div class="row">
        <%@ include file="/fragments/admin-sidebar.jsp" %>

        <div class="col-lg-10 col-md-9">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="fw-bold text-dark mb-1">Coupon &amp; Discount Management</h3>
                    <p class="text-muted small mb-0">Create promotional discount codes and eligibility criteria</p>
                </div>
                <a href="${pageContext.request.contextPath}/admin/coupons/add" class="btn btn-primary-custom btn-sm">
                    <i class="bi bi-plus-lg me-1"></i> Add Coupon
                </a>
            </div>

            <div class="card border-0 shadow-sm rounded-4 bg-white p-4">
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>Code</th>
                                <th>Discount</th>
                                <th>Min. Order</th>
                                <th>Max. Cap</th>
                                <th>Expiry Date</th>
                                <th>Status</th>
                                <th class="text-end">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="c" items="${coupons}">
                                <tr>
                                    <td><strong class="text-primary font-monospace fs-6"><c:out value="${c.code}" /></strong></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${c.discountType == 'PERCENTAGE'}">
                                                <span class="badge bg-primary-subtle text-primary">${c.discountValue}%</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-success-subtle text-success">₹${c.discountValue} Flat</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>₹<fmt:formatNumber value="${c.minimumOrder}" pattern="#,##0"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${c.maximumDiscount != null}">₹<fmt:formatNumber value="${c.maximumDiscount}" pattern="#,##0"/></c:when>
                                            <c:otherwise><span class="text-muted small">No Cap</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><span class="small ${c.expired ? 'text-danger fw-bold' : 'text-dark'}">${c.expiryDate}</span></td>
                                    <td>
                                        <span class="badge ${c.status == 'ACTIVE' && !c.expired ? 'bg-success' : 'bg-secondary'}">
                                            ${c.expired ? 'EXPIRED' : c.status}
                                        </span>
                                    </td>
                                    <td class="text-end">
                                        <a href="${pageContext.request.contextPath}/admin/coupons/edit?id=${c.couponId}" class="btn btn-outline-primary btn-sm me-1">
                                            <i class="bi bi-pencil"></i>
                                        </a>
                                        <form action="${pageContext.request.contextPath}/admin/coupons/delete" method="POST" class="d-inline" onsubmit="return confirm('Delete coupon?');">
                                            <input type="hidden" name="couponId" value="${c.couponId}">
                                            <button type="submit" class="btn btn-outline-danger btn-sm">
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
