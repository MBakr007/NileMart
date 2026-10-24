package org.route;

import java.math.BigDecimal;

public class ObjectFactory {

    public static ProductService createProductServiceObject() {
        return new ProductService(new InMemoryProductRepository(),
                new StandardPricingService());
    }
    public static ProductService createProductServiceObjectWithPromotionalPrice() {
        return new ProductService(new InMemoryProductRepository(),
                new PromotionalPricingService(BigDecimal.valueOf(10)));
    }
}
