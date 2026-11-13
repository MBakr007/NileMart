package org.route.nilemartspringboot.repository.category;

import jakarta.annotation.PostConstruct;
import org.route.nilemartspringboot.exception.CategoryNotFoundException;
import org.route.nilemartspringboot.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.util.List;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository{

    private final List<Category> categoryList;

    public CategoryRepositoryImpl(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @PostConstruct
    public void init(){
        Category c1 = Category.builder()
                .id("C001")
                .name("Food & Drinks")
                .slug("food-drinks")
                .active(true)
                .build();
        Category c2 = Category.builder()
                .id("C002")
                .name("Clothing")
                .slug("clothing")
                .active(true)
                .build();
        Category c3 = Category.builder()
                .id("C003")
                .name("Books & Media")
                .slug("books-media")
                .active(false)
                .build();

        categoryList.add(c1);
        categoryList.add(c2);
        categoryList.add(c3);
    }

    @Override
    public List<Category> findAll() {
        return categoryList;
    }

    @Override
    public Category findById(String id) throws CategoryNotFoundException {
        return categoryList.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
    }

    @Override
    public void save(Category category) {
        categoryList.add(category);
    }

    @Override
    public void delete(String id) {
        categoryList.removeIf(c -> c.getId().equals(id));
    }
}
