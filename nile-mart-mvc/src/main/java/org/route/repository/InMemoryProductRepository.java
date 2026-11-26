package org.route.repository;

import jakarta.annotation.PostConstruct;
import org.route.model.Product;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryProductRepository implements ProductRepository {

    private final Map<String, Product> products = new LinkedHashMap<>();

    @PostConstruct
    void seedProducts() {
        save(product("P002", "Desk Lamp", "lighting", "", "500.00", 10));
        save(product("P003", "ASUS TUF", "laptops", "asus", "24000.00", 4));
        save(product("PH-A55-128", "Samsung Galaxy A55 128GB", "mobile-phones", "samsung",
                "18500.00", 7));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    @Override
    public Product findBySku(String sku) {
        return products.get(sku);
    }

    @Override
    public void save(Product product) {
        products.put(product.getSku(), product);
    }

    private static Product product(String sku, String name, String category,
                                   String brandSlug, String price, int inStock) {
        return Product.builder()
                .sku(sku)
                .name(name)
                .category(category)
                .brandSlug(brandSlug)
                .price(new BigDecimal(price))
                .inStock(inStock)
                .build();
    }
}
