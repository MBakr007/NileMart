package org.route.repository;

import jakarta.annotation.PostConstruct;
import org.route.model.Product;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Repository
public class InMemoryProductRepository implements ProductRepository {
    private final List<Product> myProducts = new ArrayList<>();

    @PostConstruct
    void init() {
        myProducts.add(Product.builder()
                .sku("P002")
                .name("Desk Lamp")
                .price(BigDecimal.valueOf(500))
                .inStock(10)
                .category("lighting")
                .build());
        myProducts.add(Product.builder()
                .sku("P003")
                .name("ASUS TUF")
                .price(BigDecimal.valueOf(24000))
                .inStock(4)
                .category("laptops")
                .brandSlug("asus")
                .build());
        myProducts.add(Product.builder()
                .sku("PH-A55-128")
                .name("Samsung Galaxy A55 128GB")
                .category("mobile-phones")
                .brandSlug("samsung")
                .price(new BigDecimal("18500.00"))
                .inStock(7)
                .build());
    }

    @Override
    public List<Product> findAll() {
        return myProducts;
    }

    @Override
    public Product findBySku(String sku) {
        for (Product product : myProducts) {
            if (product.getSku().equals(sku)) {
                return product;
            }
        }
        return null;
    }

    @Override
    public void save(Product product) {
        myProducts.add(product);
    }
}
