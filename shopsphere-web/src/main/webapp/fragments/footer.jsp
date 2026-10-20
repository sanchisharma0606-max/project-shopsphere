<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

</div><!-- /.main-content -->

<footer class="footer-custom">
    <div class="container">
        <div class="row g-4 mb-4">
            <div class="col-lg-4 col-md-6">
                <div class="d-flex align-items-center gap-2 mb-3">
                    <i class="bi bi-bag-check-fill text-primary fs-3"></i>
                    <span class="brand-font fs-4 text-white">ShopSphere</span>
                </div>
                <p class="text-secondary small">
                    A comprehensive Advanced Java E-Commerce platform built according to the Rajasthan Technical University (RTU) syllabus specifications.
                </p>
                <div class="d-flex gap-3 text-secondary">
                    <i class="bi bi-shield-check text-success"></i> Secure BCrypt Authentication
                    <i class="bi bi-database text-info"></i> MySQL JDBC &amp; Transactions
                </div>
            </div>

            <div class="col-lg-2 col-md-3 col-6">
                <h5>Shop</h5>
                <ul class="list-unstyled">
                    <li><a href="${pageContext.request.contextPath}/products">All Products</a></li>
                    <li><a href="${pageContext.request.contextPath}/products?category=1">Electronics</a></li>
                    <li><a href="${pageContext.request.contextPath}/products?category=2">Fashion</a></li>
                    <li><a href="${pageContext.request.contextPath}/products?category=4">Books &amp; Guides</a></li>
                </ul>
            </div>

            <div class="col-lg-2 col-md-3 col-6">
                <h5>Account</h5>
                <ul class="list-unstyled">
                    <li><a href="${pageContext.request.contextPath}/profile">My Profile</a></li>
                    <li><a href="${pageContext.request.contextPath}/orders">Order Tracking</a></li>
                    <li><a href="${pageContext.request.contextPath}/cart">Shopping Cart</a></li>
                    <li><a href="${pageContext.request.contextPath}/wishlist">Saved Wishlist</a></li>
                </ul>
            </div>

            <div class="col-lg-4 col-md-12">
                <h5>RTU Syllabus Demos</h5>
                <div class="d-flex flex-wrap gap-2">
                    <a href="${pageContext.request.contextPath}/demo/scriptlets" class="badge bg-secondary text-white text-decoration-none p-2">JSP Elements vs EL</a>
                    <a href="${pageContext.request.contextPath}/demo/jstl-xml" class="badge bg-secondary text-white text-decoration-none p-2">JSTL XML Feed</a>
                    <a href="${pageContext.request.contextPath}/demo/jstl-sql" class="badge bg-secondary text-white text-decoration-none p-2">JSTL SQL Demo</a>
                    <a href="${pageContext.request.contextPath}/demo/url-connection" class="badge bg-secondary text-white text-decoration-none p-2">URLConnection</a>
                    <a href="${pageContext.request.contextPath}/demo/jndi" class="badge bg-secondary text-white text-decoration-none p-2">JNDI Lookup</a>
                    <a href="${pageContext.request.contextPath}/demo/applets-history" class="badge bg-secondary text-white text-decoration-none p-2">Applets History</a>
                </div>
                <div class="mt-3 text-secondary small">
                    Tomcat 11 | Jakarta EE 10 | Java 21 | MySQL 8
                </div>
            </div>
        </div>

        <hr class="border-secondary my-3">
        <div class="d-flex flex-column flex-sm-row justify-content-between align-items-center text-secondary small">
            <p class="mb-0">&copy; ${applicationScope.year != null ? applicationScope.year : 2026} ShopSphere Enterprise. RTU Advanced Java Project.</p>
            <div class="d-flex gap-3 mt-2 mt-sm-0">
                <span>Designed with MVC Architecture</span>
            </div>
        </div>
    </div>
</footer>

<!-- Bootstrap 5.3 Bundle with Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<!-- ShopSphere Custom JavaScript -->
<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
</body>
</html>
