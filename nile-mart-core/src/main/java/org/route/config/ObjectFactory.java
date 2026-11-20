package org.route.config;

import org.route.repository.InMemoryProductRepository;
import org.route.service.ProductService;
import org.route.service.PromotionalPricingService;
import org.route.service.StandardPricingService;

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
