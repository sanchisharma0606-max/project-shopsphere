package com.shopsphere.model;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model representing a customer's saved wishlist item.
 */
public class WishlistItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1006L;

    private int wishlistId;
    private int userId;
    private int productId;
    private Timestamp createdAt;
    private Product product;

    public WishlistItem() {}

    public WishlistItem(int wishlistId, int userId, int productId, Timestamp createdAt) {
        this.wishlistId = wishlistId;
        this.userId = userId;
        this.productId = productId;
        this.createdAt = createdAt;
    }

    public int getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(int wishlistId) {
        this.wishlistId = wishlistId;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "WishlistItem{" +
                "wishlistId=" + wishlistId +
                ", userId=" + userId +
                ", productId=" + productId +
                '}';
    }
}
