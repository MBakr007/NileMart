package dao.product;

import modal.Product;

import java.util.List;

public interface ProductDao {
    List<Product> findAll();

    Product findById(String id);
}
