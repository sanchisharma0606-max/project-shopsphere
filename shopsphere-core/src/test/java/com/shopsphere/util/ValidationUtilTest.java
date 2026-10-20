package com.shopsphere.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ValidationUtil Input Validation & Sanitization Tests")
class ValidationUtilTest {

    @Test
    @DisplayName("Should validate correct and invalid email formats")
    void testEmailValidation() {
        assertTrue(ValidationUtil.isValidEmail("admin@shopsphere.com"));
        assertTrue(ValidationUtil.isValidEmail("student.rtu@university.ac.in"));
        assertFalse(ValidationUtil.isValidEmail("invalid-email"));
        assertFalse(ValidationUtil.isValidEmail("missing@domain"));
        assertFalse(ValidationUtil.isValidEmail(null));
    }

    @Test
    @DisplayName("Should validate 10-digit Indian mobile numbers")
    void testMobileValidation() {
        assertTrue(ValidationUtil.isValidMobile("9876543210"));
        assertTrue(ValidationUtil.isValidMobile("7012345678"));
        assertFalse(ValidationUtil.isValidMobile("12345"));
        assertFalse(ValidationUtil.isValidMobile("abcdefghij"));
        assertFalse(ValidationUtil.isValidMobile(null));
    }

    @Test
    @DisplayName("Should validate 6-digit postal pincodes")
    void testPincodeValidation() {
        assertTrue(ValidationUtil.isValidPincode("302001"));
        assertTrue(ValidationUtil.isValidPincode("110001"));
        assertFalse(ValidationUtil.isValidPincode("30200")); // 5 digits
        assertFalse(ValidationUtil.isValidPincode("012345")); // leading 0
        assertFalse(ValidationUtil.isValidPincode(null));
    }

    @Test
    @DisplayName("Should sanitize HTML to prevent Cross-Site Scripting (XSS)")
    void testHtmlEscaping() {
        String dirty = "<script>alert('xss');</script>&\"'";
        String clean = ValidationUtil.escapeHtml(dirty);

        assertFalse(clean.contains("<script>"));
        assertTrue(clean.contains("&lt;script&gt;"));
        assertTrue(clean.contains("&quot;"));
        assertTrue(clean.contains("&#x27;"));
    }

    @Test
    @DisplayName("Should safely parse numeric parameters with fallback defaults")
    void testSafeNumericParsing() {
        assertEquals(5, ValidationUtil.parsePositiveInt("5", 1));
        assertEquals(1, ValidationUtil.parsePositiveInt("-3", 1));
        assertEquals(1, ValidationUtil.parsePositiveInt("abc", 1));
        assertEquals(1, ValidationUtil.parsePositiveInt(null, 1));

        assertEquals(new BigDecimal("199.99"), ValidationUtil.parseBigDecimal("199.99", BigDecimal.ZERO));
        assertEquals(BigDecimal.ZERO, ValidationUtil.parseBigDecimal("invalid", BigDecimal.ZERO));
    }
}
