package com.shopsphere.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JDBC Connection Factory providing fresh database connections.
 * Follows best practices:
 * - No static global Connection instance (prevents connection leaks and concurrency bottlenecks).
 * - Caller is responsible for closing the Connection via try-with-resources.
 * - Driver is explicitly registered.
 */
public class DBConnection {

    private static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER_CLASS);
            LOGGER.info("MySQL JDBC Driver registered successfully: " + DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found on classpath!", e);
            throw new RuntimeException("MySQL JDBC Driver not found: " + DRIVER_CLASS, e);
        }
    }

    private DBConnection() {
        // Factory class, do not instantiate
    }

    /**
     * Obtains a fresh database connection.
     * Callers must close this connection using try-with-resources.
     *
     * @return active java.sql.Connection
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        String url = DatabaseConfig.getDbUrl();
        String user = DatabaseConfig.getDbUser();
        String password = DatabaseConfig.getDbPassword();

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            String sanitized = DatabaseConfig.getSanitizedUrl();
            LOGGER.log(Level.SEVERE, "Database connection failure to " + sanitized + " [User: " + user + "]. Error: " + e.getMessage());
            throw new SQLException("Failed to connect to ShopSphere database at " + sanitized +
                    ". Please verify MySQL is running, database 'shopsphere' exists, and credentials are correct. Detail: " + e.getMessage(), e);
        }
    }

    /**
     * Validates if database connectivity is currently operational.
     */
    public static boolean isAvailable() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed() && conn.isValid(2);
        } catch (Exception e) {
            return false;
        }
    }
}
