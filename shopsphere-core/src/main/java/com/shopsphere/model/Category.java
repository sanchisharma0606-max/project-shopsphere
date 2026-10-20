package com.shopsphere.model;

import java.io.Serial;
import java.io.Serializable;

/**
 * Model representing an E-Commerce Product Category.
 */
public class Category implements Serializable {
    @Serial
    private static final long serialVersionUID = 1003L;

    public enum Status {
        ACTIVE, INACTIVE
    }

    private int categoryId;
    private String name;
    private String description;
    private Status status = Status.ACTIVE;

    public Category() {}

    public Category(int categoryId, String name, String description, Status status) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.status = status != null ? status : Status.ACTIVE;
    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.ACTIVE;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return name;
    }
}
