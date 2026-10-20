<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="x" uri="jakarta.tags.xml" %>

<c:set var="pageTitle" value="JSTL XML Demonstration - RTU Syllabus" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<div class="container my-5">
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb mb-4">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/academic-demos">RTU Demos</a></li>
            <li class="breadcrumb-item active" aria-current="page">JSTL XML Processing</li>
        </ol>
    </nav>

    <div class="card border-0 shadow-sm rounded-4 bg-white p-4 p-md-5 mb-4">
        <div class="d-flex align-items-center gap-3 mb-4">
            <div class="bg-info-subtle text-info rounded-3 p-3">
                <i class="bi bi-filetype-xml fs-2"></i>
            </div>
            <div>
                <h3 class="fw-bold text-dark mb-1">JSTL XML Tag Library Processing</h3>
                <p class="text-muted small mb-0">RTU Syllabus Topic: Parsing XML document streams with <code>&lt;x:parse&gt;</code> &amp; XPath</p>
            </div>
        </div>

        <p class="text-muted">
            This educational demonstration loads an XML catalog feed from <code>WEB-INF/data/products.xml</code> and parses it using standard JSTL XML tags (<code>&lt;x:parse&gt;</code>, <code>&lt;x:forEach&gt;</code>, and <code>&lt;x:out&gt;</code>).
        </p>

        <!-- Import and Parse XML document -->
        <c:import url="/WEB-INF/data/products.xml" var="xmlFeed" />
        <x:parse xml="${xmlFeed}" var="parsedDoc" />

        <div class="table-responsive my-4">
            <table class="table table-bordered align-middle">
                <thead class="table-light">
                    <tr>
                        <th>Item ID (@id)</th>
                        <th>Product Title (/name)</th>
                        <th>Category (/category)</th>
                        <th>Price (/price)</th>
                        <th>Stock (/stock)</th>
                    </tr>
                </thead>
                <tbody>
                    <x:forEach select="$parsedDoc/catalog/item">
                        <tr>
                            <td><span class="badge bg-secondary font-monospace"><x:out select="@id" /></span></td>
                            <td class="fw-bold text-dark"><x:out select="name" /></td>
                            <td><span class="badge bg-info-subtle text-info"><x:out select="category" /></span></td>
                            <td class="fw-semibold">₹<x:out select="price" /></td>
                            <td><span class="badge bg-success-subtle text-success"><x:out select="stock" /> units</span></td>
                        </tr>
                    </x:forEach>
                </tbody>
            </table>
        </div>

        <div class="bg-light p-3 rounded-3 small">
            <strong>Architecture Note:</strong> In production enterprise architectures, XML feeds from external suppliers are commonly transformed via XSLT or mapped via JAXB/DOM parsers into strongly typed DTOs within the service layer.
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>
