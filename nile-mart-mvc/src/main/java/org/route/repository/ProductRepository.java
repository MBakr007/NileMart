package org.route.repository;

import org.route.model.Product;

import java.util.List;

public interface ProductRepository {

    List<Product> findAll();

    Product findBySku(String sku);

    void save(Product product);
}
