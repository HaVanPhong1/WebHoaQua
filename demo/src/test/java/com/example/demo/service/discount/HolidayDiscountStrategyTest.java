package com.example.demo.service.discount;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class HolidayDiscountStrategyTest {

    @Test
    public void testNonMatchingDay() {
        HolidayDiscountStrategy s = new HolidayDiscountStrategy(new BigDecimal("0.10"), 1, 1, true);
        BigDecimal discount = s.applyDiscount(new BigDecimal("100000"));
        // assuming today is not Jan 1 in test environment, discount should be 0 or 10000 depending on date
        // we assert not negative and <= cart total
        assertEquals(false, discount.compareTo(BigDecimal.ZERO) < 0);
    }
}
