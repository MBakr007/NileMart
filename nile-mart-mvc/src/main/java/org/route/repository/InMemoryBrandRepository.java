package org.route.repository;

import jakarta.annotation.PostConstruct;
import org.route.model.Brand;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryBrandRepository implements BrandRepository {

    private final Map<String, Brand> brands = new LinkedHashMap<>();

    @PostConstruct
    void seedBrands() {
        save(brand("Samsung", "samsung", true));
        save(brand("Asus", "asus", true));
        save(brand("Anker", "anker", true));
        save(brand("Tefal", "tefal", true));
        save(brand("Old Brand", "old-brand", false));
    }

    @Override
    public List<Brand> findAll() {
        return new ArrayList<>(brands.values());
    }

    @Override
    public Brand findBySlug(String slug) {
        return brands.get(slug);
    }

    @Override
    public void save(Brand brand) {
        brands.put(brand.getSlug(), brand);
    }

    private static Brand brand(String name, String slug, boolean active) {
        return Brand.builder()
                .name(name)
                .slug(slug)
                .active(active)
                .build();
    }
}
