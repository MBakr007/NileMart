package org.route.shipping;

import java.math.BigDecimal;

/**
 * Egyptian shipping zones with their flat fee and whether the zone can ever
 * qualify for free shipping.
 */
public enum Zone {

    GREATER_CAIRO("50.00", true),
    ALEX_DELTA("65.00", true),
    CANAL("70.00", true),
    UPPER_EGYPT("85.00", true),
    REMOTE("120.00", false);

    private final BigDecimal fee;
    private final boolean freeShippingEligible;

    Zone(String fee, boolean freeShippingEligible) {
        this.fee = new BigDecimal(fee);
        this.freeShippingEligible = freeShippingEligible;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public boolean isFreeShippingEligible() {
        return freeShippingEligible;
    }
}
