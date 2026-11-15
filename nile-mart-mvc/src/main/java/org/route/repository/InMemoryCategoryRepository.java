package org.route.repository;

import jakarta.annotation.PostConstruct;
import org.route.model.Category;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryCategoryRepository implements CategoryRepository {
    private final List<Category> categories = new ArrayList<>();

    //it works like a seeder
    @PostConstruct
    public void init() {
        categories.add(new Category("Electronics", "electronics", "", 1, true));
        categories.add(new Category("Mobile Phones", "mobile-phones", "electronics", 1, true));
        categories.add(new Category("Laptops", "laptops", "electronics", 2, true));
        categories.add(new Category("Accessories", "electronics-accessories", "electronics", 3, true));
        categories.add(new Category("Home & Kitchen", "home-kitchen", "", 2, true));
        categories.add(new Category("Cookware", "cookware", "home-kitchen", 1, true));
        categories.add(new Category("Lighting", "lighting", "home-kitchen", 2, true));
        categories.add(new Category("Fashion", "fashion", "", 3, false));
        categories.add(new Category("Men’s Clothing", "mens-clothing", "fashion", 1, true));
    }

    @Override
    public List<Category> findAll() {
        return categories;
    }

    @Override
    public Category findBySlug(String slug) {
        //finding the category with the same slug
        for (Category category : categories) {
            if (category.getSlug().equals(slug)) {
                return category;
            }
        }
        return null;
    }

    @Override
    public void save(Category category) {
        //saving the category in the list
        categories.add(category);
    }
}
