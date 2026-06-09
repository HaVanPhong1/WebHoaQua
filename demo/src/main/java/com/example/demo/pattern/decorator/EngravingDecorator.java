package com.example.demo.pattern.decorator;

import java.math.BigDecimal;
import java.util.List;

public class EngravingDecorator extends ProductDecorator {
    public EngravingDecorator(ProductComponent wrapped) {
        super(wrapped);
    }

    @Override
    public String getName() {
        return wrapped.getName() + " (+ Khắc chữ)";
    }

    @Override
    public BigDecimal getPrice() {
        return wrapped.getPrice().add(new BigDecimal("50000"));
    }

    @Override
    public List<String> getAddons() {
        List<String> addons = wrapped.getAddons();
        addons.add("Khắc chữ nghệ thuật");
        return addons;
    }
}
