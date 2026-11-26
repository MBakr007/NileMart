package org.route.service;

import org.route.model.Brand;
import org.route.repository.BrandRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class BrandService {

    private static final String DUPLICATE_NAME = "Brand name already exists";
    private static final String DUPLICATE_SLUG = "Slug already exists";

    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    public List<Brand> getActiveBrands() {
        return findAll().stream()
                .filter(Brand::isActive)
                .toList();
    }

    public Brand findBySlug(String slug) {
        return brandRepository.findBySlug(slug);
    }

    public Map<String, String> validate(Brand brand, Set<String> fieldsWithBeanErrors) {
        Set<String> skipped = fieldsWithBeanErrors == null ? Set.of() : fieldsWithBeanErrors;
        Map<String, String> errors = new LinkedHashMap<>();

        if (!skipped.contains("name") && hasDuplicateName(brand.getName())) {
            errors.put("name", DUPLICATE_NAME);
        }
        if (!skipped.contains("slug") && hasDuplicateSlug(brand.getSlug())) {
            errors.put("slug", DUPLICATE_SLUG);
        }
        return errors;
    }

    public void save(Brand brand) {
        brandRepository.save(brand);
    }

    private boolean hasDuplicateName(String name) {
        if (name == null) {
            return false;
        }
        return findAll().stream()
                .anyMatch(brand -> name.equalsIgnoreCase(brand.getName()));
    }

    private boolean hasDuplicateSlug(String slug) {
        return slug != null && brandRepository.findBySlug(slug) != null;
    }
}
