package org.route;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findBySku(String sku);
    List<Product> findAll();
    void save(Product product);

}
