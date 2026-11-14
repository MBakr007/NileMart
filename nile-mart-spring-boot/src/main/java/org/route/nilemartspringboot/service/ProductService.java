package org.route.nilemartspringboot.service;

import org.route.nilemartspringboot.config.StoreProperties;
import org.route.nilemartspringboot.dto.PageResponse;
import org.route.nilemartspringboot.dto.product.AddProductRequest;
import org.route.nilemartspringboot.mapper.ProductMapper;
import org.route.nilemartspringboot.model.Product;
import org.route.nilemartspringboot.repository.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final StoreProperties storeProperties;
    private final ProductMapper productMapper;


    public ProductService(ProductRepository productRepository, StoreProperties storeProperties, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.storeProperties = storeProperties;
        this.productMapper = productMapper;
    }

    public List<Product> getAvailableProducts() {
        List<Product> availableProducts = new ArrayList<>();
        for (Product p : productRepository.findAll()) {
            if (p.getInStock() > 0)
                availableProducts.add(p);
        }
        return availableProducts;
    }

    public PageResponse<Product> getAllProducts(Integer size, Integer page) {

        List<Product> availableProducts = productRepository.findAll();

        int start = page * size;
        int end = Math.min(start + size, availableProducts.size());

        PageResponse<Product> pageResponse = new PageResponse<>();
        pageResponse.setPage(page);
        pageResponse.setSize(size);
        pageResponse.setContent(availableProducts.subList(start, end));
        pageResponse.setTotalElements(availableProducts.size());
        return pageResponse;

    }

    public void saveProduct(AddProductRequest request) {
        productRepository.save(productMapper.toProduct(request));
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

    public Product getProductById(String sku) {
        return productRepository.findBySku(sku);
    }
}
