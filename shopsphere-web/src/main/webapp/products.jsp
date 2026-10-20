<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:set var="pageTitle" value="All Products - ShopSphere" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<div class="container my-4">
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb mb-4">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">Home</a></li>
            <li class="breadcrumb-item active" aria-current="page">Product Catalogue</li>
        </ol>
    </nav>

    <div class="row g-4">
        <!-- Sidebar Filters -->
        <div class="col-lg-3">
            <div class="card shadow-sm border-0 rounded-3 sticky-top" style="top: 80px;">
                <div class="card-body p-4">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h5 class="fw-bold mb-0"><i class="bi bi-funnel me-1"></i> Filters</h5>
                        <a href="${pageContext.request.contextPath}/products" class="small text-muted">Reset</a>
                    </div>

                    <form action="${pageContext.request.contextPath}/products" method="GET">
                        <!-- Preserve search query -->
                        <c:if test="${not empty query}">
                            <input type="hidden" name="q" value="<c:out value='${query}' />">
                        </c:if>

                        <!-- Category Filter -->
                        <div class="mb-4">
                            <label class="form-label fw-semibold text-dark">Category</label>
                            <select name="category" class="form-select form-select-sm" onchange="this.form.submit()">
                                <option value="">All Categories</option>
                                <c:forEach var="cat" items="${categories}">
                                    <option value="${cat.categoryId}" ${selectedCategoryId == cat.categoryId ? 'selected' : ''}>
                                        <c:out value="${cat.name}" />
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Price Range -->
                        <div class="mb-4">
                            <label class="form-label fw-semibold text-dark">Price Range (₹)</label>
                            <div class="row g-2">
                                <div class="col-6">
                                    <input type="number" name="minPrice" class="form-control form-control-sm" placeholder="Min" value="${minPrice}">
                                </div>
                                <div class="col-6">
                                    <input type="number" name="maxPrice" class="form-control form-control-sm" placeholder="Max" value="${maxPrice}">
                                </div>
                            </div>
                        </div>

                        <!-- Sort By -->
                        <div class="mb-4">
                            <label class="form-label fw-semibold text-dark">Sort By</label>
                            <select name="sort" class="form-select form-select-sm" onchange="this.form.submit()">
                                <option value="latest" ${sortBy == 'latest' ? 'selected' : ''}>Newest Arrivals</option>
                                <option value="price_asc" ${sortBy == 'price_asc' ? 'selected' : ''}>Price: Low to High</option>
                                <option value="price_desc" ${sortBy == 'price_desc' ? 'selected' : ''}>Price: High to Low</option>
                                <option value="name_asc" ${sortBy == 'name_asc' ? 'selected' : ''}>Name: A to Z</option>
                                <option value="rating" ${sortBy == 'rating' ? 'selected' : ''}>Customer Rating</option>
                            </select>
                        </div>

                        <button type="submit" class="btn btn-primary-custom btn-sm w-100">
                            Apply Filters
                        </button>
                    </form>
                </div>
            </div>
        </div>

        <!-- Product Listing Grid -->
        <div class="col-lg-9">
            <!-- Results Bar -->
            <div class="d-flex justify-content-between align-items-center mb-3 bg-white p-3 rounded-3 shadow-sm border">
                <div class="text-muted small">
                    Showing <span class="fw-bold text-dark">${totalResults}</span> products
                    <c:if test="${not empty query}">
                        for "<strong class="text-dark"><c:out value="${query}" /></strong>"
                    </c:if>
                </div>
            </div>

            <c:choose>
                <c:when test="${not empty products}">
                    <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-4">
                        <c:forEach var="prod" items="${products}">
                            <div class="col">
                                <tags:productCard product="${prod}" />
                            </div>
                        </c:forEach>
                    </div>

                    <!-- Pagination -->
                    <c:if test="${totalPages > 1}">
                        <nav class="mt-5" aria-label="Products pagination">
                            <ul class="pagination justify-content-center">
                                <li class="page-item ${currentPage <= 1 ? 'disabled' : ''}">
                                    <a class="page-link" href="${pageContext.request.contextPath}/products?page=${currentPage - 1}&q=${query}&category=${selectedCategoryId}&minPrice=${minPrice}&maxPrice=${maxPrice}&sort=${sortBy}">Previous</a>
                                </li>
                                <c:forEach var="p" begin="1" end="${totalPages}">
                                    <li class="page-item ${currentPage == p ? 'active' : ''}">
                                        <a class="page-link" href="${pageContext.request.contextPath}/products?page=${p}&q=${query}&category=${selectedCategoryId}&minPrice=${minPrice}&maxPrice=${maxPrice}&sort=${sortBy}">${p}</a>
                                    </li>
                                </c:forEach>
                                <li class="page-item ${currentPage >= totalPages ? 'disabled' : ''}">
                                    <a class="page-link" href="${pageContext.request.contextPath}/products?page=${currentPage + 1}&q=${query}&category=${selectedCategoryId}&minPrice=${minPrice}&maxPrice=${maxPrice}&sort=${sortBy}">Next</a>
                                </li>
                            </ul>
                        </nav>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <div class="card p-5 text-center border-0 shadow-sm rounded-4 bg-white">
                        <i class="bi bi-search text-muted fs-1 mb-3"></i>
                        <h4 class="fw-bold">No Products Found</h4>
                        <p class="text-muted">Try adjusting your search criteria, price range, or category filter.</p>
                        <div class="mt-3">
                            <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-custom">Clear All Filters</a>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>
