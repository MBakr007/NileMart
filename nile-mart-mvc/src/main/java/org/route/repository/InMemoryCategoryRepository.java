package org.route.repository;

import jakarta.annotation.PostConstruct;
import org.route.model.Category;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryCategoryRepository implements CategoryRepository {

    private final Map<String, Category> categories = new LinkedHashMap<>();

    @PostConstruct
    void seedCategories() {
        save(category("Electronics", "electronics", "", 1, true));
        save(category("Mobile Phones", "mobile-phones", "electronics", 1, true));
        save(category("Laptops", "laptops", "electronics", 2, true));
        save(category("Accessories", "electronics-accessories", "electronics", 3, true));
        save(category("Home & Kitchen", "home-kitchen", "", 2, true));
        save(category("Cookware", "cookware", "home-kitchen", 1, true));
        save(category("Lighting", "lighting", "home-kitchen", 2, true));
        save(category("Fashion", "fashion", "", 3, false));
        save(category("Men’s Clothing", "mens-clothing", "fashion", 1, true));
    }

    @Override
    public List<Category> findAll() {
        return new ArrayList<>(categories.values());
    }

    @Override
    public Category findBySlug(String slug) {
        return categories.get(slug);
    }

    @Override
    public void save(Category category) {
        categories.put(category.getSlug(), category);
    }

    private static Category category(String name, String slug, String parentSlug,
                                     int displayOrder, boolean active) {
        return Category.builder()
                .name(name)
                .slug(slug)
                .parentSlug(parentSlug)
                .displayOrder(displayOrder)
                .active(active)
                .build();
    }
}
