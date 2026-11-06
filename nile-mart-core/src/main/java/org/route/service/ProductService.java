package org.route.service;

import lombok.RequiredArgsConstructor;
import org.route.exception.StoreException;
import org.route.model.Product;
import org.route.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final PricingService pricingService;

    public List<Product> findAll() {
        return repository.findAll();
    }

    public Product findBySku(String sku) {
        return repository.findBySku(sku)
                .orElseThrow(() -> new StoreException("Product not found: " + sku));
    }

    public BigDecimal getPrice(String sku, int qty) {
        Product product = findBySku(sku);
        if (!product.isActive()) {
            throw new StoreException("Product isn't available: " + product.getName());
        }
        return pricingService.getPrice(product, qty);
    }

    /**
     * Decrements stock after a checkout. The caller is expected to have
     * validated every line first so this method never fails halfway through.
     */
    public void reduceStock(String sku, int qty) {
        if (qty <= 0) {
            throw new StoreException("Quantity must be greater than zero.");
        }
        Product product = findBySku(sku);
        if (product.getStockQuantity() < qty) {
            throw new StoreException("Insufficient stock for " + product.getName()
                    + ": only " + product.getStockQuantity() + " available");
        }
        product.setStockQuantity(product.getStockQuantity() - qty);
    }
}
