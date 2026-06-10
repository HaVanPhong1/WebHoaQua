package com.example.demo.service.discount;

import java.math.BigDecimal;

public interface DiscountStrategy {
    /**
     * Trả về số tiền giảm cho tổng `cartTotal` (không phải phần trăm).
     */
    BigDecimal applyDiscount(BigDecimal cartTotal);
}
