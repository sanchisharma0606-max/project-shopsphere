<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Error - ShopSphere" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<div class="container my-5 text-center">
    <div class="card border-0 shadow-sm rounded-4 bg-white p-5 mx-auto" style="max-width: 600px;">
        <div class="mx-auto bg-danger-subtle text-danger rounded-circle d-flex align-items-center justify-content-center mb-4" style="width: 80px; height: 80px;">
            <i class="bi bi-exclamation-triangle-fill display-4"></i>
        </div>

        <h2 class="fw-bold text-dark mb-2">Notice</h2>
        <p class="text-muted mb-4">
            <c:choose>
                <c:when test="${pageContext.errorData.statusCode == 404}">
                    The requested page or resource could not be found.
                </c:when>
                <c:when test="${pageContext.errorData.statusCode == 403}">
                    Access is restricted. Administrator privileges are required.
                </c:when>
                <c:otherwise>
                    An unexpected application error occurred while processing your request. Please try again or return to the homepage.
                </c:otherwise>
            </c:choose>
        </p>

        <div class="d-flex justify-content-center gap-3">
            <a href="${pageContext.request.contextPath}/home" class="btn btn-primary-custom px-4">
                <i class="bi bi-house me-1"></i> Return Home
            </a>
            <a href="javascript:history.back()" class="btn btn-outline-custom px-4">
                Go Back
            </a>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>
