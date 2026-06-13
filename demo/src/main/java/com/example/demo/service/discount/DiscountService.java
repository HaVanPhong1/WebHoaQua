package com.example.demo.service.discount;

import java.math.BigDecimal;
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
                if (p.getType() == null) continue;
                
                // Fetch the strategy by name. 
                // e.g. "bulk" -> "bulkThreshold" bean, "holiday" -> "holidayStrategy" bean
                String key = p.getType().equalsIgnoreCase("holiday") ? "holidayStrategy" : 
                            (p.getType().equalsIgnoreCase("bulk") ? "bulkThreshold" : p.getType().toLowerCase());
                
                DiscountStrategy strategy = strategies.get(key);
                
                if (strategy != null) {
                    total = total.add(strategy.applyDiscount(cartTotal, p));
                }
            } catch (Exception ignored) {
                // Ignore any strategy failures to avoid blocking the checkout
            }
        }
        
        return total;
    }
}
