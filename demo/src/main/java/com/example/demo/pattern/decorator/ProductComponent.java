package com.example.demo.pattern.decorator;

import java.math.BigDecimal;
import java.util.List;

public interface ProductComponent {
    Long getProductId();
    String getName();
    BigDecimal getPrice();
    List<String> getAddons();
}
