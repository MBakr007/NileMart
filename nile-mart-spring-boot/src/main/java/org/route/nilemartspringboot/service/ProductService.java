package org.route.nilemartspringboot.service;

import org.route.nilemartspringboot.config.StoreProperties;
import org.route.nilemartspringboot.model.Product;
import org.route.nilemartspringboot.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final StoreProperties storeProperties;


    public ProductService(ProductRepository productRepository, StoreProperties storeProperties) {
        this.productRepository = productRepository;
        this.storeProperties = storeProperties;
    }

    public List<Product> getAvailableProducts() {
        List<Product> availableProducts = new ArrayList<>();
        for (Product p : productRepository.findAll()) {
            if (p.getInStock() > 0)
                availableProducts.add(p);
        }
        return availableProducts;
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public boolean isProductExist(String sku) {
        return productRepository.findBySku(sku) != null;
    }

    public List<Product> getAllProductWithVat() {
        List<Product> products = new ArrayList<>();
        for (Product p : productRepository.findAll()) {
            BigDecimal totalVat = BigDecimal.valueOf(1).add(storeProperties.getVatRate());
            BigDecimal totalPrice = p.getPrice().multiply(totalVat);
            p.setPrice(totalPrice);
            products.add(p);
        }
        return products;
    }
}
