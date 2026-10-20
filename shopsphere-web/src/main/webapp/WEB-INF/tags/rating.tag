<%@ tag description="Product Rating Star Component" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ attribute name="value" required="true" type="java.lang.Double" description="Rating value between 0.0 and 5.0" %>
<%@ attribute name="reviews" required="false" type="java.lang.Integer" description="Total review count" %>

<div class="d-flex align-items-center gap-1 text-warning small">
    <c:forEach var="i" begin="1" end="5">
        <c:choose>
            <c:when test="${value >= i}">
                <i class="bi bi-star-fill"></i>
            </c:when>
            <c:when test="${value >= (i - 0.5)}">
                <i class="bi bi-star-half"></i>
            </c:when>
            <c:otherwise>
                <i class="bi bi-star text-muted"></i>
            </c:otherwise>
        </c:choose>
    </c:forEach>
    <c:if test="${not empty reviews && reviews > 0}">
        <span class="text-muted ms-1">(${reviews})</span>
    </c:if>
</div>
