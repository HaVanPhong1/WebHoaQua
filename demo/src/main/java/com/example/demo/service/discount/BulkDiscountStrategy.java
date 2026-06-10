package com.example.demo.service.discount;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("bulkThreshold")
public class BulkDiscountStrategy implements DiscountStrategy {

    private final BigDecimal threshold;
    private final BigDecimal percent;

    public BulkDiscountStrategy(@Value("${discount.bulk.threshold:300000}") BigDecimal threshold,
                                @Value("${discount.bulk.percent:0.10}") BigDecimal percent) {
        this.threshold = threshold;
        this.percent = percent;
    }

    @Override
    public BigDecimal applyDiscount(BigDecimal cartTotal) {
        if (cartTotal == null) return BigDecimal.ZERO;
        if (cartTotal.compareTo(threshold) >= 0) {
            return cartTotal.multiply(percent).setScale(0, RoundingMode.DOWN);
        }
        return BigDecimal.ZERO;
    }
}
