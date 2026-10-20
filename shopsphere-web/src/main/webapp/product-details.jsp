<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:set var="pageTitle" value="${product.name} - ShopSphere" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<div class="container my-4">
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb mb-4">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">Home</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/products">Products</a></li>
            <li class="breadcrumb-item active" aria-current="page"><c:out value="${product.name}" /></li>
        </ol>
    </nav>

    <!-- Product Showcase Card -->
    <div class="card border-0 shadow-sm rounded-4 bg-white p-4 mb-5">
        <div class="row g-5">
            <!-- Left: Product Image -->
            <div class="col-lg-6">
                <div class="bg-light rounded-4 p-4 text-center d-flex align-items-center justify-content-center position-relative" style="min-height: 400px;">
                    <c:if test="${product.discount > 0}">
                        <span class="badge bg-danger position-absolute top-0 start-0 m-3 px-3 py-2 fs-6">-${product.discount}% OFF</span>
                    </c:if>
                    <img src="${not empty product.imageUrl ? product.imageUrl : 'assets/images/default-product.png'}"
                         alt="<c:out value='${product.name}' />"
                         class="img-fluid detail-img"
                         style="max-height: 380px; object-fit: contain;">
                </div>
            </div>

            <!-- Right: Details & Buying Actions -->
            <div class="col-lg-6">
                <span class="badge bg-light text-primary border mb-2 px-3 py-1 fw-bold text-uppercase">${product.brand}</span>
                <h1 class="h2 fw-bold text-dark mb-2"><c:out value="${product.name}" /></h1>

                <!-- Ratings and Reviews -->
                <div class="d-flex align-items-center gap-2 mb-3">
                    <tags:rating value="${product.averageRating}" reviews="${product.reviewCount}" />
                    <span class="text-muted small">| Category: <strong class="text-dark">${product.categoryName}</strong></span>
                </div>

                <hr class="my-3">

                <!-- Price Block -->
                <div class="mb-4">
                    <div class="d-flex align-items-baseline gap-2">
                        <span class="display-6 fw-bold text-dark">₹<fmt:formatNumber value="${product.discountedPrice}" pattern="#,##0.00"/></span>
                        <c:if test="${product.discount > 0}">
                            <span class="fs-5 text-muted text-decoration-line-through">₹<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></span>
                            <span class="text-success fw-bold small">Save ₹<fmt:formatNumber value="${product.price - product.discountedPrice}" pattern="#,##0.00"/></span>
                        </c:if>
                    </div>
                    <small class="text-muted d-block mt-1">Inclusive of all taxes</small>
                </div>

                <!-- Stock Status -->
                <div class="mb-4">
                    <c:choose>
                        <c:when test="${product.stock > 0}">
                            <span class="badge bg-success-subtle text-success px-3 py-2 rounded-pill fw-semibold">
                                <i class="bi bi-check-circle-fill me-1"></i> In Stock (${product.stock} units available)
                            </span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge bg-danger-subtle text-danger px-3 py-2 rounded-pill fw-semibold">
                                <i class="bi bi-x-circle-fill me-1"></i> Currently Out of Stock
                            </span>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Description -->
                <div class="mb-4">
                    <h6 class="fw-bold text-dark">Product Overview</h6>
                    <p class="text-muted leading-relaxed"><c:out value="${product.description}" /></p>
                </div>

                <!-- Action Controls -->
                <c:if test="${product.stock > 0}">
                    <form action="${pageContext.request.contextPath}/cart/add" method="POST" class="d-flex gap-3 mb-4">
                        <input type="hidden" name="productId" value="${product.productId}">
                        <div class="input-group" style="width: 140px;">
                            <span class="input-group-text bg-light border-end-0">Qty</span>
                            <input type="number" name="quantity" class="form-control text-center" value="1" min="1" max="${product.stock}">
                        </div>
                        <button type="submit" class="btn btn-primary-custom px-4 flex-grow-1 d-flex align-items-center justify-content-center gap-2">
                            <i class="bi bi-cart-plus fs-5"></i> Add to Cart
                        </button>
                    </form>
                </c:if>

                <!-- Wishlist Action -->
                <form action="${pageContext.request.contextPath}/wishlist/add" method="POST" class="d-inline">
                    <input type="hidden" name="productId" value="${product.productId}">
                    <button type="submit" class="btn btn-outline-custom w-100 d-flex align-items-center justify-content-center gap-2">
                        <i class="bi bi-heart"></i> Add to Wishlist
                    </button>
                </form>
            </div>
        </div>
    </div>

    <!-- Customer Reviews Section -->
    <div class="row g-4 mb-5">
        <div class="col-lg-7">
            <div class="card border-0 shadow-sm rounded-4 bg-white p-4">
                <h4 class="fw-bold mb-4">Customer Reviews (${product.reviewCount})</h4>

                <c:choose>
                    <c:when test="${not empty reviews}">
                        <div class="d-flex flex-column gap-3">
                            <c:forEach var="rev" items="${reviews}">
                                <div class="border-bottom pb-3">
                                    <div class="d-flex justify-content-between align-items-center mb-1">
                                        <div class="fw-bold text-dark"><c:out value="${rev.userName}" /></div>
                                        <tags:rating value="${rev.rating + 0.0}" />
                                    </div>
                                    <p class="text-muted small mb-1"><c:out value="${rev.reviewText}" /></p>
                                    <small class="text-secondary" style="font-size: 0.75rem;"><c:out value="${rev.createdAt}" /></small>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <p class="text-muted">No reviews yet for this product. Be the first to review!</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Add Review Form -->
        <div class="col-lg-5">
            <div class="card border-0 shadow-sm rounded-4 bg-white p-4">
                <h5 class="fw-bold mb-3">Write a Review</h5>
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <form action="${pageContext.request.contextPath}/review/add" method="POST">
                            <input type="hidden" name="productId" value="${product.productId}">
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Your Rating</label>
                                <select name="rating" class="form-select">
                                    <option value="5">★★★★★ - Excellent (5 Stars)</option>
                                    <option value="4">★★★★☆ - Very Good (4 Stars)</option>
                                    <option value="3">★★★☆☆ - Average (3 Stars)</option>
                                    <option value="2">★★☆☆☆ - Below Average (2 Stars)</option>
                                    <option value="1">★☆☆☆☆ - Poor (1 Star)</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Review Comments</label>
                                <textarea name="reviewText" rows="3" class="form-control" placeholder="Share your experience with this item..." required></textarea>
                            </div>
                            <button type="submit" class="btn btn-primary-custom w-100">Submit Review</button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-light border text-center p-4">
                            <p class="text-muted mb-3">Please sign in to your account to write a review.</p>
                            <a href="${pageContext.request.contextPath}/login" class="btn btn-primary-custom btn-sm">Sign In to Review</a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Related Products -->
    <c:if test="${not empty relatedProducts}">
        <div class="mb-5">
            <h4 class="fw-bold mb-4">Related Products in ${product.categoryName}</h4>
            <div class="row row-cols-1 row-cols-sm-2 row-cols-md-4 g-4">
                <c:forEach var="rel" items="${relatedProducts}">
                    <div class="col">
                        <tags:productCard product="${rel}" />
                    </div>
                </c:forEach>
            </div>
        </div>
    </c:if>
</div>

<%@ include file="/fragments/footer.jsp" %>
