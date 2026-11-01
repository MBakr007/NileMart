package org.route;

import java.math.BigDecimal;

public interface ShippingCalculator {
    BigDecimal calculate(Governorate governorate);
}
