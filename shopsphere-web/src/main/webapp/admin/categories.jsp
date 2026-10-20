<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Category Management - ShopSphere Admin" />
<c:set var="activePage" value="categories" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<div class="container-fluid py-4 px-lg-5">
    <div class="row">
        <%@ include file="/fragments/admin-sidebar.jsp" %>

        <div class="col-lg-10 col-md-9">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="fw-bold text-dark mb-1">Category Management</h3>
                    <p class="text-muted small mb-0">Organize and group catalog offerings</p>
                </div>
                <a href="${pageContext.request.contextPath}/admin/categories/add" class="btn btn-primary-custom btn-sm">
                    <i class="bi bi-plus-lg me-1"></i> Add Category
                </a>
            </div>

            <div class="card border-0 shadow-sm rounded-4 bg-white p-4">
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Description</th>
                                <th>Status</th>
                                <th class="text-end">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="c" items="${categories}">
                                <tr>
                                    <td class="fw-bold">#${c.categoryId}</td>
                                    <td class="fw-semibold text-dark"><c:out value="${c.name}" /></td>
                                    <td class="text-muted small"><c:out value="${c.description}" /></td>
                                    <td>
                                        <span class="badge ${c.status == 'ACTIVE' ? 'bg-success-subtle text-success' : 'bg-secondary-subtle text-secondary'}">
                                            ${c.status}
                                        </span>
                                    </td>
                                    <td class="text-end">
                                        <a href="${pageContext.request.contextPath}/admin/categories/edit?id=${c.categoryId}" class="btn btn-outline-primary btn-sm me-1">
                                            <i class="bi bi-pencil"></i>
                                        </a>
                                        <form action="${pageContext.request.contextPath}/admin/categories/delete" method="POST" class="d-inline" onsubmit="return confirm('Delete this category? If products exist, please deactivate instead.');">
                                            <input type="hidden" name="categoryId" value="${c.categoryId}">
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
