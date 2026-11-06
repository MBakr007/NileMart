package org.route.shipping;

import java.math.BigDecimal;

public interface ShippingCalculator {

    BigDecimal calculate(Governorate governorate);
}
