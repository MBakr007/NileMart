package org.route;

import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findBySku(String sku);
}
