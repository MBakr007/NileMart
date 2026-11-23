package org.route.nilemartspringboot.repository.product;

import org.route.nilemartspringboot.model.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao {
    List<Product> findAll();

    void save(Product product);

    Product findById(String id);

    void deleteById(String id);

    void updatePriceById(String id, BigDecimal price);
}
