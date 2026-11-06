package org.route.nilemartspringboot.repository;

import jakarta.annotation.PostConstruct;
import org.route.nilemartspringboot.model.Product;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryProductRepository implements ProductRepository{
    List<Product> myProducts = new ArrayList<>();

    @PostConstruct
    void init() {
        myProducts.add(Product.builder()
                .sku("P002")
                .name("Desk Lamp")
                .price(BigDecimal.valueOf(500))
                .inStock(10)
                .category("furniture")
                .build());
        myProducts.add(Product.builder()
                .sku("P003")
                .name("ASUS TUF")
                .price(BigDecimal.valueOf(24000))
                .inStock(4)
                .category("Laptop")
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
