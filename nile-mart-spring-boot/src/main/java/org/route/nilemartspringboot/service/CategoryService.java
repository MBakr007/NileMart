package org.route.nilemartspringboot.service;


import org.route.nilemartspringboot.exception.CategoryNotFoundException;
import org.route.nilemartspringboot.model.Category;
import org.route.nilemartspringboot.repository.category.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return this.categoryRepository.findAll();
    }

    public Category getCategoryById(String id) throws CategoryNotFoundException {
        return categoryRepository.findById(id);
    }

    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    public void deleteCategory(String id) {
        categoryRepository.delete(id);
    }
}
