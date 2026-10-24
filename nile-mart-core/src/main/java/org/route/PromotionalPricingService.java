package org.route;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Primary
public class PromotionalPricingService implements PricingService{

    private final BigDecimal discountAmount;

    public PromotionalPricingService(@Value("${store.vat-rate}") BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Override
    public BigDecimal getPrice(Product product) {
        return product.getPrice().multiply(discountAmount);
    }
}
