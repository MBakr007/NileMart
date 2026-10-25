package org.route;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class StandardPricingService implements PricingService{

    @Override
    public BigDecimal getPrice(Product product, int qty) {
        return product.getPrice().multiply(BigDecimal.valueOf(qty));
    }
}
