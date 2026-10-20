<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="${not empty category ? 'Edit Category' : 'Add Category'} - ShopSphere Admin" />
<c:set var="activePage" value="categories" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<div class="container-fluid py-4 px-lg-5">
    <div class="row">
        <%@ include file="/fragments/admin-sidebar.jsp" %>

        <div class="col-lg-10 col-md-9">
            <div class="card border-0 shadow-sm rounded-4 bg-white p-4 mx-auto" style="max-width: 600px;">
                <h4 class="fw-bold text-dark mb-4">${not empty category ? 'Edit Category' : 'Add New Category'}</h4>

                <form action="${pageContext.request.contextPath}/admin/categories" method="POST">
                    <input type="hidden" name="categoryId" value="${category.categoryId != null ? category.categoryId : 0}">

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Category Name</label>
                        <input type="text" name="name" class="form-control" value="<c:out value='${category.name}' />" required placeholder="e.g. Smart Wearables">
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Description</label>
                        <textarea name="description" rows="3" class="form-control" placeholder="Short summary of items..."><c:out value="${category.description}" /></textarea>
                    </div>

                    <div class="mb-4">
                        <label class="form-label fw-semibold">Status</label>
                        <select name="status" class="form-select">
                            <option value="ACTIVE" ${category.status == 'ACTIVE' ? 'selected' : ''}>ACTIVE</option>
                            <option value="INACTIVE" ${category.status == 'INACTIVE' ? 'selected' : ''}>INACTIVE</option>
                        </select>
                    </div>

                    <div class="d-flex justify-content-end gap-2 pt-3 border-top">
                        <a href="${pageContext.request.contextPath}/admin/categories" class="btn btn-outline-custom">Cancel</a>
                        <button type="submit" class="btn btn-primary-custom px-4">Save Category</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>
