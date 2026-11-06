package org.route.model;

import org.route.shipping.Governorate;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Immutable snapshot of a placed order.
 */
public record Order(String orderNumber,
                    Customer customer,
                    Governorate governorate,
                    Map<String, Integer> lines,
                    BigDecimal subtotal,
                    BigDecimal vat,
                    BigDecimal shippingFee,
                    BigDecimal total) {

    public Order {
        lines = Collections.unmodifiableMap(new LinkedHashMap<>(lines));
    }
}
