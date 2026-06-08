package com.example.demo.pattern.decorator;

import com.example.demo.entity.Product;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BaseProductComponent implements ProductComponent {
    private final Product product;

    public BaseProductComponent(Product product) {
        this.product = product;
    }

    @Override
    public Long getProductId() {
        return product.getId();
    }

    @Override
    public String getName() {
        return product.getName();
    }

    @Override
    public BigDecimal getPrice() {
        return product.getPrice();
    }

    @Override
    public List<String> getAddons() {
        return new ArrayList<>();
    }
}
