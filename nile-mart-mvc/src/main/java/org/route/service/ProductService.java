package org.route.service;


import org.route.model.Product;
import org.route.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final BrandService brandService;

    public ProductService(ProductRepository productRepository,
                          CategoryService categoryService,
                          BrandService brandService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.brandService = brandService;
    }

    public List<Product> getAvailableProducts() {
        List<Product> availableProducts = new ArrayList<>();
        for (Product p : productRepository.findAll()) {
            if (p.getInStock() > 0)
                availableProducts.add(p);
        }
        return availableProducts;
    }

    public List<Map<String, Object>> getProductsForDisplay() {
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Product product : getAvailableProducts()) {
            rows.add(Map.of("product", product,
                    "categoryName", categoryService.getCategoryName(product.getCategory()),
                    "brandName", brandService.getBrandName(product.getBrandSlug())));
        }
        return rows;
    }

    public boolean saveProduct(Product product, Errors errors) {
        if (!errors.hasFieldErrors("sku") && isProductExist(product.getSku())) {
            errors.rejectValue("sku", "product.sku.duplicate", "SKU already exists");
        }
        if (!errors.hasFieldErrors("category")
                && !categoryService.isAvailableSubCategory(product.getCategory())) {
            errors.rejectValue("category", "product.category.invalid",
                    "Choose an active sub-category with an active parent");
        }
        String brandSlug = product.getBrandSlug();
        if (brandSlug != null && !brandSlug.isEmpty() && !brandService.isActiveBrand(brandSlug)) {
            errors.rejectValue("brandSlug", "product.brand.invalid", "Choose an existing active brand");
        }
        if (errors.hasErrors()) {
            return false;
        }
        if (brandSlug != null && brandSlug.isEmpty()) {
            product.setBrandSlug(null);
        }
        productRepository.save(product);
        return true;
    }

    public boolean isProductExist(String sku) {
        return productRepository.findBySku(sku) != null;
    }
}
