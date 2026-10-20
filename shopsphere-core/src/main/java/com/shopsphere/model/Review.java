package com.shopsphere.model;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model representing a customer product review and rating.
 */
public class Review implements Serializable {
    @Serial
    private static final long serialVersionUID = 1010L;

    private int reviewId;
    private int userId;
    private int productId;
    private int rating; // 1 to 5
    private String reviewText;
    private Timestamp createdAt;

    // Enriched user details for display
    private String userName;

    public Review() {}

    public Review(int reviewId, int userId, int productId, int rating, String reviewText, Timestamp createdAt) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.productId = productId;
        this.rating = rating;
        this.reviewText = reviewText;
        this.createdAt = createdAt;
    }

    public Review(int userId, int productId, int rating, String reviewText) {
        this.userId = userId;
        this.productId = productId;
        this.rating = rating;
        this.reviewText = reviewText;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", productId=" + productId +
                ", rating=" + rating +
                '}';
    }
}
