package com.shopsphere.web.controller;

import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.service.CategoryService;
import com.shopsphere.service.ProductService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the ShopSphere storefront homepage.
 * Demonstrates Servlet lifecycle methods (init, doGet, destroy) and ServletConfig/ServletContext.
 */
@WebServlet(name = "HomeServlet", urlPatterns = {"", "/home"})
public class HomeServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(HomeServlet.class.getName());

    private ProductService productService;
    private CategoryService categoryService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.productService = new ProductService();
        this.categoryService = new CategoryService();
        LOGGER.info("HomeServlet initialized with ServletConfig: " + config.getServletName());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Load active categories for navigation and featured section
            List<Category> categories = categoryService.getActiveCategories();
            req.setAttribute("categories", categories);

            // Load featured and trending products
            List<Product> featuredProducts = productService.getFeaturedProducts(8);
            req.setAttribute("featuredProducts", featuredProducts);

            // Forward to index.jsp view
            req.getRequestDispatcher("/index.jsp").forward(req, resp);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to load homepage data", e);
            req.setAttribute("errorMessage", "Database connection is currently unavailable. Please verify MySQL service.");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
    }

    @Override
    public void destroy() {
        LOGGER.info("HomeServlet destroyed.");
        super.destroy();
    }
}
