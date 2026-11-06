package org.route.shipping;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Chooses between the zone fee and the free-shipping calculator.
 *
 * <p>An order ships free only when both conditions hold: the subtotal reaches
 * the configured threshold (SHIP-02) and the governorate's zone is eligible.</p>
 */
@Service
public class ShippingQuoteService {

    private final ShippingCalculator zoneCalculator;
    private final ShippingCalculator freeShippingCalculator;
    private final BigDecimal freeShippingThreshold;

    // Hand-written constructor: the two ShippingCalculator beans need a qualifier.
    public ShippingQuoteService(ShippingCalculator zoneCalculator,
                                @Qualifier("freeShipping") ShippingCalculator freeShippingCalculator,
                                @Value("${store.free-shipping-threshold}") BigDecimal freeShippingThreshold) {
        this.zoneCalculator = zoneCalculator;
        this.freeShippingCalculator = freeShippingCalculator;
        this.freeShippingThreshold = freeShippingThreshold;
    }

    public BigDecimal quote(Governorate governorate, BigDecimal subtotal) {
        boolean meetsThreshold = subtotal.compareTo(freeShippingThreshold) >= 0;
        boolean zoneEligible = governorate.getZone().isFreeShippingEligible();

        BigDecimal fee = (meetsThreshold && zoneEligible)
                ? freeShippingCalculator.calculate(governorate)
                : zoneCalculator.calculate(governorate);
        return fee.setScale(2, RoundingMode.HALF_UP);
    }
}
