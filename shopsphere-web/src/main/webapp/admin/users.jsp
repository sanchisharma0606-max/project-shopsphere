<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Customer Management - ShopSphere Admin" />
<c:set var="activePage" value="users" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<div class="container-fluid py-4 px-lg-5">
    <div class="row">
        <%@ include file="/fragments/admin-sidebar.jsp" %>

        <div class="col-lg-10 col-md-9">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="fw-bold text-dark mb-1">Customer Accounts</h3>
                    <p class="text-muted small mb-0">Inspect customer profiles, access states, and account status</p>
                </div>

                <!-- Customer Search Bar -->
                <form action="${pageContext.request.contextPath}/admin/users" method="GET" class="d-flex" style="width: 280px;">
                    <div class="input-group input-group-sm">
                        <input type="text" name="q" class="form-control" placeholder="Search by name, email..." value="<c:out value='${searchQuery}' />">
                        <button class="btn btn-outline-custom" type="submit"><i class="bi bi-search"></i></button>
                    </div>
                </form>
            </div>

            <div class="card border-0 shadow-sm rounded-4 bg-white p-4">
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>ID</th>
                                <th>Customer Name</th>
                                <th>Email</th>
                                <th>Mobile</th>
                                <th>Role</th>
                                <th>Status</th>
                                <th>Registered</th>
                                <th class="text-end">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="u" items="${users}">
                                <tr>
                                    <td class="fw-bold">#${u.userId}</td>
                                    <td class="fw-semibold text-dark"><c:out value="${u.name}" /></td>
                                    <td><c:out value="${u.email}" /></td>
                                    <td><c:out value="${u.mobile != null ? u.mobile : 'N/A'}" /></td>
                                    <td>
                                        <span class="badge ${u.admin ? 'bg-danger' : 'bg-secondary'}">
                                            ${u.role}
                                        </span>
                                    </td>
                                    <td>
                                        <span class="badge ${u.status == 'ACTIVE' ? 'bg-success-subtle text-success' : 'bg-warning-subtle text-warning'}">
                                            ${u.status}
                                        </span>
                                    </td>
                                    <td class="small text-muted"><c:out value="${u.createdAt}" /></td>
                                    <td class="text-end">
                                        <c:if test="${!u.admin}">
                                            <form action="${pageContext.request.contextPath}/admin/users/status" method="POST" class="d-inline">
                                                <input type="hidden" name="userId" value="${u.userId}">
                                                <input type="hidden" name="status" value="${u.status == 'ACTIVE' ? 'INACTIVE' : 'ACTIVE'}">
                                                <button type="submit" class="btn btn-sm ${u.status == 'ACTIVE' ? 'btn-outline-danger' : 'btn-outline-success'}">
                                                    ${u.status == 'ACTIVE' ? 'Deactivate' : 'Activate'}
                                                </button>
                                            </form>
                                        </c:if>
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
