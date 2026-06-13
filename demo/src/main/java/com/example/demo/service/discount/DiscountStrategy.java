package com.example.demo.service.discount;

import java.math.BigDecimal;
import com.example.demo.entity.Promotion;

public interface DiscountStrategy {
    /**
     * Trả về số tiền giảm cho tổng `cartTotal` (không phải phần trăm).
     */
    BigDecimal applyDiscount(BigDecimal cartTotal, Promotion promotion);
}
