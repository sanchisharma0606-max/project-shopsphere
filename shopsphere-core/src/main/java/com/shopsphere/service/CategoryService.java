package com.shopsphere.service;

import com.shopsphere.dao.CategoryDAO;
import com.shopsphere.model.Category;
import com.shopsphere.util.ValidationUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Service managing product categories.
 */
public class CategoryService {

    private final CategoryDAO categoryDAO;

    public CategoryService() {
        this.categoryDAO = new CategoryDAO();
    }

    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public List<Category> getActiveCategories() throws SQLException {
        return categoryDAO.findActiveCategories();
    }

    public List<Category> getAllCategories() throws SQLException {
        return categoryDAO.findAll();
    }

    public Optional<Category> getCategoryById(int categoryId) throws SQLException {
        return categoryDAO.findById(categoryId);
    }

    public int saveCategory(Category category) throws SQLException {
        if (!ValidationUtil.isNotEmpty(category.getName())) {
            throw new IllegalArgumentException("Category name cannot be empty.");
        }
        if (category.getCategoryId() > 0) {
            boolean updated = categoryDAO.updateCategory(category);
            return updated ? category.getCategoryId() : -1;
        } else {
            return categoryDAO.createCategory(category);
        }
    }

    public boolean deleteCategory(int categoryId) throws SQLException {
        return categoryDAO.deleteCategory(categoryId);
    }
}
