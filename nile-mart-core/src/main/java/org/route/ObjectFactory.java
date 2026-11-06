package org.route;

import org.route.repository.InMemoryProductRepository;
import org.route.service.ProductService;
import org.route.service.PromotionalPricingService;
import org.route.service.StandardPricingService;
import org.route.shipping.FreeShippingCalculator;
import org.route.shipping.ShippingQuoteService;
import org.route.shipping.ZoneShippingCalculator;

import java.math.BigDecimal;

/**
 * Plain-Java wiring used by the Session 1 factory exercise. No Spring context
 * is involved, so the repository seed method is called explicitly.
 */
public final class ObjectFactory {

    private static final BigDecimal FREE_SHIPPING_THRESHOLD = new BigDecimal("5000.00");

    private ObjectFactory() {
    }

    public static ProductService createProductServiceObject() {
        return new ProductService(preparedRepository(), new StandardPricingService());
    }

    public static ProductService createProductServiceObjectWithPromotionalPrice() {
        return new ProductService(preparedRepository(),
                new PromotionalPricingService(new BigDecimal("20")));
    }

    public static ShippingQuoteService createShippingQuoteService() {
        return new ShippingQuoteService(
                new ZoneShippingCalculator(),
                new FreeShippingCalculator(),
                FREE_SHIPPING_THRESHOLD);
    }

    private static InMemoryProductRepository preparedRepository() {
        InMemoryProductRepository repository = new InMemoryProductRepository();
        repository.seedProducts();
        return repository;
    }
}
