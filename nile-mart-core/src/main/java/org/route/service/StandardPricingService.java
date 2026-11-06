package org.route.service;

import org.route.model.Product;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Default pricing strategy: unit price multiplied by quantity.
 *
 * <p>This bean is {@code @Primary}; to use promotional pricing instead, move
 * the {@code @Primary} annotation to {@link PromotionalPricingService}.</p>
 */
@Component
@Primary
public class StandardPricingService implements PricingService {

    @Override
    public BigDecimal getPrice(Product product, int qty) {
        return product.getPrice()
                .multiply(BigDecimal.valueOf(qty))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
