package com.shopsphere.service;

import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.User;
import com.shopsphere.util.PasswordUtil;
import com.shopsphere.util.ValidationUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Service managing user registration, authentication, and profile workflows.
 */
public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Registers a new customer account.
     *
     * @param name customer name
     * @param email customer email
     * @param plainPassword plaintext password
     * @param mobile customer mobile
     * @return created User
     * @throws IllegalArgumentException on validation error or duplicate email
     * @throws SQLException on database error
     */
    public User registerUser(String name, String email, String plainPassword, String mobile) throws SQLException {
        if (!ValidationUtil.isNotEmpty(name)) {
            throw new IllegalArgumentException("Full name is required.");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("A valid email address is required.");
        }
        if (plainPassword == null || plainPassword.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long.");
        }
        if (mobile != null && !mobile.trim().isEmpty() && !ValidationUtil.isValidMobile(mobile)) {
            throw new IllegalArgumentException("Please provide a valid 10-digit mobile number.");
        }

        if (userDAO.emailExists(email)) {
            throw new IllegalArgumentException("An account with this email address already exists.");
        }

        String hashedPassword = PasswordUtil.hashPassword(plainPassword);
        User user = new User(name.trim(), email.trim().toLowerCase(), hashedPassword,
                mobile != null ? mobile.trim() : null, User.Role.CUSTOMER);

        int generatedId = userDAO.createUser(user);
        user.setUserId(generatedId);
        return user;
    }

    /**
     * Authenticates a user using email and password.
     *
     * @param email user email
     * @param plainPassword plaintext password
     * @return Optional of authenticated User
     * @throws SQLException on database error
     */
    public Optional<User> authenticate(String email, String plainPassword) throws SQLException {
        if (!ValidationUtil.isValidEmail(email) || plainPassword == null || plainPassword.isEmpty()) {
            return Optional.empty();
        }

        Optional<User> userOpt = userDAO.findByEmail(email);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        User user = userOpt.get();
        if (user.getStatus() != User.Status.ACTIVE) {
            return Optional.empty();
        }

        if (PasswordUtil.checkPassword(plainPassword, user.getPasswordHash())) {
            return Optional.of(user);
        }

        return Optional.empty();
    }

    public Optional<User> getUserById(int userId) throws SQLException {
        return userDAO.findById(userId);
    }

    public boolean updateProfile(int userId, String name, String mobile) throws SQLException {
        if (!ValidationUtil.isNotEmpty(name)) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (mobile != null && !mobile.trim().isEmpty() && !ValidationUtil.isValidMobile(mobile)) {
            throw new IllegalArgumentException("Invalid mobile number.");
        }
        return userDAO.updateProfile(userId, name.trim(), mobile != null ? mobile.trim() : null);
    }

    public boolean changePassword(int userId, String oldPassword, String newPassword) throws SQLException {
        Optional<User> userOpt = userDAO.findById(userId);
        if (userOpt.isEmpty()) {
            return false;
        }
        User user = userOpt.get();
        if (!PasswordUtil.checkPassword(oldPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }
        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters.");
        }

        String newHash = PasswordUtil.hashPassword(newPassword);
        return userDAO.updatePassword(userId, newHash);
    }

    public List<User> getAllUsers() throws SQLException {
        return userDAO.findAll();
    }

    public List<User> searchUsers(String query) throws SQLException {
        if (query == null || query.trim().isEmpty()) {
            return userDAO.findAll();
        }
        return userDAO.searchUsers(query);
    }

    public boolean toggleUserStatus(int userId, User.Status newStatus) throws SQLException {
        return userDAO.updateStatus(userId, newStatus);
    }

    public int getTotalCustomerCount() throws SQLException {
        return userDAO.countTotalCustomers();
    }
}
