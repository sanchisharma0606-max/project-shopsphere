package com.shopsphere.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

/**
 * Model representing a Product in ShopSphere.
 * Prices and monetary values use BigDecimal for precision.
 */
public class Product implements Serializable {
    @Serial
    private static final long serialVersionUID = 1004L;

    public enum Status {
        ACTIVE, INACTIVE
    }

    private int productId;
    private int categoryId;
    private String categoryName;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price = BigDecimal.ZERO;
    private BigDecimal discount = BigDecimal.ZERO; // percentage e.g. 15.00
    private int stock;
    private String imageUrl;
    private Status status = Status.ACTIVE;
    private Timestamp createdAt;

    // Computed / aggregated fields for UI presentation
    private double averageRating = 0.0;
    private int reviewCount = 0;

    public Product() {}

    public Product(int productId, int categoryId, String name, String brand, String description,
                   BigDecimal price, BigDecimal discount, int stock, String imageUrl, Status status, Timestamp createdAt) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price != null ? price : BigDecimal.ZERO;
        this.discount = discount != null ? discount : BigDecimal.ZERO;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.status = status != null ? status : Status.ACTIVE;
        this.createdAt = createdAt;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price != null ? price : BigDecimal.ZERO;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount != null ? discount : BigDecimal.ZERO;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status != null ? status : Status.ACTIVE;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    /**
     * Calculates final selling price after applying percentage discount.
     */
    public BigDecimal getDiscountedPrice() {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        if (discount == null || discount.compareTo(BigDecimal.ZERO) <= 0) {
            return price;
        }
        BigDecimal discountMultiplier = BigDecimal.ONE.subtract(
                discount.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
        return price.multiply(discountMultiplier).setScale(2, RoundingMode.HALF_UP);
    }

    public boolean isInStock() {
        return stock > 0;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", price=" + price +
                ", discount=" + discount + "%" +
                ", stock=" + stock +
                '}';
    }
}
