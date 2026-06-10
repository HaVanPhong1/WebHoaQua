package com.example.demo.service.discount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("holidayStrategy")
public class HolidayDiscountStrategy implements DiscountStrategy {

    private final BigDecimal percent;
    private final Integer recurringMonth;
    private final Integer recurringDay;
    private final boolean active;

    public HolidayDiscountStrategy(@Value("${discount.holiday.percent:0.15}") BigDecimal percent,
                                   @Value("${discount.holiday.recurringMonth:0}") Integer recurringMonth,
                                   @Value("${discount.holiday.recurringDay:0}") Integer recurringDay,
                                   @Value("${discount.holiday.active:false}") boolean active) {
        this.percent = percent;
        this.recurringMonth = recurringMonth != 0 ? recurringMonth : null;
        this.recurringDay = recurringDay != 0 ? recurringDay : null;
        this.active = active;
    }

    @Override
    public BigDecimal applyDiscount(BigDecimal cartTotal) {
        if (!active || cartTotal == null) return BigDecimal.ZERO;
        LocalDate now = LocalDate.now();
        boolean match = false;
        if (recurringMonth != null && recurringDay != null) {
            match = now.getMonthValue() == recurringMonth && now.getDayOfMonth() == recurringDay;
        }
        if (match) {
            return cartTotal.multiply(percent).setScale(0, RoundingMode.DOWN);
        }
        return BigDecimal.ZERO;
    }
}
