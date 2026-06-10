package com.example.demo.service.discount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Promotion;
import com.example.demo.repository.PromotionRepository;

@Service
public class DiscountService {

    private final Map<String, DiscountStrategy> strategies;
    private final PromotionRepository promotionRepository;

    public DiscountService(Map<String, DiscountStrategy> strategies, PromotionRepository promotionRepository) {
        this.strategies = strategies;
        this.promotionRepository = promotionRepository;
    }

    /**
     * Tính tổng tiền giảm dựa trên promotions active trong DB.
     */
    public BigDecimal calculateTotalDiscount(BigDecimal cartTotal) {
        BigDecimal total = BigDecimal.ZERO;
        for (Promotion p : promotionRepository.findByActiveTrue()) {
            try {
                if (p.getType() != null && p.getType().equalsIgnoreCase("bulk")) {
                    // Use promotion-configured threshold and percent when present
                    BigDecimal percent = p.getDiscountPercent();
                    BigDecimal threshold = p.getThreshold();
                    if (percent != null && threshold != null && cartTotal != null
                            && cartTotal.compareTo(threshold) >= 0) {
                        // Accept admin input as either 0.10 or 10 (meaning 10%).
                        BigDecimal pct = percent;
                        if (pct.compareTo(BigDecimal.ONE) > 0) {
                            pct = pct.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
                        }
                        BigDecimal discount = cartTotal.multiply(pct).setScale(0, RoundingMode.DOWN);
                        total = total.add(discount);
                    }
                } else if (p.getType() != null && p.getType().equalsIgnoreCase("holiday")) {
                    // Use Promotion fields for holiday promotions stored in DB
                    BigDecimal percent = p.getDiscountPercent();
                    if (percent != null && cartTotal != null) {
                        boolean applicable = false;
                        java.time.LocalDate now = java.time.LocalDate.now();
                        if (p.getStartDate() != null && p.getEndDate() != null) {
                            applicable = !(now.isBefore(p.getStartDate()) || now.isAfter(p.getEndDate()));
                        }
                        if (!applicable && p.getRecurringMonth() != null && p.getRecurringDay() != null) {
                            applicable = (now.getMonthValue() == p.getRecurringMonth() && now.getDayOfMonth() == p.getRecurringDay());
                        }
                        if (applicable) {
                            BigDecimal pct = percent;
                            if (pct.compareTo(BigDecimal.ONE) > 0) {
                                pct = pct.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
                            }
                            BigDecimal discount = cartTotal.multiply(pct).setScale(0, RoundingMode.DOWN);
                            total = total.add(discount);
                        }
                    }
                } else {
                    // Fallback to configured strategy beans for other cases
                    String key = p.getType() != null && p.getType().equalsIgnoreCase("holiday") ? "holidayStrategy" : "bulkThreshold";
                    DiscountStrategy s = strategies.get(key);
                    if (s != null) {
                        total = total.add(s.applyDiscount(cartTotal.multiply(BigDecimal.ONE)));
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return total;
    }
}
