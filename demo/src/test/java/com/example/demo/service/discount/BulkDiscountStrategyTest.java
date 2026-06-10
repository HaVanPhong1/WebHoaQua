package com.example.demo.service.discount;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class BulkDiscountStrategyTest {

    @Test
    public void testBelowThreshold() {
        BulkDiscountStrategy s = new BulkDiscountStrategy(new BigDecimal("300000"), new BigDecimal("0.10"));
        BigDecimal discount = s.applyDiscount(new BigDecimal("299999"));
        assertEquals(BigDecimal.ZERO.setScale(0), discount);
    }

    @Test
    public void testAtThreshold() {
        BulkDiscountStrategy s = new BulkDiscountStrategy(new BigDecimal("300000"), new BigDecimal("0.10"));
        BigDecimal discount = s.applyDiscount(new BigDecimal("300000"));
        assertEquals(new BigDecimal("30000"), discount);
    }
}
