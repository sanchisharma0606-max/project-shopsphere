<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="My Profile - ShopSphere" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<div class="container my-4">
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb mb-4">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home">Home</a></li>
            <li class="breadcrumb-item active" aria-current="page">Account Profile</li>
        </ol>
    </nav>

    <div class="row g-4">
        <!-- Left: Personal Information & Password -->
        <div class="col-lg-5">
            <!-- Profile Info Card -->
            <div class="card border-0 shadow-sm rounded-4 bg-white p-4 mb-4">
                <div class="d-flex align-items-center gap-3 mb-4">
                    <div class="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center fs-3 fw-bold" style="width: 64px; height: 64px;">
                        ${fn:toUpperCase(fn:substring(sessionScope.user.name, 0, 1))}
                    </div>
                    <div>
                        <h5 class="fw-bold text-dark mb-0"><c:out value="${sessionScope.user.name}" /></h5>
                        <small class="text-muted"><c:out value="${sessionScope.user.email}" /></small>
                        <div><span class="badge bg-primary-subtle text-primary mt-1">${sessionScope.user.role}</span></div>
                    </div>
                </div>

                <form action="${pageContext.request.contextPath}/profile/update" method="POST">
                    <div class="mb-3">
                        <label class="form-label small fw-semibold text-dark">Full Name</label>
                        <input type="text" name="name" class="form-control" value="<c:out value='${sessionScope.user.name}' />" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-semibold text-dark">Mobile Number</label>
                        <input type="text" name="mobile" class="form-control" value="<c:out value='${sessionScope.user.mobile}' />" placeholder="10-digit mobile">
                    </div>
                    <button type="submit" class="btn btn-primary-custom btn-sm w-100">Update Profile</button>
                </form>
            </div>

            <!-- Change Password Card -->
            <div class="card border-0 shadow-sm rounded-4 bg-white p-4">
                <h6 class="fw-bold text-dark mb-3"><i class="bi bi-key me-1"></i> Change Password</h6>
                <form action="${pageContext.request.contextPath}/profile/change-password" method="POST">
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Current Password</label>
                        <input type="password" name="currentPassword" class="form-control form-control-sm" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">New Password</label>
                        <input type="password" name="newPassword" class="form-control form-control-sm" required minlength="6">
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Confirm New Password</label>
                        <input type="password" name="confirmPassword" class="form-control form-control-sm" required minlength="6">
                    </div>
                    <button type="submit" class="btn btn-outline-custom btn-sm w-100">Update Password</button>
                </form>
            </div>
        </div>

        <!-- Right: Address Book -->
        <div class="col-lg-7">
            <div class="card border-0 shadow-sm rounded-4 bg-white p-4">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h5 class="fw-bold text-dark mb-0"><i class="bi bi-geo-alt-fill text-primary me-2"></i>Saved Delivery Addresses</h5>
                    <button class="btn btn-primary-custom btn-sm" type="button" data-bs-toggle="collapse" data-bs-target="#profileAddressForm">
                        + Add Address
                    </button>
                </div>

                <!-- Add Address Collapse -->
                <div class="collapse mb-4" id="profileAddressForm">
                    <div class="card card-body bg-light border-0 rounded-3">
                        <h6 class="fw-bold mb-3">Add Delivery Address</h6>
                        <form action="${pageContext.request.contextPath}/addresses/add" method="POST">
                            <div class="mb-3">
                                <label class="form-label small fw-semibold">Street / Flat / Locality</label>
                                <input type="text" name="addressLine" class="form-control form-control-sm" required>
                            </div>
                            <div class="row g-2 mb-3">
                                <div class="col-md-4">
                                    <label class="form-label small fw-semibold">City</label>
                                    <input type="text" name="city" class="form-control form-control-sm" required>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label small fw-semibold">State</label>
                                    <input type="text" name="state" class="form-control form-control-sm" required>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label small fw-semibold">Pincode</label>
                                    <input type="text" name="pincode" class="form-control form-control-sm" pattern="[0-9]{6}" required>
                                </div>
                            </div>
                            <div class="mb-3">
                                <label class="form-label small fw-semibold">Address Type</label>
                                <select name="addressType" class="form-select form-select-sm">
                                    <option value="HOME">Home</option>
                                    <option value="WORK">Work / Office</option>
                                    <option value="OTHER">Other</option>
                                </select>
                            </div>
                            <button type="submit" class="btn btn-primary-custom btn-sm">Save Address</button>
                        </form>
                    </div>
                </div>

                <!-- Addresses List -->
                <c:choose>
                    <c:when test="${not empty addresses}">
                        <div class="d-flex flex-column gap-3">
                            <c:forEach var="addr" items="${addresses}">
                                <div class="border rounded-3 p-3 d-flex justify-content-between align-items-center">
                                    <div>
                                        <span class="badge bg-secondary-subtle text-secondary mb-1">${addr.addressType}</span>
                                        <div class="fw-semibold text-dark"><c:out value="${addr.addressLine}" /></div>
                                        <small class="text-muted"><c:out value="${addr.city}" />, <c:out value="${addr.state}" /> - <c:out value="${addr.pincode}" /></small>
                                    </div>
                                    <form action="${pageContext.request.contextPath}/addresses/delete" method="POST" onsubmit="return confirm('Delete this address?');">
                                        <input type="hidden" name="addressId" value="${addr.addressId}">
                                        <button type="submit" class="btn btn-outline-danger btn-sm" title="Delete">
                                            <i class="bi bi-trash"></i>
                                        </button>
                                    </form>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <p class="text-muted">No saved addresses yet. Add your primary shipping address above.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<%@ include file="/fragments/footer.jsp" %>
