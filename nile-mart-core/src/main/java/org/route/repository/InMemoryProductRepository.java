package org.route.repository;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.route.model.Product;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryProductRepository implements ProductRepository {

    /** LinkedHashMap keeps the catalogue in the order the seed data was loaded. */
    private final Map<String, Product> store = new LinkedHashMap<>();

    /**
     * Loads the eight catalogue products. Public so the plain-Java ObjectFactory
     * can prepare the repository without a Spring container.
     */
    @PostConstruct
    public void seedProducts() {
        save(Product.builder()
                .name("Smartphone X5 128GB")
                .sku("PH-X5-128")
                .price(new BigDecimal("16000.00"))
                .category("Electronics")
                .isActive(true)
                .stockQuantity(25)
                .build());

        save(Product.builder()
                .name("Fast Charger 20W")
                .sku("AC-CH20")
                .price(new BigDecimal("800.00"))
                .category("Accessories")
                .isActive(true)
                .stockQuantity(50)
                .build());

        save(Product.builder()
                .name("Wireless Earbuds Pro")
                .sku("AC-EB-PRO")
                .price(new BigDecimal("1200.00"))
                .category("Audio")
                .isActive(true)
                .stockQuantity(8)
                .build());

        save(Product.builder()
                .name("Cotton Galabeya")
                .sku("CL-GAL-COT")
                .price(new BigDecimal("600.00"))
                .category("Clothing")
                .isActive(true)
                .stockQuantity(40)
                .build());

        save(Product.builder()
                .name("Non-stick Frying Pan 28cm")
                .sku("HK-FP-28")
                .price(new BigDecimal("450.00"))
                .category("Kitchen")
                .isActive(true)
                .stockQuantity(3)
                .build());

        save(Product.builder()
                .name("Sun Cream SPF50")
                .sku("BT-SC-50")
                .price(new BigDecimal("250.00"))
                .category("Beauty")
                .isActive(true)
                .stockQuantity(0)
                .build());

        save(Product.builder()
                .name("Egyptian Cotton Towel Set")
                .sku("HM-TW-SET")
                .price(new BigDecimal("350.00"))
                .category("Home")
                .isActive(true)
                .stockQuantity(30)
                .build());

        save(Product.builder()
                .name("Arabica Coffee 1kg")
                .sku("GR-COF-1K")
                .price(new BigDecimal("420.00"))
                .category("Grocery")
                .isActive(true)
                .stockQuantity(60)
                .build());
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        return Optional.ofNullable(store.get(sku));
    }

    @Override
    public void save(Product product) {
        store.put(product.getSku(), product);
    }

    @PreDestroy
    public void close() {
        System.out.println("Catalog closed");
    }
}
