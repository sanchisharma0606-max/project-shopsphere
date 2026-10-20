package com.shopsphere.web.controller.admin;

import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.service.CategoryService;
import com.shopsphere.service.ProductService;
import com.shopsphere.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for Administrator Product CRUD operations.
 */
@WebServlet(name = "AdminProductServlet", urlPatterns = {"/admin/products", "/admin/products/add", "/admin/products/edit", "/admin/products/delete"})
public class AdminProductServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AdminProductServlet.class.getName());

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

        try {
            if ("/admin/products/add".equals(servletPath)) {
                List<Category> categories = categoryService.getActiveCategories();
                req.setAttribute("categories", categories);
                req.getRequestDispatcher("/admin/product-form.jsp").forward(req, resp);
                return;
            }

            if ("/admin/products/edit".equals(servletPath)) {
                int id = ValidationUtil.parsePositiveInt(req.getParameter("id"), -1);
                Optional<Product> prodOpt = productService.getProductById(id);
                if (prodOpt.isEmpty()) {
                    resp.sendRedirect(req.getContextPath() + "/admin/products");
                    return;
                }
                List<Category> categories = categoryService.getActiveCategories();
                req.setAttribute("product", prodOpt.get());
                req.setAttribute("categories", categories);
                req.getRequestDispatcher("/admin/product-form.jsp").forward(req, resp);
                return;
            }

            // Default: List all products
            List<Product> products = productService.getAllProducts(true);
            req.setAttribute("products", products);
            req.getRequestDispatcher("/admin/products.jsp").forward(req, resp);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading admin products", e);
            req.setAttribute("errorMessage", "Error loading products.");
            req.getRequestDispatcher("/admin/products.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String servletPath = req.getServletPath();

        if ("/admin/products/delete".equals(servletPath)) {
            handleDeleteProduct(req, resp);
            return;
        }

        handleSaveProduct(req, resp);
    }

    private void handleSaveProduct(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int productId = ValidationUtil.parsePositiveInt(req.getParameter("productId"), 0);
        int categoryId = ValidationUtil.parsePositiveInt(req.getParameter("categoryId"), -1);
        String name = req.getParameter("name");
        String brand = req.getParameter("brand");
        String description = req.getParameter("description");
        BigDecimal price = ValidationUtil.parseBigDecimal(req.getParameter("price"), BigDecimal.ZERO);
        BigDecimal discount = ValidationUtil.parseBigDecimal(req.getParameter("discount"), BigDecimal.ZERO);
        int stock = ValidationUtil.parsePositiveInt(req.getParameter("stock"), 0);
        String imageUrl = req.getParameter("imageUrl");
        String statusParam = req.getParameter("status");

        Product.Status status = Product.Status.ACTIVE;
        try {
            status = Product.Status.valueOf(statusParam);
        } catch (Exception ignored) {}

        try {
            Product product = new Product(productId, categoryId, name, brand, description, price, discount, stock, imageUrl, status, null);
            productService.saveProduct(product);
            req.getSession().setAttribute("flashSuccess", "Product '" + name + "' saved successfully.");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error saving product: " + e.getMessage());
            req.getSession().setAttribute("flashError", e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/admin/products");
    }

    private void handleDeleteProduct(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int productId = ValidationUtil.parsePositiveInt(req.getParameter("productId"), -1);

        try {
            productService.deleteProduct(productId);
            req.getSession().setAttribute("flashSuccess", "Product deleted successfully.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting product #" + productId, e);
            req.getSession().setAttribute("flashError", "Failed to delete product. It may be linked to previous orders.");
        }

        resp.sendRedirect(req.getContextPath() + "/admin/products");
    }
}
