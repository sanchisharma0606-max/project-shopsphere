package com.shopsphere.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("CartItem Subtotal Logic Tests")
class CartItemTest {

    @Test
    @DisplayName("Should accurately calculate item subtotal based on discounted unit price")
    void testCartItemSubtotal() {
        Product product = new Product();
        product.setPrice(new BigDecimal("500.00"));
        product.setDiscount(new BigDecimal("10.00")); // discounted unit price = 450.00

        CartItem item = new CartItem(1, 10, 101, 3);
        item.setProduct(product);

        // 3 units * 450.00 = 1350.00
        assertEquals(new BigDecimal("1350.00"), item.getSubtotal());
    }
}
