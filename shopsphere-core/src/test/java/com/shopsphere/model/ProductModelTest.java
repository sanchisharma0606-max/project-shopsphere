package com.shopsphere.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Product Domain Model Logic Tests")
class ProductModelTest {

    @Test
    @DisplayName("Should accurately calculate discounted prices using BigDecimal")
    void testDiscountCalculation() {
        Product product = new Product();
        product.setPrice(new BigDecimal("1000.00"));
        product.setDiscount(new BigDecimal("10.00")); // 10% off

        assertEquals(new BigDecimal("900.00"), product.getDiscountedPrice());

        // 25% discount on 2000
        product.setPrice(new BigDecimal("2000.00"));
        product.setDiscount(new BigDecimal("25.00"));
        assertEquals(new BigDecimal("1500.00"), product.getDiscountedPrice());

        // 0% discount
        product.setDiscount(BigDecimal.ZERO);
        assertEquals(new BigDecimal("2000.00"), product.getDiscountedPrice());
    }

    @Test
    @DisplayName("Should detect stock availability correctly")
    void testStockAvailability() {
        Product inStock = new Product();
        inStock.setStock(5);
        assertTrue(inStock.isInStock());

        Product outOfStock = new Product();
        outOfStock.setStock(0);
        assertFalse(outOfStock.isInStock());
    }
}
