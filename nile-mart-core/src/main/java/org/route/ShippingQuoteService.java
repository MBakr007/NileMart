package org.route;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ShippingQuoteService {


    private final ShippingCalculator primaryCalculator;
    private final ShippingCalculator freeShippingCalculator;
    private final BigDecimal freeShippingThreshold;

    public ShippingQuoteService(
            ShippingCalculator primaryCalculator,
            @Qualifier("freeShipping") ShippingCalculator freeShippingCalculator,
            @Value("${store.free-shipping-threshold}") BigDecimal freeShippingThreshold
    ) {
        this.primaryCalculator = primaryCalculator;
        this.freeShippingCalculator = freeShippingCalculator;
        this.freeShippingThreshold = freeShippingThreshold;
    }
    public BigDecimal quote(Governorate governorate, BigDecimal subtotal) {

        Zone zone = governorate.getZone();

        if (subtotal.compareTo(freeShippingThreshold) >= 0
                && zone.isFreeShippingEligible()) {

            return freeShippingCalculator.calculate(governorate);
        }

        return primaryCalculator.calculate(governorate);
    }
}
