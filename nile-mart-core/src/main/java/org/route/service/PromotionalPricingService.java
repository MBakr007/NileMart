package org.route.service;

import org.route.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Percentage discount pricing strategy. The discount rate is read from
 * {@code store.discount-amount} (for example {@code 20} means 20% off).
 */
@Component
public class PromotionalPricingService implements PricingService {

    private final BigDecimal discountPercent;

    public PromotionalPricingService(@Value("${store.discount-amount}") BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    @Override
    public BigDecimal getPrice(Product product, int qty) {
        BigDecimal gross = product.getPrice()
                .multiply(BigDecimal.valueOf(qty))
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal discount = gross
                .multiply(discountPercent)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return gross.subtract(discount).setScale(2, RoundingMode.HALF_UP);
    }
}
