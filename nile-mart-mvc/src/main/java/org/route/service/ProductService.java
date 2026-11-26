package org.route.service;

import org.route.model.Brand;
import org.route.model.Category;
import org.route.model.Product;
import org.route.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ProductService {

    private static final String DUPLICATE_SKU = "SKU already exists";
    private static final String INVALID_CATEGORY = "Category must be an existing sub-category";
    private static final String INVALID_BRAND = "Brand must be an existing active brand";

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

    public List<ProductRow> getAvailableProducts() {
        return productRepository.findAll().stream()
                .filter(this::isAvailable)
                .map(this::toRow)
                .toList();
    }

    public List<Category> getSubCategories() {
        return categoryService.getSubCategories();
    }

    public List<Brand> getActiveBrands() {
        return brandService.getActiveBrands();
    }

    public Map<String, String> validate(Product product, Set<String> fieldsWithBeanErrors) {
        normalize(product);
        Set<String> skipped = fieldsWithBeanErrors == null ? Set.of() : fieldsWithBeanErrors;
        Map<String, String> errors = new LinkedHashMap<>();

        if (!skipped.contains("sku") && hasDuplicateSku(product.getSku())) {
            errors.put("sku", DUPLICATE_SKU);
        }
        if (!skipped.contains("category") && !isSubCategory(product.getCategory())) {
            errors.put("category", INVALID_CATEGORY);
        }
        if (!skipped.contains("brandSlug") && !isUsableBrand(product.getBrandSlug())) {
            errors.put("brandSlug", INVALID_BRAND);
        }
        return errors;
    }

    public void saveProduct(Product product) {
        normalize(product);
        productRepository.save(product);
    }

    private boolean isAvailable(Product product) {
        return product.getInStock() != null && product.getInStock() > 0;
    }

    private ProductRow toRow(Product product) {
        return new ProductRow(
                product.getSku(),
                product.getName(),
                product.getPrice(),
                product.getInStock(),
                categoryName(product.getCategory()),
                brandName(product.getBrandSlug()));
    }

    private String categoryName(String slug) {
        Category category = categoryService.findBySlug(slug);
        return category == null ? slug : category.getName();
    }

    private String brandName(String slug) {
        if (slug == null || slug.isBlank()) {
            return "—";
        }
        Brand brand = brandService.findBySlug(slug);
        return brand == null ? "—" : brand.getName();
    }

    private boolean hasDuplicateSku(String sku) {
        return sku != null && productRepository.findBySku(sku) != null;
    }

    private boolean isSubCategory(String slug) {
        if (slug == null) {
            return false;
        }
        Category category = categoryService.findBySlug(slug);
        return category != null && category.getParentSlug() != null
                && !category.getParentSlug().isBlank();
    }

    private boolean isUsableBrand(String slug) {
        if (slug == null || slug.isBlank()) {
            return true;
        }
        Brand brand = brandService.findBySlug(slug);
        return brand != null && brand.isActive();
    }

    private static void normalize(Product product) {
        if (product.getBrandSlug() == null || product.getBrandSlug().isBlank()) {
            product.setBrandSlug("");
        }
    }
}
