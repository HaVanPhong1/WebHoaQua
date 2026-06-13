package com.example.demo.service.discount;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.example.demo.entity.Promotion;

public class BulkDiscountStrategyTest {

    @Test
    public void testBelowThreshold() {
        BulkDiscountStrategy s = new BulkDiscountStrategy();
        Promotion p = new Promotion();
        p.setThreshold(new BigDecimal("300000"));
        p.setDiscountPercent(new BigDecimal("0.10"));
        
        BigDecimal discount = s.applyDiscount(new BigDecimal("299999"), p);
        assertEquals(BigDecimal.ZERO.setScale(0), discount);
    }

    @Test
    public void testAtThreshold() {
        BulkDiscountStrategy s = new BulkDiscountStrategy();
        Promotion p = new Promotion();
        p.setThreshold(new BigDecimal("300000"));
        p.setDiscountPercent(new BigDecimal("0.10"));
        
        BigDecimal discount = s.applyDiscount(new BigDecimal("300000"), p);
        assertEquals(new BigDecimal("30000"), discount);
    }
}
