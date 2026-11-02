package org.route;

import java.math.BigDecimal;
import java.util.Map;

public record Order(String orderNumber, Customer customer, Governorate governorate,
                    Map<String,Integer> lines, BigDecimal subtotal, BigDecimal vat, BigDecimal shippingFee, BigDecimal total) {
}
