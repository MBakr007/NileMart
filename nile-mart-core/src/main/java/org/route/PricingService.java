package org.route;

import java.math.BigDecimal;

public interface PricingService {

    BigDecimal getPrice(Product product, int qty);
}
