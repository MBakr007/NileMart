package org.route.repository;

import org.route.model.Product;

import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findBySku(String sku);
}
