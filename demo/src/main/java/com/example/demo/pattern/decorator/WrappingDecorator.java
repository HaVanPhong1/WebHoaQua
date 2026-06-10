package com.example.demo.pattern.decorator;

import java.math.BigDecimal;
import java.util.List;

public class WrappingDecorator extends ProductDecorator {
    public WrappingDecorator(ProductComponent wrapped) {
        super(wrapped);
    }

    @Override
    public String getName() {
        return wrapped.getName() + " (+ Bọc lụa)";
    }

    @Override
    public BigDecimal getPrice() {
        return wrapped.getPrice().add(new BigDecimal("15000"));
    }

    @Override
    public List<String> getAddons() {
        List<String> addons = wrapped.getAddons();
        addons.add("Bọc xốp lụa cao cấp");
        return addons;
    }
}
