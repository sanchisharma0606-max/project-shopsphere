<%@ tag description="Reusable Product Card Tag" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="product" required="true" type="com.shopsphere.model.Product" description="The product model" %>

<div class="product-card">
    <div class="product-img-wrapper">
        <!-- Discount Badge -->
        <c:if test="${product.discount > 0}">
            <span class="product-badge-discount">-${product.discount}% OFF</span>
        </c:if>

        <!-- Wishlist Button -->
        <form action="${pageContext.request.contextPath}/wishlist/add" method="POST" class="d-inline">
            <input type="hidden" name="productId" value="${product.productId}">
            <button type="submit" class="wishlist-btn-overlay" title="Save to Wishlist">
                <i class="bi bi-heart"></i>
            </button>
        </form>

        <!-- Product Image -->
        <a href="${pageContext.request.contextPath}/product-details?id=${product.productId}">
            <img src="${not empty product.imageUrl ? product.imageUrl : 'assets/images/default-product.png'}"
                 alt="<c:out value='${product.name}' />"
                 class="product-img">
        </a>
    </div>

    <div class="product-body">
        <div class="product-brand"><c:out value="${product.brand}" /></div>
        <a href="${pageContext.request.contextPath}/product-details?id=${product.productId}" class="product-title" title="<c:out value='${product.name}' />">
            <c:out value="${product.name}" />
        </a>

        <!-- Rating component -->
        <div class="my-2">
            <tags:rating value="${product.averageRating}" reviews="${product.reviewCount}" />
        </div>

        <!-- Price Section -->
        <div class="d-flex align-items-baseline mb-3">
            <span class="price-current">₹<fmt:formatNumber value="${product.discountedPrice}" pattern="#,##0.00"/></span>
            <c:if test="${product.discount > 0}">
                <span class="price-original">₹<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></span>
            </c:if>
        </div>

        <!-- Add To Cart Form -->
        <div class="mt-auto">
            <c:choose>
                <c:when test="${product.inStock}">
                    <form action="${pageContext.request.contextPath}/cart/add" method="POST">
                        <input type="hidden" name="productId" value="${product.productId}">
                        <input type="hidden" name="quantity" value="1">
                        <button type="submit" class="btn btn-primary-custom btn-sm w-100 d-flex align-items-center justify-content-center gap-2">
                            <i class="bi bi-cart-plus"></i> Add to Cart
                        </button>
                    </form>
                </c:when>
                <c:otherwise>
                    <button class="btn btn-secondary btn-sm w-100" disabled>
                        Out of Stock
                    </button>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
