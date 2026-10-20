package com.shopsphere.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Coupon Model Eligibility & Discount Logic Tests")
class CouponModelTest {

    @Test
    @DisplayName("Should validate percentage discount with maximum cap")
    void testPercentageCouponWithCap() {
        Coupon coupon = new Coupon();
        coupon.setCode("SUMMER20");
        coupon.setDiscountType(Coupon.DiscountType.PERCENTAGE);
        coupon.setDiscountValue(new BigDecimal("20.00")); // 20%
        coupon.setMinimumOrder(new BigDecimal("1000.00"));
        coupon.setMaximumDiscount(new BigDecimal("300.00")); // Cap at 300
        coupon.setStatus(Coupon.Status.ACTIVE);
        coupon.setExpiryDate(Date.valueOf(LocalDate.now().plusDays(10)));

        // Order total 2000 -> 20% is 400 -> capped at 300
        BigDecimal discount = coupon.calculateDiscount(new BigDecimal("2000.00"));
        assertEquals(new BigDecimal("300.00"), discount);

        // Order total 1200 -> 20% is 240 -> not capped
        BigDecimal discount2 = coupon.calculateDiscount(new BigDecimal("1200.00"));
        assertEquals(new BigDecimal("240.00"), discount2);
    }

    @Test
    @DisplayName("Should reject order below minimum order requirement")
    void testMinimumOrderEnforcement() {
        Coupon coupon = new Coupon();
        coupon.setMinimumOrder(new BigDecimal("1500.00"));
        coupon.setStatus(Coupon.Status.ACTIVE);
        coupon.setExpiryDate(Date.valueOf(LocalDate.now().plusDays(5)));

        assertFalse(coupon.isValidForTotal(new BigDecimal("1000.00")));
        assertTrue(coupon.isValidForTotal(new BigDecimal("1500.00")));
        assertEquals(BigDecimal.ZERO, coupon.calculateDiscount(new BigDecimal("800.00")));
    }

    @Test
    @DisplayName("Should detect expired coupon correctly")
    void testExpiredCoupon() {
        Coupon expired = new Coupon();
        expired.setStatus(Coupon.Status.ACTIVE);
        expired.setExpiryDate(Date.valueOf(LocalDate.now().minusDays(1)));

        assertTrue(expired.isExpired());
        assertFalse(expired.isValidForTotal(new BigDecimal("5000.00")));
    }
}
