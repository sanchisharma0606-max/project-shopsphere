<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="${not empty product ? 'Edit Product' : 'Add Product'} - ShopSphere Admin" />
<c:set var="activePage" value="products" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<div class="container-fluid py-4 px-lg-5">
    <div class="row">
        <%@ include file="/fragments/admin-sidebar.jsp" %>

        <div class="col-lg-10 col-md-9">
            <div class="card border-0 shadow-sm rounded-4 bg-white p-4 mx-auto" style="max-width: 800px;">
                <h4 class="fw-bold text-dark mb-4">${not empty product ? 'Edit Product' : 'Add New Product'}</h4>

                <form action="${pageContext.request.contextPath}/admin/products" method="POST">
                    <input type="hidden" name="productId" value="${product.productId != null ? product.productId : 0}">

                    <div class="row g-3">
                        <div class="col-md-8">
                            <label class="form-label fw-semibold">Product Title</label>
                            <input type="text" name="name" class="form-control" value="<c:out value='${product.name}' />" required placeholder="e.g. Apple MacBook Air M3">
                        </div>
                        <div class="col-md-4">
                            <label class="form-label fw-semibold">Brand</label>
                            <input type="text" name="brand" class="form-control" value="<c:out value='${product.brand}' />" required placeholder="e.g. Apple">
                        </div>

                        <div class="col-md-6">
                            <label class="form-label fw-semibold">Category</label>
                            <select name="categoryId" class="form-select" required>
                                <c:forEach var="c" items="${categories}">
                                    <option value="${c.categoryId}" ${product.categoryId == c.categoryId ? 'selected' : ''}>
                                        <c:out value="${c.name}" />
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label fw-semibold">Status</label>
                            <select name="status" class="form-select">
                                <option value="ACTIVE" ${product.status == 'ACTIVE' ? 'selected' : ''}>ACTIVE</option>
                                <option value="INACTIVE" ${product.status == 'INACTIVE' ? 'selected' : ''}>INACTIVE</option>
                            </select>
                        </div>

                        <div class="col-md-4">
                            <label class="form-label fw-semibold">Base Price (₹)</label>
                            <input type="number" step="0.01" name="price" class="form-control" value="${product.price}" required min="0">
                        </div>

                        <div class="col-md-4">
                            <label class="form-label fw-semibold">Discount (%)</label>
                            <input type="number" step="0.01" name="discount" class="form-control" value="${product.discount != null ? product.discount : 0}" min="0" max="100">
                        </div>

                        <div class="col-md-4">
                            <label class="form-label fw-semibold">Stock Quantity</label>
                            <input type="number" name="stock" class="form-control" value="${product.stock != null ? product.stock : 10}" required min="0">
                        </div>

                        <div class="col-12">
                            <label class="form-label fw-semibold">Image Asset Path / URL</label>
                            <input type="text" name="imageUrl" class="form-control" value="<c:out value='${product.imageUrl}' />" placeholder="assets/images/products/example.jpg">
                        </div>

                        <div class="col-12">
                            <label class="form-label fw-semibold">Description</label>
                            <textarea name="description" rows="4" class="form-control" placeholder="Detailed product specifications..."><c:out value="${product.description}" /></textarea>
                        </div>
                    </div>

                    <div class="d-flex justify-content-end gap-2 mt-4 pt-3 border-top">
                        <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-outline-custom">Cancel</a>
                        <button type="submit" class="btn btn-primary-custom px-4">Save Product</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>
