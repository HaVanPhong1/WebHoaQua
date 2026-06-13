package com.example.demo.service.discount;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

import com.example.demo.entity.Promotion;

@Component("bulkThreshold")
public class BulkDiscountStrategy implements DiscountStrategy {

    @Override
    public BigDecimal applyDiscount(BigDecimal cartTotal, Promotion promotion) {
        if (cartTotal == null || promotion == null) return BigDecimal.ZERO;
        
        BigDecimal threshold = promotion.getThreshold();
        BigDecimal percent = promotion.getDiscountPercent();
        
        if (threshold == null || percent == null) return BigDecimal.ZERO;

        if (cartTotal.compareTo(threshold) >= 0) {
            BigDecimal pct = percent;
            if (pct.compareTo(BigDecimal.ONE) > 0) {
                pct = pct.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
            }
            return cartTotal.multiply(pct).setScale(0, RoundingMode.DOWN);
        }
        return BigDecimal.ZERO;
    }
}
