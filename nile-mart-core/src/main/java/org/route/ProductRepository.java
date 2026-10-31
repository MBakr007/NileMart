package org.route;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    public List<Product> findAll();
    public Optional<Product> findBySku(String sku);
    public void save(Product product);
}
