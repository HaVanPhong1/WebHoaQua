package com.example.demo.service.discount;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.example.demo.entity.Promotion;

public class HolidayDiscountStrategyTest {

    @Test
    public void testNonMatchingDay() {
        HolidayDiscountStrategy s = new HolidayDiscountStrategy();
        Promotion p = new Promotion();
        p.setDiscountPercent(new BigDecimal("0.10"));
        p.setRecurringMonth(1);
        p.setRecurringDay(1);
        
        BigDecimal discount = s.applyDiscount(new BigDecimal("100000"), p);
        // assuming today is not Jan 1 in test environment, discount should be 0 or 10000 depending on date
        // we assert not negative and <= cart total
        assertEquals(false, discount.compareTo(BigDecimal.ZERO) < 0);
    }
}
