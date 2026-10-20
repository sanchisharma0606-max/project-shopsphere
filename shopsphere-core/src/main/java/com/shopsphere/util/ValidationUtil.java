package com.shopsphere.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Utility for validating and sanitizing user inputs across Web and Desktop tiers.
 */
public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern MOBILE_PATTERN = Pattern.compile(
            "^[6-9]\\d{9}$");

    private static final Pattern PINCODE_PATTERN = Pattern.compile(
            "^[1-9]\\d{5}$");

    private ValidationUtil() {}

    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidMobile(String mobile) {
        if (mobile == null) return false;
        String clean = mobile.replaceAll("[\\s-]", "");
        return MOBILE_PATTERN.matcher(clean).matches();
    }

    public static boolean isValidPincode(String pincode) {
        if (pincode == null) return false;
        return PINCODE_PATTERN.matcher(pincode.trim()).matches();
    }

    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Sanitizes strings to prevent Cross-Site Scripting (XSS) when rendered in HTML.
     */
    public static String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#x27;");
    }

    public static int parsePositiveInt(String val, int defaultValue) {
        if (val == null || val.trim().isEmpty()) return defaultValue;
        try {
            int parsed = Integer.parseInt(val.trim());
            return parsed > 0 ? parsed : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static BigDecimal parseBigDecimal(String val, BigDecimal defaultValue) {
        if (val == null || val.trim().isEmpty()) return defaultValue;
        try {
            return new BigDecimal(val.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
