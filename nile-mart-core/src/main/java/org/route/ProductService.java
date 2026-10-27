package org.route;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final PricingService pricingService;

    public ProductService(ProductRepository repository,
                          PricingService pricingService) {
        this.repository = repository;
        this.pricingService = pricingService;
    }

    public BigDecimal getPrice(String sku, int qty) {
        Product product = repository.findBySku(sku)
                .filter(Product::getIsActive)
                .orElseThrow(() -> new RuntimeException("Product isn't available."));
        return pricingService.getPrice(product, qty);
    }
}
