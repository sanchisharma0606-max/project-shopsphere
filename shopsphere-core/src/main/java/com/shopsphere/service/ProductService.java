package com.shopsphere.service;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.dao.ReviewDAO;
import com.shopsphere.model.Product;
import com.shopsphere.model.Review;
import com.shopsphere.util.ValidationUtil;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Service managing product catalog, searching, ratings, and inventory updates.
 */
public class ProductService {

    private final ProductDAO productDAO;
    private final ReviewDAO reviewDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
        this.reviewDAO = new ReviewDAO();
    }

    public ProductService(ProductDAO productDAO, ReviewDAO reviewDAO) {
        this.productDAO = productDAO;
        this.reviewDAO = reviewDAO;
    }

    public Optional<Product> getProductById(int productId) throws SQLException {
        return productDAO.findById(productId);
    }

    public List<Product> getFeaturedProducts(int limit) throws SQLException {
        return productDAO.findFeaturedProducts(limit > 0 ? limit : 8);
    }

    public List<Product> searchAndFilter(String query, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice,
                                         String sortBy, int page, int pageSize) throws SQLException {
        int limit = pageSize > 0 ? pageSize : 12;
        int offset = (Math.max(page, 1) - 1) * limit;
        return productDAO.searchAndFilter(query, categoryId, minPrice, maxPrice, sortBy, offset, limit);
    }

    public int countSearchResults(String query, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice) throws SQLException {
        return productDAO.countSearchResults(query, categoryId, minPrice, maxPrice);
    }

    public List<Product> getAllProducts(boolean includeInactive) throws SQLException {
        return productDAO.findAll(includeInactive);
    }

    public int saveProduct(Product product) throws SQLException {
        if (!ValidationUtil.isNotEmpty(product.getName())) {
            throw new IllegalArgumentException("Product name is required.");
        }
        if (!ValidationUtil.isNotEmpty(product.getBrand())) {
            throw new IllegalArgumentException("Product brand is required.");
        }
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price must be greater than or equal to 0.");
        }
        if (product.getStock() < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
        if (product.getDiscount() == null || product.getDiscount().compareTo(BigDecimal.ZERO) < 0 ||
                product.getDiscount().compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("Discount must be between 0% and 100%.");
        }

        if (product.getProductId() > 0) {
            boolean updated = productDAO.updateProduct(product);
            return updated ? product.getProductId() : -1;
        } else {
            return productDAO.createProduct(product);
        }
    }

    public boolean deleteProduct(int productId) throws SQLException {
        return productDAO.deleteProduct(productId);
    }

    public boolean updateStock(int productId, int newStock) throws SQLException {
        if (newStock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
        return productDAO.updateStockDirect(productId, newStock);
    }

    public List<Product> getLowStockProducts(int threshold) throws SQLException {
        return productDAO.findLowStock(threshold > 0 ? threshold : 10);
    }

    public int getTotalProductCount() throws SQLException {
        return productDAO.countTotalProducts();
    }

    // Reviews
    public List<Review> getProductReviews(int productId) throws SQLException {
        return reviewDAO.findByProductId(productId);
    }

    public boolean addReview(int userId, int productId, int rating, String reviewText) throws SQLException {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        Review review = new Review(userId, productId, rating, reviewText != null ? reviewText.trim() : "");
        return reviewDAO.addReview(review) > 0;
    }
}
