package com.example.demo.pattern.decorator;

import java.math.BigDecimal;
import java.util.List;

public class PeelingDecorator extends ProductDecorator {
    public PeelingDecorator(ProductComponent wrapped) {
        super(wrapped);
    }

    @Override
    public String getName() {
        return wrapped.getName() + " (+ Cắt gọt)";
    }

    @Override
    public BigDecimal getPrice() {
        return wrapped.getPrice().add(new BigDecimal("20000"));
    }

    @Override
    public List<String> getAddons() {
        List<String> addons = wrapped.getAddons();
        addons.add("Cắt gọt sẵn đóng hộp");
        return addons;
    }
}
