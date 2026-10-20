package com.shopsphere.web.controller;

import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.model.Review;
import com.shopsphere.service.CategoryService;
import com.shopsphere.service.ProductService;
import com.shopsphere.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for product catalogue browsing, searching, filtering, and detail views.
 * Demonstrates HTTP Cookies for recently viewed products.
 */
@WebServlet(name = "ProductServlet", urlPatterns = {"/products", "/product-details"})
public class ProductServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ProductServlet.class.getName());
    private static final String COOKIE_RECENTLY_VIEWED = "recently_viewed";

    private ProductService productService;
    private CategoryService categoryService;

    @Override
    public void init() {
        this.productService = new ProductService();
        this.categoryService = new CategoryService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();

        if ("/product-details".equals(servletPath)) {
            handleProductDetails(req, resp);
        } else {
            handleProductCatalog(req, resp);
        }
    }

    private void handleProductCatalog(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String query = req.getParameter("q");
            String catParam = req.getParameter("category");
            String minPriceParam = req.getParameter("minPrice");
            String maxPriceParam = req.getParameter("maxPrice");
            String sortBy = req.getParameter("sort");
            String pageParam = req.getParameter("page");

            Integer categoryId = null;
            if (catParam != null && !catParam.trim().isEmpty()) {
                try {
                    categoryId = Integer.parseInt(catParam.trim());
                } catch (NumberFormatException ignored) {}
            }

            BigDecimal minPrice = null;
            if (minPriceParam != null && !minPriceParam.trim().isEmpty()) {
                minPrice = ValidationUtil.parseBigDecimal(minPriceParam, null);
            }

            BigDecimal maxPrice = null;
            if (maxPriceParam != null && !maxPriceParam.trim().isEmpty()) {
                maxPrice = ValidationUtil.parseBigDecimal(maxPriceParam, null);
            }

            int page = ValidationUtil.parsePositiveInt(pageParam, 1);
            int pageSize = 9;

            List<Product> products = productService.searchAndFilter(query, categoryId, minPrice, maxPrice, sortBy, page, pageSize);
            int totalResults = productService.countSearchResults(query, categoryId, minPrice, maxPrice);
            int totalPages = (int) Math.ceil((double) totalResults / pageSize);

            List<Category> categories = categoryService.getActiveCategories();

            req.setAttribute("products", products);
            req.setAttribute("categories", categories);
            req.setAttribute("query", query);
            req.setAttribute("selectedCategoryId", categoryId);
            req.setAttribute("minPrice", minPrice);
            req.setAttribute("maxPrice", maxPrice);
            req.setAttribute("sortBy", sortBy);
            req.setAttribute("currentPage", page);
            req.setAttribute("totalPages", Math.max(totalPages, 1));
            req.setAttribute("totalResults", totalResults);

            req.getRequestDispatcher("/products.jsp").forward(req, resp);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving products", e);
            req.setAttribute("errorMessage", "Unable to load products. Please try again.");
            req.getRequestDispatcher("/products.jsp").forward(req, resp);
        }
    }

    private void handleProductDetails(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idParam = req.getParameter("id");
        int productId = ValidationUtil.parsePositiveInt(idParam, -1);

        if (productId <= 0) {
            resp.sendRedirect(req.getContextPath() + "/products");
            return;
        }

        try {
            Optional<Product> prodOpt = productService.getProductById(productId);
            if (prodOpt.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found.");
                return;
            }

            Product product = prodOpt.get();
            List<Review> reviews = productService.getProductReviews(productId);

            // Update recently viewed cookie (store list of product IDs)
            updateRecentlyViewedCookie(req, resp, productId);

            // Fetch related products from the same category
            List<Product> related = productService.searchAndFilter(null, product.getCategoryId(), null, null, null, 1, 4);
            related.removeIf(p -> p.getProductId() == product.getProductId());

            req.setAttribute("product", product);
            req.setAttribute("reviews", reviews);
            req.setAttribute("relatedProducts", related);

            req.getRequestDispatcher("/product-details.jsp").forward(req, resp);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading product details for ID " + productId, e);
            resp.sendRedirect(req.getContextPath() + "/products");
        }
    }

    private void updateRecentlyViewedCookie(HttpServletRequest req, HttpServletResponse resp, int currentId) {
        String existing = "";
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (COOKIE_RECENTLY_VIEWED.equals(c.getName())) {
                    existing = c.getValue();
                    break;
                }
            }
        }

        LinkedHashSet<String> ids = new LinkedHashSet<>();
        ids.add(String.valueOf(currentId)); // Most recent first
        if (!existing.isEmpty()) {
            for (String part : existing.split("-")) {
                if (!part.trim().isEmpty() && !part.equals(String.valueOf(currentId))) {
                    ids.add(part.trim());
                    if (ids.size() >= 5) break; // Limit to 5
                }
            }
        }

        String newValue = String.join("-", ids);
        Cookie cookie = new Cookie(COOKIE_RECENTLY_VIEWED, newValue);
        cookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
        cookie.setHttpOnly(true);
        resp.addCookie(cookie);
    }
}
