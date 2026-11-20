package org.route.service;

import org.route.model.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class StandardPricingService implements PricingService{

    @Override
    public BigDecimal getPrice(Product product, int qty) {
        return product.getPrice().multiply(BigDecimal.valueOf(qty));
    }
}
