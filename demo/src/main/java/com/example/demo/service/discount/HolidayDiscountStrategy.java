package com.example.demo.service.discount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.example.demo.entity.Promotion;

@Component("holidayStrategy")
public class HolidayDiscountStrategy implements DiscountStrategy {

    @Override
    public BigDecimal applyDiscount(BigDecimal cartTotal, Promotion promotion) {
        if (cartTotal == null || promotion == null || promotion.getDiscountPercent() == null) {
            return BigDecimal.ZERO;
        }

        boolean applicable = false;
        LocalDate now = LocalDate.now();

        if (promotion.getStartDate() != null && promotion.getEndDate() != null) {
            applicable = !(now.isBefore(promotion.getStartDate()) || now.isAfter(promotion.getEndDate()));
        }
        
        if (!applicable && promotion.getRecurringMonth() != null && promotion.getRecurringDay() != null) {
            applicable = (now.getMonthValue() == promotion.getRecurringMonth() && now.getDayOfMonth() == promotion.getRecurringDay());
        }

        if (applicable) {
            BigDecimal pct = promotion.getDiscountPercent();
            if (pct.compareTo(BigDecimal.ONE) > 0) {
                pct = pct.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
            }
            return cartTotal.multiply(pct).setScale(0, RoundingMode.DOWN);
        }
        return BigDecimal.ZERO;
    }
}
