package org.route.repository;

import org.route.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    List<Product> findAll();

    Optional<Product> findBySku(String sku);

    void save(Product product);
}
