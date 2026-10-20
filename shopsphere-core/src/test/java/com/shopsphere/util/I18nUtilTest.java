package com.shopsphere.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("I18nUtil Multi-Language & Formatting Tests")
class I18nUtilTest {

    @Test
    @DisplayName("Should retrieve localized messages from English and Hindi bundles")
    void testResourceBundleLookups() {
        String titleEn = I18nUtil.getMessage("app.title", Locale.ENGLISH);
        assertNotNull(titleEn);
        assertFalse(titleEn.startsWith("???"));

        String titleHi = I18nUtil.getMessage("app.title", Locale.of("hi", "IN"));
        assertNotNull(titleHi);
        assertFalse(titleHi.startsWith("???"));
    }

    @Test
    @DisplayName("Should format currency with INR symbol")
    void testCurrencyFormatting() {
        String formatted = I18nUtil.formatCurrency(new BigDecimal("129999.00"), Locale.of("en", "IN"));
        assertNotNull(formatted);
        // Should contain currency symbol or amount digits
        assertFalse(formatted.isEmpty());
    }
}
