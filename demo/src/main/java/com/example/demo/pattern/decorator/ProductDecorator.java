package com.example.demo.pattern.decorator;

import java.math.BigDecimal;
import java.util.List;

public abstract class ProductDecorator implements ProductComponent {
    protected final ProductComponent wrapped;

    public ProductDecorator(ProductComponent wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public Long getProductId() {
        return wrapped.getProductId();
    }

    @Override
    public String getName() {
        return wrapped.getName();
    }

    @Override
    public BigDecimal getPrice() {
        return wrapped.getPrice();
    }

    @Override
    public List<String> getAddons() {
        return wrapped.getAddons();
    }
}
