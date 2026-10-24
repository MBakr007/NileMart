package org.route;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Component
public class ProductService {

    private final ProductRepository repository;
    private final PricingService pricingService;

    public ProductService(ProductRepository repository,
                          PricingService pricingService) {
        this.repository = repository;
        this.pricingService = pricingService;
    }

    public BigDecimal getPrice(Product product) {
        return pricingService.getPrice(product);
    }
}
