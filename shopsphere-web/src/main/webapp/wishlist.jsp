<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="My Wishlist - ShopSphere" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<div class="container my-4">
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb mb-4">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">Home</a></li>
            <li class="breadcrumb-item active" aria-current="page">Wishlist</li>
        </ol>
    </nav>

    <h2 class="fw-bold mb-4">Saved Wishlist Items</h2>

    <c:choose>
        <c:when test="${not empty wishlist}">
            <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
                <c:forEach var="item" items="${wishlist}">
                    <div class="col">
                        <div class="card border-0 shadow-sm rounded-4 bg-white p-3 h-100 d-flex flex-column">
                            <div class="text-center p-3 bg-light rounded-3 mb-3">
                                <img src="${not empty item.product.imageUrl ? item.product.imageUrl : 'assets/images/default-product.png'}"
                                     alt="<c:out value='${item.product.name}' />"
                                     class="img-fluid product-img"
                                     style="max-height: 160px; object-fit: contain;">
                            </div>
                            <div class="small text-muted text-uppercase fw-bold"><c:out value="${item.product.brand}" /></div>
                            <h6 class="fw-bold text-dark text-truncate mt-1"><c:out value="${item.product.name}" /></h6>

                            <div class="d-flex align-items-baseline gap-2 my-2">
                                <span class="fw-bold fs-5 text-dark">₹<fmt:formatNumber value="${item.product.discountedPrice}" pattern="#,##0.00"/></span>
                                <c:if test="${item.product.discount > 0}">
                                    <span class="text-muted small text-decoration-line-through">₹<fmt:formatNumber value="${item.product.price}" pattern="#,##0.00"/></span>
                                    <span class="badge bg-danger small">-${item.product.discount}%</span>
                                </c:if>
                            </div>

                            <div class="mt-auto pt-3 border-top d-flex gap-2">
                                <form action="${pageContext.request.contextPath}/wishlist/move-to-cart" method="POST" class="flex-grow-1">
                                    <input type="hidden" name="productId" value="${item.productId}">
                                    <button type="submit" class="btn btn-primary-custom btn-sm w-100">
                                        <i class="bi bi-cart-plus me-1"></i> Move to Cart
                                    </button>
                                </form>
                                <form action="${pageContext.request.contextPath}/wishlist/remove" method="POST">
                                    <input type="hidden" name="productId" value="${item.productId}">
                                    <button type="submit" class="btn btn-outline-danger btn-sm" title="Remove">
                                        <i class="bi bi-trash"></i>
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="card border-0 shadow-sm rounded-4 bg-white p-5 text-center my-4">
                <i class="bi bi-heartbreak text-muted display-1 mb-3"></i>
                <h4 class="fw-bold">Your Wishlist is Empty</h4>
                <p class="text-muted">Save your favorite items here while browsing so you can find them later.</p>
                <div class="mt-3">
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary-custom px-4">Explore Products</a>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/fragments/footer.jsp" %>
