package org.route.shipping;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Calculator used when an order qualifies for free shipping.
 */
@Component("freeShipping")
public class FreeShippingCalculator implements ShippingCalculator {

    @Override
    public BigDecimal calculate(Governorate governorate) {
        return BigDecimal.ZERO.setScale(2);
    }
}
