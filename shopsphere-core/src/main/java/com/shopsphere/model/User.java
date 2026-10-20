package com.shopsphere.model;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model representing a registered User in ShopSphere.
 */
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1001L;

    public enum Role {
        CUSTOMER, ADMIN
    }

    public enum Status {
        ACTIVE, INACTIVE, SUSPENDED
    }

    private int userId;
    private String name;
    private String email;
    private String passwordHash;
    private String mobile;
    private Role role = Role.CUSTOMER;
    private Status status = Status.ACTIVE;
    private Timestamp createdAt;

    public User() {
    }

    public User(int userId, String name, String email, String passwordHash, String mobile, Role role, Status status, Timestamp createdAt) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.mobile = mobile;
        this.role = role != null ? role : Role.CUSTOMER;
        this.status = status != null ? status : Status.ACTIVE;
        this.createdAt = createdAt;
    }

    public User(String name, String email, String passwordHash, String mobile, Role role) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.mobile = mobile;
        this.role = role != null ? role : Role.CUSTOMER;
        this.status = Status.ACTIVE;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", status=" + status +
                '}';
    }
}
