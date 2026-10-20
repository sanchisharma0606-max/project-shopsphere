<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="My Shopping Cart - ShopSphere" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<div class="container my-4">
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb mb-4">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">Home</a></li>
            <li class="breadcrumb-item active" aria-current="page">Shopping Cart</li>
        </ol>
    </nav>

    <h2 class="fw-bold mb-4">Shopping Cart</h2>

    <c:choose>
        <c:when test="${not empty cartItems}">
            <div class="row g-4">
                <!-- Cart Items List -->
                <div class="col-lg-8">
                    <div class="card border-0 shadow-sm rounded-4 bg-white p-3 p-md-4">
                        <div class="table-responsive">
                            <table class="table align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <th scope="col" style="min-width: 250px;">Product</th>
                                        <th scope="col">Price</th>
                                        <th scope="col" style="width: 140px;">Quantity</th>
                                        <th scope="col">Subtotal</th>
                                        <th scope="col"></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${cartItems}">
                                        <tr>
                                            <td>
                                                <div class="d-flex align-items-center gap-3">
                                                    <img src="${not empty item.product.imageUrl ? item.product.imageUrl : 'assets/images/default-product.png'}"
                                                         alt="<c:out value='${item.product.name}' />"
                                                         class="rounded-3 border product-img"
                                                         style="width: 60px; height: 60px; object-fit: contain;">
                                                    <div>
                                                        <a href="${pageContext.request.contextPath}/product-details?id=${item.productId}" class="fw-semibold text-dark text-decoration-none">
                                                            <c:out value="${item.product.name}" />
                                                        </a>
                                                        <div class="small text-muted"><c:out value="${item.product.brand}" /></div>
                                                    </div>
                                                </div>
                                            </td>
                                            <td class="fw-semibold">
                                                ₹<fmt:formatNumber value="${item.product.discountedPrice}" pattern="#,##0.00"/>
                                            </td>
                                            <td>
                                                <!-- Quantity Control Form -->
                                                <form action="${pageContext.request.contextPath}/cart/update" method="POST" class="d-inline">
                                                    <input type="hidden" name="productId" value="${item.productId}">
                                                    <div class="input-group input-group-sm qty-control-group" style="width: 120px;">
                                                        <button class="btn btn-outline-secondary btn-qty-minus" type="button">-</button>
                                                        <input type="number" name="quantity" class="form-control text-center qty-input" value="${item.quantity}" min="1" max="${item.product.stock}">
                                                        <button class="btn btn-outline-secondary btn-qty-plus" type="button">+</button>
                                                    </div>
                                                </form>
                                            </td>
                                            <td class="fw-bold text-dark">
                                                ₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/>
                                            </td>
                                            <td class="text-end">
                                                <form action="${pageContext.request.contextPath}/cart/remove" method="POST" class="d-inline">
                                                    <input type="hidden" name="productId" value="${item.productId}">
                                                    <button type="submit" class="btn btn-link text-danger p-0 border-0" title="Remove item">
                                                        <i class="bi bi-trash fs-5"></i>
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>

                        <!-- Continue Shopping Link -->
                        <div class="d-flex justify-content-between align-items-center mt-3 pt-3 border-top">
                            <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-custom btn-sm">
                                <i class="bi bi-arrow-left me-1"></i> Continue Shopping
                            </a>
                        </div>
                    </div>
                </div>

                <!-- Order Summary & Coupons Sidebar -->
                <div class="col-lg-4">
                    <!-- Coupon Card -->
                    <div class="card border-0 shadow-sm rounded-4 bg-white p-4 mb-4">
                        <h6 class="fw-bold text-dark mb-3"><i class="bi bi-ticket-perforated me-2"></i>Have a Coupon?</h6>
                        <c:choose>
                            <c:when test="${not empty coupon}">
                                <div class="alert alert-success d-flex justify-content-between align-items-center p-2 mb-0">
                                    <div>
                                        <strong><c:out value="${coupon.code}" /></strong>
                                        <div class="small text-success">Applied successfully</div>
                                    </div>
                                    <form action="${pageContext.request.contextPath}/cart/remove-coupon" method="POST" class="d-inline">
                                        <button type="submit" class="btn btn-sm btn-outline-danger py-0 px-2">Remove</button>
                                    </form>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <form action="${pageContext.request.contextPath}/cart/apply-coupon" method="POST">
                                    <div class="input-group">
                                        <input type="text" name="couponCode" class="form-control text-uppercase" placeholder="Enter coupon code" required>
                                        <button class="btn btn-outline-custom" type="submit">Apply</button>
                                    </div>
                                    <small class="text-muted d-block mt-2">Try codes: <code>WELCOME10</code>, <code>FLAT500</code></small>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <!-- Summary Card -->
                    <div class="card border-0 shadow-sm rounded-4 bg-white p-4">
                        <h5 class="fw-bold text-dark mb-3">Order Summary</h5>
                        <div class="d-flex justify-content-between mb-2">
                            <span class="text-muted">Subtotal</span>
                            <span class="fw-semibold">₹<fmt:formatNumber value="${subtotal}" pattern="#,##0.00"/></span>
                        </div>
                        <c:if test="${couponDiscount > 0}">
                            <div class="d-flex justify-content-between mb-2 text-success">
                                <span>Coupon Discount</span>
                                <span>-₹<fmt:formatNumber value="${couponDiscount}" pattern="#,##0.00"/></span>
                            </div>
                        </c:if>
                        <div class="d-flex justify-content-between mb-3">
                            <span class="text-muted">Estimated Shipping</span>
                            <span class="text-success fw-bold">FREE</span>
                        </div>

                        <hr class="my-3">

                        <div class="d-flex justify-content-between mb-4">
                            <span class="fs-5 fw-bold text-dark">Total Amount</span>
                            <span class="fs-5 fw-bold text-primary">₹<fmt:formatNumber value="${grandTotal}" pattern="#,##0.00"/></span>
                        </div>

                        <a href="${pageContext.request.contextPath}/checkout" class="btn btn-primary-custom w-100 py-2 d-flex align-items-center justify-content-center gap-2">
                            <i class="bi bi-shield-lock-fill"></i> Proceed to Checkout
                        </a>
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="card border-0 shadow-sm rounded-4 bg-white p-5 text-center my-4">
                <i class="bi bi-cart-x text-muted display-1 mb-3"></i>
                <h3 class="fw-bold">Your Cart is Currently Empty</h3>
                <p class="text-muted">Explore our curated catalogue and add items to your shopping cart.</p>
                <div class="mt-3">
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary-custom px-4">
                        <i class="bi bi-bag me-1"></i> Start Shopping
                    </a>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/fragments/footer.jsp" %>
