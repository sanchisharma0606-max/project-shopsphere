package com.shopsphere.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PasswordUtil & BCrypt Unit Tests")
class PasswordUtilTest {

    @Test
    @DisplayName("Should generate valid BCrypt hash and verify matching password")
    void testHashAndVerifyPassword() {
        String plain = "SecretPassword@123";
        String hash = PasswordUtil.hashPassword(plain);

        assertNotNull(hash);
        assertTrue(hash.startsWith("$2a$") || hash.startsWith("$2b$"));
        assertTrue(PasswordUtil.checkPassword(plain, hash));
        assertTrue(PasswordUtil.checkPassword("Password@123", "$2a$10$p5exMtzCmRU6ydGlNfckw..aS0TqSWDwiflCcEW5cjihaKgVMyNAm"));
    }

    @Test
    @DisplayName("Should reject incorrect password against hash")
    void testRejectIncorrectPassword() {
        String plain = "SecretPassword@123";
        String hash = PasswordUtil.hashPassword(plain);

        assertFalse(PasswordUtil.checkPassword("WrongPassword", hash));
    }

    @Test
    @DisplayName("Should handle null and empty inputs safely")
    void testNullAndEmptyInputs() {
        assertThrows(IllegalArgumentException.class, () -> PasswordUtil.hashPassword(null));
        assertThrows(IllegalArgumentException.class, () -> PasswordUtil.hashPassword(""));
        assertFalse(PasswordUtil.checkPassword(null, "someHash"));
        assertFalse(PasswordUtil.checkPassword("password", null));
        assertFalse(PasswordUtil.checkPassword("password", "invalidHashFormat"));
    }
}
