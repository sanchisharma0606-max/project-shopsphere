package com.shopsphere.util;

import java.io.InputStream;
import java.util.Properties;

/**
 * Manages configuration properties for database and services.
 * Prioritizes Environment Variables > System Properties > db.properties in classpath.
 */
public class DatabaseConfig {

    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/shopsphere?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";

    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;

    static {
        loadConfiguration();
    }

    public static synchronized void loadConfiguration() {
        Properties props = new Properties();
        // Check classpath resource
        try (InputStream in = DatabaseConfig.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (Exception ignored) {
        }
        // Check local working directory (overrides classpath if present)
        try {
            java.io.File localFile = new java.io.File("db.properties");
            if (localFile.exists() && localFile.isFile()) {
                try (InputStream in = new java.io.FileInputStream(localFile)) {
                    props.load(in);
                }
            }
        } catch (Exception ignored) {
        }

        // Priority 1: Environment Variables
        String envUrl = System.getenv("SHOPSPHERE_DB_URL");
        String envUser = System.getenv("SHOPSPHERE_DB_USER");
        String envPass = System.getenv("SHOPSPHERE_DB_PASSWORD");

        // Priority 2: System Properties
        String sysUrl = System.getProperty("shopsphere.db.url");
        String sysUser = System.getProperty("shopsphere.db.user");
        String sysPass = System.getProperty("shopsphere.db.password");

        // Resolve URL
        if (envUrl != null && !envUrl.trim().isEmpty()) {
            dbUrl = envUrl.trim();
        } else if (sysUrl != null && !sysUrl.trim().isEmpty()) {
            dbUrl = sysUrl.trim();
        } else {
            dbUrl = props.getProperty("db.url", DEFAULT_URL).trim();
        }

        // Resolve User
        if (envUser != null && !envUser.trim().isEmpty()) {
            dbUser = envUser.trim();
        } else if (sysUser != null && !sysUser.trim().isEmpty()) {
            dbUser = sysUser.trim();
        } else {
            dbUser = props.getProperty("db.user", DEFAULT_USER).trim();
        }

        // Resolve Password
        if (envPass != null) {
            dbPassword = envPass;
        } else if (sysPass != null) {
            dbPassword = sysPass;
        } else {
            dbPassword = props.getProperty("db.password", DEFAULT_PASSWORD);
        }
    }

    public static String getDbUrl() {
        return dbUrl;
    }

    public static String getDbUser() {
        return dbUser;
    }

    public static String getDbPassword() {
        return dbPassword;
    }

    public static void setCredentials(String url, String user, String password) {
        dbUrl = url;
        dbUser = user;
        dbPassword = password;
    }

    /**
     * Returns sanitized URL for safe logging without sensitive parameters.
     */
    public static String getSanitizedUrl() {
        if (dbUrl == null) return "null";
        int queryIdx = dbUrl.indexOf('?');
        return queryIdx > 0 ? dbUrl.substring(0, queryIdx) : dbUrl;
    }
}
