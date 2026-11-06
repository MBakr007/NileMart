package org.route.nilemartspringboot.repository;

import org.route.nilemartspringboot.model.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> findAll();

    Product findBySku(String sku);

    void save(Product product);
}
