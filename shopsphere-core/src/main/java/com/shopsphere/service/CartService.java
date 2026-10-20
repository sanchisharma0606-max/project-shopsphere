package com.shopsphere.service;

import com.shopsphere.dao.CartDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.Product;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Service managing user shopping cart operations and stock validation.
 */
public class CartService {

    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public CartService() {
        this.cartDAO = new CartDAO();
        this.productDAO = new ProductDAO();
    }

    public CartService(CartDAO cartDAO, ProductDAO productDAO) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    public boolean addToCart(int userId, int productId, int quantity) throws SQLException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }

        Optional<Product> prodOpt = productDAO.findById(productId);
        if (prodOpt.isEmpty() || prodOpt.get().getStatus() != Product.Status.ACTIVE) {
            throw new IllegalArgumentException("Product is unavailable.");
        }

        Product product = prodOpt.get();
        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Only " + product.getStock() + " units available in stock.");
        }

        return cartDAO.addToCart(userId, productId, quantity);
    }

    public boolean updateQuantity(int userId, int productId, int newQuantity) throws SQLException {
        if (newQuantity <= 0) {
            return cartDAO.removeFromCart(userId, productId);
        }

        Optional<Product> prodOpt = productDAO.findById(productId);
        if (prodOpt.isEmpty() || prodOpt.get().getStatus() != Product.Status.ACTIVE) {
            throw new IllegalArgumentException("Product is unavailable.");
        }

        Product product = prodOpt.get();
        if (product.getStock() < newQuantity) {
            throw new IllegalArgumentException("Requested quantity exceeds available stock (" + product.getStock() + ").");
        }

        return cartDAO.updateQuantity(userId, productId, newQuantity);
    }

    public boolean removeFromCart(int userId, int productId) throws SQLException {
        return cartDAO.removeFromCart(userId, productId);
    }

    public List<CartItem> getCart(int userId) throws SQLException {
        return cartDAO.getCartByUserId(userId);
    }

    public int getCartItemCount(int userId) throws SQLException {
        return cartDAO.getCartItemCount(userId);
    }

    public BigDecimal calculateCartTotal(List<CartItem> items) {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    public void clearCart(int userId) throws SQLException {
        cartDAO.clearCart(userId);
    }
}
