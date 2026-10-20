package com.shopsphere.web.controller.admin;

import com.shopsphere.model.Category;
import com.shopsphere.service.CategoryService;
import com.shopsphere.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for Administrator Category CRUD operations.
 */
@WebServlet(name = "AdminCategoryServlet", urlPatterns = {"/admin/categories", "/admin/categories/add", "/admin/categories/edit", "/admin/categories/delete"})
public class AdminCategoryServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AdminCategoryServlet.class.getName());

    private CategoryService categoryService;

    @Override
    public void init() {
        this.categoryService = new CategoryService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();

        try {
            if ("/admin/categories/add".equals(servletPath)) {
                req.getRequestDispatcher("/admin/category-form.jsp").forward(req, resp);
                return;
            }

            if ("/admin/categories/edit".equals(servletPath)) {
                int id = ValidationUtil.parsePositiveInt(req.getParameter("id"), -1);
                Optional<Category> catOpt = categoryService.getCategoryById(id);
                if (catOpt.isEmpty()) {
                    resp.sendRedirect(req.getContextPath() + "/admin/categories");
                    return;
                }
                req.setAttribute("category", catOpt.get());
                req.getRequestDispatcher("/admin/category-form.jsp").forward(req, resp);
                return;
            }

            // Default list
            List<Category> categories = categoryService.getAllCategories();
            req.setAttribute("categories", categories);
            req.getRequestDispatcher("/admin/categories.jsp").forward(req, resp);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading categories", e);
            req.setAttribute("errorMessage", "Error loading category data.");
            req.getRequestDispatcher("/admin/categories.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String servletPath = req.getServletPath();

        if ("/admin/categories/delete".equals(servletPath)) {
            handleDeleteCategory(req, resp);
            return;
        }

        handleSaveCategory(req, resp);
    }

    private void handleSaveCategory(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int categoryId = ValidationUtil.parsePositiveInt(req.getParameter("categoryId"), 0);
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        String statusParam = req.getParameter("status");

        Category.Status status = Category.Status.ACTIVE;
        try {
            status = Category.Status.valueOf(statusParam);
        } catch (Exception ignored) {}

        try {
            Category category = new Category(categoryId, name != null ? name.trim() : "", description, status);
            categoryService.saveCategory(category);
            req.getSession().setAttribute("flashSuccess", "Category '" + name + "' saved successfully.");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error saving category: " + e.getMessage());
            req.getSession().setAttribute("flashError", e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/admin/categories");
    }

    private void handleDeleteCategory(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int categoryId = ValidationUtil.parsePositiveInt(req.getParameter("categoryId"), -1);

        try {
            categoryService.deleteCategory(categoryId);
            req.getSession().setAttribute("flashSuccess", "Category deleted successfully.");
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Cannot delete category #" + categoryId + ": " + e.getMessage());
            req.getSession().setAttribute("flashError", e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/admin/categories");
    }
}
