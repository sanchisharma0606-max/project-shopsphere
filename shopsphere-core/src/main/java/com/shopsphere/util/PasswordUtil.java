package com.shopsphere.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility for hashing and verifying user passwords securely using BCrypt.
 */
public class PasswordUtil {

    private static final int LOG_ROUNDS = 10;

    private PasswordUtil() {}

    /**
     * Hashes a plaintext password using BCrypt with salt rounds = 10.
     *
     * @param plainPassword plaintext password
     * @return 60-character BCrypt hash string
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(LOG_ROUNDS));
    }

    /**
     * Verifies a candidate plaintext password against an existing BCrypt hash.
     *
     * @param plainPassword candidate plaintext password
     * @param hashedPassword stored BCrypt hash
     * @return true if password matches, false otherwise
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null || hashedPassword.isEmpty()) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            // Malformed hash
            return false;
        }
    }
}
