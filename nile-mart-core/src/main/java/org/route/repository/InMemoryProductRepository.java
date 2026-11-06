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
     * Loads the eight catalogue products exactly as specified by the assignment.
     * Public so the plain-Java ObjectFactory can prepare the repository without
     * a Spring container.
     */
    @PostConstruct
    public void seedProducts() {
        save(Product.builder()
                .sku("PH-X5-128")
                .name("Smartphone X5 128GB")
                .category("Electronics")
                .price(new BigDecimal("16000.00"))
                .stockQuantity(12)
                .isActive(true)
                .build());

        save(Product.builder()
                .sku("ACC-CHG-20W")
                .name("Fast Charger 20W")
                .category("Electronics")
                .price(new BigDecimal("650.00"))
                .stockQuantity(40)
                .isActive(true)
                .build());

        save(Product.builder()
                .sku("ACC-BUDS-6")
                .name("Wireless Earbuds")
                .category("Electronics")
                .price(new BigDecimal("1100.00"))
                .stockQuantity(8)
                .isActive(true)
                .build());

        save(Product.builder()
                .sku("FSH-GAL-001")
                .name("Cotton Galabeya")
                .category("Fashion")
                .price(new BigDecimal("900.00"))
                .stockQuantity(25)
                .isActive(true)
                .build());

        save(Product.builder()
                .sku("HOM-PAN-28")
                .name("Non-stick Frying Pan 28cm")
                .category("Home & Kitchen")
                .price(new BigDecimal("1200.00"))
                .stockQuantity(3)
                .isActive(true)
                .build());

        save(Product.builder()
                .sku("HOM-SPC-RCK")
                .name("Wooden Spice Rack")
                .category("Home & Kitchen")
                .price(new BigDecimal("750.00"))
                .stockQuantity(10)
                .isActive(true)
                .build());

        save(Product.builder()
                .sku("BTY-SUN-50")
                .name("Sun Cream SPF50")
                .category("Beauty")
                .price(new BigDecimal("350.00"))
                .stockQuantity(0)
                .isActive(true)
                .build());

        save(Product.builder()
                .sku("BK-CAI-TRI")
                .name("The Cairo Trilogy")
                .category("Books")
                .price(new BigDecimal("450.00"))
                .stockQuantity(15)
                .isActive(true)
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
