package com.shopsphere.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Model representing an item in the user's shopping cart.
 */
public class CartItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1005L;

    private int cartId;
    private int userId;
    private int productId;
    private int quantity = 1;
    private Product product;

    public CartItem() {}

    public CartItem(int cartId, int userId, int productId, int quantity) {
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getSubtotal() {
        if (product == null) {
            return BigDecimal.ZERO;
        }
        return product.getDiscountedPrice().multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "cartId=" + cartId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
