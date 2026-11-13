package service;

import dao.product.ProductDao;
import modal.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductService {

    private final ProductDao productDao;

    public ProductService (ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> getAvailableProducts() {
        List<Product> availableProducts = new ArrayList<>();
        for (Product product : productDao.findAll()) {
            if (product.getInStock() > 0) {
                availableProducts.add(product);
            }
        }
        return availableProducts;
    }
}
