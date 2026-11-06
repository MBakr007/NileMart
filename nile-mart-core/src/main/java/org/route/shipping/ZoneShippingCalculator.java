package org.route.shipping;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Standard calculator: returns the flat fee of the governorate's zone.
 */
@Component
@Primary
public class ZoneShippingCalculator implements ShippingCalculator {

    @Override
    public BigDecimal calculate(Governorate governorate) {
        return governorate.getZone().getFee().setScale(2, RoundingMode.HALF_UP);
    }
}
