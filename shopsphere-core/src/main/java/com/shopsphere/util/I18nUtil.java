package com.shopsphere.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Utility for Internationalization (I18n) and localization.
 * Demonstrates Locale, ResourceBundle, NumberFormat, and currency formatting for English and Hindi.
 */
public class I18nUtil {

    private static final String BUNDLE_BASE_NAME = "messages";
    public static final Locale LOCALE_EN = Locale.ENGLISH;
    public static final Locale LOCALE_HI = Locale.of("hi", "IN");

    private I18nUtil() {}

    public static String getMessage(String key, Locale locale) {
        if (key == null) return "";
        try {
            Locale targetLocale = locale != null ? locale : LOCALE_EN;
            ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_BASE_NAME, targetLocale);
            return bundle.getString(key);
        } catch (Exception e) {
            return "???" + key + "???";
        }
    }

    public static String formatCurrency(BigDecimal amount, Locale locale) {
        if (amount == null) return "₹0.00";
        Locale target = locale != null ? locale : LOCALE_EN;
        try {
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.of("en", "IN"));
            return nf.format(amount);
        } catch (Exception e) {
            return "₹" + amount.toPlainString();
        }
    }

    public static String formatDate(java.sql.Timestamp timestamp, Locale locale) {
        if (timestamp == null) return "";
        Locale target = locale != null ? locale : LOCALE_EN;
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(target);
        return timestamp.toLocalDateTime().format(formatter);
    }
}
